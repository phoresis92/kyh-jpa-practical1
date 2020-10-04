package tk.youngdk.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.youngdk.jpashop.domain.item.Book;

import javax.persistence.EntityManager;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, 1L);

        //TX
        book.setName("aaa");

        //변경감지 == dirty checking

        //TX COMMIT
    }
}


/*

준영속 엔티티를 수정하는 2가지 방법
변경 감지 기능 사용
병합( merge ) 사용

*/
