package com.example.demo.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.common.Profiles;
import com.example.demo.security.common.LdapConfig;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Autowired
	private Environment env;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {		
		http
		.csrf().disable()
		.authorizeRequests()
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();

		return http.build();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		if(Profiles.PROD.equalsIgnoreCase(activeProfile)) {
			String ldapUrls = env.getProperty(LdapConfig.ldapUrls);
			String ldapBaseDn = env.getProperty(LdapConfig.ldapBaseDn);
			String ldapUserName = env.getProperty(LdapConfig.ldapUserName);
			String ldapPassword = env.getProperty(LdapConfig.ldapPassword);
			String ldapUserDnPattern = env.getProperty(LdapConfig.ldapUserDnPattern);

			auth
			.ldapAuthentication()
			.contextSource()
			.url(ldapUrls + ldapBaseDn)
			.managerDn(ldapUserName)
			.managerPassword(ldapPassword)
			.and()
			.userDnPatterns(ldapUserDnPattern);
		} else {

			auth
			.ldapAuthentication()
			.userDnPatterns("uid={0},ou=people")
			.groupSearchBase("ou=groups")
			.contextSource()
			.url("ldap://localhost:8389/dc=springframework,dc=org")
			.and()
			.passwordCompare()
			.passwordEncoder(new BCryptPasswordEncoder())
			.passwordAttribute("userPassword");
		}
	}
}