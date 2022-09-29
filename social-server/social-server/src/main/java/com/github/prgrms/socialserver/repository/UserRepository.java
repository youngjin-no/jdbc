package com.github.prgrms.socialserver.repository;

import java.util.List;

import com.github.prgrms.socialserver.user.domain.User;
import java.util.Optional;

public interface UserRepository {
	public Long insert(User user);

	public List<User> findAll();

	public Optional<User> findBySeq(Long seq);

	public Optional<User> findByEmail(String email);
}
