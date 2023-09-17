    package com.spring.shopping.controller;

    import com.spring.shopping.DTO.OrderDTO;
    import com.spring.shopping.entity.Order;
    import com.spring.shopping.service.OrderService;
    import com.spring.user.entity.User;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/orders")
    public class OrderController {

        private final OrderService orderService;

        @Autowired
        public OrderController(OrderService orderService) {
            this.orderService = orderService;
        }

        // 주문 생성
        @PostMapping
        public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
            OrderDTO createdOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        }

        // 유저로 주문 조회
        @GetMapping("/user/{userId}")
        public List<OrderDTO> getOrdersByUser(@PathVariable Long userId) {
            // 사용자 정보 가져오는 로직
            User user = orderService.getUserInfo(userId);
            return orderService.getOrdersByUser(user);
        }

        // 주문번호로 주문 조회
        @GetMapping("/{orderId}")
        public Optional<OrderDTO> getOrderById(@PathVariable Long orderId) {
            Order order = orderService.getOrderInfo(orderId);
            return orderService.getOrderById(orderId);
        }

        // 주문상태 업데이트
        @PutMapping("/{orderId}/status/{orderStatus}")
        public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @PathVariable String orderStatus) {
            int rowsAffected = orderService.updateOrderStatus(orderId, orderStatus);

//            if (rowsAffected > 0) {
                return ResponseEntity.ok("주문 상태가 업데이트되었습니다.");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없거나 상태 업데이트에 실패했습니다.");
//            }  예외처리 로직 -> 서비스 레이어로 옮김
        }

        // 주문번호로 주문 삭제
        @DeleteMapping("/{orderId}")
        public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {

            // 주문 삭제 로직을 호출하고 성공 여부를 확인합니다.
            boolean deleted = orderService.deleteOrder(orderId);

//            if (deleted) {
                return ResponseEntity.ok("주문이 삭제되었습니다.");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없거나 삭제에 실패했습니다.");
//            } 예외처리 로직 -> 서비스 레이어로 옮김
        }

        // 결제 금액 검증
//        @PostMapping("/verify-payment")
//        public ResponseEntity<String> verifyPaymentAmount(@RequestBody OrderDTO orderDTO) {
//            Long orderId = orderDTO.getOrderId();
//            Long paymentAmount = orderDTO.getTotalPrice();    // 클라이언트에서 제공한 결제 금액
//            System.out.println(paymentAmount);
//
//            // 주문 정보를 DB에서 가져옴
//            Order order = orderService.getOrderInfo(orderId);
//
//            if (order == null) {
//                // 주문을 찾을 수 없음
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
//            }
//
//            // 주문에 저장된 금액
//            Long actualOrderAmount = orderService.calculateActualOrderAmount(order);
//
//            if (paymentAmount.equals(actualOrderAmount)) {
//                // 결제 금액이 실제 주문 금액과 일치하면 주문 생성
//                OrderDTO createdOrder = orderService.createOrder(orderDTO);
//                return ResponseEntity.status(HttpStatus.CREATED).body("주문이 생성되었습니다.");
//            } else {
//                // 결제 금액이 일치하지 않음
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 금액이 일치하지 않습니다.");
//            }
//        }

    }
