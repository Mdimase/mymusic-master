package ar.edu.unnoba.pdyc.mymusic.security;

import ar.edu.unnoba.pdyc.mymusic.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;    //para encryptar la password en la BD

    public SecurityConfiguration (UserService userService,BCryptPasswordEncoder bCryptPasswordEncoder ){
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //sobrescribo el metodo configure de spring security, para adaptarlo a jwt
    // determino que recursos seran publicos y cuales seran privados(con proteccion via filtros)
    @Override
    protected void configure (HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable().authorizeRequests()
                //.antMatchers(HttpMethod.POST, "/login").permitAll()      //publico
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/app/signup").permitAll()
                .antMatchers("/**").fullyAuthenticated()   //cualquier otra peticion requiere auntenticacion
                .and()
                // filtros que debera cumplir una peticion para tener acceso al servicio
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //esto desactiva la creacion de sessions en spring security. trabajamos sin sesiones
    }

    // clase de capa de servicio que me permite gestionar los usuarios y el mecanismo de encryptacion para el password
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.addExposedHeader("Authorization");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    // test error code scene quality gate
    public static int validateGate(int a,int b, int c,int d, int e, int f){
        if(a > 10 && b < 15) {
            for (int i = 0; i < 10; i++) {
                c += a;
                if (c > 100) {
                    if (c == 1000) {
                        break;
                    }
                    c -= b;
                }
            }
        }
        return c;
    }

}
