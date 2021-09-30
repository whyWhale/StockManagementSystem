package jpa.jpa_shop.Config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/","/member/login","/member/signUp","/h2-console/**","/js/**","/css/**","/test","/testall").permitAll()
                .antMatchers(HttpMethod.POST,"/api/member").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/Manager/**").hasRole("MANAGER")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/member/login").defaultSuccessUrl("/")
                .and()
                .formLogin().loginProcessingUrl("/loginAction").defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutSuccessUrl("/member/login");
    }


}
