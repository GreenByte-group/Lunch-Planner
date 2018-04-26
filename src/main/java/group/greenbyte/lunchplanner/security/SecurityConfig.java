package group.greenbyte.lunchplanner.security;

import group.greenbyte.lunchplanner.security.handler.AjaxAuthenticationFailureHandler;
import group.greenbyte.lunchplanner.security.handler.AjaxAuthenticationSuccessHandler;
import group.greenbyte.lunchplanner.security.handler.AjaxLogoutSuccessHandler;
import group.greenbyte.lunchplanner.security.handler.Http401UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    final AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    final AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    final Http401UnauthorizedEntryPoint authenticationEntryPoint;

    final AuthentificationProvider authProvider;

    final SecurityProperties security;

    final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    public SecurityConfig(AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler, AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler, AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler, Http401UnauthorizedEntryPoint authenticationEntryPoint, AuthentificationProvider authProvider, SecurityProperties security, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) {
        this.ajaxAuthenticationSuccessHandler = ajaxAuthenticationSuccessHandler;
        this.ajaxAuthenticationFailureHandler = ajaxAuthenticationFailureHandler;
        this.ajaxLogoutSuccessHandler = ajaxLogoutSuccessHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authProvider = authProvider;
        this.security = security;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                    .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                    .antMatchers("/user", "/login", "/registration").permitAll()
                    //.antMatchers("/**").authenticated()
                    .and()
                .formLogin()
                    .defaultSuccessUrl("/event")
                    .loginPage("/login")
                    .successHandler(ajaxAuthenticationSuccessHandler)
                    .failureHandler(ajaxAuthenticationFailureHandler)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and()
                .logout()
                    .logoutSuccessUrl("/login?logout")
                    .and()
                // by default uses a Bean by the name of corsConfigurationSource
                .headers()
                    .frameOptions().sameOrigin()
                    .httpStrictTransportSecurity().disable();

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }

}
