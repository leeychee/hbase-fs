package org.lychee.fs.hbase.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
	public static final String API_VERSION = "v1";
	public static final String API_DOMAIN = "hfs";
//    public static final String BASE_URI = "http://0.0.0.0:8080/v1/hfs";
	public static URI BASE_URI;

	public static URI getBaseURI(String bindIp, int port) {
		return UriBuilder.fromUri("http://localhost/")
				.host(bindIp).port(port)
				.path("v1").path("hfs")
				.build();
	}
	/**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 * <br/>BindIP = 0.0.0.0
	 * <br/>Port = 8080
	 * @return 
	 */
	public static HttpServer startServer() {
		return startServer("0.0.0.0", 8080);
	}

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 * @param bindIP
	 * @param port
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(String bindIP, int port) {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.lychee.fs.hbase.rest package
        final ResourceConfig rc = new ResourceConfig()
				.packages("org.lychee.fs.hbase.rest")
				.register(MultiPartFeature.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
		BASE_URI = getBaseURI(bindIP, port);
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
		String bindIp = "0.0.0.0";
		int port = 8080;
		if (args != null) {
			if (args.length > 0 && StringUtils.isNotEmpty(args[0])) {
				bindIp = args[0];
			}
			if (args.length > 1 && StringUtils.isNotEmpty(args[1])) {
				port = Integer.decode(args[1]);
			}
		}
        final HttpServer server = startServer(bindIp, port);
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%s/application.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}

