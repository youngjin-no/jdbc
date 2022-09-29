package com.github.prgrms.socialserver.service;

import com.github.prgrms.socialserver.controller.dto.UserJoinRequest;
import com.github.prgrms.socialserver.domain.user.User;
import com.github.prgrms.socialserver.global.exception.user.UserErrorCode;
import com.github.prgrms.socialserver.global.exception.user.UserException;
import com.github.prgrms.socialserver.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long join(UserJoinRequest request) {
        String email = request.getPrincipal();
        String passwd = request.getCredentials();

        validationEmail(email);
        User user = new User(email, passwd);
        return userRepository.insert(user);
    }


    public Optional<User> findUser(Long seq) {
        return userRepository.findBySeq(seq);
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    private void validationEmail(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new UserException(UserErrorCode.INVALID_EMAIL);
        }
    }

}
