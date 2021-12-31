package si.fri.rso2021.Suggestions.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "Suggestions API", version = "v1",
        contact = @Contact(email = "jf6340@student.uni-lj.si"),
        license = @License(name = "dev"), description = "API for managing suggestions."),
        servers = @Server(url = "http://localhost:8081/"))
@RegisterService(value = "suggestions", ttl = 20, pingInterval = 15, environment = "test", version = "1.0.0", singleton = false)
@ApplicationPath("/v1")
public class SuggestionsApplication extends Application{
}
