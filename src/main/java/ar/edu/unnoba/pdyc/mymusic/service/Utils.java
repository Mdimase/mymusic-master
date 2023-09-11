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

    public static final Integer PAGE_SIZE = 2;

    @Autowired
    private UserService userService;

    public String getEmailLogged(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //contexto de seguridad de spring
        return auth.getPrincipal().toString();   //email del usuario loggeado
    }

    public User getUserLogged(String email){
        return userService.findByEmail(email);
    }

    public static Integer getOffsetPage(Integer page){
        int offset = 0;
        if(page > 1){
            offset = Utils.PAGE_SIZE * (page - 1);
        }
        return offset;
    }

    // codigo con errores, estado inicial
    public static int test(int a,int b, int c,int d, int e, int f){
        c = 1;
        if(a > 10 && b < 15){
            for(int i = 0; i < 10; i++){
                c += a;
                if(c > 100){
                    if(c == 1000){
                        break;
                    }
                    c -= b;
                }
            }
        }
        if(a < 10 && b < 15){
            for(int i = 0; i < 10; i++){
                c -= a;
                if(c > 100){
                    if(c == 2000){
                        break;
                    }
                    c += b;
                }
            }
        }
        return c;
    }


}
