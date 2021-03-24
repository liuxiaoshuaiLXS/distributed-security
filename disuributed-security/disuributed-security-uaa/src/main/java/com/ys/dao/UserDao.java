package com.ys.dao;

import com.ys.model.PermissionDto;
import com.ys.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/21 下午6:52
 * @describe $
 */
@Repository
public class UserDao {


    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserDto getUserByUserName(String s) {
        String sql = "select id,username,password,fullname,mobile from t_user where username=?";

        List<UserDto> list = jdbcTemplate.query(sql, new Object[]{s}, new BeanPropertyRowMapper<>(UserDto.class));
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }


    public List<String> findPermissByUserId(int userId) {
        String sql = "select tp.* from t_permission tp left join \n" +
                "t_role_permission trp on tp.id=trp.permission_id\n" +
                "left join t_role tr on trp.role_id=tr.id\n" +
                "left join t_user_role tur on tr.id=tur.role_id\n" +
                "left join t_user tu on tur.user_id=tu.id where tu.id=?";
        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(PermissionDto.class));
        List<String> permissions = new ArrayList<>();
        list.forEach(c -> permissions.add(c.getCode()));
        return permissions;
    }
}
