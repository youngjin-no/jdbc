package com.github.prgrms.socialserver.controller.dto;

import com.github.prgrms.socialserver.domain.user.User;
import java.util.Objects;

public class UserDto {
    private final Long seq;
    private final String email;

    public UserDto(Long seq, String email) {
        this.seq = seq;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Long getSeq() {
        return seq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto userDto = (UserDto) o;
        return Objects.equals(seq, userDto.seq) && Objects.equals(email,
            userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return "UserDto{" +
            "seq=" + seq +
            ", email='" + email + '\'' +
            '}';
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder().seq(user.getSeq()).email(user.getEmail()).build();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private Long seq;
        private String email;

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public UserDto build() {
            return new UserDto(seq, email);
        }
    }
}
