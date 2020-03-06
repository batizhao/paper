package io.github.batizhao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 简单粗粒度管理 jwt refresh token。
 * 当 access token 失效后，用 refresh token 时，达到控制（拉黑、锁定）账号的目的。
 *
 * JWT 本身的优点就是无状态，简化服务端的管理。
 * 如果对 token 管理要求比较高，建议不要使用 JWT，使用普通的 UUID token。
 *
 * @author batizhao
 * @since 2020-03-05
 **/
//@Component
public class ManageableJwtTokenStore extends JwtTokenStore {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTokenStore jdbcTokenStore;

    @Bean
    JdbcTokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    public ManageableJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
        super(jwtTokenEnhancer);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        OAuth2RefreshToken token = jdbcTokenStore.readRefreshToken(tokenValue);
        return token;
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        jdbcTokenStore.storeRefreshToken(refreshToken, authentication);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        jdbcTokenStore.removeRefreshToken(token);
    }

}
