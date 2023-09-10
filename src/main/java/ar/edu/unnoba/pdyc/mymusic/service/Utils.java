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

    // solucion code scene
    private static class TestObject{
        int a;
        int b;
        int c;
        int d;
        int e;
        int f;
    }

    private static void f1(TestObject testObject){
        if(testObject.a > 10 && testObject.b > 15){
            for(int i = 0; i < 10; i++){
                testObject.c += testObject.a;
                if(testObject.c > 100 && testObject.c != 1000){
                    testObject.c -= testObject.b;
                }
            }
        }
    }

    private static void f2(TestObject testObject){
        if(testObject.d < 10 && testObject.e < 15){
            for(int i = 0; i < 10; i++){
                testObject.c -= testObject.a;
                if(testObject.f < 100 && testObject.c != 2000){
                    testObject.c += testObject.b;
                }
            }
        }
    }

    private static int test(TestObject testObject){
        testObject.c = 1;
        f1(testObject);
        f2(testObject);
        return testObject.c;
    }


}
