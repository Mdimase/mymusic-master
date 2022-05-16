package ar.edu.unnoba.pdyc.mymusic.security;

import ar.edu.unnoba.pdyc.mymusic.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static ar.edu.unnoba.pdyc.mymusic.security.SecurityConstants.EXPIRATION_TIME;
import static ar.edu.unnoba.pdyc.mymusic.security.SecurityConstants.HEADER_STRING;
import static ar.edu.unnoba.pdyc.mymusic.security.SecurityConstants.SECRET;
import static ar.edu.unnoba.pdyc.mymusic.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private final AuthenticationManager authenticationManager;

    // POST http://localhost:8080/mymusic/app/auth          login

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/app/auth");    // en que ruta interviene este filtro
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationRequestDTO auth = new ObjectMapper().readValue(request.getInputStream(),AuthenticationRequestDTO.class);
            System.out.println(new BCryptPasswordEncoder().encode(auth.getPassword())); // encriptacion de la password
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            auth.getEmail(),
                            auth.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void successfulAuthentication (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // genero el token JWT
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis () + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER_STRING,TOKEN_PREFIX + token);
    }
}
