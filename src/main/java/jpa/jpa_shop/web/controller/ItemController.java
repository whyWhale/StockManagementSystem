package jpa.jpa_shop.web.controller;

import jpa.jpa_shop.domain.item.Album;
import jpa.jpa_shop.domain.item.Book;
import jpa.jpa_shop.domain.item.Movie;
import jpa.jpa_shop.service.IFS.ItemServiceIFS;
import jpa.jpa_shop.web.dto.PageRequestDTO;
import jpa.jpa_shop.web.dto.request.item.AlbumSaveRequestDto;
import jpa.jpa_shop.web.dto.request.item.BookSaveRequestDto;
import jpa.jpa_shop.web.dto.request.item.MovieSaveRequestDto;
import jpa.jpa_shop.web.dto.response.item.AlbumUpdateResponseDto;
import jpa.jpa_shop.web.dto.response.item.BookUpdateResponseDto;
import jpa.jpa_shop.web.dto.response.item.MovieUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/item")
@Controller
public class ItemController {

    private final ItemServiceIFS itemService;
    // Get
    @GetMapping("")
    public String createForm(Model model)
    {
        return "item/create";
    }


    // saveForm

    @GetMapping("/createBook")
    public String CreateBook(Model model)
    {
        model.addAttribute("bookSaveRequestDto", new BookSaveRequestDto());
        return "item/create/createBook";
    }

    @GetMapping("/createAlbum")
    public String CreateAlbum(Model model)
    {
        model.addAttribute("albumSaveRequestDto", new AlbumSaveRequestDto());
        return "item/create/createAlbum";
    }

    @GetMapping("/createMovie")
    public String CreateMovie(Model model)
    {
        model.addAttribute("movieSaveRequestDto", new MovieSaveRequestDto());
        return "item/create/createMovie";
    }


    // list
    @GetMapping("/list")
    public String list(@ModelAttribute("requestDto")PageRequestDTO pageRequestDTO, Model model)
    {
        log.info("Paging ItemList {}",pageRequestDTO);

        model.addAttribute("requestDto", pageRequestDTO);
        model.addAttribute("items", itemService.findItems(pageRequestDTO));
        return "item/itemList";
    }

    // updateForm
    @GetMapping("/book/{itemId}")
    public String updateFormBook(@PathVariable("itemId") Long itemId,Model model){
        Book book=(Book) itemService.findById(itemId);
        model.addAttribute("updateBook",book.toDto());
        return "item/update/updateBook";
    }

    @GetMapping("/movie/{itemId}")
    public String updateFormMovie(@PathVariable("itemId") Long itemId,Model model){
        Movie movie=(Movie) itemService.findById(itemId);
        model.addAttribute("updateMovie",movie.toDto());
        return "item/update/updateMovie";
    }

    @GetMapping("/album/{itemId}")
    public String updateFormAlbum(@PathVariable("itemId") Long itemId,Model model){
        Album album=(Album) itemService.findById(itemId);
        model.addAttribute("updateAlbum",album.toDto());
        return "item/update/updateAlbum";
    }


    // Create POST
    @PostMapping("/book")
    public String save(@Valid BookSaveRequestDto bookSaveRequestDto, BindingResult result)
    {
        if(result.hasErrors())
        {
            return "/item/create/createBook";
        }
        itemService.saveItem(bookSaveRequestDto.toEntity());
        return "redirect:/";
    }

    @PostMapping("/album")
    public String save(@Valid AlbumSaveRequestDto albumSaveRequestDto, BindingResult result)
    {
        if(result.hasErrors())
        {
            return "/item/create/createAlbum";
        }
        itemService.saveItem(albumSaveRequestDto.toEntity());
        return "redirect:/";
    }

    @PostMapping("/movie")
    public String save(@Valid MovieSaveRequestDto movieSaveRequestDto, BindingResult result)
    {
        if(result.hasErrors())
        {
            return "/item/create/createMovie";
        }
        itemService.saveItem(movieSaveRequestDto.toEntity());
        return "redirect:/";
    }

    // Update Post
    @PostMapping("/book/{itemId}")
    public String updateBook(@Valid @ModelAttribute("updateBook") BookUpdateResponseDto bookUpdateResponseDto,BindingResult result){
        if(result.hasErrors())
        {
            return "/item/update/updateBook";
        }
        log.info("update Item -> {}",bookUpdateResponseDto);
        itemService.updateItem(bookUpdateResponseDto.toEntity());
        return "redirect:/item/list";
    }

    @PostMapping("/movie/{itemId}")
    public String updateMovie(@Valid @ModelAttribute(value = "updateMovie") MovieUpdateResponseDto movieUpdateResponseDto,BindingResult result){
        if(result.hasErrors())
        {
            return "/item/update/updateMovie";
        }
        log.info("update Item -> {}",movieUpdateResponseDto);
        itemService.updateItem(movieUpdateResponseDto.toEntity());
        return "redirect:/item/list";
    }

    @PostMapping("/album/{itemId}")
    public String updateAlbum(@Valid @ModelAttribute(value = "updateAlbum")AlbumUpdateResponseDto albumUpdateResponseDto,BindingResult result){
        if(result.hasErrors())
        {
            return "/item/update/updateAlbum";
        }
        itemService.updateItem(albumUpdateResponseDto.toEntity());
        return "redirect:/item/list";
    }




}
