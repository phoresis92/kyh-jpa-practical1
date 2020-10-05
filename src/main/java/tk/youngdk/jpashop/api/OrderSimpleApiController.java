package tk.youngdk.jpashop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.youngdk.jpashop.domain.Order;
import tk.youngdk.jpashop.repository.OrderSearch;
import tk.youngdk.jpashop.service.OrderServcie;

import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
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

}
