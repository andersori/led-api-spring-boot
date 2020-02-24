package io.andersori.led.api.app.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.andersori.led.api.app.web.config.security.UserDetailsServiceImp;
import io.andersori.led.api.app.web.config.security.exception.CustomAccessDenied;
import io.andersori.led.api.app.web.config.security.exception.CustomAuthenticationEntryPoint;
import io.andersori.led.api.app.web.config.security.filter.JWTAuthenticationFilter;
import io.andersori.led.api.app.web.config.security.filter.JWTAuthorizationFilter;
import io.andersori.led.api.app.web.controller.util.PathConfig;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JWTAuthenticationFilter authenticationFilter;
	
	@Autowired
	private JWTAuthorizationFilter authorizationFilter; 
	
	@Autowired
	private UserDetailsServiceImp userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomAccessDenied accessHandler;

	@Autowired
	private CustomAuthenticationEntryPoint entryHandle;

	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		http
		.csrf().disable()
        .authorizeRequests()
        	.antMatchers(PathConfig.VERSION + PathConfig.PROTECTED_PATH + "/**").hasRole("DEFAULT")
        	.antMatchers(PathConfig.VERSION + PathConfig.ADMIN_PATH + "/**").hasRole("ADMIN")
        	.antMatchers(PathConfig.VERSION + PathConfig.PUBLIC_PATH + "/**").permitAll()
            .anyRequest().authenticated()
        .and()
        	.addFilter(authenticationFilter)
			.addFilterBefore(authorizationFilter, JWTAuthenticationFilter.class)
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.exceptionHandling().accessDeniedHandler(accessHandler).authenticationEntryPoint(entryHandle);
		
//		http
//        .headers()
//        .frameOptions().sameOrigin()
//        .cacheControl();
		
		http.cors().disable();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
        .and()
        .ignoring().antMatchers("/h2-console/**/**");
	}

}
