package jpa.jpa_shop.service.UnitTest;

import jpa.jpa_shop.domain.category.Category;
import jpa.jpa_shop.domain.repository.CategoryRepository;
import jpa.jpa_shop.service.CategoryService;
import jpa.jpa_shop.web.dto.response.category.CategoryResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class unitCategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    List<Category> categories;

    @Before
    public void setUp() throws Exception {
        Category category = Category.builder().name("").build();
        ReflectionTestUtils.setField(category, "id", -1L);
        Category category1 = Category.builder().name("sub1").build();
        Category category2 = Category.builder().name("sub2").build();
        ReflectionTestUtils.setField(category1, "id", 1L);
        ReflectionTestUtils.setField(category2, "id", 2L);
        category.addChildCategory(category1);
        category.addChildCategory(category2);

        Category category3 = Category.builder().name("sub1-1").build();
        Category category4 = Category.builder().name("sub1-2").build();
        Category category5 = Category.builder().name("sub2-1").build();
        Category category6 = Category.builder().name("sub2-2").build();
        ReflectionTestUtils.setField(category3, "id", 3L);
        ReflectionTestUtils.setField(category4, "id", 4L);
        ReflectionTestUtils.setField(category5, "id", 5L);
        ReflectionTestUtils.setField(category6, "id", 6L);
        category1.addChildCategory(category3);
        category1.addChildCategory(category4);

        category2.addChildCategory(category5);
        category2.addChildCategory(category6);

        categories = List.of(category1, category2, category3, category4, category5, category6);
    }

    @Test
    public void createCategoryList() {
        // given
        BDDMockito.given(categoryRepository.findAll()).willReturn(categories);
        // when
        CategoryResponseDto categoryRoot = categoryService.createCategoryList();
        // then
        assertThat(categoryRoot).isNotNull();
        assertThat(categoryRoot.getSubCategory().size()).isEqualTo(2);
        assertThat(categoryRoot.getSubCategory().get(0).getSubCategory().size()).isEqualTo(2);
        assertThat(categoryRoot.getSubCategory().get(0).getSubCategory().get(0).getId()).isEqualTo(3);
        assertThat(categoryRoot.getSubCategory().get(0).getSubCategory().get(1).getId()).isEqualTo(4);
    }
}