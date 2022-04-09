package ar.edu.unnoba.pdyc.mymusic.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static ar.edu.unnoba.pdyc.mymusic.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING); //consigo authorization del header
        //si no esta authorization o el header no empieza con el prefijo de token correcto, no lo autorizo
        if (header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request,response); //continuar cadena de filtros
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);  //consigo la auntenticacion
        SecurityContextHolder.getContext().setAuthentication(authentication); //seteo la autenticacion en el contexto de spring security
        chain.doFilter(request,response); //continuo cadena de filtros
    }

    private  UsernamePasswordAuthenticationToken getAuthentication (HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING);
        if(token != null){
            //verificar si el token es valido
            String userEmail = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX,""))
                    .getSubject();  //si el verify se cumple, obtiene el email
            if(userEmail != null){
                return new UsernamePasswordAuthenticationToken(userEmail,null,new ArrayList<>());
            }
            return null;
        }
        return null;
    }

}
