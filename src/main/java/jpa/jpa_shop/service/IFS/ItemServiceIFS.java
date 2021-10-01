package jpa.jpa_shop.service.IFS;

import jpa.jpa_shop.domain.item.Item;
import jpa.jpa_shop.web.dto.PageRequestDTO;
import jpa.jpa_shop.web.dto.PageResponseDTO;
import jpa.jpa_shop.web.dto.response.item.ItemListResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemServiceIFS {
    public Long saveItem(Item item);

    public List<ItemListResponseDto> findItems(Pageable pageable);

    public List<ItemListResponseDto> findItems();

    public PageResponseDTO<ItemListResponseDto, ? extends Item> findItems(PageRequestDTO requestDTO);

    public List<Item> findItemsToOrder();

    public Item findById(Long itemId);

    public Long updateItem(Item item);

    public void delete(Long id);
}
