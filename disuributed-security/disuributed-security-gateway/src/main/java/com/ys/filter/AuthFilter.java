package com.ys.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ys.common.Base64Util;
import com.ys.common.EncryptUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/23 下午3:40
 * @describe $
 */
public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //转发token
        //获取当前的用户身份信息
        //获取当前用户的权限信息
        //把身份信息，权限信息放在json中，加入到http的header
        RequestContext ctx = RequestContext.getCurrentContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2Authentication)) {
            //无token访问网关内资源的情况，目前仅有uaa服务直接暴露
            return null;
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        //
        String principal = userAuthentication.getName();
        //Object principal = userAuthentication.getPrincipal();
        List<String> authorities = new ArrayList<>();
        userAuthentication.getAuthorities().stream().forEach(s -> authorities.add(((GrantedAuthority) s).getAuthority()));
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String, Object> jsonToken = new HashMap<>(requestParameters);
        if (userAuthentication != null) {
            jsonToken.put("principal", principal);
            jsonToken.put("authorities", authorities);
        }
        /**
         * 组装明文token,转发给微服务，放入header,名称为json-token
         */
        ctx.addZuulRequestHeader("json-token", Base64Util.encodeData(JSON.toJSONString(jsonToken)));
        return null;
    }
}
