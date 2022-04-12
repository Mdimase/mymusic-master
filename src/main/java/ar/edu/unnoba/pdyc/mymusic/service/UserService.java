package ar.edu.unnoba.pdyc.mymusic.service;

import ar.edu.unnoba.pdyc.mymusic.exception.NotFoundException;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import org.hibernate.HibernateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.concurrent.CompletableFuture;

public interface UserService extends UserDetailsService {
    CompletableFuture<User> newUserAsync(String email, String password) throws DataIntegrityViolationException;
    User findByEmail(String email);
    User findById(long id)throws NotFoundException;
}
