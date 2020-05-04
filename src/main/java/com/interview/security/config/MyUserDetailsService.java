package com.interview.security.config;

import com.interview.security.bean.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author kj
 * @Date 2020/4/28 16:31
 * @Version 1.0
 */
@Component
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String sql = "select username,password,authority from " +
                "authentication where username = ?";
        List<MyUser> users = jdbcTemplate.query(sql, new Object[]{username}, new RowMapper<MyUser>() {
            @Override
            public MyUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyUser user = new MyUser();
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAuthority(rs.getString("authority"));
                return user;
            }
        });

        if(users == null || users.size() != 1) {
            System.out.println("不存在此用户");
            throw new UsernameNotFoundException("不存在此用户");
        }

        MyUser user = users.get(0);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        User u = new User(user.getUserName(), user.getPassword(),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthority()));

        return u;
    }
}
