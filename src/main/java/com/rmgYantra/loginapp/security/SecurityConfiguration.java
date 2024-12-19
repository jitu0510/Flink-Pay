package com.rmgYantra.loginapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/maker/dispute/**").hasAnyRole("HDFCMAKER","ICICIMAKER")
			.antMatchers("/login").hasAnyRole("ADMIN","HDFCMAKER","HDFCCHECKER","ICICIMAKER","ICICICHECKER")
			.antMatchers("/swagger-ui.html").permitAll()
			.antMatchers("/signup").permitAll()
			.antMatchers("/export").permitAll()
			.antMatchers("/exportincsv").permitAll()
			.antMatchers("/add-transaction").permitAll()
			.antMatchers("/exportinpdf").permitAll()
			.antMatchers("/employees","/employees/{empId}","/employee/{userName}","/employees/{empId}","/employees/{empId}","/employees/resetPassword","employee/{employeeId}","/logs").authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().csrf().disable().formLogin().disable(); 
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
