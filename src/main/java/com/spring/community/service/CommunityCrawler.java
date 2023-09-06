package com.spring.community.service;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.*;



public class CommunityCrawler {
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";



    public static void saveData(List<String> imageUrls, long userId, String content, String nickname, long likes, String title, long viewcount, long categoryId) {


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 테스트용 sql setting
            // String sql = "INSERT INTO posttest (image_url1, image_url2, image_url3, user_id, content, nickname, likes, title) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            //PreparedStatement pstmt = conn.prepareStatement(sql);

            String createTableSql = "CREATE TABLE IF NOT EXISTS post_" + nickname + " ("
                    + "post_id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                    + "user_id BIGINT,"
                    + "nickname VARCHAR(255),"
                    + "category_id INT,"
                    + "title VARCHAR(255),"
                    + "content TEXT,"
                    + "view_count BIGINT DEFAULT 0,"
                    + "created_at DATETIME,"
                    + "updated_at DATETIME,"
                    + "image_url1 TEXT,"
                    + "image_url2 TEXT,"
                    + "image_url3 TEXT,"
                    + "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)";

            PreparedStatement createTableStmt = conn.prepareStatement(createTableSql);
            createTableStmt.execute();

            String insertDynamicTableSql =
                    "INSERT INTO post_" + nickname
                   + "(image_url1, image_url2, image_url3, user_id, content, nickname, category_id, title, created_at, updated_at)"
                   + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
            PreparedStatement pstmt = conn.prepareStatement(insertDynamicTableSql);



            for (int i = 0; i < 3; i++) {
                pstmt.setString(i + 1, imageUrls.size() > i ? imageUrls.get(i) : null); // 이미지 URL 설정. 이미지가 없는 경우 null
            }
            pstmt.setLong(4,userId);
            pstmt.setString(5,content);
            pstmt.setString(6,nickname);
            pstmt.setLong(7, categoryId);
            pstmt.setString(8,title);

            int rowsAffected = pstmt.executeUpdate();

            System.out.println(rowsAffected + " row(s) inserted into the dynamic table.");


            // Insert into post table

            String insertPostSql =
                    "INSERT INTO post"
                    + "(image_url1, image_url2, image_url3, user_id, content, nickname, category_id, title, view_count, created_at, updated_at)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

            PreparedStatement pstmt2=conn.prepareStatement(insertPostSql);

            for (int i = 0; i < 3; i++) {
                pstmt2.setString(i + 1, imageUrls.size() > i ? imageUrls.get(i) : null); // 이미지 URL 설정. 이미지가 없는 경우 null
            }
            pstmt2.setLong(4,userId);
            pstmt2.setString(5,content);
            pstmt2.setString(6,nickname);
            pstmt2.setLong(7, categoryId);
            pstmt2.setString(8,title);
            pstmt2.setLong(9,viewcount);

            pstmt2.executeUpdate();


            String updateUsers =
                    "UPDATE users " +
                        "SET nickname = ? " +
                        "WHERE user_id = ?";

            PreparedStatement pstmt3 = conn.prepareStatement(updateUsers);

            pstmt3.setString(1,nickname);
            pstmt3.setLong(2, userId);

            pstmt3.executeUpdate();




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {



        //System.setProperty("webdriver.chrome.driver", "C:/chromedriver-win64/chromedriver.exe"); // 크롬드라이버 경로 설정

        WebDriverManager.chromedriver().setup();// 크롬드라이버 자동 셋업

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");// 로봇으로 인식하지 않게 만드는 코드

        WebDriver driver = new ChromeDriver();


        try {
            driver.get("https://www.instagram.com/accounts/login/"); // 인스타그램 로그인 페이지 접속
            System.out.println("인스타그램 접속");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15)); // 페이지가 완전히 로딩될 때까지 기다림

            WebElement username = driver.findElement(By.name("username")); // 아이디 입력창 선택
            WebElement password = driver.findElement(By.name("password")); // 비밀번호 입력창 선택

            //dongja971002@gmail.com
            //dongja1002@gmail.com
            //dongja12345

            username.sendKeys("dongja971002@gmail.com"); // 아이디 입력
            password.sendKeys("dongja12345"); // 비밀번호 입력

            driver.findElement(By.xpath("//div[text()='로그인']")).click();// 로그인 버튼 클릭
            System.out.println("로그인 중....");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));


            driver.findElement(By.xpath("//div[text()='나중에 하기']")).click();// 나중에하기 클릭
            Thread.sleep(1000);

            WebElement modalCloseButton = driver.findElement(By.cssSelector("._a9--._a9_1"));// 알림설정 나중에하기 클릭
            modalCloseButton.click();

            Thread.sleep(1000);


            // ----------------------------- 여기까지 로그인 ---------------------------------------------------


            String url = "https://www.instagram.com/explore/tags/식단/"; 	// #오운완 해시태그 URL
            driver.get(url);	// 해당 URL로 이동
            System.out.println("태그 검색 결과창으로 이동");


            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15)); // 15초안에 페이지 렌더링 완료시, 바로넘어감.


            WebElement firstImg = driver.findElement(By.cssSelector("._aagw")); // 첫번째 게시물 클릭
            firstImg.click();
            System.out.println("첫번째 게시물 클릭...");

            Thread.sleep(1000);
            System.out.println("게시물 크롤링 시작");



// =======================================================================================================



            ExecutorService executor = Executors.newSingleThreadExecutor();
            Random random = new Random();

            // 쓰레드 슬립 시간 난수로 지정(봇 추적 회피)
            int randomSleep = random.nextInt(1000);
            // 511번째 글, id는 508번까지
            for (int i = 508; i < 600; i++) {// 게시물 몇개 탐색할지

                List<String> imageUrls = new ArrayList<>(); // 이미지 URL을 담는 리스트 생성

                // 이미지url 여러개 가져오기
                Future<List<WebElement>> future = executor.submit(new Callable<List<WebElement>>() {
                    @Override
                    public List<WebElement> call() throws Exception {
                        return driver.findElements(By.cssSelector("li._acaz img"));
                    }
                });
                try {

                    List<WebElement> postImages = future.get(2, TimeUnit.SECONDS);

                    for (WebElement image : postImages) {
                        String imageUrl = image.getAttribute("src");
                        imageUrls.add(imageUrl);
                        System.out.println("Image URL: " + imageUrl);
                        Thread.sleep(randomSleep);
                    }

                } catch (java.util.concurrent.TimeoutException e) {
                    System.out.println("예외처리 발생, 단일이미지 검색 코드 실행");


                    WebElement postImage = driver.findElement(By.cssSelector("div.x1cy8zhl.x9f619.x78zum5.xl56j7k.x2lwn1j.xeuugli.x47corl img"));
                    String imageUrl = postImage.getAttribute("src");
                    imageUrls.add(imageUrl);
                    System.out.println("Image URL: " + imageUrl);


                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

                try {

                     // content 가져오기
                    WebElement postContent = driver.findElement(By.cssSelector("div._a9zs > h1"));
                    String contentText = postContent.getText();
                    System.out.println("Post Content: " + contentText);

                    // nickname 가져오기
                    WebElement nickname = driver.findElement(By.cssSelector("div.xt0psk2 > a"));
                    String nicknameText = nickname.getText();
                    System.out.println("nickname: " + nicknameText);

                    // 좋아요 난수
                    long likes = (long)random.nextLong(1000);
                    long userId = i + 1;
                    String title;
                    // title을 content의 20자까지만 제한시키기.
                    if (contentText.length() > 20) {
                        title = contentText.substring(0, 20);
                    } else {
                        title = contentText;
                    }
                    long viewcount = random.nextLong(1000);
                    long categoryId= 2L;
                    nicknameText = nickname.getText().replace(".", "_");

                    saveData(imageUrls , userId, contentText, nicknameText ,likes, title, viewcount, categoryId);


                    System.out.println("=====================" + (i + 1) + "번째 게시물 저장 완료 " + "=============================");



                } catch (NoSuchElementException e) {
                    System.out.println("No more posts found");
                    break;
                }

                new Actions(driver)
                        .keyDown(Keys.ARROW_RIGHT) // 다음 게시물로 이동
                        .keyUp(Keys.ARROW_RIGHT)
                        .perform();


            }
            executor.shutdown();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
