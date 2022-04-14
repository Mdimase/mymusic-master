package ar.edu.unnoba.pdyc.mymusic.resource;

import ar.edu.unnoba.pdyc.mymusic.dto.SongDTO;
import ar.edu.unnoba.pdyc.mymusic.model.Genre;
import ar.edu.unnoba.pdyc.mymusic.service.SongService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

/*
                        URI area
GET http://localhost:8080/mymusic/app/songs/pageValue?author=value&genre=value      lista de canciones con filtros opcionales
POST http://localhost:8080/mymusic/app/songs                                nueva cancion
PUT http://localhost:8080/mymusic/app/songs/:id                             actualizo la cancion = id
DELETE http://localhost:8080/mymusic/app/songs/:id                          borra la cancion = id
 */

@Path("/songs")
public class SongResource {

    @Autowired
    private SongService songService;

    //metodo asincronico para obtener todas las canciones (con paginacion)
    @GET
    @Path("/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getSongs(@Suspended AsyncResponse response,@PathParam("page") int page,@QueryParam("author") String author, @QueryParam("genre") Genre genre){
        if(page <= 0){
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
        }
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SongDTO>>(){}.getType();
        if(genre==null && author==null){ //sin filtros
            songService.getSongsAsync(page).thenAccept((list) -> {
                List<SongDTO> listDto = modelMapper.map(list, listType);
                response.resume(Response.ok(listDto).build());
            });
        }
        else if(genre == null){ // filtro author
            songService.getSongsAsync(author,page).thenAccept((list) -> {
                List<SongDTO> listDto = modelMapper.map(list, listType);
                response.resume(Response.ok(listDto).build());
            });
        }
        else if(author == null){ // filtro genre
            songService.getSongsAsync(genre,page).thenAccept((list) -> {
                List<SongDTO> listDto = modelMapper.map(list, listType);
                response.resume(Response.ok(listDto).build());
            });
        }
        else{ //filtro genre y author
            songService.getSongsAsync(author,genre,page).thenAccept((list) -> {
                List<SongDTO> listDto = modelMapper.map(list, listType);
                response.resume(Response.ok(listDto).build());
            });
        }
    }

    /*************************
    ejemplos de clase

    //crear una nueva cancion
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSong(SongDTO songDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //contexto de seguridad de spring
        String loggedEmail = (String) auth.getPrincipal();   //email del usuario loggeado
        ModelMapper modelMapper = new ModelMapper();
        Song song = modelMapper.map(songDTO,Song.class);    //mapeo a song los datos recibidos en body de la request
        songService.create(song,loggedEmail);
        return Response.status(Response.Status.CREATED).build();
    }

    //actualizar una cancion
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSong(@PathParam("id") long id,UpdateSongDTO updateSongDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = (String) auth.getPrincipal();
        ModelMapper modelMapper = new ModelMapper();
        Song song = modelMapper.map(updateSongDTO,Song.class);
        try{
            songService.update(id,song,userEmail);
            return Response.ok().build();
        } catch (Exception e){
            return Response.status(Response.Status.FORBIDDEN).build();  //  403 no tenes permiso
        }
    }

    //borrar una cancion
    @DELETE
    @Path("/{id}")
    public Response deleteSong(@PathParam("id") long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = (String) auth.getPrincipal();
        try{
            songService.delete(id,userEmail);
            return Response.ok().build();
        } catch (Exception e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    **************************************************************/
}
