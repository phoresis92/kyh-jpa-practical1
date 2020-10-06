package tk.youngdk.jpashop.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.youngdk.jpashop.domain.Address;
import tk.youngdk.jpashop.domain.Order;
import tk.youngdk.jpashop.domain.OrderItem;
import tk.youngdk.jpashop.domain.OrderStatus;
import tk.youngdk.jpashop.repository.OrderRepository;
import tk.youngdk.jpashop.repository.OrderSearch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());

        all.stream()
                .forEach(order -> {
                    order.getMember().getName();
                    order.getDelivery().getAddress();
                    List<OrderItem> orderItems = order.getOrderItems();
                    orderItems.stream()
                            .forEach(orderItem -> orderItem.getItem().getName());
                });

        return all;
    }

    @GetMapping("/api/v2/orders")
    public ReturnDto ordersV2(){
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

        List<OrderDto> collect = orders.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());

        return new ReturnDto(collect, collect.size());
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order){
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName; //상품명
        private int orderPrice; //주문 가
        private int count; //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }

    @Data
    @AllArgsConstructor
    static class ReturnDto<T> {
        T data;
        int count;
    }
}
