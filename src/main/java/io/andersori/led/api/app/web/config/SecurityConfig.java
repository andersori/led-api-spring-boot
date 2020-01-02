package io.andersori.led.api.app.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.andersori.led.api.app.web.config.security.UserDetailsServiceImp;
import io.andersori.led.api.app.web.config.security.exception.CustomAccessDenied;
import io.andersori.led.api.app.web.config.security.exception.CustomAuthenticationEntryPoint;
import io.andersori.led.api.app.web.config.security.filter.JWTAuthenticationFilter;
import io.andersori.led.api.app.web.config.security.filter.JWTAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImp userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomAccessDenied accessHandler;

	@SuppressWarnings("unused")
	@Autowired
	private CustomAuthenticationEntryPoint entryHandle;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*http.cors()

				.and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/sign-up").permitAll()

				.anyRequest().authenticated()

				.and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()))

				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()
				.httpBasic()
				.authenticationEntryPoint(entryHandle)
				
				.and()
				.exceptionHandling().accessDeniedHandler(accessHandler);
		*/
		http
        .authorizeRequests()
            .anyRequest().authenticated()
        .and()
        	.addFilter(new JWTAuthenticationFilter(authenticationManager()))
			.addFilter(new JWTAuthorizationFilter(authenticationManager()))
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
			.exceptionHandling().accessDeniedHandler(accessHandler)
        .and()
            .csrf().disable();
	}

}
