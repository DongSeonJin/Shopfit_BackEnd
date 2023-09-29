package com.spring.news.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;         // getNewsId; 추가
import java.util.regex.Pattern;         // getNewsId; 추가
import java.util.Set;                   // newsId 중복 체크; 추가
import java.sql.ResultSet;              // newsId 중복 체크; 추가


public class NewsCrawler {

    public static void main(String[] args) {

        String baseUrl = "https://www.ggjil.com/";
        String url = baseUrl + "section.php?thread=24r01";

        try {
            // Jsoup를 사용하여 웹 페이지 가져오기
            Document document = Jsoup.connect(url).get();

            // 필요한 데이터를 크롤링하는 로직을 추가하세요
            Elements elements = document.select(".title a");                    // 최신 기사 선택
            String latestNews = baseUrl + elements.get(0).attr("href");       // 최신 기사 url
            int latestNewsNumber = Integer.parseInt(getNewsId(latestNews));              // 최신 기사 news_id
            int newsDataRange = 120;                                                       // db 크기 설정
            int lastNewsId = latestNewsNumber-newsDataRange+1;


            // MySQL 연결 정보
            String jdbcUrl = "jdbc:mysql://localhost:3306/nc_project";                   // 스키마 : nc_project
            String username = "root";
            String password = "mysql";

            // MySQL 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // MySQL 연결
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // MySQL 입력 쿼리 및 데이터 입력
            String insertQuery = "INSERT INTO news (news_id, title, content, image_url, news_url, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // news_id 중복 체크를 위한 Set
            Set<Integer> existingNewsIds = new HashSet<>();

            // 이미 있는 news_id 값들을 가져와 Set 에 저장
            String existingNewsIdQuery = "SELECT news_id FROM news";
            PreparedStatement existingNewsIdStatement = connection.prepareStatement(existingNewsIdQuery);
            ResultSet existingNewsIdResultSet = existingNewsIdStatement.executeQuery();
            while (existingNewsIdResultSet.next()) {
                existingNewsIds.add(existingNewsIdResultSet.getInt("news_id"));
            }

            int count = 1;
            // 크롤링 범위 설정 (최신 기사 ~ n번 째 기사 까지)
            for (int newsId = latestNewsNumber; newsId >= lastNewsId; newsId--) {
                String newsUrl = "https://www.ggjil.com/detail.php?number="+newsId+"&thread=24r01";     // 크롤링 할 n번 째 기사 url


                // news_id 중복 확인
                if (existingNewsIds.contains(newsId)) {
                    System.out.println("Skipping duplicate news_id: " + newsId);
                    count += 1;
                    continue;                                                       // 중복 되는 경우 스킵
                }

                String newsTitle = getNewsTitle(newsUrl);                           // 기사 페이지 제목
                if(newsTitle == ""){
                    System.out.println("Skipping null newsTitle: " + newsId);
                    count += 1;
                    continue;
                }
                String newsContent = getNewsContent(newsUrl);                       // 기사 페이지 첫 내용
                String newsImageUrl = getNewsImageUrl(newsUrl, newsId);             // 기사 페이지 썸네일 이미지 url
                String newsDate = getNewsDate(newsUrl);                             // 기사 페이지 날짜

                // 데이터 입력
                preparedStatement.setInt(1, newsId);
                preparedStatement.setString(2, newsTitle);
                preparedStatement.setString(3, newsContent);
                preparedStatement.setString(4, newsImageUrl);
                preparedStatement.setString(5, newsUrl);
                preparedStatement.setString(6, newsDate);
                preparedStatement.executeUpdate();

                // 크롤링 진행 상황
                System.out.println(count+"/"+newsDataRange+"완료");
                count += 1;
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }


    // getNews...
    private static String getNewsId(String newsPageUrl) throws IOException {
        try {
            Document document = Jsoup.connect(newsPageUrl).get();
            Element newsLinkElement = document.select(".thumb a").first();
            String newsLinkUrl = "https://www.ggjil.com" + Objects.requireNonNull(newsLinkElement).attr("href");

            String regex = "number=(\\d+)";                     // 정규 표현식
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(newsLinkUrl);

            if (matcher.find()) {
                int newsId = Integer.parseInt(matcher.group(1));
                if (newsId <= 0) {
                    return "null";                              // newsId가 0 이하일 경우 "null"로 처리
                } else {
                    return Integer.toString(newsId);
                }
            } else {
                return "null";                                  // number 없을 경우; 예외 처리
            }
        } catch (NumberFormatException e){
            return "null";                                      // 숫자 변환 예외가 발생 하면 "null"로 처리 하여 다음 번호를 가져 오도록 함
        }
    }

    private static String getNewsTitle(String newsUrl) throws IOException {
        Document document = Jsoup.connect(newsUrl).get();
        Element titleElement = document.select("h2.font_22.font_malgun.de_tit").first();
        return (titleElement != null? titleElement.text() : "");
    }

    private static String getNewsContent(String newsUrl) throws IOException {
        Document document = Jsoup.connect(newsUrl).get();
        Element contentElement = document.select("div#ct p").first();
        String inputData = Objects.requireNonNull(contentElement).text();

        // 기사 내용 길이 제한
        int maxLength = 255;                                      // 웹 페이지 설계 후 필요한 길이 만큼 설정 예정
        if (inputData.length() > maxLength) {
            inputData = inputData.substring(0, maxLength);
        }
        return inputData;
    }

    private static String getNewsImageUrl(String newsUrl, int newsId) throws IOException {
        Document document = Jsoup.connect("https://www.ggjil.com/section.php?thread=24r01").get();
        Element Element = document.select(".thumb a[href*="+ newsId + "] img").first();

        // document 내 썸네일 이미지가 없는 경우가 많음 -> 해당 경우 기사 내 첫 이미지를 썸네일로 대체
        if (Element != null) {
            return "https://www.ggjil.com" + Element.attr("src");
        } else {
            Document document2 = Jsoup.connect(newsUrl).get();
            Element ctDiv = document2.getElementById("ct");
            Element Element2 = Objects.requireNonNull(ctDiv).select("img").first();
            return "https://www.ggjil.com" + (Element2 != null ? Element2.attr("src") : "");
        }

    }

    private static String getNewsDate(String newsUrl) throws IOException {
        Document document = Jsoup.connect(newsUrl).get();
        Element dateElement = document.select("div.de_date_txt span.font_12").first();
        return Objects.requireNonNull(dateElement).text();
    }
}



/*

DB 쿼리문
CREATE TABLE news (
    news_id INT,
    title VARCHAR(100),
	content VARCHAR(5),
    image_url TEXT,
    news_link_url TEXT,
    created_at VARCHAR(25)
);

SELECT * FROM news;

DROP TABLE news;

SELECT COUNT(*) FROM news;


newsdb
	news_id	integer		완
	title varchar		완
	content varchar		완
	image_url varchar	완
	news_url varchar	완
	created_at varchar	완

중복처리					완
크롤링 범위 설정           완

 */