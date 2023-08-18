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
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
            String insertQuery = "INSERT INTO product (product_id, shop_category, product_name, thumbnail_url, price, quantity) VALUES (?, ?, ?, ?, ?, ?)";
//            String insertQuery = "INSERT INTO product (product_id, shop_category, product_name, thumbnail_url, price, quantity, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // news_id 중복 체크를 위한 Set
            Set<Integer> existingProductIds = new HashSet<>();

            // 이미 있는 product_id 값들을 가져와 Set 에 저장
            String existingProductIdQuery = "SELECT product_id FROM product";
            PreparedStatement existingProductIdStatement = connection.prepareStatement(existingProductIdQuery);
            ResultSet existingProductIdResultSet = existingProductIdStatement.executeQuery();
            while (existingProductIdResultSet.next()) {
                existingProductIds.add(existingProductIdResultSet.getInt("product_id"));
            }


            for (int productId = 20000; productId >= 0; productId--) {
                String productUrl = "https://www.rankingdak.com/product/view?productCd=" + productId;
                int count = 1;

                if (existingProductIds.contains(productId)) {
                    System.out.println("Skipping duplicate productId: " + productId);
                    continue;
                }
                
                String shopCategory = getShopCategory(productUrl);
                if (shopCategory == "") {
                    System.out.println("shopCategory null:" + productId);
                    continue;
                }

                String productName = getProductName(productUrl);
                if (productName == "") {
                    System.out.println("productName null:" + productId);
                    continue;
                }

                String thumbnailUrl = getProductThumbnailUrl(productUrl);
                if (thumbnailUrl == "") {
                    System.out.println("thumbnailUrl null:" + productId);
                    continue;
                }

                int price = Integer.parseInt(getPrice(productUrl));

                int quantity = 500;

                preparedStatement.setInt(1, productId);
                preparedStatement.setString(2, shopCategory);
                preparedStatement.setString(3, productName);
                preparedStatement.setString(4, thumbnailUrl);
                preparedStatement.setInt(5, price);
                preparedStatement.setInt(6, quantity);

                preparedStatement.executeUpdate();
                System.out.println(count);
                count += 1;
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getNews...
    private static String getShopCategory(String productUrl) throws IOException {
        Document document = Jsoup.connect(productUrl).get();
        Element inputTag = document.selectFirst("input[type=hidden][name=categoryCd]");
        if(inputTag != null){
            return inputTag.attr("value");
        }
        return "";
    }

    private static String getProductThumbnailUrl(String productUrl) throws IOException {
        Document document = Jsoup.connect(productUrl).get();
        Element imgTag = document.selectFirst("div.goods-top img");
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