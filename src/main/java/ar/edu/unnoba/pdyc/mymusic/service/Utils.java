package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.ws.rs.BadRequestException;

@Service
public class Utils {

    public static final Integer PAGE_SIZE = 3;

    @Autowired
    private UserService userService;

    public String getEmailLogged(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //contexto de seguridad de spring
        return auth.getPrincipal().toString();   //email del usuario loggeado
    }

    public User getUserLogged(String email){
        return userService.findByEmail(email);
    }

    public static Integer getOffsetPage(Integer page)throws BadRequestException{
        if(page <= 0){
            throw new BadRequestException();
        }
        int offset = 0;
        if(page > 1){
            offset = Utils.PAGE_SIZE * (page - 1);
        }
        return offset;
    }

}
