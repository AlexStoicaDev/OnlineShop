package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;


/**
 * @EnableAuthorizationServer enables an Authorization Server (i.e. an AuthorizationEndpoint and a TokenEndpoint) in the current application context.
 * class AuthorizationServerConfigurerAdapter implements AuthorizationServerConfigurer which provides all the necessary methods to configure an Authorization server.
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    //provides persistence for tokens
    private final TokenStore tokenStore;

    //Basic interface for determining whether a given client authentication request has been approved by the current user.
    private final UserApprovalHandler userApprovalHandler;

    @Qualifier("authenticationManagerBean")
    private final AuthenticationManager authenticationManager;

    @Value("${spring.security.oauth2.clientId:}")
    private String clientId;

    @Value("${spring.security.oauth2.authorizedGrantTypes:}")
    private String[] authorizedGrantTypes;

    @Value("${spring.security.oauth2.authorities:}")
    private String[] authorities;

    @Value("${spring.security.oauth2.scopes:}")
    private String[] scopes;

    @Value("${spring.security.oauth2.clientSecret:}")
    private String clientSecret;

    @Value("${spring.security.oauth2.accessTokenValiditySeconds:}")
    private int accessTokenValiditySeconds;

    @Value("${spring.security.oauth2.refreshTokenValiditySeconds:}")
    private int refreshTokenValiditySeconds;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(clientId)
            .authorizedGrantTypes(authorizedGrantTypes)
            .authorities(authorities)
            .scopes(scopes)
            .secret(clientSecret)
            .accessTokenValiditySeconds(accessTokenValiditySeconds)
            .refreshTokenValiditySeconds(refreshTokenValiditySeconds);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        });
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler).authenticationManager(authenticationManager)
            .allowedTokenEndpointRequestMethods(HttpMethod.GET);
    }
}
