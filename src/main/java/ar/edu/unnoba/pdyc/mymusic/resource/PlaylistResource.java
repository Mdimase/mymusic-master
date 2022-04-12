package ar.edu.unnoba.pdyc.mymusic.resource;

import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistWithSongsDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.PlaylistsSongsDTO;
import ar.edu.unnoba.pdyc.mymusic.dto.SongDTO;
import ar.edu.unnoba.pdyc.mymusic.exception.NotFoundException;
import ar.edu.unnoba.pdyc.mymusic.exception.UnauthorizedException;
import ar.edu.unnoba.pdyc.mymusic.model.User;
import ar.edu.unnoba.pdyc.mymusic.service.PlaylistService;
import ar.edu.unnoba.pdyc.mymusic.service.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

/*
*   TO DO
*   HACER EL SIGNUP ENDPOINT
*   EL GET ALL PLAYLISTS SI DEVUELVE TODAS O SOLO LAS USER LOGGED?
*   EL PAGINADO
*
* */

/*
GET http://localhost:8080/mymusic/playlists                   lista de playlists (sin canciones)
GET http://localhost:8080/mymusic/playlists/:id               info de una playlist (con canciones)
POST http://localhost:8080/mymusic/playlists                  nueva playlist
PUT http://localhost:8080/mymusic/playlists/:id               modificar nombre de la playlist = id
DELETE http://localhost:8080/mymusic/playlists/:id            borra la playlist = id
POST http://localhost:8080/mymusic/playlists/:id/songs        insertar una cancion en una playlist = id
DELETE http://localhost:8080/mymusic/playlists/:id/songs/:id            borra una cancion de un playlist
DELETE http://localhost:8080/mymusic/playlists/:id            borra una playlist
 */

//el .map(objeto a convertir el cual sirve de fuente de datos, el tipo al que quiero que lo convierta)

@Path("/playlists")
public class PlaylistResource {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private Utils utils;

    //metodo asincronico para obtener todas las playlists
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylist(@Suspended AsyncResponse response) {
        playlistService.getPlaylistsAsync().thenAccept((list) -> {
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<PlaylistDTO>>(){}.getType();
            List<PlaylistDTO> listDto = modelMapper.map(list,listType);
            response.resume(Response.ok(listDto).build());
        });
    }

    // crear una playlist
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createPlaylist(@Suspended AsyncResponse response,PlaylistDTO playlistDTO){
        playlistService.createAsync(playlistDTO,utils.getEmailLogged()).thenAccept((res) ->{
            response.resume(Response.status(Response.Status.CREATED).build());
        });
    }

    //info de una playlist (incluye todas sus canciones)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylistById(@Suspended AsyncResponse response,@PathParam("id") long id){
        ModelMapper modelMapper = new ModelMapper();
        PlaylistWithSongsDTO playlistWithSongsDTO = new PlaylistWithSongsDTO(); //creo el DTO a retornar
        Type listSongDTOType = new TypeToken<List<SongDTO>>(){}.getType(); //armo el tipo lista de songsDTO (sin el id)
        playlistService.getNameByIdAsync(id).thenCombine(playlistService.getSongsByPlaylistIdAsync(id), (name,list)->{
            playlistWithSongsDTO.setPlaylistName(name);  // le seteo el nombre de la playlist
            List<SongDTO> listSongsDTO = modelMapper.map(list,listSongDTOType); // creo la lista de songsdto
            playlistWithSongsDTO.setSongs(listSongsDTO);
            response.resume(Response.ok(playlistWithSongsDTO).build()) ;
            return null;
        }).exceptionally(ex ->{
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return null;
        });
    }

    // modificar el nombre de una playlist
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePlaylist (@Suspended AsyncResponse response,@PathParam("id")long id,PlaylistDTO playlistDTO){
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.updateAsync(id,playlistDTO,userLogged).handle((res,ex) ->{
            if(res != null){
                response.resume(Response.ok().build());
            }
            if(ex.getCause() instanceof NotFoundException){
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
            if(ex.getCause() instanceof UnauthorizedException){
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return null;
        });
    }

    // metodo asincrono para agregar una cancion a una playlist
    @POST
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSongInPlaylist(@Suspended AsyncResponse response,@PathParam("id") long idPlaylist, PlaylistsSongsDTO playlistsSongsDTO){
        long idSong = Long.parseLong(playlistsSongsDTO.getId());
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.addSongAsync(idPlaylist,idSong,userLogged).handle((res,ex) ->{
            if(res!=null){
                response.resume(Response.status(Response.Status.CREATED).build());
            }
            if(ex.getCause() instanceof NotFoundException){
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
            if(ex.getCause() instanceof UnauthorizedException){
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            if(ex.getCause() instanceof DataIntegrityViolationException){
                response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            }
            return null;
        });
    }

    // metodo asincrono para eliminar una cancion a una playlist
    @DELETE
    @Path("/{idPlaylist}/songs/{idSong}")
    public void deleteSongInPlaylist(@Suspended AsyncResponse response,@PathParam("idPlaylist") long idPlaylist,@PathParam("idSong") long idSong){
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.deleteSongAsync(idPlaylist,idSong, userLogged).handle((res,ex) ->{
            if(res != null){
                response.resume(Response.ok().build());
            }
            if(ex.getCause() instanceof NotFoundException){
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
            if(ex.getCause() instanceof UnauthorizedException){
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return null;
        });
    }

    // metodo asincrono para eliminar una playlist
    @DELETE
    @Path("/{idPlaylist}")
    public void delete(@Suspended AsyncResponse response,@PathParam("idPlaylist") long idPlaylist){
        User userLogged = utils.getUserLogged(utils.getEmailLogged());
        playlistService.deletePlaylistAsync(idPlaylist,userLogged).handle((res,ex)->{
            if(res != null){
                response.resume(Response.ok().build());
            }
            if(ex.getCause() instanceof NotFoundException){
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
            if(ex.getCause() instanceof UnauthorizedException){
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return null;
            });
    }
}
