package tk.youngdk.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.youngdk.jpashop.domain.item.Item;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.item = item;
        orderItem.orderPrice = orderPrice;
        orderItem.count = count;

        item.removeStock(count);

        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        // 재고 수량을 원복해준다.
        item.addStock(count);
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
