package com.github.prgrms.socialserver.repository;

import java.util.List;

import java.util.Optional;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.github.prgrms.socialserver.domain.user.User;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate template;

    public UserRepositoryImpl(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Long insert(User user) {
        String sql =
            "insert into users(email, passwd, login_count, last_login_at, create_at) "
                + "values (:email, :passwd, :loginCount, :lastLoginAt, :createAt) ";

        SqlParameterSource param = new BeanPropertySqlParameterSource(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, param, keyHolder, new String[]{"seq"});
        long userSeq = keyHolder.getKey().longValue();

        return userSeq;
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from Users";
        return template.query(sql, userRowMapper());
    }

    @Override
    public Optional<User> findBySeq(Long seq) {
        String sql = "select * from Users where seq = :seq";
        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("seq", seq);
        return getObjectWithParam(sql, param);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "select * from users where email = :email";
        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("email", email);
        return getObjectWithParam(sql, param);
    }

    private Optional<User> getObjectWithParam(String sql, SqlParameterSource param) {
        try{
            return Optional.ofNullable(template.queryForObject(sql, param, userRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User(
                rs.getLong("seq"),
                rs.getString("email"),
                rs.getString("passwd"),
                rs.getInt("login_count"),
                rs.getTimestamp("last_login_at").toLocalDateTime(),
                rs.getTimestamp("create_at").toLocalDateTime()
            );
            return user;
        };
    }

    private User getUser(User user, long key) {
        return User.builder()
            .seq(key)
            .email(user.getEmail())
            .passwd(user.getPasswd())
            .loginCount(user.getLoginCount())
            .lastLoginAt(user.getLastLoginAt())
            .createAt(user.getCreateAt())
            .build();
    }
}
