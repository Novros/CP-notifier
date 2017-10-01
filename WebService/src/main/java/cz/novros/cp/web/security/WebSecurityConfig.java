package cz.novros.cp.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	CpAuthenticationProvider authProvider;
	CustomAccessDeniedHandler accessDeniedHandler;

	@Autowired
	public WebSecurityConfig(final CpAuthenticationProvider authProvider, final CustomAccessDeniedHandler accessDeniedHandler) {
		this.authProvider = authProvider;
		this.accessDeniedHandler = accessDeniedHandler;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// FIXME
		http.csrf().disable();
//				.authorizeRequests()
//				.antMatchers("/", "/home", "/about", "/tracking", "/add-tracking").permitAll()
//				.anyRequest().authenticated()
//				.and()
//				.formLogin()
//				.loginPage("/login")
//				.permitAll()
//				.and()
//				.logout()
//				.permitAll()
//				.and()
//				.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}
}
