package com.github.prgrms.socialserver.controller.dto;

import javax.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class UserJoinRequest {

    @Email(message = "{email}")
    @Length(min = 1, max = 50, message = "{length.principal}")
    private String principal;
    @Length(min = 1, max = 80, message = "{length.credentials}")
    private String credentials;

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }

    public UserJoinRequest(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String principal;
        private String credentials;

        public Builder principal(String principal) {
            this.principal = principal;
            return this;
        }

        public Builder credentials(String credentials) {
            this.credentials = credentials;
            return this;
        }

        public UserJoinRequest build() {
            return new UserJoinRequest(principal, credentials);
        }
    }


    @Override
    public String toString() {
        return "UserJoinRequest{" +
            "principal='" + principal + '\'' +
            ", credentials='" + credentials + '\'' +
            '}';
    }
}
