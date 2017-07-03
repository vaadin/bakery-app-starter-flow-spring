/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.demo.patientportal;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;

import com.vaadin.flow.demo.patientportal.service.DBInitService;
import com.vaadin.flow.router.RouterConfigurator;
import com.vaadin.hummingbird.ext.spring.SpringAwareConfigurator;
import com.vaadin.hummingbird.ext.spring.VaadinUIScope;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;

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
 *
 * @author Vaadin Ltd
 *
 */
@Configuration
@EnableWebSecurity
@ComponentScan({ "com.vaadin.flow.demo.patientportal", "com.vaadin.flow.demo.patientportal.service" })
@EnableJpaRepositories("com.vaadin.flow.demo.patientportal.repositories")
@EntityScan("com.vaadin.flow.demo.patientportal.entity")
public class PatientApplicationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DBInitService initService;

    /**
     * This is temporary decision to figure out how enable some existing
     * decision voter in the project.
     * <p>
     * AccessDecisionVoter` is required by the {@link SpringAwareConfigurator}
     * class so we need to provide it somehow via configuration. There should be
     * another good way to do it or we should use just a custom
     * {@link RouterConfigurator} for this project.
     */
    private static class FakeVoter implements AccessDecisionVoter<Object> {

        @Override
        public boolean supports(ConfigAttribute attribute) {
            return false;
        }

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public int vote(Authentication authentication, Object object,
                Collection<ConfigAttribute> attributes) {
            return 0;
        }

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/*").permitAll();
    }

    /**
     * Provides a fake decision voter.
     * <p>
     * This is temporary solution. Should be remove in the future. See comments
     * for {@link FakeVoter} above.
     *
     * @return a fake decision voter
     */
    @Bean
    public static FakeVoter voter() {
        return new FakeVoter();
    }

    @PostConstruct
    private void init() {
        initService.initDatabase();
    }

}
