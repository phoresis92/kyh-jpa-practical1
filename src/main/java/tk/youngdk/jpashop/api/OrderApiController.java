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

}
