package ar.edu.unnoba.pdyc.mymusic.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // el username que espera el spring security, para nosotros es nuestro email
    @Override
    public String getUsername(){
        return this.email;
    }

    public Long getId() {
        return id;
    }

    // roles que tienen los usuarios
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    //nunca van a expirar
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    // nunca se van a bloquear
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    // nunca van a expirar
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    //siempre va a estar habilitado
    @Override
    public boolean isEnabled(){
        return true;
    }

    @Override
    public int hashCode(){
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        } else //mismos id en BD -> mismo objeto
            if (!(obj instanceof User)){ //sino no es un user
            return false;
        } else return ((User) obj).id.equals(this.id);
    }

}
