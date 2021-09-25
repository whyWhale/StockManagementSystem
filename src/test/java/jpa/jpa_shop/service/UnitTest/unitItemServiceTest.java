package jpa.jpa_shop.service.UnitTest;


import jpa.jpa_shop.domain.item.Album;
import jpa.jpa_shop.domain.item.Book;
import jpa.jpa_shop.domain.item.Item;
import jpa.jpa_shop.domain.item.Movie;
import jpa.jpa_shop.domain.repository.ItemRepository;
import jpa.jpa_shop.exception.NoEntity;
import jpa.jpa_shop.service.ItemService;
import jpa.jpa_shop.web.dto.response.item.AlbumUpdateResponseDto;
import jpa.jpa_shop.web.dto.response.item.BookUpdateResponseDto;
import jpa.jpa_shop.web.dto.response.item.ItemListResponseDto;
import jpa.jpa_shop.web.dto.response.item.MovieUpdateResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class unitItemServiceTest {


    @InjectMocks
    private ItemService itemService;


    @Mock
    private ItemRepository itemRepository;

    private Movie movie;
    private Book book;
    private Album album;


    @Before
    public void ItemData() {
        movie = Movie.builder()
                .actor("송강호")
                .director("봉준호")
                .stockQuantity(10)
                .name("괴물")
                .price(10000)
                .build();

        book = Book.builder()
                .author("구종만")
                .isbn("isbn")
                .name("알고리즘 문제 해결 전략")
                .stockQuantity(10)
                .price(33000)
                .build();

        album = Album.builder()
                .artist("IU")
                .name("밤 편지")
                .price(55000)
                .stockQuantity(10)
                .build();

        ReflectionTestUtils.setField(movie, "id", 1L);
        ReflectionTestUtils.setField(book, "id", 2L);
        ReflectionTestUtils.setField(album, "id", 3L);
    }


    @Test
    public void ItemSave() {
        // given
        given(itemRepository.save(movie)).willReturn(movie);
        given(itemRepository.findById(movie.getId())).willReturn(Optional.of(movie));
        given(itemRepository.save(book)).willReturn(book);
        given(itemRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(itemRepository.save(album)).willReturn(album);
        given(itemRepository.findById(album.getId())).willReturn(Optional.of(album));

        // when
        Long movieItemId = itemService.saveItem(movie);
        Long bookItemId = itemService.saveItem(book);
        Long albumItemId = itemService.saveItem(album);
        Item findMovie = itemService.findById(movieItemId);
        Item findBook = itemService.findById(bookItemId);
        Item findAlbum = itemService.findById(albumItemId);
        // then
        assertThat(movieItemId).isEqualTo(movie.getId());
        assertThat(findMovie.getName()).isEqualTo("괴물");
        assertThat(bookItemId).isEqualTo(book.getId());
        assertThat(findBook.getName()).isEqualTo("알고리즘 문제 해결 전략");
        assertThat(albumItemId).isEqualTo(album.getId());
        assertThat(findAlbum.getName()).isEqualTo("밤 편지");
    }

    @Test
    public void ItemUpdate() {

        // given
        given(itemRepository.findById(movie.getId())).willReturn(Optional.of(movie));
        given(itemRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(itemRepository.findById(album.getId())).willReturn(Optional.of(album));
        int stockQuantity=30;
        int price1=8000;
        int price2= 45000;

        Movie updateMovie = MovieUpdateResponseDto.builder()
                .id(movie.getId())
                .actor("송강호")
                .director("봉준호")
                .stockQuantity(10)
                .name("괴물")
                .price(price1) // 가격 인하
                .build().toEntity();

        Book updateBook = BookUpdateResponseDto.builder()
                .id(book.getId())
                .author("구종만")
                .isbn("isbn")
                .name("알고리즘 문제 해결 전략")
                .stockQuantity(stockQuantity) // 재고 증가
                .price(33000)
                .build().toEntity();

        Album updateAlbum = AlbumUpdateResponseDto.builder()
                .id(album.getId())
                .artist("IU")
                .name("밤 편지")
                .price(price2) // 가격 인하
                .stockQuantity(10)
                .build().toEntity();
        // when
        Long movieId = itemService.updateItem(updateMovie);
        Long bookId = itemService.updateItem(updateBook);
        Long albumId = itemService.updateItem(updateAlbum);

        Optional <Item> findMovie = itemRepository.findById(movieId);
        Optional <Item> findBook = itemRepository.findById(bookId);
        Optional <Item> findAlbum = itemRepository.findById(albumId);
        // then
        assertThat(findMovie.orElseThrow(NoEntity::new).getPrice()).isEqualTo(price1);
        assertThat(findBook.orElseThrow(NoEntity::new).getStockQuantity()).isEqualTo(stockQuantity);
        assertThat(findAlbum.orElseThrow(NoEntity::new).getPrice()).isEqualTo(price2);
    }

    @Test
    public void ItemServiceFindItems() {
        // given
        List<Item> items = itemList();
        given(itemRepository.findAll()).willReturn(items);
        // when

        List<ItemListResponseDto> findItems = itemService.findItems();
        ItemListResponseDto itemListResponseDto1 = findItems.get(0);
        ItemListResponseDto itemListResponseDto2 = findItems.get(1);
        ItemListResponseDto itemListResponseDto3 = findItems.get(2);
        // then
        assertThat(itemListResponseDto1.getName()).isEqualTo(movie.getName());
        assertThat(itemListResponseDto2.getName()).isEqualTo(book.getName());
        assertThat(itemListResponseDto3.getName()).isEqualTo(album.getName());
    }

    private List<Item> itemList() {
        List<Item> items=new LinkedList<>();
        items.add(movie);
        items.add(book);
        items.add(album);
        return items;
    }
}
