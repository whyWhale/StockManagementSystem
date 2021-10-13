package jpa.jpa_shop.web.controller.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpa.jpa_shop.service.MemberService;
import jpa.jpa_shop.web.dto.request.member.MemberSaveRequestDto;
import jpa.jpa_shop.web.dto.request.member.MemberUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(MemberApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class MemberApiControllerTest {
    @MockBean
    private MemberService memberService;

    @Autowired
    MockMvc mvc;

    private JacksonTester<MemberSaveRequestDto> jsonMemberSaveRequestDto;
    private JacksonTester<MemberUpdateRequestDto> jsonMemberUpdateRequestDto;
    private MemberSaveRequestDto saveDto;
    private MemberUpdateRequestDto updateDto;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
        saveDto = MemberSaveRequestDto.builder()
                .username("admin@naver.com")
                .password("admin")
                .name("admin")
                .detail("test")
                .street("test")
                .zipcode("t1-2e-3s-4t")
                .build();
        log.info("dto : {}", saveDto);

        updateDto = MemberUpdateRequestDto.builder()
                .name("testUpdate")
                .detail("testUpdate")
                .street("testUpdate")
                .zipcode("testUpdate").build();
    }

    @Test
    public void saveTest() throws Exception {
        //given
        //when
        final MockHttpServletResponse response = mvc.perform(
                post("/api/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMemberSaveRequestDto.write(saveDto).getJson())
        ).andDo(MockMvcResultHandlers.print()).andReturn().getResponse();

        //then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(roles="USER")
    public void updateTest() throws Exception {
        //given
        //when
        final MockHttpServletResponse response = mvc.perform(
                put("/api/member/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMemberUpdateRequestDto.write(updateDto).getJson())
        ).andDo(MockMvcResultHandlers.print()).andReturn().getResponse();

        //then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser(roles="USER")
    public void deleteTest() throws Exception {
        //given
        //when
        final MockHttpServletResponse response = mvc.perform(
                delete("/api/member/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()).andReturn().getResponse();

        //then
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}