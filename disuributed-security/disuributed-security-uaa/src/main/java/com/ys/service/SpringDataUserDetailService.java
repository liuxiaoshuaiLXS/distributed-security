package com.ys.service;

import com.alibaba.fastjson.JSON;
import com.ys.dao.UserDao;
import com.ys.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/21 下午5:10
 * @describe $
 */
@Service
public class SpringDataUserDetailService implements UserDetailsService {


    @Autowired
    private UserDao userDao;

    //根据账号插叙用户
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserDto userDto = userDao.getUserByUserName(s);
        if (userDto == null) {
            return null;
        }
        System.out.println("username" + s);
        //
        List<String> permissions = userDao.findPermissByUserId(userDto.getId());
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        //将User转成json
        String principal = JSON.toJSONString(userDto);
        UserDetails user = User.withUsername(principal).password(userDto.getPassword()).authorities(permissionArray).build();
        return user;
    }
}
