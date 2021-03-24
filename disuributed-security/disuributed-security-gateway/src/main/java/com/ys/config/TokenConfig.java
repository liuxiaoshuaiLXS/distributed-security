package com.ys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/22 下午10:14
 * @describe 令牌存储策略$
 */
@Configuration
public class TokenConfig {


    private String SIGNING_KEY = "uaa123";

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);  //对称密钥，资源服务器使用该密钥来验证
        return converter;
    }

//    @Bean
//    public TokenStore tokenStore() {
//        //内存方式，生成普通令牌
//        return new InMemoryTokenStore();
//    }
}
