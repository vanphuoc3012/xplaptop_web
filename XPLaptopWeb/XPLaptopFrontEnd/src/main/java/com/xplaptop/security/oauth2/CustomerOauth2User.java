package com.xplaptop.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Getter
public class CustomerOauth2User implements OAuth2User {
    private OAuth2User oAuth2User;
    private String clientName;

    /**
     * @return
     */
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getFullName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }
}
