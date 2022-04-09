package ar.edu.unnoba.pdyc.mymusic.dto;

public class PlaylistDTO {
    private String name;
    //posible agregada de nombre de usuario owner. preguntarle al profe
    //haciendo un inner join con tabla usuario, y el return de la query deberia ser p.name y u.email

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
