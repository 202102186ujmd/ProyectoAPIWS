package ws;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import modelo.dao.daoUsuario;
import modelo.entidad.Usuario;

@Path("Login")
public class IniciarSesion {

    @Context
    private UriInfo context;

    public IniciarSesion() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validar(Usuario usr) {
        daoUsuario du = new daoUsuario();
        boolean status = du.validar(usr.getUsername(), usr.getPassword());
        if (status) {
            String secret = "miClave";//cambiar
            long tiempo = System.currentTimeMillis();
            String jwt = Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .setSubject("Pedro Navarrete")
                    .setIssuedAt(new Date(tiempo))
                    .setExpiration(new Date(tiempo + 900000)) // 900000 milisegundos es equivalente a 15 minutos
                    .claim("email", "Pedronavarrete@gmail.com")
                    .compact();
            JsonObject json = Json.createObjectBuilder().add("JWT", jwt).build();
            return Response.status(Response.Status.CREATED).entity(jwt).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}