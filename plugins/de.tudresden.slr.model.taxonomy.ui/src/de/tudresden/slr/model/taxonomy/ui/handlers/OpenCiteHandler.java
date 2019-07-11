package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.ui.views.Activator;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.logic.ChartGenerator;
import de.tudresden.slr.ui.chart.logic.TermSort;
import de.tudresden.slr.ui.chart.settings.*;
import de.tudresden.slr.ui.chart.views.ICommunicationView;

public class OpenCiteHandler implements IHandler {

	// TODO: find a better way to store these strings

	private final String chartViewId = "chart.view.chartview";
	private ICommunicationView view;
	private String noDataToDisplay = "Could not create a cite chart. \n Try to select a single Term.";
	
	protected static int numberCalled = 0;

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// get selection
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		Map<String, Integer> data = processSelectionData((IStructuredSelection) selection);
		
		boolean dataWrittenSuccessfully = false;
		boolean htmlWrittenSuccessfully = false;
		
		try {
			// overwrite csv with new data
			dataWrittenSuccessfully = overwriteCSVFile(data);
			
			// write html file with right header and title...
			htmlWrittenSuccessfully = overwriteHTMLFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// call website on writen file
		if (dataWrittenSuccessfully && htmlWrittenSuccessfully) {
		Program.launch(Activator.getUrl() + "indexCite.html");
		}
		// profit
		
		// TODO: seems to be there is some refactoring needed in here
		//Program.launch("http://localhost:8080");
		/*FileReader fr = null;
		try {
			//fr = new FileReader("test.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*BufferedReader br = null;
		if (null != fr) {
			br = new BufferedReader(fr);
			String zeile1;
			try {
				zeile1 = br.readLine();
				System.out.println(zeile1);
				String zeile2 = br.readLine();
				System.out.println(zeile2);
				String zeile3 = br.readLine();
				System.out.println(zeile3);
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() == 1) {
			ChartDataProvider provider = new ChartDataProvider();
			Term input = (Term) currentSelection.getFirstElement();
			Map<String, Integer> citeChartData = provider.calculateNumberOfPapersPerClass(input);
			Chart citeChart = ChartGenerator.createCiteBar(citeChartData);
		} else {
			return null;
		}*/
		// opens firefox window on the localhost site, provided by the server that serves the graphs
		//Program.launch(Activator.getUrl() + "index.html");
		
		/*try {
			changeCSV(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		// add hello world handler to the server
		//HandlerCollection handlers = Activator.getHandlers();
        //HelloHandler helloHandler = new HelloHandler();       
        //handlers.addHandler(helloHandler);
        
		return null;
	}
	
	private boolean overwriteCSVFile(Map<String, Integer> selectionData) throws IOException {
		if (null == selectionData) {
			return false;
		}
	    File writeToFile = new File(Activator.getWebAppWorkspace() + "/bar.data.csv");
	    PrintWriter writer = new PrintWriter(new FileWriter(writeToFile));
	    writer.println("category,amount");
	    for (Map.Entry<String, Integer> entry : selectionData.entrySet()) {
	    	writer.println(entry.getKey() + "," + Integer.toString(entry.getValue()));
	    }
	    writer.close();
		return true;
	}
	
	/**
	 * Takes the preconfigured html from within the bundle, adjusts it with the correct title and
	 * stuff and writes it to the workspace folder.
	 * 
	 * @return true, if the file was written successfully
	 */
	private boolean overwriteHTMLFile() throws IOException {
    	InputStream in = getClass().getResourceAsStream("/html/indexCite.html");
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String st;
	    File writeToFile = new File(Activator.getWebAppWorkspace() + "/indexCite.html");
	    PrintWriter writer = new PrintWriter(new FileWriter(writeToFile));
	    while ((st = br.readLine()) != null) {
    		writer.println(st);
	    }
	    writer.close();
		return true;
	}
	
	private Map<String, Integer> processSelectionData(IStructuredSelection selection) {
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		if (currentSelection.size() == 1) {
			ChartDataProvider provider = new ChartDataProvider();
			Term input = (Term) currentSelection.getFirstElement();
			Map<String, Integer> citeChartData = provider.calculateNumberOfPapersPerClass(input);
			return citeChartData;
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}
	
	private static class HelloHandler extends AbstractHandler {
        
		final String greeting;
	    final String body;

	    public HelloHandler()
	    {
	        this("Hello World");
	    }

	    public HelloHandler( String greeting )
	    {
	        this(greeting, null);
	    }

	    public HelloHandler( String greeting, String body )
	    {
	        this.greeting = greeting;
	        this.body = body;
	    }

	    public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) 
	    		throws IOException, ServletException {
	    	PrintWriter out = response.getWriter();
	    	response.setContentType("text/html; charset=utf-8");
	    	baseRequest.setContextPath("/test");
	    	InputStream in = getClass().getResourceAsStream("/html/index.html"); 
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    	File file = new File("/home/pibebtole/Workspace/slr-toolkit/plugins/de.tudresden.slr.model.taxonomy.ui/html/index.html");
	    	BufferedReader br = new BufferedReader(new FileReader(file)); 
	    	String st;
	    	while ((st = br.readLine()) != null) {
	    		out.println(st);
	    		if ("<body>".equals(st)) {
	    			numberCalled++;
	    			out.println("<span>success" + numberCalled + "</span>");
	    			System.out.println("I got called the " + numberCalled + " time");
	    		}
	    	}
	    	response.setStatus(HttpServletResponse.SC_OK);
	        baseRequest.setHandled(true);
	    	/*out.println("<!DOCTYPE html>");
	    	out.println("<head>");
	    	out.println("<script src=\"https://cdn.jsdelivr.net/npm/vega@5\"></script>");
	    	out.println("<title>testfromjava</title>");
	    	out.println("</head>");
	    	out.println("<body>");
	    	out.println(" <div>");
	    	out.println("<span>Title: Test Date: Test</span>");
	    	out.println("</div>");
			out.println("<div id=\"view\"></div>");
			out.println("<button onclick=\"exportSVG()\">vega-export</button>");
			out.println("<button onclick=\"changeGraph()\">change graph</button>");
			out.println("<script type=\"text/javascript\">");
			out.println("var view;");
			out.println("const file_bar = './bar.vg.json';");
			out.println("const file_bubble = './bubble.vg.json';	");        																									
			out.println("var svg_url;");
			out.println("fetch(file_bar)");
			out.println(".then(res => res.json())");
			out.println(".then(spec => render(spec))");
			out.println(".catch(err => console.error(err));");
			out.println("function render(spec) {");
			out.println("view = new vega.View(vega.parse(spec), {");
			out.println("renderer:  'svg',  // renderer (canvas or svg)");
			out.println("container: '#view',   // parent DOM container");
			out.println("hover:     true       // enable hover processing");
			out.println("});");
			out.println("view.toImageURL('svg').then(function(url) {");
			out.println("svg_url = url;");
			out.println("}).catch(function(error) { /* error handling  });");*/
			/*out.println("return view.runAsync();");
			out.println("}");
			out.println("function changeGraph() {");
			out.println("fetch(file_bubble)");
			out.println(".then(res => res.json())");
			out.println(".then(spec => render(spec))");
			out.println(".catch(err => console.error(err)");
			out.println("}");
			
			out.println("function exportSVG() {");
			out.println("var link = document.createElement('a');");
			out.println("link.setAttribute('href', svg_url);");
			out.println("link.setAttribute('target', '_blank');");
			out.println("link.setAttribute('download', 'vega-export.svg');");
			out.println("link.dispatchEvent(new MouseEvent('click'));");
			out.println("}");
			out.println("</script>");
			out.println("</body>");*/

	        /*out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>My Web App</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>My Web App</h1>");
			out.println("<p>The current time is: test </p>");
			out.println("</body>");
			out.println("<script type=\"text/javascript\">");
			out.println("alert()");
			out.println("</script>");
			out.println("</html>");
	        if (body != null)
	        {
	            out.println(body);
	        }*/

	    }
    }
	
	private static class CsvHandler extends AbstractHandler {
        
		final String greeting;
	    final String body;

	    public CsvHandler()
	    {
	        this("Hello World");
	    }

	    public CsvHandler( String greeting )
	    {
	        this(greeting, null);
	    }

	    public CsvHandler( String greeting, String body )
	    {
	        this.greeting = greeting;
	        this.body = body;
	    }

	    public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response ) 
	    		throws IOException, ServletException {
	    	PrintWriter out = response.getWriter();
	    	response.setContentType("text/csv");
	    	//response.setHeader("Content-Disposition", "attachment; filename=\"data.csv\"");
	    	response.setHeader("Content-Disposition", "attachment; filename=\"data.csv\"");
	    	baseRequest.setContextPath("/");
	    	File file = new File("/home/pibebtole/Workspace/slr-toolkit/plugins/de.tudresden.slr.model.taxonomy.ui/html/data.csv");
	    	BufferedReader br = new BufferedReader(new FileReader(file)); 
	    	String st;
	    	while ((st = br.readLine()) != null) {
	    		out.println(st);
	    		if ("china,8".equals(st)) {
	    			out.println("testetstset,123");
	    		}
	    	}
	    	response.setStatus(HttpServletResponse.SC_OK);
	        baseRequest.setHandled(true);
	    }
    }
}
