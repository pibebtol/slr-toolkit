package de.tudresden.slr.model.taxonomy.ui.handlers;

import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.ui.chart.logic.ChartDataProvider;
import de.tudresden.slr.ui.chart.logic.ChartGenerator;
import de.tudresden.slr.ui.chart.logic.TermSort;
import de.tudresden.slr.ui.chart.settings.PieChartConfiguration;
import de.tudresden.slr.ui.chart.views.ICommunicationView;

public class CreatePieChartHandler implements IHandler {

	
	private final String chartViewId = "chart.view.chartview";
	private ICommunicationView view;
	private String noDataToDisplay = "Could not create a pie chart. \n Try to select a Term with subclasses.";
	
	public CreatePieChartHandler() {
		// TODO Auto-generated constructor stub
	}

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
		// TODO: seems to be there is some refactoring needed in here
		
		//servertest
		// start server on a new available port
		Server server = new Server(0);

        // Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
        // a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
        ResourceHandler resource_handler = new ResourceHandler();


        // Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
        // In this example it is the current directory but it can be configured to anything that the jvm has access to.
        resource_handler.setDirectoriesListed(false);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
        String location = Platform.getBundle("de.tudresden.slr.model.taxonomy.ui").getLocation().substring(15);
        location = location.concat("html/");
        System.out.println(location);
        System.out.println("test");
        resource_handler.setResourceBase(location);

        // Add the ResourceHandler to the server.
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);

        // Start things up! By using the server.join() the server thread will join with the current thread.
        // See "http://docs.oracle.com/javase/1.5.0java jetty find out, if server is running already/docs/api/java/lang/Thread.html#join()" for more details.
        try {
			server.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        int port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();
        //String localhost = "http://localhost:" + port + "/";
        // returns localhost uri of the server
        String localhost = server.getURI().toString();
        System.out.println(localhost);
        //servertestende
		
		
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		IViewPart part = null;
		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					part = page.showView(chartViewId);
				}
			}
		} catch (PartInitException e) {
			e.printStackTrace();
			return null;
		}
		if (part instanceof ICommunicationView) {
			view = (ICommunicationView) part;
		} else {
			return null;
		}

		view.getPreview().setTextToShow(noDataToDisplay);
		if (currentSelection.size() == 1) {
			view.getPreview().setDataPresent(true);
			ChartDataProvider provider = new ChartDataProvider();
			Term input = (Term) currentSelection.getFirstElement();
			Map<String, Integer> citeChartData = provider.calculateNumberOfPapersPerClass(input);
			PieChartConfiguration.get().getGeneralSettings().setChartTitle("Number of cites per subclass of " + input.getName());
			PieChartConfiguration.get().setPieTermSort(TermSort.SUBCLASS);
			Chart citeChart = ChartGenerator.createPie(citeChartData);
			view.setAndRenderChart(citeChart);
		} else {
			view.setAndRenderChart(null);
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		
	}

}
