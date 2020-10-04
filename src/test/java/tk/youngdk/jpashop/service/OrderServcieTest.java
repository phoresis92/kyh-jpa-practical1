package tk.youngdk.jpashop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tk.youngdk.jpashop.domain.Address;
import tk.youngdk.jpashop.domain.Member;
import tk.youngdk.jpashop.domain.Order;
import tk.youngdk.jpashop.domain.OrderStatus;
import tk.youngdk.jpashop.domain.item.Book;
import tk.youngdk.jpashop.exception.NotEnoughStockException;
import tk.youngdk.jpashop.repository.OrderRepository;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServcieTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderServcie orderService;
    @Autowired
    OrderRepository orderRepository;

    final int STOCK_QUANTITY = 10;
    final int ORDER_COUNT = 2;
    final int PRICE = 10000;

    @Test
    @DisplayName("상품주문")
    public void order() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook(PRICE, STOCK_QUANTITY, "JPA");

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), ORDER_COUNT);

        //then
        Order resultOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, resultOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, resultOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야한다.");
        assertEquals(PRICE * ORDER_COUNT, resultOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(STOCK_QUANTITY - ORDER_COUNT, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");

    }

    @Test
    @DisplayName("상품주문_재고수량초과")
    public void stockQuantityOverflow() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook(PRICE, STOCK_QUANTITY, "JPA");

        final int ORDER_COUNT = 11;

        //when

        //then
        assertThrows(NotEnoughStockException.class, () -> {
            Long orderId = orderService.order(member.getId(), book.getId(), ORDER_COUNT);
        });
    }

    @Test
    @DisplayName("주문취소")
    public void cancelOrder() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook(PRICE, STOCK_QUANTITY, "JPA");

        final int ORDER_COUNT = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), ORDER_COUNT);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다.");
        assertEquals(STOCK_QUANTITY, book.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");

    }

    private Book createBook(int PRICE, int STOCK_QUANTITY, String name) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(PRICE);
        book.setStockQuantity(STOCK_QUANTITY);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("memberA");
        member.setAddress(new Address("seoul", "street", "123-123"));
        em.persist(member);
        return member;
    }

}