package com.spring.shopping.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;


public class ShoppingCrawler {

    public static void main(String[] args) {

        String baseUrl = "https://www.rankingdak.com/";
        String chickenUrl = baseUrl + "product/list?depth1=R019";
        String drinkUrl = baseUrl + "product/list?depth1=R024";
        String equipUrl = baseUrl + "product/list?depth1=R032";

        try {
            // Jsoup를 사용하여 웹 페이지 가져오기
//            Document document = Jsoup.connect(chickenUrl).get();                        // 치킨 판매
//
//            // 필요한 데이터를 크롤링하는 로직을 추가하세요
//            Elements elements = document.select("figure.img a");                           // 제품 상세페이지 url 일부
//            System.out.println(elements.size());
//            String latestNews = baseUrl + elements.get(0).attr("href");       // 최신 기사 url
//            int latestNewsNumber = Integer.parseInt(getNewsId(latestNews));              // 최신 기사 news_id
//            int newsDataRange = 120;                                                       // db 크기 설정
//            int lastNewsId = latestNewsNumber-newsDataRange+1;


            // MySQL 연결 정보
            String jdbcUrl = "jdbc:mysql://localhost:3306/nc_project";                   // 스키마 : nc_project
            String username = "root";
            String password = "mysql";

            // MySQL 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // MySQL 연결
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // MySQL 입력 쿼리 및 데이터 입력
            String insertProduct = "INSERT INTO product (product_id, category_id, product_name, thumbnail_url, price, quantity) VALUES (?, ?, ?, ?, ?, ?)";
            String insertProductImage = "INSERT INTO product_image (product_id, image_url) VALUES (?, ?)";
//            String insertProduct = "INSERT INTO product (product_id, category_id, product_name, thumbnail_url, price, quantity, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertProduct);

            // news_id 중복 체크를 위한 Set
            Set<Integer> existingProductIds = new HashSet<>();

            // 이미 있는 product_id 값들을 가져와 Set 에 저장
            String existingProductIdQuery = "SELECT product_id FROM product";
            PreparedStatement existingProductIdStatement = connection.prepareStatement(existingProductIdQuery);
            ResultSet existingProductIdResultSet = existingProductIdStatement.executeQuery();
            while (existingProductIdResultSet.next()) {
                existingProductIds.add(existingProductIdResultSet.getInt("product_id"));
            }


            for (int productId = 8976; productId >= 8976; productId--) {
                String productUrl = "https://www.rankingdak.com/product/view?productCd=" + productId;
                System.out.println(productId + " 진행 중");

                if (existingProductIds.contains(productId)) {
                    System.out.println("Skipping duplicate productId: " + productId);
                    continue;
                }

                String categoryId = getCategoryId(productUrl);
                if (categoryId == "") {
                    System.out.println("categoryId null:");
                    continue;
                }

                String productName = getProductName(productUrl);
                if (productName == "") {
                    System.out.println("productName null:");
                    continue;
                }

                String thumbnailUrl = getProductThumbnailUrl(productUrl);
                if (thumbnailUrl == "") {
                    System.out.println("thumbnailUrl null:");
                    continue;
                }

                int price = Integer.parseInt(getPrice(productUrl));
                int quantity = 500;

                Elements elements = getCheckImageAmount(productUrl);
                System.out.println(elements.size());
                for(Element element : elements){
                    Elements divTags = element.select("div");
                    for (Element divTag : divTags) {
                        Elements imgTags = divTag.select("img"); // Select all <img> tags within the element
                        System.out.println(imgTags.size());
                        for (Element imgTag : imgTags) {
                            String imgUrl = imgTag.attr("src"); // Get the value of the 'src' attribute
                            System.out.println("Image URL: " + imgUrl);
                        }
                    }
                }

                // product_image table 데이터 입력
//                for (Element element : elements) {
//                    PreparedStatement preparedPrImg = connection.prepareStatement(insertProductImage);
//                    preparedPrImg.setInt(1, productId);
//                    preparedPrImg.setString(2, element.attr("src"));
//                    preparedPrImg.executeUpdate();
//                    preparedPrImg.close();
//                }

                // product table 데이터 입력
                preparedStatement.setInt(1, productId);
                preparedStatement.setString(2, categoryId);
                preparedStatement.setString(3, productName);
                preparedStatement.setString(4, thumbnailUrl);
                preparedStatement.setInt(5, price);
                preparedStatement.setInt(6, quantity);

                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getNews...
    private static String getCategoryId(String productUrl) throws IOException {
        Document document = Jsoup.connect(productUrl).get();
        Element categoryId = document.selectFirst("input[type=hidden][name=categoryCd]");
        if (categoryId != null) {
            return categoryId.attr("value");
        }
        return "";
    }

    private static String getProductThumbnailUrl(String productUrl) throws IOException {
        Document document = Jsoup.connect(productUrl).get();
        Element imgTag = document.selectFirst("div.goods-img-area img");
        String imgUrl = imgTag.attr("src");
        if (imgTag != null) {
            return imgUrl;
        }
        return "";
    }

    private static String getProductName(String productUrl) throws IOException {
        Document document = Jsoup.connect(productUrl).get();
        Element h2Tag = document.selectFirst("h2.goods-tit");  // Find the <h2> tag with class "goods-tit"
        if (h2Tag != null) {
            return h2Tag.text();
        }
        return "";
    }

    private static String getPrice(String productUrl) throws IOException {
        Document document = Jsoup.connect(productUrl).get();
        Element priceString = document.selectFirst("div.goods-price strong");
        if (priceString != null) {
            return priceString.text().replace(",", "").trim();
        }
        return "";
    }

    private static Elements getCheckImageAmount(String productUrl) throws IOException {
        Document document = Jsoup.connect(productUrl).get();
        return document.select(".inner-content.productCont");
//        return document.select("img");
    }
}