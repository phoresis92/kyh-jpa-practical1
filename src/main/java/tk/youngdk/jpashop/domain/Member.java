package tk.youngdk.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

//    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    /*
    엔티티에 프레젠테이션계층을 위한 로직이 추가되면 안된다!
    */
//    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<Order>();

    public void update(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
