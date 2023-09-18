package com.spring.chatbot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/rest/*")
public class ChatBotController {

    @Value("${secretKey}")
    private String secretKey;

    @Value("${apiUrl}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final SignatureGenerator signatureGenerator;

    @Autowired
    public ChatBotController (RestTemplate restTemplate, SignatureGenerator signatureGenerator1) {
        this.restTemplate = restTemplate;
        this.signatureGenerator = signatureGenerator1;
    }

    // 클라이언트의 요청을 받아 처리 후 응답 반환.
    @RequestMapping("chatBot")
    public ResponseEntity<String> chat(@org.springframework.web.bind.annotation.RequestBody String requestBody) {
        jakarta.servlet.http.HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                .getSession();

        Boolean hasReceivedWelcomeMessage = (Boolean) session.getAttribute("hasReceivedWelcomeMessage");
        if (hasReceivedWelcomeMessage == null) {
            hasReceivedWelcomeMessage = false;
            session.setAttribute("hasReceivedWelcomeMessage", false);
        }

        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(requestBody);
        } catch (ParseException e) {
            e.printStackTrace();
            json = new JSONObject();
        }

        String eventType = (String) json.get("event");
        String inputText = "";

        String chatbotMessage = "";

        JSONObject responseObject = new JSONObject();

        if (eventType.equals("open")) {
            System.out.println("클라이언트에서 서버로 메시지가 도착했습니다:" + requestBody);
            responseObject = sendMessageToNaverChatBot(requestBody);
            // 웰컴메시지 이벤트처리
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(responseObject.toString(), headers, HttpStatus.OK);
        } else if (eventType.equals("send")) {
            // 정상 채팅응답
            inputText = (String) json.get("inputText");
            try {
                String apiURL = this.apiUrl;

                URL url = new URL(apiURL);

                String message = getReqMessage(inputText);
                System.out.println("##" + message);
                String encodeBase64String = signatureGenerator.generateSignature(message, this.secretKey);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json;UTF-8");
                con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

                // post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());

                wr.write(message.getBytes("UTF-8"));
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;

                if (responseCode == 200) {
                    System.out.println(con.getResponseMessage());

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String decodedString;
                    while ((decodedString = in.readLine()) != null) {
                        JSONObject chatbotJson = (JSONObject) parser.parse(decodedString);
                        JSONArray bubbles = (JSONArray) chatbotJson.get("bubbles");
                        JSONObject firstBubble = (JSONObject) bubbles.get(0);
                        JSONObject data = (JSONObject) firstBubble.get("data");
                        chatbotMessage = (String) data.get("description");
                        responseObject = createResponseMessage(chatbotMessage);
                    }
                    in.close();

                } else {
                    chatbotMessage = con.getResponseMessage();
                    responseObject = createResponseMessage(chatbotMessage);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(responseObject.toString(), headers, HttpStatus.OK);
    }


    // 챗봇 API 요청 메세지를 생성하는 메서드
    // 사용자의 입력을 기반으로 API 요청에 필요한 JSON 객체 생성
    public static String getReqMessage(String voiceMessage) {
        String requestBody = "";
        try {
            JSONObject obj = new JSONObject();
            long timestamp = new Date().getTime();
            System.out.println("##" + timestamp);
            obj.put("version", "v2");
            obj.put("userId", 1);
            obj.put("timestamp", timestamp);

            JSONObject bubbles_obj = new JSONObject();
            bubbles_obj.put("type", "text");

            JSONObject data_obj = new JSONObject();
            data_obj.put("description", voiceMessage);

            bubbles_obj.put("type", "text");
            bubbles_obj.put("data", data_obj);

            JSONArray bubbles_array = new JSONArray();
            bubbles_array.add(bubbles_obj);

            obj.put("bubbles", bubbles_array);
            if (voiceMessage == null || voiceMessage.isEmpty()) {
                obj.put("event", "open"); // 웰컴메시지
            } else {
                obj.put("event", "send"); // 사용자가 보낸 메시지의 경우
            }
            requestBody = obj.toString();

        } catch (Exception e) {
            System.out.println("## Exception : " + e);
        }
        System.out.println("203 RequestBody" + requestBody);
        return requestBody;
    }

    // 챗봇 응답 메세지를 생성하는 메서드
    // 챗봇에서 받은 응답 (chatBotMessage) 을 기반으로 클라이언트에게 전송할 JSON 객체 생성
    private static JSONObject createResponseMessage(String chatBotMessage) {
        JSONObject response = new JSONObject();

        response.put("version", "v2");
        response.put("userId", 1);
        response.put("timestamp", System.currentTimeMillis());
        response.put("event", "send");

        JSONObject bubble = new JSONObject();
        bubble.put("type", "text");

        JSONObject data = new JSONObject();
        data.put("description", chatBotMessage);

        bubble.put("data", data);

        JSONArray bubbles = new JSONArray();
        bubbles.add(bubble);

        response.put("bubbles", bubbles);
        System.out.println("230 리스폰스? " + response);
        return response;
    }

    // 웰컴메세지 생성 메서드
    private static JSONObject createWelcomeMessage() {
        String welcomeMessage = "안녕하세요";

        return createResponseMessage(welcomeMessage);
    }

    // 네이버 챗봇 API 에 메세지를 전송하는 메서드
    // 입력된 요청 (requestBody) 을 네이버 챗봇 API 로 전송하고, 그 결과를 반환한다.
    public JSONObject sendMessageToNaverChatBot(String requestBody) {
        JSONObject responseObject = null;
        try {
            String apiUrl = this.apiUrl;
            String signature = signatureGenerator.generateSignature(requestBody, this.secretKey);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json; charset=utf-8");
            headers.add("X-NCP-CHATBOT_SIGNATURE", signature);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST,
                    requestEntity, String.class);
            JSONParser parser = new JSONParser();

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                System.out.println("네이버 챗봇 응답 :" + responseBody);
                responseObject = (JSONObject) parser.parse(responseBody);
            } else {
                System.out.println("네이버 챗봇 응답 에러 :" + response.getStatusCode());
                responseObject = new JSONObject();
                responseObject.put("error", "챗봇에서 응답하지 않습니다. " + response.getStatusCode());
            }

            // 웰컴 메세지를 생성하여 응답에 추가하기
            JSONObject welcomeMessage = createWelcomeMessage();
            JSONArray bubbles = new JSONArray();
            bubbles.add(welcomeMessage);
            responseObject.put("bubbles", bubbles);

        } catch (Exception e) {
            System.out.println("네이버 챗봇 요청 실패: " + e.getMessage());
            responseObject = new JSONObject();
            responseObject.put("error", "네이버 챗봇 서버 요청 실패: " + e.getMessage());
        }
        if (responseObject != null) {
            System.out.println("성공적으로 응답을 받았습니다: " + responseObject);
        } else {
            System.out.println("응답을 받지 못했습니다.");
        }
        return responseObject;
    }




}