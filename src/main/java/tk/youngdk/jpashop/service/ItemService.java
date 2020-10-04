package tk.youngdk.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.youngdk.jpashop.domain.item.Item;
import tk.youngdk.jpashop.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item){
        itemRepository.save(item);
        return item.getId();
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

    @Transactional
    public void updateItem(Long itemId, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);

//        findItem.setPrice(price);
//        findItem.setStockQuantity(stockQuantity);

        /*
        세터를 모두 닫아서
        추후에 유지보수에 있어서 어디서 데이터가 변경되는지 파악할 수 있도록 하

        차라리 아래와 같이 엔티티 내부 로직으로 처리하자!
        * */

        findItem.change(price, stockQuantity);

//        return findItem;

    }
}
