package com.spring.news.service;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static java.lang.Math.min;

public class NewsCrawler {

    public static void main(String[] args) {
        String url = "https://www.ggjil.com/section.php?thread=24r01";
        String baseUrl = "https://www.ggjil.com/";

        try {
            // Jsoup를 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect(url).get();

            // 필요한 데이터를 크롤링하는 로직을 추가하세요
            Elements elements = document.select(".title a");                   // 제목 선택 - for문 반복 횟수를 정하기 위해서?
            Elements elementsContent = document.select(".substance a");        // 내용 선택

            System.out.println(elements);                                               // 1 ~ n번째 제목
            System.out.println("----------------------------------");
            System.out.println(elementsContent);                                        // 1 ~ n번째 내용
            System.out.println("----------------------------------");
            System.out.println(elements.size());                                        // 16
            System.out.println(elementsContent.size());                                 // 10
            // thread=28r01(대회정보) 리스트까지 들고옴
            // 대회정보 리스트는 뉴스 리스트와 구성요소가 달라(substance 요소 없음) 사이즈 차이남


            // MySQL 연결 정보
            String jdbcUrl = "jdbc:mysql://localhost:3306/nc_project";
            String username = "root";
            String password = "mysql";

            // MySQL 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // MySQL 연결
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // 제목과 날짜를 MySQL에 저장
            String insertQuery = "INSERT INTO news (title, created_at) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            for (int i = 0; i < elements.size(); i++) {
//            for (int i = 0; i < 1; i++) {
                Element titleElement = elements.get(i);                 // i번째 기사 선택
//                Element contentElement = elements.get(i);
//                Element dateElement = elements.get(i);

                String newsUrl = titleElement.attr("href");  // 뉴스 페이지의 URL 추출
                String fullUrl = baseUrl + newsUrl;
                System.out.println(fullUrl);
                String fullTitle = getFullTitle(fullUrl);   // 뉴스 페이지로 이동하여 전체 제목 가져오기
                String newsDate = getNewsDate(fullUrl);     // 날짜 데이터 가져오기

                preparedStatement.setString(1, fullTitle);
//                preparedStatement.setString(2, content);
                preparedStatement.setString(2, newsDate);
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFullTitle(String newsUrl) throws IOException {
        Document document = Jsoup.connect(newsUrl).get();
        Element titleElement = document.select("h2.font_22.font_malgun.de_tit").first();
        return titleElement.text();
    }

    private static String getNewsDate(String newsUrl) throws IOException {
        Document document = Jsoup.connect(newsUrl).get();
        Element dateElement = document.select("div.de_date_txt span.font_12").first();
        return dateElement.text();
    }
}