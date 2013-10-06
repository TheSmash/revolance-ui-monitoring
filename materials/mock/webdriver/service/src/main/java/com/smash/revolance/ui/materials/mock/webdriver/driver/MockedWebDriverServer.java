package com.smash.revolance.ui.materials.mock.webdriver.driver;


import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * User: wsmash
 * Date: 28/09/13
 * Time: 18:38
 */
public class MockedWebDriverServer
{
    private Server server;

    public MockedWebDriverServer(int port)
    {
        ServletContainer container = new ServletContainer();
        ServletHolder h = new ServletHolder( container );

        h.setInitParameter( "com.sun.jersey.config.property.packages", "com.smash.revolance.ui.materials.mock" );
        h.setInitParameter( "com.sun.jersey.api.json.POJOMappingFeature", "com.sun.jersey.api.json.POJOMappingFeature" );

        server = new Server( port );
        ServletContextHandler context = new ServletContextHandler( ServletContextHandler.SESSIONS );
        context.setContextPath( "/" );
        server.setHandler( context );

        context.addServlet( h, "/*" );
    }

    public void start() throws Exception
    {
        server.start();
    }

    public void stop() throws Exception
    {
        server.stop();
    }


    public static void main(String[] argv) throws Exception
    {
        new MockedWebDriverServer( 9090 ).start();
        Thread.sleep( 3000000 );
    }
}
