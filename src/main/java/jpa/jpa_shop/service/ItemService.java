package jpa.jpa_shop.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jpa.jpa_shop.domain.item.*;
import jpa.jpa_shop.domain.repository.AlbumRepository;
import jpa.jpa_shop.domain.repository.BookRepository;
import jpa.jpa_shop.domain.repository.ItemRepository;
import jpa.jpa_shop.domain.repository.MovieRepository;
import jpa.jpa_shop.exception.NoEntity;
import jpa.jpa_shop.service.IFS.ItemServiceIFS;
import jpa.jpa_shop.web.dto.PageRequestDTO;
import jpa.jpa_shop.web.dto.PageResponseDTO;
import jpa.jpa_shop.web.dto.response.item.ItemListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService implements ItemServiceIFS {
    private final ItemRepository itemRepository;
    private final BookRepository bookRepository;
    private final AlbumRepository albumRepository;
    private final MovieRepository movieRepository;

    @Transactional
    @Override
    public Long saveItem(Item item) {
        return itemRepository.save(item).getId();
    }

    @Override
    public List<ItemListResponseDto> findItems(Pageable pageable) {
        return itemRepository.findAll(pageable).stream().map(item -> item.toResponseDTO(item.getClass().getSimpleName().toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<ItemListResponseDto, ? extends Item> findItems(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("modifyLDT").descending());


        if (requestDTO.getType() != null && !requestDTO.getType().trim().equals("")) {
            String type = requestDTO.getType();
            String name = requestDTO.getName();
            BooleanBuilder booleanBuilder=new BooleanBuilder();
            switch (type) {
                case "BOOK":
                    BooleanBuilder booleanBuilder1 = booleanBuilder.and(QBook.book.name.contains(name));
                    Page<Book> pageTypeBook = bookRepository.findAll(booleanBuilder1, pageable);
                    Function<Book, ItemListResponseDto> function1 = (book -> book.toResponseDTO(book.getClass().getSimpleName().toLowerCase()));
                    return new PageResponseDTO<ItemListResponseDto, Book>(pageTypeBook, function1);
                case "MOVIE":
                    BooleanBuilder booleanBuilder2 = booleanBuilder.and(QMovie.movie.name.contains(name));
                    Page<Movie> pageTypeMovie = movieRepository.findAll(booleanBuilder2, pageable);
                    Function<Movie, ItemListResponseDto> function2 = (movie -> movie.toResponseDTO(movie.getClass().getSimpleName().toLowerCase()));
                    return new PageResponseDTO<ItemListResponseDto, Movie>(pageTypeMovie, function2);
                case "ALBUM":
                    BooleanBuilder booleanBuilder3 = booleanBuilder.and(QAlbum.album.name.contains(name));
                    Page<Album> pageTypeAlbum = albumRepository.findAll(booleanBuilder3, pageable);
                    Function<Album, ItemListResponseDto> function3 = album -> album.toResponseDTO(album.getClass().getSimpleName().toLowerCase());
                    return new PageResponseDTO<ItemListResponseDto, Album>(pageTypeAlbum, function3);
            }
        }
        BooleanBuilder booleanBuilder = getSearch(requestDTO); //검색 조건 처리
        Page<Item> pageTypeItem = itemRepository.findAll(booleanBuilder, pageable); //Querydsl 사용
        Function<Item, ItemListResponseDto> fn = (item -> item.toResponseDTO(item.getClass().getSimpleName().toLowerCase()));
        return new PageResponseDTO<>(pageTypeItem, fn);
    }


    public List<ItemListResponseDto> findItems() {
        return itemRepository.findAll().stream().map(item -> item.toResponseDTO(item.getClass().getSimpleName().toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemsToOrder() {
        return itemRepository.findAll();
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(NoEntity::new);
    }

    @Transactional
    @Override
    public Long updateItem(Item item) {
        Item requestItem = itemRepository.findById(item.getId()).orElseThrow(NoEntity::new);
        switch (requestItem.getClass().getSimpleName().toLowerCase())
        {
            case "movie":
                Movie movie = (Movie) requestItem;
                movie.update(item);
                break;
            case "book":
                Book book = (Book) requestItem;
                book.update(item);
                break;
            case "album":
                Album album = (Album) requestItem;
                album.update(item);
                break;
        }
        return requestItem.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Item> deleteItem = itemRepository.findById(id);
        if (deleteItem.isEmpty()) {
            throw new NoEntity("No info");
        }
        itemRepository.delete(deleteItem.get());
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {

        String name = requestDTO.getName();
        return new BooleanBuilder().and(nameContain(name));

    }

    private BooleanExpression nameContain(String name) {
        if (name == null ||!StringUtils.hasText(name)) {
            return null;
        }
        return QItem.item.name.contains(name);
    }
}
