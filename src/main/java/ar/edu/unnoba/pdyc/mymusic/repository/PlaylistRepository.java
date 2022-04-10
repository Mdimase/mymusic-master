package ar.edu.unnoba.pdyc.mymusic.repository;

import ar.edu.unnoba.pdyc.mymusic.model.Playlist;
import ar.edu.unnoba.pdyc.mymusic.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.ws.rs.Path;
import java.util.List;

@Repository("playlistRepository")
public interface PlaylistRepository extends JpaRepository<Playlist,Long>{
    
    @Query("select s from Playlist p inner join PlaylistsSongs pl on (p.id=pl.playlist.id) inner join Song s on (s.id=pl.song.id) where p.id=:id")
    List<Song> getSongsByPlaylistId(@Param("id") long id);

    @Query ("select p.name from Playlist p where p.id=:id")
    String getNameById(@Param("id") long id);

    @Query ("select p.user.id from Playlist p where p.id=:id")
    long getOwner(@Param("id") long id);
}
