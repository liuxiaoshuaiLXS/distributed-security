package com.ys.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ys.common.Base64Util;
import com.ys.model.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/23 下午4:46
 * @describe $
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //解析出头中的token
        String token = httpServletRequest.getHeader("json-token");
        if (token != null) {
            try {
                System.out.println("拿到的json-token为" + token);
                String json = Base64Util.decodeData(token);
                //将json转json对象
                //取出用户身份
                JSONObject jsonObject = JSON.parseObject(json);
                UserDto userDto = JSON.parseObject(jsonObject.getString("principal"), UserDto.class);
//                UserDto userDto=new UserDto();
//                String principal = jsonObject.getString("principal");
//                userDto.setUsername(principal);
                //取出权限
                JSONArray authoritiesArray = jsonObject.getJSONArray("authorities");
                String[] authorities = authoritiesArray.toArray(new String[authoritiesArray.size()]);
                //将用户信息和权限填充，到用户身份token对象中
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto, null, AuthorityUtils.createAuthorityList(authorities));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                //将对象authenticationToken添加到安全上下文
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
