package tk.youngdk.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;


    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();

        result.stream()
                .forEach(orderDto -> {
                    List<OrderItemQueryDto> orderItems = findOrderItems(orderDto.getOrderId());
                    orderDto.setOrderItems(orderItems);
                });

        /* N + 1 의 문제가 있다! */

        return result;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new tk.youngdk.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        " from Order o " +
                        " join o.member m" +
                        " join o.delivery d "
        , OrderQueryDto.class)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new tk.youngdk.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        " from OrderItem oi " +
                        " join oi.item i " +
                        " where oi.order.id = :orderId "
                , OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

}
