package ar.edu.unnoba.pdyc.mymusic.dto;

import java.io.Serializable;

public class PlaylistDTO implements Serializable {
    private String id;
    private String name;

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
