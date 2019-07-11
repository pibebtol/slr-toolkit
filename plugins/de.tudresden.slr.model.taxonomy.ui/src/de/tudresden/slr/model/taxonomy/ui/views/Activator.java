package de.tudresden.slr.model.taxonomy.ui.views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.osgi.framework.BundleContext;

import de.tudresden.slr.model.taxonomy.ui.internal.TaxonomyActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends TaxonomyActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.tudresden.slr.model.taxonomy.ui"; //$NON-NLS-1$

	private static String URL;

	private static String WEB_ROOT_LOCATION;

	private static String WEB_APP_WORKSPACE;
	
	private static HandlerCollection HANDLERS;

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * Inherited from TaxonomyActivator and overwritten;
	 * 
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String webappRoot = workspace.getRoot().getLocation().toString().concat("/webapp");
		new File(webappRoot).mkdirs();
		
		Activator.WEB_APP_WORKSPACE = webappRoot;
		
		// create the server on a free port
		Server server = new Server(0);

		// Configure the ResourceHandler. Setting the resource base indicates where the
		// files should be served out of.
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setResourceBase(webappRoot);
		//resource_handler.setWelcomeFiles(new String[] { "index.html" });

		// Add the ResourceHandler to the server.
		HandlerCollection handlers = new HandlerCollection(true);
		handlers.addHandler(resource_handler);

		// server.getConnectors()[0].getConnectionFactory(HttpConnectionFactory.class);

		server.setHandler(handlers);

		// Start server up.
		try {
			server.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Get the URI of the server and set it global for the
		// diagram handlers to access
		if (null != server && null != server.getURI()) {
			String localhost = server.getURI().toString();
			Activator.URL = localhost;
			System.out.println(localhost);
		}

		// provide the handlers for the other classes, to add ressources
		Activator.HANDLERS = handlers;

		// test for relative src path
		URL webRootLocation = Activator.class.getResource("/html/indexCite.html");
		//System.out.println(webRootLocation.toString());
		URI webRootUri = URI.create(webRootLocation.toURI().toASCIIString().replaceFirst("/indexCite.html$", "/"));
		//System.out.println(webRootUri.toASCIIString());
		//resource_handler.setResourceBase(webRootUri.toASCIIString());

		Activator.WEB_ROOT_LOCATION = webRootUri.toASCIIString();

		System.out.println("web root location: " + this.WEB_ROOT_LOCATION);
		System.out.println("web app workspace: " + this.WEB_APP_WORKSPACE);
		
		writeVegaFiles();
		// Get the location to the base directory of the plugin (remove unnecessary
		// head)
		// String location =
		// Platform.getBundle("de.tudresden.slr.model.taxonomy.ui").getLocation().substring(15);
		// location = location.concat("html/");
		// System.out.println(location);

		// resource_handler.setContextPath("/");

		// resource_handler.setDirectoriesListed(true);;
		// String location1 = "html";
		// resource_handler.setResourceBase(location1);

		// Versuch, den ordner auszulesen
		// File file = new
		// File("/home/pibebtole/Workspace/slr-toolkit/plugins/de.tudresden.slr.model.taxonomy.ui/html/");
		/*
		 * File file = new File(webRootLocation.toString()); File[] fileList =
		 * file.listFiles();
		 * 
		 * for (File filename: fileList) {
		 * 
		 * System.out.println(filename); }
		 */

		// meine annahme verfestigt sich, dass ich die files in den Jars auf jeden fall
		// nicht schreiben kann...
		/*
		 * OutputStream outputStream =
		 * webRootLocation.openConnection().getOutputStream(); InputStream inputStream =
		 * webRootLocation.openConnection().getInputStream(); BufferedReader in = new
		 * BufferedReader(new InputStreamReader(inputStream)); String inputLine; Writer
		 * writer = new OutputStreamWriter(outputStream); BufferedWriter out = new
		 * BufferedWriter(writer);
		 * 
		 * while ((inputLine = in.readLine()) != null) { if
		 * (inputLine.contains("unspezifisch")) { out.write(inputLine + "1"); }
		 * out.write(inputLine); }
		 * 
		 * in.close();
		 */

		/*
		 * CsvHandler csvHandler = new CsvHandler(); handlers.addHandler(csvHandler);
		 */

		// HelloHandler helloHandler = new HelloHandler();
		// handlers.addHandler(helloHandler);
		// working
		// start server that serves the graphs on a new available port
		/*
		 * Server server = new Server(0);
		 * 
		 * // Configure the ResourceHandler. Setting the resource base indicates where
		 * the files should be served out of. ResourceHandler resource_handler = new
		 * ResourceHandler(); resource_handler.setDirectoriesListed(false);
		 * //resource_handler.setWelcomeFiles(new String[]{ "index.html" }); // Get the
		 * location to the base directory of the plugin (remove unnecessary head) String
		 * location =
		 * Platform.getBundle("de.tudresden.slr.model.taxonomy.ui").getLocation().
		 * substring(15); location = location.concat("html/");
		 * resource_handler.setResourceBase(location);
		 * 
		 * // Add the ResourceHandler to the server. HandlerCollection handlers = new
		 * HandlerCollection(true); handlers.setHandlers(new Handler[] {
		 * resource_handler, new DefaultHandler() });
		 * 
		 * server.getConnectors()[0].getConnectionFactory(HttpConnectionFactory.class);
		 * HelloHandler helloHandler = new HelloHandler(); //helloHandler.start();
		 * handlers.addHandler(helloHandler);
		 * 
		 * server.setHandler(handlers);
		 * 
		 * // Start server up. try { server.start(); } catch (Exception e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); } // Get the URI of the
		 * server TODO and set it to global variable String localhost =
		 * server.getURI().toString(); Activator.URL = localhost;
		 */

		/*
		 * Server server = new Server(0); try {
		 * server.getConnectors()[0].getConnectionFactory(HttpConnectionFactory.class);
		 * server.setHandler(new HelloHandler());
		 * 
		 * server.start(); } catch (Exception e) { e.printStackTrace(); }type filter
		 * text String localhost = server.getURI().toString(); Activator.URL =
		 * localhost;
		 */

		plugin = this;
	}

	/**
	 * Takes the preconfigured vega source and graph files from within the bundle and writes
	 * them to the workspace folder.
	 * 
	 * @return true, if the file was written successfully
	 * @throws IOException 
	 */
	private void writeVegaFiles() throws IOException {
		writeVegaSource();
		writeBarChartJson();
		writeBubbleChartJson();
	}
	
	private void writeVegaSource() throws IOException {
		InputStream in = getClass().getResourceAsStream("/html/vega.js");
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String st;
	    File writeToFile = new File(this.WEB_APP_WORKSPACE + "/vega.js");
	    PrintWriter writer = new PrintWriter(new FileWriter(writeToFile));
	    while ((st = br.readLine()) != null) {
    		writer.println(st);
	    }
	    writer.close(); 
	}
	
	private void writeBarChartJson() throws IOException {
		InputStream in = getClass().getResourceAsStream("/html/bar.vg.json");
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String st;
	    File writeToFile = new File(this.WEB_APP_WORKSPACE + "/bar.vg.json");
	    PrintWriter writer = new PrintWriter(new FileWriter(writeToFile));
	    while ((st = br.readLine()) != null) {
    		writer.println(st);
	    }
	    writer.close(); 
	}
	
	private void writeBubbleChartJson() throws IOException {
		InputStream in = getClass().getResourceAsStream("/html/bubble.vg.json");
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String st;
	    File writeToFile = new File(this.WEB_APP_WORKSPACE + "/bubble.vg.jsone");
	    PrintWriter writer = new PrintWriter(new FileWriter(writeToFile));
	    while ((st = br.readLine()) != null) {
    		writer.println(st);
	    }
	    writer.close(); 
	}
	
	public static String getUrl() {
		return URL;
	}

	public static String getWebRootLocation() {
		return WEB_ROOT_LOCATION;
	}

	public static String getWebAppWorkspace() {
		return WEB_APP_WORKSPACE;
	}

	public static HandlerCollection getHandlers() {
		return HANDLERS;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}