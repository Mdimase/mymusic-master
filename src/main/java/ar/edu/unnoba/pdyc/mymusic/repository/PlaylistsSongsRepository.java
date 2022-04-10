package ar.edu.unnoba.pdyc.mymusic.repository;

import ar.edu.unnoba.pdyc.mymusic.model.PlaylistsSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistsSongsRepository extends JpaRepository<PlaylistsSongs,Long> {

    @Query("select pl.id from PlaylistsSongs pl where pl.playlist.id=:plId")
    List<Long> getIdByPlaylistId(@Param("plId") long id);

    @Query("select pl.id from PlaylistsSongs pl where pl.playlist.id=:plId and pl.song.id=:sId")
    Long getIdByPlaylistIdAndSongId (@Param("plId") long plId, @Param("sId") long sId);

}
