/*
 * @author Anthony Gough : Student Number: n8578290
 * @author Michael Sachs : Student Number: n0259373
 * May 2014
 */
 /** <p>
 * The ChartPanel class creates a JFreeChart and places this on a chart panel. 
 * This class displays a graphic representation of the results of a query from
 * the NGram Service. This class also can refresh the current chart when a new 
 * data set is created. </p>
 */
package assign2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import assign2.ngram.NGramStore;
import assign2.ngram.NGramContainer;


public class ChartPanel extends JPanel {
	
	/* Private class variables for the graph */
	private static final String TITLE = "-grams";
	private static final String Y_AXIS_LEGEND = "Probabilities";
	private static final String X_AXIS_LEGEND = "Phrase _________";
	private static final long serialVersionUID = 1L;
	
	/* Data set to hold the results to be graphed */
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
	JFreeChart chart;
	
	/**
	 * @author Michael Sachs
	 * <p>Constructor for the ChartPanel class</p>
	 * <p>
	 * To create a new ChartPanel object requires three parameters passed to the constructor</p>
	 * <p>
	 * @param numberOfResults - the number of results the user requested<br>
	 * @param queries - the queries to be used as a legend for the chart plus data set<br>
	 * @param store - The NGramStore containing all the NGram data<br>
	 * </p>
	 * <p> A new ChartPanel object is created and added to center of BorderLayout
	 * placed on the panel</p>
	 */
	public ChartPanel(Integer numberOfResults, List<String> queries, NGramStore store) {
		/* create a BorderLayout and place on the JPanel */
		this.setLayout(new BorderLayout());
		
		/* Create the dataset */
		dataset = createDataset(queries,store);
		
		/* Create the chart from the data set*/
		chart = createChart(dataset, graphTitle(numberOfResults));
		org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(chart);
		
		/* Add the chart to the panel */
		this.add(chartPanel, BorderLayout.CENTER);
		this.setVisible(true);		
	}
	
	
	/**
	 * @author Michael Sachs
	 * Public method used by NGramGUI to refresh the chart when a new successful query
	 * has generated NGrams in the store - Updates the old chart and replaces this
	 * with the new graph data when user selects the Graph button on the NGramGUI	 * 
	 * @param queries - the queries that have successfully generated results from the NGram service
	 * @param store - The NGramStore containing all the NGram containers
	 * @param numberOfResults - the number of results the user requested
	 */
	public void refreshChart(List<String> queries, NGramStore store, Integer numberOfResults) {
		dataset.clear();
		/* Update the title and data set */
		chart.setTitle(graphTitle(numberOfResults));
		createDataset(queries,store);			
	}	
	

	/**
	 * @author Michael Sachs
	 * Method to create a new data set for the chart
	 * @param queries - queries that will form the basis of the chart
	 * @param store - The NGramStore containing the NGrams
	 * @return - a data set generated from the queries
	 */
	private DefaultCategoryDataset createDataset(List<String> queries, NGramStore store) {
		
		NGramContainer node;		

		/* Iterate through the queries to find the corresponding NGram in the store */
		for (String query : queries) {
			node = store.getNGram(query);
			
			/* Add the data to the data set if the query is in the store */
			if (node != null) {				
				/* Get the data from the node */
				Double[] probabilities = node.getProbabilities();
				String[] predictions = node.getPredictions();
				
				for (int i = 0; i < probabilities.length; i++) {
					dataset.setValue(probabilities[i], query, predictions[i]);
				}
			}
		}		
		return dataset;
	}
	
	
	/**
	 * @author Michael Sachs
	 * Method to create a JFreeChart object - chart	 
	 * @param dataset - the data set generated from the queries
	 * @param title - Title for the chart
	 * @return - returns a JFreeChart object - chart
	 */
    private JFreeChart createChart(CategoryDataset dataset, String title) {
    	
    	/* Setup the chart type and layout */
    	JFreeChart chart = ChartFactory.createStackedBarChart3D(
    		title, X_AXIS_LEGEND, Y_AXIS_LEGEND,
    		dataset, PlotOrientation.VERTICAL, 
    		true, false, false);
    	
    	/* set chart attributes */
    	chart.setBackgroundPaint(Color.WHITE);
    	chart.setBorderVisible(true);
    	chart.setBorderPaint(Color.BLACK);
    	chart.getTitle().setPaint(Color.LIGHT_GRAY);     	
    	CategoryPlot p = chart.getCategoryPlot(); 
    	p.setRangeGridlinePaint(Color.red); 
    			
        return chart;
    }
    
    /**
     * @author Anthony Gough
     * Private helper method - creates a title for the graph     *      
     * @param resultNumber - the number of results requested
     * @return - returns a String representing the chart title
     */
    private String graphTitle(Integer resultNumber) {
    	return resultNumber.toString() + TITLE;
    }
	
}