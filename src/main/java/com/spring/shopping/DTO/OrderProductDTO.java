    package com.spring.shopping.DTO;

    import com.spring.shopping.entity.OrderProduct;
    import lombok.*;

    import java.util.List;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public class OrderProductDTO {
        private Long orderProductId;
        private Long orderId;
        private Long productId;
        private Long quantity;


    }
