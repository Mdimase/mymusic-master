package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.exception.NotFoundException;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    @Async("taskExecutor")
    public CompletableFuture<User> newUserAsync(String email, String password) throws DataIntegrityViolationException{
       return CompletableFuture.supplyAsync(()->{
           User user = new User(email,new BCryptPasswordEncoder().encode(password));
           userRepository.save(user);
           return user;
       });
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(long id)throws NotFoundException{
        if(!userRepository.existsById(id)){
            throw new NotFoundException("Recurso inexistente");
        }
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.findByEmail(email);
    }

    // testing code scene
    public static void test(int a, int b, int c, int d, int e, int f){
        System.out.println("Testing");
    }

}
