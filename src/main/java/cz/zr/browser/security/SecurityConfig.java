package cz.zr.browser.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public SecurityConfig() {
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .mvcMatchers("/v1/").permitAll()
      .and()
      .csrf().disable()
      .authorizeRequests().antMatchers("/swagger-ui/", "/v2/api-docs", "/v3/api-docs").permitAll();
  }

}
