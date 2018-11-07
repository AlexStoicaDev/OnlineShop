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

    @Value("${client.details.id:}")
    private String clientId;

    @Value("${client.details.authorized.grant.types:}")
    private String[] authorizedGrantTypes;

    @Value("${client.details.authorities:}")
    private String[] authorities;

    @Value("${client.details.scopes:}")
    private String[] scopes;

    @Value("${client.details.client.secret:}")
    private String clientSecret;

    @Value("${client.details.access.token.validity.seconds:}")
    private int accessTokenValiditySeconds;

    @Value("${client.details.refresh.token.validity.seconds:}")
    private int refreshTokenValiditySeconds;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(clientId)
            .authorizedGrantTypes(authorizedGrantTypes)
            .authorities(authorities)
            .scopes(scopes)
            .secret(passwordEncoder.encode(clientSecret))
            .accessTokenValiditySeconds(accessTokenValiditySeconds)
            .refreshTokenValiditySeconds(refreshTokenValiditySeconds);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler).authenticationManager(authenticationManager)
            .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);


    }


}
