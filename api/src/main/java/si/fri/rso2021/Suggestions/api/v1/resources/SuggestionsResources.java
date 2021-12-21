package si.fri.rso2021.Suggestions.api.v1.resources;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso2021.Suggestions.services.v1.config.RestProperties;
import si.fri.rso2021.Suggestions.models.v1.objects.Customers;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/suggestions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST")
public class SuggestionsResources {

    private Logger log = Logger.getLogger(SuggestionsResources.class.getName());

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    String url = "";

    private List<Customers> makeListRequest(String type, String urlparam) throws IOException {
        String dburl = restProperties.getCustomersURL();
        log.info("STARTING " + type + " REQUEST " + dburl);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        List<Customers> customers = mapper.readValue(responseStream, new TypeReference<>(){});
        return customers;
    }

    private String makeObjectRequest(String type, String urlparam) throws IOException {
        String dburl = restProperties.getCustomersURL();
        log.info("STARTING FIRST " + type + "REQUEST " + dburl);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        InputStream responseStream = con.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        Customers customer = mapper.readValue(responseStream, Customers.class);
        return customer.getPostcode()+", "+ customer.getTown();
    }

    private String makeLocationRequest(String type, String urlparam) throws IOException {
        String dburl = "https://atlas.microsoft.com/search/address/json?&subscription-key=aE9xmxoCJYiA4iR68peVW3FYLVFfenEVz_2VxrO4JUE&api-version=1.0&language=en-US&query="+urlparam;
        log.info("STARTING" + type + "REQUEST " + dburl);
        URL url = new URL(dburl + urlparam);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("accept", "application/json");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        String [] content = new String[0];
        int count = 0;
        int pos = 0;
        while ((inputLine = in.readLine()) != null) {
            if(inputLine.contains("position")) {
                count = 2;
            }
            if(count > 0){
                if(inputLine.contains("lat:")){
                    inputLine = inputLine.replace("lat:","");
                    inputLine = inputLine.replace(",","");
                    inputLine = inputLine.replaceAll(" ","");
                }
                if(inputLine.contains("lon:")){
                    inputLine = inputLine.replace("lon:","");
                    inputLine = inputLine.replaceAll(" ","");
                }
                content[pos] = (inputLine);
                pos++;
            }

        }
        in.close();
        log.info(content[0] +", " + content[1]);
        return content[0] +", " + content[1];
    }


    @GET
    @Path("/{id}")
    public Response getCustomerLocationByID(@PathParam("id") Integer id) throws IOException {

        String c = makeObjectRequest("GET", String.format("/%d", id));
        String coordinates = makeLocationRequest("GET", c);
        if (c == "") {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(c).build();
    }

}
