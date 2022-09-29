package com.github.prgrms.socialserver.user;

import com.github.prgrms.socialserver.domain.user.User;
import com.github.prgrms.socialserver.controller.dto.UserJoinRequest;
import java.time.LocalDateTime;

public class UserConstantForTest {
    public static final String BASE_URL = "/api/users";
    public static final String PRINCIPAL = "test@test.com";
    public static final String CREDENTIALS = "test";
    public static final long TEST_USER_SEQ = 1L;
    public static final String APPLICATION_JSON = "application/json";

    protected static User getUser() {
        return User.builder()
            .seq(TEST_USER_SEQ)
            .email("test@test.com")
            .passwd("test")
            .loginCount(0)
            .lastLoginAt(LocalDateTime.now())
            .createAt(LocalDateTime.now())
            .build();
    }

    protected static UserJoinRequest getUserJoinRequest() {
        return UserJoinRequest.builder()
            .principal(PRINCIPAL)
            .credentials(CREDENTIALS)
            .build();
    }
}
