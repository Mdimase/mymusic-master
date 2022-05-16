package ar.edu.unnoba.pdyc.mymusic.repository;

import ar.edu.unnoba.pdyc.mymusic.model.Genre;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("songRepository")
public interface SongRepository extends JpaRepository<Song,Long>{

    List<Song> findByAuthorAndGenre(String author,Genre genre);
    List<Song> findByAuthor(String author);
    List<Song> findByGenre(Genre genre);
    Song findById(long id);

}
