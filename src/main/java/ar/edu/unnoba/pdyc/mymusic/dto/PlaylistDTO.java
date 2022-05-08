package ar.edu.unnoba.pdyc.mymusic.dto;

import java.io.Serializable;

public class PlaylistDTO implements Serializable {
    private String id;
    private String name;
    //posible agregada de nombre de usuario owner. preguntarle al profe
    //haciendo un inner join con tabla usuario, y el return de la query deberia ser p.name y u.email

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlaylistDTO() {
    }

    public PlaylistDTO(String name) {
        this.name = name;
    }
}
