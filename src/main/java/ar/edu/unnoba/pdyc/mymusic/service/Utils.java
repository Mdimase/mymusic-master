package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Utils {

    public String getEmailLogged(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //contexto de seguridad de spring
        return (String) auth.getPrincipal();   //email del usuario loggeado
    }

    public boolean isLogged(){
        return SecurityContextHolder.getContext().getAuthentication().getCredentials() != null;
    }

}
