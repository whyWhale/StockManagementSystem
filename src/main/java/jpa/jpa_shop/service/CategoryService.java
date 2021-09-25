package jpa.jpa_shop.service;

import jpa.jpa_shop.domain.category.Category;
import jpa.jpa_shop.domain.repository.CategoryRepository;
import jpa.jpa_shop.web.dto.response.category.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;



    public CategoryResponseDto createCategoryList() {
        List<Category> categories = categoryRepository.findAll();

        Map<Long,List<CategoryResponseDto>> groupingByParent= categories
                .stream()
                .map(category -> CategoryResponseDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .parentId(category.getParent().getId())
                        .build()).collect(Collectors.groupingBy(CategoryResponseDto::getParentId));
        CategoryResponseDto root = CategoryResponseDto.builder().name("ROOT").id(-1L).parentId(null).build();
        addSubCategories(root,groupingByParent);
        return root;
    }
    private void addSubCategories(CategoryResponseDto parent, Map<Long,List<CategoryResponseDto>> groupingByParent)
    {
        List<CategoryResponseDto> categoryResponseDtos = groupingByParent.get(parent.getId());

        if(categoryResponseDtos==null)
            return;

        parent.setSubCategory(categoryResponseDtos);

        categoryResponseDtos.forEach(categoryResponseDto -> {
            addSubCategories(categoryResponseDto,groupingByParent);
        });
    }



}
