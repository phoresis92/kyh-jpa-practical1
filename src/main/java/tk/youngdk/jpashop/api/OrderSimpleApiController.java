package tk.youngdk.jpashop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.youngdk.jpashop.domain.Order;
import tk.youngdk.jpashop.repository.OrderSearch;
import tk.youngdk.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import tk.youngdk.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import tk.youngdk.jpashop.service.OrderServcie;

import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * 1 + N + N 으로 총 5번의 쿼리가 나간
 * 1 + 2 + 2
 * Order (2건)
 * Order -> Member
 * Order -> Delivery
 *
 *
 * 쿼리 방식 선택 권장 순서
 * 1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
 * 2. 필요하면 페치 조인으로 성능을 최적화 한다. => 대부분의 성능 이슈가 해결된다.
 * 3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
 * 4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.
 * */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderServcie orderServcie;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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
    public List<OrderSimpleQueryDto> ordersV2(){
        List<Order> orders = orderServcie.findOrders(new OrderSearch());

        List<OrderSimpleQueryDto> collect = orders.stream()
                .map(order -> new OrderSimpleQueryDto(order))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> ordersV3(){
        List<Order> orders = orderServcie.findAllWithMemberDelivery();

        List<OrderSimpleQueryDto> collect = orders.stream()
                .map(OrderSimpleQueryDto::new)
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        List<OrderSimpleQueryDto> orders = orderSimpleQueryRepository.findOrderDtos();

        return orders;
    }

}
