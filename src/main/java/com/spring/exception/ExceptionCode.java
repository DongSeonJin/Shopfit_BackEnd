package com.spring.exception;


// enum을 활용했을 시의 장점 > 일관성 있는 응답 포맷(Response Format)(우리는 status, code, message로 통일)
public enum ExceptionCode { // 예외 발생시, body에 실어 날려줄 상태, code, message 커스텀

    // 예외 이름들 상수로 구분해서 적어줄 것.

    POST_NOT_FOUND(404, "POST_001", ""),
    REPLY_NOT_FOUND(404, "REPLY_001", "해당되는 id의 댓글을 찾을 수 없습니다."),
    ALREADY_LIKED(400, "LIKE_001", "이미 '좋아요'를 누른 상태입니다."),


    /* 예외들은 서로 , 콤마로 구분하고 있으므로 잘 확인하고 작성해주세요. */
// ==================================== community ========================================
    // ==칸으로 구분하여 예외처리 추가 할 것.(이칸은 shopping)


    // ==================================== shopping =========================================
    USER_NOT_FOUND(404,"USER_001","로그인이 필요한 기능입니다.") ;


// ==================================== news =============================================
    EXIST_EMAIL(409, "USER_001", "이미 존재하는 이메일 입니다."),
    PASSWORD_WRONG(400,"USER_002", "비밀번호를 확인해주세요."),

    EXIST_NICKNAME(409,"USER_003", "이미 존재하는 닉네임입니다.");

// ==================================== user ==============================================
    // 1. status = 날려줄 상태코드
    // 2. code = 해당 오류가 어느부분과 관련있는지 카테고리화 해주는 코드. 예외 원인 식별하기 편하기에 추가
    // 3. message = 발생한 예외에 대한 설명.

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}