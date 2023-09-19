package com.spring.exception;


// enum을 활용했을 시의 장점 > 일관성 있는 응답 포맷(Response Format)(우리는 status, code, message로 통일)
public enum ExceptionCode { // 예외 발생시, body에 실어 날려줄 상태, code, message 커스텀

    // 예외 이름들 상수로 구분해서 적어줄 것.


    POST_NOT_FOUND(404, "POST_001", "해당되는 id 의 글을 찾을 수 없습니다."),
    REPLY_NOT_FOUND(404, "REPLY_001", "해당되는 id의 댓글을 찾을 수 없습니다."),
    ALREADY_LIKED(400, "LIKE_001", "이미 '좋아요'를 누른 상태입니다."),


    /* 예외들은 서로 , 콤마로 구분하고 있으므로 잘 확인하고 작성해주세요. */
// ==================================== community ========================================
    // ==칸으로 구분하여 예외처리 추가 할 것.(이칸은 shopping)
    PRODUCT_ID_NOT_FOUND(404, "SHOP_001", "해당되는 id의 상품을 찾을 수 없습니다."),
    CART_ID_NOT_FOUND(404, "SHOP_002", "해당되는 id의 장바구니를 찾을 수 없습니다."),
    ORDER_ID_NOT_FOUND(404, "SHOP_003", "해당되는 id의 주문을 찾을 수 없습니다."),
    IMAGE_ID_NOT_FOUND(404, "SHOP_004", "해당되는 id의 이미지를 찾을 수 없습니다."),
    CATEGORY_ID_NOT_FOUND(404, "SHOP_005", "해당되는 id의 카테고리를 찾을 수 없습니다."),
    WISHLIST_ID_NOT_FOUND(404, "SHOP_006", "해당되는 id의 찜을 찾을 수 없습니다"),
    WISHLIST_NOT_FOUND(404, "SHOP_007", "해당하는 찜을 찾을 수 없습니다."),
    ORDER_PRODUCT_NOT_FOUND(404, "SHOP_008", "주어진 order로 주문상품을 찾을 수 없습니다."),
    PRODUCT_CAN_NOT_BE_NULL(400, "SHOP_009", "상품은 null이 될 수 없습니다."),
    ORDER_CAN_NOT_BE_NULL(400, "SHOP_010", "주문은 null이 될 수 없습니다."),
    COUPON_ISSUANCE_EXCEPTION(403,"SHOP_011", "준비된 쿠폰이 모두 소진되었습니다."),
    PRODUCT_SAVE_EXCEPTION(500, "SHOP_012", "상품 저장 중 예외가 발생하였습니다."),
    STOCK_UPDATE_EXCEPTION(500, "SHOP_013", "상품 재고 수정 중 예외가 발생하였습니다."),
    STOCK_QUANTITY_INVALID(400, "SHOP_014", "재고수량은 음수일 수 없습니다."),
    QUANTITY_INVALID(400, "SHOP_015", "수량은 0이거나 음수일 수 없습니다."),
    SORT_INVALID(400, "SHOP_016", "잘못된 정렬 요청입니다."),
    ORDERSTATUS_UPDATE_FAILED(400, "SHOP_017", "주어진 OrderId로 OrderStatus 수정에 실패했습니다."),
    INSUFFICIENT_POINT_EXCEPTION(400, "SHOP_018", "포인트가 부족합니다."),

// ==================================== shopping =========================================
    NEWS_ID_NOT_FOUND(404, "NEWS_001", "해당되는 id의 뉴스를 찾을 수 없습니다."),


// ==================================== news =============================================
    EXIST_EMAIL(409, "USER_001", "이미 존재하는 이메일 입니다."),
    PASSWORD_WRONG(400,"USER_002", "비밀번호를 확인해주세요."),
    EXIST_NICKNAME(409,"USER_003", "이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND(404, "USER_004", "해당 유저를 찾을 수 없습니다."),
    TOKEN_NOT_VALID(401, "TOKEN_001", "토큰이 만료되었습니다. 다시 로그인 해주세요."),
    USER_CAN_NOT_BE_NULL(400, "USER_005", "사용자는 null이 될 수 없습니다."),
    USER_ID_NOT_FOUND(404, "USER_006", "해당되는 id의 사용자를 찾을 수 없습니다.");


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
