package com.github.prgrms.socialserver.controller;

import com.github.prgrms.socialserver.controller.dto.UserDto;
import com.github.prgrms.socialserver.controller.dto.UserJoinRequest;
import com.github.prgrms.socialserver.global.exception.user.UserErrorCode;
import com.github.prgrms.socialserver.global.exception.user.UserException;
import com.github.prgrms.socialserver.service.UserService;
import com.github.prgrms.socialserver.global.response.Response;
import java.net.URI;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;
    private static final String REDIRECT_URL = "/api/users/%d";

    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<Response> getUserList() {
        Response response = Response.success(
            userService.findUsers().stream().map(UserDto::toUserDto).collect(
                Collectors.toList())
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userSeq}")
    public ResponseEntity<Response> getUserDetails(@PathVariable("userSeq") Long userSeq) {
        Response response = Response.success(getUserDto(userSeq));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<Response> join(@Valid @RequestBody UserJoinRequest request,
        Locale locale) {
        Long seq = userService.join(request);
        String redirectUrl = String.format(REDIRECT_URL, seq);
        Response response = Response.success(
            messageSource.getMessage("result.join.success", null, locale)
        );
        return ResponseEntity.created(URI.create(redirectUrl)).body(response);
    }
    private UserDto getUserDto(Long userSeq) {
        return userService.findUser(userSeq).map(UserDto::toUserDto)
            .orElseThrow(() -> new UserException(UserErrorCode.ENTITY_NOT_FOUND));
    }
}
