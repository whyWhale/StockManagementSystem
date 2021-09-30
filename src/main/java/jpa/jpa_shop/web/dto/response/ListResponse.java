package jpa.jpa_shop.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ListResponse<T> {
    private int count;
    private List<T> data;

}

