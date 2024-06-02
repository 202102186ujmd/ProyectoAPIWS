package ws;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import modelo.dao.daoCliente;
import modelo.entidad.Cliente;

@Path("Clientes")
public class ServicioCliente {

    @Context
    private UriInfo context;

    public ServicioCliente() {
    }

    private boolean validarToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            Jwts.parser().setSigningKey("miClave").parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            return false;
        } catch (Exception e) {
            System.out.println("Token parsing error: " + e.getMessage());
            return false;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListaClientes(@HeaderParam("Authorization") String token) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoCliente dc = new daoCliente();
        return Response.ok(dc.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertarCliente(@HeaderParam("Authorization") String token, Cliente cliente) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }
        System.out.println("Token válido: " + token);
        System.out.println("Actualizando cliente: " + cliente);

        daoCliente dc = new daoCliente();
        int resultado = dc.insertarCliente(cliente);
        if (resultado > 0) {
            System.out.println("Cliente creado exitosamente.");
            return Response.status(Response.Status.CREATED).entity("Client created successfully").build();
        } else {
            System.out.println("Error al crear cliente.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating client").build();
        }
    }

    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarCliente(@HeaderParam("Authorization") String token, Cliente cliente) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        System.out.println("Token válido: " + token);
        System.out.println("Actualizando cliente: " + cliente);

        daoCliente dc = new daoCliente();
        int resultado = dc.actualizarCliente(cliente);
        if (resultado > 0) {
            System.out.println("Cliente actualizado exitosamente.");
            return Response.status(Response.Status.OK).entity("Client updated successfully").build();
        } else {
            System.out.println("Error al actualizar cliente.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating client").build();
        }
    }
    

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarCliente(@HeaderParam("Authorization") String token, @PathParam("id") int id) {
        if (!validarToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }

        daoCliente dc = new daoCliente();
        int resultado = dc.eliminarCliente(id);
        if (resultado > 0) {
            return Response.status(Response.Status.OK).entity("Client deleted successfully").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting client").build();
        }
    }
}
