package tk.youngdk.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    /*

    값 타입은 기본적으로 immutable 하게 설계하자자
    변경 되면 안된다.

    생성자로 데이터를 입력 받고 추가 setter는 모두 닫아준다.

    jpa구현 라이브러리 객체를 생성할때 프록시,리플렉션 기술을 사용할 수 있도록 지원해야 하기에
    기본 생성자를 만들어 주되 protected로 접근자를 설정하여 직접 호출하지 못하도록 한다.
    (jpa 스펙상 protected까지 허용해준다.)

   */
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    private String city;
    private String street;
    private String zipcode;

}
