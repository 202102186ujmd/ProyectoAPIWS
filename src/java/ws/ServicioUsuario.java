package ws;

import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Base64;
import modelo.dao.daoUsuario;

@Path("Usuarios")
public class ServicioUsuario {

    @Context
    private UriInfo context;

    public ServicioUsuario() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListaUsuarios(@HeaderParam("Authorization") String token) {
        String datos[] = token.split("\\.");
        Base64.Decoder decodificador = Base64.getUrlDecoder();
        try {
            Jwts.parser().setSigningKey("miClave").parseClaimsJws(token);
            System.out.println("Esta Autorizado");
            System.out.println("Payload" + new String(decodificador.decode(datos[1])));
            daoUsuario dc = new daoUsuario();
            return Response.ok(dc.getAll()).build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("No Esta Autorizado");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}
