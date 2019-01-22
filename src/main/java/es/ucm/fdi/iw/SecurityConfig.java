package es.ucm.fdi.iw;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * Security configuration.
 * 
 * Most security configuration will appear in this file, but according to 
 * https://spring.io/guides/topicals/spring-security-architecture/, it is not
 * a bad idea to also use method security (via @Secured annotations in methods) 
 * 
 * @author mfreire
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .authorizeRequests()
	            .antMatchers("/css/**", "/js/**", "/img/**", "/").permitAll()
	            .antMatchers("/admin**").hasRole("ADMIN")
	            .anyRequest().authenticated()
	            .and()
	        .formLogin();
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		// by default in Spring Security 5, a wrapped new BCryptPasswordEncoder();
		return PasswordEncoderFactories.createDelegatingPasswordEncoder(); 
	}	
	
	private void addDummyUser(UserDetailsManager manager, String nameAndPass, String ... roles) {
		manager.createUser(User.builder()
        		.username(nameAndPass).password(getPasswordEncoder().encode(nameAndPass))
        		.roles(roles).build());
	}
	
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        addDummyUser(manager, "a", "USER", "ADMIN");
        addDummyUser(manager, "b", "USER");
        addDummyUser(manager, "c", "USER");
        return manager;
    }    
}
