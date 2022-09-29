package com.github.prgrms.socialserver.user.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.socialserver.controller.UserController;
import com.github.prgrms.socialserver.controller.dto.UserJoinRequest;
import com.github.prgrms.socialserver.controller.dto.UserResult;
import com.github.prgrms.socialserver.service.UserService;
import com.github.prgrms.socialserver.global.exception.user.UserErrorCode;
import com.github.prgrms.socialserver.user.UserConstantForTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class UserControllerIntegrationTest extends UserConstantForTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;


    @DisplayName("유저 저장 - 성공")
    @Test
    void joinUserTest() throws Exception {
        //given
        UserJoinRequest request = getUserJoinRequest();

        //when
        ResultActions perform = mockMvc.perform(
            post(BASE_URL + "/join")
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().isCreated())
            .andExpect(handler().handlerType(UserController.class))
            .andExpect(handler().methodName("join"))
            .andExpect(success())
            .andExpect(jsonPath("$.data").value(UserResult.JOIN_SUCCESS.getMessage()));
    }

    @DisplayName("유저 저장시 메일 중복 - 예외")
    @Test
    void joinUserEmailInvalidTest() throws Exception {
        //given
        UserJoinRequest request = getUserJoinRequest();
        userService.join(getUserJoinRequest());

        //when
        ResultActions perform = mockMvc.perform(
            post(BASE_URL + "/join")
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andDo(print()).andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(UserController.class))
            .andExpect(handler().methodName("join"))
            .andExpect(fail())
            .andExpect(jsonPath("$.message").value(UserErrorCode.INVALID_EMAIL.getMessage()));
    }

    @DisplayName("유저 조회 - 성공")
    @Test
    void getUserTest() throws Exception {
        //given
        Long userSeq = userService.join(getUserJoinRequest());

        //when
        ResultActions perform = mockMvc.perform(get(BASE_URL + "/" + userSeq));

        //then
        perform.andDo(print()).andExpect(status().isOk())
            .andExpect(handler().handlerType(UserController.class))
            .andExpect(handler().methodName("getUserDetails"))
            .andExpect(success())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.seq").value(userSeq))
            .andExpect(jsonPath("$.data.email").value(PRINCIPAL));
    }

    @DisplayName("유저 조회 실패 - 예외 ")
    @Test
    void getUserFailTest() throws Exception {
        //given
        Long seq = 1000L;

        //when
        ResultActions perform = mockMvc.perform(get(BASE_URL + "/" + seq));

        //then
        perform.andDo(print()).andExpect(status().is4xxClientError())
            .andExpect(handler().handlerType(UserController.class))
            .andExpect(handler().methodName("getUserDetails"))
            .andExpect(fail())
            .andExpect(jsonPath("$.message").value(UserErrorCode.ENTITY_NOT_FOUND.getMessage()));
    }

    @DisplayName("유저 목록 조회 - 성공")
    @Test
    @Sql("classpath:data/data.sql")
    void getUserListTest() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(get(BASE_URL));

        //then
        perform.andDo(print()).andExpect(status().isOk())
            .andExpect(handler().handlerType(UserController.class))
            .andExpect(handler().methodName("getUserList"))
            .andExpect(success())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data[0].seq").value(TEST_USER_SEQ))
            .andExpect(jsonPath("$.data[0].email").value("test1@test.com"))
            .andExpect(jsonPath("$.data[1].seq").value(2L))
            .andExpect(jsonPath("$.data[1].email").value("test2@test.com"));
    }

    private static ResultMatcher success() {
        return jsonPath("$.result").value("SUCCESS");
    }

    private static ResultMatcher fail() {
        return jsonPath("$.result").value("FAIL");
    }
}
