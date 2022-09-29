package com.github.prgrms.socialserver.user.unit.controller;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
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
import com.github.prgrms.socialserver.domain.user.User;
import com.github.prgrms.socialserver.global.exception.user.UserErrorCode;
import com.github.prgrms.socialserver.global.exception.user.UserException;
import com.github.prgrms.socialserver.service.UserService;
import com.github.prgrms.socialserver.user.UserConstantForTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

@WebMvcTest({UserController.class})
@Profile("test")
public class UserControllerUnitTest extends UserConstantForTest {
    @Autowired
    public MockMvc mockMvc;
    @Autowired
    public ObjectMapper objectMapper;
    @MockBean
    public UserService userService;

    @DisplayName("유저 저장 - 성공")
    @Test
    void joinUserTest() throws Exception {
        //given
        UserJoinRequest request = getUserJoinRequest();
        given(userService.join(any(UserJoinRequest.class))).willReturn(TEST_USER_SEQ);

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
        given(userService.join(any(UserJoinRequest.class))).willThrow(
            new UserException(UserErrorCode.INVALID_EMAIL));

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
        given(userService.findUser(any(Long.class))).willReturn(
            Optional.of(getUser()));

        //when
        ResultActions perform = mockMvc.perform(get(BASE_URL + "/" + TEST_USER_SEQ));

        //then
        perform.andDo(print()).andExpect(status().isOk())
            .andExpect(handler().handlerType(UserController.class))
            .andExpect(handler().methodName("getUserDetails"))
            .andExpect(success())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.seq").value(TEST_USER_SEQ))
            .andExpect(jsonPath("$.data.email").value(PRINCIPAL));
    }

    @DisplayName("유저 조회 실패 - 예외 ")
    @Test
    void getUserFailTest() throws Exception {
        //given
        Long seq = 1000L;
        given(userService.findUser(any(Long.class))).willThrow(
            new UserException(UserErrorCode.ENTITY_NOT_FOUND));

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
    void getUserListTest() throws Exception {
        //given
        List<User> users = new ArrayList<>();
        users.add(getUser());
        users.add(
            User.builder()
                .seq(2L)
                .email("test2@test.com")
                .passwd("test")
                .build()
        );

        given(userService.findUsers()).willReturn(users);

        //when
        ResultActions perform = mockMvc.perform(get(BASE_URL));

        //then
        perform.andDo(print()).andExpect(status().isOk())
            .andExpect(handler().handlerType(UserController.class))
            .andExpect(handler().methodName("getUserList"))
            .andExpect(success())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data[0].seq").value(TEST_USER_SEQ))
            .andExpect(jsonPath("$.data[0].email").value(PRINCIPAL))
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
