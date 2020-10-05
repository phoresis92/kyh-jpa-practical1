package tk.youngdk.jpashop.repository.order.simplequery;

import lombok.Data;
import tk.youngdk.jpashop.domain.Address;
import tk.youngdk.jpashop.domain.Order;
import tk.youngdk.jpashop.domain.OrderStatus;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName(); //Lazy 초기화
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress(); //Lazy 초기화
    }

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId ;
        this.name = name ; //Lazy 초기화
        this.orderDate = orderDate ;
        this.orderStatus = orderStatus ;
        this.address = address ; //Lazy 초기화
    }
}
