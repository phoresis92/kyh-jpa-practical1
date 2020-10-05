package tk.youngdk.jpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.youngdk.jpashop.domain.Address;
import tk.youngdk.jpashop.domain.Order;
import tk.youngdk.jpashop.domain.OrderStatus;
import tk.youngdk.jpashop.repository.OrderSearch;
import tk.youngdk.jpashop.service.OrderServcie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * 1 + N + N 으로 총 5번의 쿼리가 나간
 * 1 + 2 + 2 
 * Order (2건)
 * Order -> Member
 * Order -> Delivery
 * */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderServcie orderServcie;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> orders = orderServcie.findOrders(new OrderSearch());

        // 사용자가 직접 강제로 Lazy 로딩을 호출한다.
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }

        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<Order> orders = orderServcie.findOrders(new OrderSearch());

        List<SimpleOrderDto> collect = orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());

        return collect;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName(); //Lazy 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress(); //Lazy 초기화
        }
    }


}
