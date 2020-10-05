package tk.youngdk.jpashop.repository;

import lombok.Getter;
import lombok.Setter;
import tk.youngdk.jpashop.domain.OrderStatus;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

}
