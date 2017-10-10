package com.vaadin.starter.bakery;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.hummingbird.ext.spring.SpringAwareConfigurator;
import com.vaadin.hummingbird.ext.spring.VaadinUIScope;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.util.LocalDateJpaConverter;
import com.vaadin.starter.bakery.repositories.UserRepository;

/**
 * Spring boot application configuration class.
 * <p>
 * For now it does:
 * <ul>
 * <li>Create and configure custom Vaadin UI scope</li>
 * <li>Disables HTTP security (gives access to anyone to any page)</li>
 * <li>Creates a fake AccessDecisionVoter to make
 * {@link SpringAwareConfigurator} happy</li>
 * </ul>
 *
 */
@Configuration
@ComponentScan({
    "com.vaadin.starter.bakery",
    "com.vaadin.starter.bakery.backend.service",
    "com.vaadin.starter.bakery.app",
    "com.vaadin.starter.bakery.app.security",
    "com.vaadin.hummingbird.ext.spring" })
@EnableJpaRepositories(basePackageClasses = { UserRepository.class})
@EntityScan(basePackageClasses={User.class, LocalDateJpaConverter.class})
public class BakeryApplicationConfig extends WebSecurityConfigurerAdapter {

    public static final String APP_URL = "/";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/login?logout";
    public static final String LOGIN_FAILURE_URL = "/login?error";
    public static final String LOGIN_PROCESSING_URL = "/login";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a custom scope configurer to be able to use Vaadin UI scope
     * ({@link UIScope}).
     *
     * @return a Vaadin UI scope configurer
     */
    @Bean
    public static CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer result = new CustomScopeConfigurer();
        result.setScopes(Collections.singletonMap(
                VaadinUIScope.VAADIN_UI_SCOPE_NAME, new VaadinUIScope()));
        return result;
    }

    @PostConstruct
    private void init() {
//        initService.initDatabase();
    }

    private static class AccessDecisionVoterImpl implements AccessDecisionVoter<Object> {

        @Override
        public boolean supports(ConfigAttribute attribute) {
            return false;
        }

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

		@Override
		public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
			Set<String> authenticatedUserRoles = authentication.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
			boolean hasAccess = authenticatedUserRoles.contains(Role.ADMIN) || attributes.stream()
					.map(ConfigAttribute::getAttribute).anyMatch(authenticatedUserRoles::contains);
			return hasAccess ? ACCESS_GRANTED : ACCESS_ABSTAIN;
		}
    }

    /**
     * Provides a decision voter.
     */
    @Bean
    public static AccessDecisionVoterImpl voter() {
        return new AccessDecisionVoterImpl();
    }
}
