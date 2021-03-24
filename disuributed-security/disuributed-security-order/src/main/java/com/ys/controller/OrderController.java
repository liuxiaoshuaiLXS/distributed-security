package com.ys.controller;

import com.ys.model.UserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/22 下午11:55
 * @describe $
 */
@RestController
public class OrderController {

    @GetMapping("/r1")
    @PreAuthorize("hasAuthority('p1')")
    public String r1() {
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDto.getFullname() + "访问r1";
    }
}
