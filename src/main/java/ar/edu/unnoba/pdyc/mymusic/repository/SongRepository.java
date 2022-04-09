package ar.edu.unnoba.pdyc.mymusic.repository;

import ar.edu.unnoba.pdyc.mymusic.model.Genre;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("songRepository")
public interface SongRepository extends JpaRepository<Song,Long>{

    /*
    @Query("select s from Song s where s.author=:author and s.genre =:genre")
    public List<Song> getSongsByAuthorGenre(@Param("author") String author, @Param("genre") Genre genre);
    */

    List<Song> findByAuthorAndGenre(String author,Genre genre);

}
