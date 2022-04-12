package ar.edu.unnoba.pdyc.mymusic;

import ar.edu.unnoba.pdyc.mymusic.resource.PlaylistResource;
import ar.edu.unnoba.pdyc.mymusic.resource.SongResource;
import ar.edu.unnoba.pdyc.mymusic.resource.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@Configuration
@ApplicationPath("/mymusic")
public class JerseyConfig extends ResourceConfig
{
    // listado de resources de la app
    public JerseyConfig()
    {
        register(SongResource.class);
        register(PlaylistResource.class);
        register(UserResource.class);
    }
}
