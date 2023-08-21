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
            String insertProduct = "INSERT INTO product (product_id, price, product_name, stock_quantity, thumbnail_url, category_id) VALUES (?, ?, ?, ?, ?, ?)";

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


            for (int productId = 20000; productId >= 1; productId--) {
                String productUrl = "https://www.rankingdak.com/product/view?productCd=" + productId;
                System.out.println(productId + " 진행 중");

                if (existingProductIds.contains(productId)) {
                    System.out.println("Skipping duplicate productId: " + productId);
                    continue;
                }

//                String categoryId = getCategoryId(productUrl);
//                if (categoryId == "") {
//                    System.out.println("categoryId null:");
//                    continue;
//                }
                int categoryId = 1;

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
                int stockQuantity = 500;

                // product table 데이터 입력
                preparedStatement.setInt(1, productId);
                preparedStatement.setInt(2, price);
                preparedStatement.setString(3, productName);
                preparedStatement.setInt(4, stockQuantity);
                preparedStatement.setString(5, thumbnailUrl);
                preparedStatement.setInt(6, categoryId);

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
}