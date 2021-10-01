package jpa.jpa_shop.web.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class PageRequestDTO {

    private int page;
    private int size;
    private String type;
    private String name;

    private void setType(String type) {
        this.type = type;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Builder
    public PageRequestDTO(Integer page, Integer size) {
        if ((page == null || page<1) || (size == null|| size<1)) {
            this.page = 1;
            this.size = 10;
        } else {
            this.page=page;
            this.size=size;
        }
    }

    public Pageable getPageable(Sort sort){

        return PageRequest.of(page -1, size, sort);

    }
}
