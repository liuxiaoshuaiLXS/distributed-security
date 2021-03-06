package com.ys.config;

import com.google.inject.internal.cglib.core.$ClassEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/22 ??????10:01
 * @describe ???????????????$
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
        //????????????
//        clients.inMemory()
//                .withClient("c1") //client_id
//                .secret(new BCryptPasswordEncoder().encode("secret"))  //???????????????
//                .resourceIds("res1")  //????????????
//                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")//???client?????????????????????
//                .scopes("all")  //?????????????????????
//                .autoApprove(false)  //false ?????????????????????
//                //????????????????????????
//                .redirectUris("http://www.baidu.com");
    }

    //????????????????????????????????????
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    //??????????????????
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService); //?????????????????????
        service.setSupportRefreshToken(true);   //????????????????????????
        service.setTokenStore(tokenStore);  //??????????????????
        //????????????
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        service.setAccessTokenValiditySeconds(7200); //?????????????????????2??????
        service.setRefreshTokenValiditySeconds(259200); //???????????????????????????3???
        return service;
    }

//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices() { //?????????????????????????????????
//        return new InMemoryAuthorizationCodeServices();
//    }

    //??????????????????????????????
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    //??????????????????
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)  //??????????????????
                .authorizationCodeServices(authorizationCodeServices)  //?????????????????????
                .tokenServices(tokenService())   //??????????????????
                .allowedTokenEndpointRequestMethods(HttpMethod.POST); //??????post??????

    }

    //??????????????????????????????
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients(); //??????????????????,????????????
    }

//    public static void main(String[] args) {
//        String str="secret";
//        String hashpw = BCrypt.hashpw(str, BCrypt.gensalt());
//        System.out.println(hashpw);
//    }
}
