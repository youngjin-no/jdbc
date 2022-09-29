package com.github.prgrms.socialserver.user.domain;

import static java.time.LocalDateTime.*;
import static org.assertj.core.util.Preconditions.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
	private final Long seq;
	private final String email;
	private String passwd;
	private int loginCount;
	private LocalDateTime lastLoginAt;
	private final LocalDateTime createAt;

	public User(String email, String passwd) {
		this(null, email, passwd, 0, null, null);
	}

	public User(Long seq, String email, String passwd, int loginCount, LocalDateTime lastLoginAt,
		LocalDateTime createAt) {
		checkNotNullOrEmpty(email, "email must not be empty");
		checkNotNullOrEmpty(passwd, "passwd must not be empty");

		this.seq = seq;
		this.email = email;
		this.passwd = passwd;
		this.loginCount = loginCount;
		this.lastLoginAt = defaultIfDateNull(lastLoginAt);
		this.createAt = defaultIfDateNull(createAt);
	}

	private LocalDateTime defaultIfDateNull(LocalDateTime date) {
		return date != null ? date : now();
	}

	public void afterLoginSuccess() {
		loginCount++;
		lastLoginAt = now();
	}

	public void changePasswd(String passwd) {
		this.passwd = passwd;
	}

	public Long getSeq() {
		return seq;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswd() {
		return passwd;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public LocalDateTime getLastLoginAt() {
		return lastLoginAt;
	}

	public LocalDateTime getCreateAt() {
		return createAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User)o;
		return loginCount == user.loginCount && Objects.equals(seq, user.seq) && Objects.equals(email,
			user.email) && Objects.equals(passwd, user.passwd) && Objects.equals(lastLoginAt,
			user.lastLoginAt) && Objects.equals(createAt, user.createAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq);
	}

	@Override
	public String toString() {
		return "User{" +
			"seq=" + seq +
			", email='" + email + '\'' +
			", passwd='" + passwd + '\'' +
			", login_count=" + loginCount +
			", lastLoginAt=" + lastLoginAt +
			", createdAt=" + createAt +
			'}';
	}

	public static Builder builder(){
		return new Builder();
	}

	public static class Builder {
		private Long seq;
		private String email;
		private String passwd;
		private int login_count;
		private LocalDateTime lastLoginAt;
		private LocalDateTime createAt;

		public Builder seq(Long seq){
			this.seq = seq;
			return this;
		}

		public Builder email(String email){
			this.email = email;
			return this;
		}

		public Builder passwd(String passwd){
			this.passwd = passwd;
			return this;
		}

		public Builder loginCount(int login_count){
			this.login_count = login_count;
			return this;
		}

		public Builder lastLoginAt(LocalDateTime lastLoginAt){
			this.lastLoginAt = lastLoginAt;
			return this;
		}

		public Builder createAt(LocalDateTime createAt){
			this.createAt = createAt;
			return this;
		}

		public User build() {
			return new User(seq, email, passwd, login_count, lastLoginAt, createAt);
		}
	}
}
