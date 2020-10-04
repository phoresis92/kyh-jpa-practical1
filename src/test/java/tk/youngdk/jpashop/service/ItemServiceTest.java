package tk.youngdk.jpashop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tk.youngdk.jpashop.domain.item.Album;
import tk.youngdk.jpashop.domain.item.Item;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    ItemService itemService;

    @Test
    @DisplayName("아이템 저장")
    public void itemSave() throws Exception {
        //given
        Album itemA = new Album();

        itemA.setArtist("artistA");
        itemA.setEtc("etc");

        //when
        itemService.saveItem(itemA);

        //then
        assertEquals(itemA, itemService.findOne(itemA.getId()));

    }

}