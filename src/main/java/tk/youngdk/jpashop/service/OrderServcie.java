package tk.youngdk.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.youngdk.jpashop.domain.*;
import tk.youngdk.jpashop.domain.item.Item;
import tk.youngdk.jpashop.repository.ItemRepository;
import tk.youngdk.jpashop.repository.MemberRepository;
import tk.youngdk.jpashop.repository.OrderRepository;
import tk.youngdk.jpashop.repository.OrderSearch;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServcie {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     * */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        List<OrderItem> orderItemList = new ArrayList<>();

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.READY);
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem1 = OrderItem.createOrderItem(item, item.getPrice(), count);

        orderItemList.add(orderItem1);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItemList.toArray(new OrderItem[orderItemList.size()]));

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);

        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }

}
