package ws;
import java.util.Set;

@jakarta.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends jakarta.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.IniciarSesion.class);
        resources.add(ws.ServicioCliente.class);
        resources.add(ws.ServicioReserva.class);
        resources.add(ws.ServicioRoom.class);
        resources.add(ws.ServicioUsuario.class);
    }
    
}
