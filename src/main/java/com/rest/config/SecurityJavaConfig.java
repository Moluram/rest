package com.rest.config;

import com.rest.security.MySavedRequestAwareAuthenticationSuccessHandler;
import com.rest.security.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.rest.security")
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Autowired
  private MySavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired
  private AccessDeniedHandler accessDeniedHandler;

  @Autowired
  private AuthenticationEntryPoint digestEntryPoint;

  @Autowired
  private DigestAuthenticationFilter digestFilter;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("temporary").password("temporary").roles("ADMIN")
        .and()
        .withUser("user").password("userPass").roles("USER");
  }

  @Bean
  @Autowired
  public DigestAuthenticationFilter digestFilter(
      DigestAuthenticationEntryPoint entryPoint, UserDetailsService service) {

    DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
    filter.setAuthenticationEntryPoint(entryPoint);
    filter.setUserDetailsService(service);
    return filter;
  }

  @Bean
  public DigestAuthenticationEntryPoint digestEntryPoint() {
    DigestAuthenticationEntryPoint auth = new DigestAuthenticationEntryPoint();
    auth.setKey("acegi");
    auth.setRealmName("Contacts Realm via Digest Authentication");
    return auth;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
          .exceptionHandling()
          .authenticationEntryPoint(digestEntryPoint)
        .and()
          .addFilter(digestFilter)
          .httpBasic()
        .and()
          .authorizeRequests()
          .antMatchers("/admin/*").hasAnyRole("ROLE_ADMIN")
          .antMatchers("/api/foos").authenticated()
        .and()
          .formLogin()
          .successHandler(authenticationSuccessHandler)
          .failureHandler(new SimpleUrlAuthenticationFailureHandler())
        .and()
          .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
        .and()
          .logout();
  }
}
