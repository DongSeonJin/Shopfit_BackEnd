package com.spring.shopping.service;

import com.spring.shopping.DTO.OrderProductDTO;
import com.spring.shopping.entity.Order;
import com.spring.shopping.entity.OrderProduct;
import com.spring.shopping.entity.Product;
import com.spring.shopping.repository.OrderProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderProductServiceTest {

    @Mock
    private OrderProductRepository orderProductRepository;

    @InjectMocks
    private OrderProductServiceImpl orderProductService;


    @Test
    void testGetOrderProductsByOrder() {
        Order testOrder = new Order();
        // orderProductRepository의 findByOrder 메서드가 빈 리스트를 반환하도록 설정
        when(orderProductRepository.findByOrder(testOrder)).thenReturn(Collections.emptyList());

        // getOrderProductsByOrder 메서드를 호출하여 빈 리스트가 반환되는지 확인
        List<OrderProductDTO> orderProducts = orderProductService.getOrderProductsByOrder(testOrder);

        // 반환된 리스트의 크기가 0인지 검증
        assertEquals(0, orderProducts.size());
    }


    @Test
    void testCreateOrderProduct() {
        Order testOrder = new Order();
        Product testProduct = new Product();
        Long quantity = 2L;

        // 기대되는 OrderProduct 객체 생성
        OrderProduct expectedOrderProduct = new OrderProduct(testOrder, testProduct, quantity);
        // ArgumentMatcher를 사용하여 인자를 비교할 수 있도록 준비
        ArgumentMatcher<OrderProduct> orderProductMatcher = new OrderProductMatcher(expectedOrderProduct);

        // createOrderProduct 메서드 호출
        orderProductService.createOrderProduct(testOrder, testProduct, quantity);

        // orderProductRepository의 save 메서드가 ArgumentMatcher와 일치하는 인자로 호출되었는지 검증
        verify(orderProductRepository).save(argThat(orderProductMatcher));
    }


    @Test
    void testAddOrderProduct() {
        Order testOrder = new Order();
        Product testProduct = new Product();
        Long quantity = 3L;
        // 실제로 생성되는 OrderProduct 객체
        OrderProduct savedOrderProduct = new OrderProduct(testOrder, testProduct, quantity);

        // orderProductRepository의 save 메서드가 빈 리스트를 반환하도록 lenient로 설정
        lenient().when(orderProductRepository.save(savedOrderProduct)).thenReturn(savedOrderProduct);

        // addOrderProduct 메서드 호출
        OrderProductDTO orderProductDTO = orderProductService.addOrderProduct(testOrder, testProduct, quantity);

        // 반환된 orderProductDTO의 필드와 기대값이 같은지 검증
        assertEquals(testOrder.getOrderId(), orderProductDTO.getOrderId());
        assertEquals(testProduct.getProductId(), orderProductDTO.getProductId());
        assertEquals(quantity, orderProductDTO.getQuantity());
    }

    // OrderProductService의 removeOrderProduct 메서드를 테스트하는 코드
    @Test
    void testRemoveOrderProduct() {
        Order testOrder = new Order();
        Long productId = 1L;
        // orderProductRepository의 findByOrderOrderIdAndProductProductId 메서드가 빈 OrderProduct 객체를 반환하도록 설정
        when(orderProductRepository.findByOrderOrderIdAndProductProductId(testOrder, productId)).thenReturn(new OrderProduct());

        // removeOrderProduct 메서드 호출
        orderProductService.removeOrderProduct(testOrder, productId);

        // orderProductRepository의 delete 메서드가 인자로 전달한 빈 OrderProduct 객체와 일치하는지 검증
        verify(orderProductRepository).delete(new OrderProduct());
    }

    // OrderProductMatcher 클래스
    // ArgumentMatcher를 사용하여 객체의 내용을 비교하기 위한 클래스
    private record OrderProductMatcher(OrderProduct expectedOrderProduct) implements ArgumentMatcher<OrderProduct> {
        @Override
        public boolean matches(OrderProduct actualOrderProduct) {
            // OrderProduct의 필드 값들을 비교하여 동일한지 확인
            return actualOrderProduct.getOrder().equals(expectedOrderProduct.getOrder()) &&
                    actualOrderProduct.getProduct().equals(expectedOrderProduct.getProduct()) &&
                    actualOrderProduct.getQuantity().equals(expectedOrderProduct.getQuantity());
        }
    }
}
