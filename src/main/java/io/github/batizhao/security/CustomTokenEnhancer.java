package io.github.batizhao.security;

import io.github.batizhao.constant.SecurityConstants;
import io.github.batizhao.util.ResultEnum;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * AccessToken 附加信息
 * @author batizhao
 * @since 2017/11/8
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>(5);
        PecadoUser user = (PecadoUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put(SecurityConstants.DETAILS_USER_ID, user.getUserId());
        additionalInfo.put(SecurityConstants.DETAILS_USERNAME, user.getUsername());
        additionalInfo.put(SecurityConstants.DETAILS_DEPT_ID, user.getDeptId());
        additionalInfo.put("code", ResultEnum.SUCCESS.getCode());
        additionalInfo.put("message", ResultEnum.SUCCESS.getMessage());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
