/*
 * @author Anthony Gough : Student Number: n8578290
 * @author Michael Sachs : Student Number: n0259373
 * May 2014
 */
/** <p>The NGramGUI class creates the Search Suggestion GUI and also contains
 * the logic required for the operation of the GUI. The class uses a general
 * BorderLayout to divide the GUI into logically related components and each of
 * these logically related components are contained within their own specific
 * Layout. The main query of the NGram service is implemented as a separate thread</p>
 */
package assign2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.MaskFormatter;
import assign2.ngram.NGramContainer;
import assign2.ngram.NGramException;
import assign2.ngram.NGramStore;

public class NGramGUI extends JFrame implements ActionListener, Runnable {
	
	/* Initial size of the GUI */
	private static final int WIDTH = 800;
	private static final int HEIGHT = 500;
	
	/* Instance of the NGramStore to access stored NGrams */
	private static NGramStore store = new NGramStore();
	
	/* Thread used to query the NGram Service */
	private Thread thread;
	
	/* Setup main panels */
	private JPanel pnlDisplay;
	private JPanel pnlSearch;
	private JPanel pnlSouth;
	private JPanel pnlResetClose;
	private JPanel pnlOutputControl;
	
	/* Setup the buttons for the GUI */
	private JButton btnSearch;
	private JButton btnGraph;
	private JButton btnText;
	private JButton btnReset;
	private JButton btnExit;
	
	/* Text field for the entry of a Query */
	private JTextField phraseField;
	private JLabel phraseFieldLabel;
	private String phraseFieldHint = "Enter Search Phrase";

	/* Setup for the number of results TextField and label */
	private JLabel numberResultsLabel;	
	private JFormattedTextField resultNumber;
	private String numResultsString = "Number of Results:";
	
	/* Formatter for the number of results text field */
	private MaskFormatter formatter;	
	
	/* JPanels for the text and graphic results output */
	private ResultPanel results;
	private ChartPanel chart;
	
	/* Class constants */
	private static final long serialVersionUID = -7031008862559936404L;
	final static int ONE_RESULT = 1;
	final static int FIVE_RESULTS = 5;
	final static int FIVE_QUERIES = 5;
	final static int ONE_QUERY = 1;
	final static int NO_INPUT = 0;
	final static int VERTICAL_SIZE = 20;
	final static int LATERAL_BORDER = 30;
	final static int FILLER_LATERAL = 10;
	final static int LARGE_SIZE = 110;
	final static int INITIAL_LOCATION = 200;
	final static int POSITION_ZERO = 0;
	final static int POSITION_ONE = 1;
	final static int POSITION_TWO = 2;
	final static int WIDTH_ONE = 1;
	final static int WIDTH_TWO = 2;
	final static int ALIGNMENT_TEN = 10;
	final static int ALIGNMENT_ZERO = 0;
	final static int ALIGNMENT_TWO = 2;
	final static double WEIGHT_Y = .15;
	final static int WEIGHT_ZERO = 0;
	final static int WEIGHT_ONE = 1;	
	final static int DEFAULT_WIDTH = 2;
	final static String MAXIMUM_NUMBER_OF_QUERIES = "5";
	final static String INVALID_NUMBER_OF_REQUESTS = "Select a number between 1 and 5 results required.";
	final static String INVALID_NUMBER_OF_QUERIES = "Please enter between 1 and 5 queries";
	final static String NO_QUERY_MSG = "Please enter a query to process";
	final static String ERR_TITLE = "Input Error";
	final static String GENERIC_ERROR = "Error";
	final static String RESULTS_HEADER = "NGram Results for Query: ";
	final static String NO_RESULTS_HEADER = "No results were returned for this phrase: ";	
	final static String ERR_INVALID_QUERY = "There are invalid characters in your query.\n"
		    + "Use only letters, numbers, spaces and apostrophes.\n"
		    + "Separate each query with a comma.\n";
	final static String NEWLINE = "\n";
	final static String SPACE = " ";
	final static String EMPTY = "";

	/* Regular expression pattern for valid inputs into the GUI */
	final static String REGEX = "^[a-zA-Z0-9,' ]+$";
	
	/* Regular expression pattern to remove excessive whitespace between words in phrase */
	final static String EXCESSIVE_WHITESPACE_BETWEEN_WORDS = "\\s+";
	
	/* Container to hold the current queries for a valid search */
	private static List<String> currentQuery = new ArrayList<String>();	
	
	/* Global variable to determine if a new ChartPanel object has been created */
	static boolean CHART_CREATED = false;
	
	/* Initialise the number of results requested by the user */
	Integer resultsRequested = 0;

	

	
	/**
	 * <p>Constructor for the NGramGUI class</p>
	 * @param title - Title for the GUI
	 * @throws HeadlessException - Thrown when code that is dependent on a keyboard,
	 * 							   display, or mouse is called in an environment that
	 * 							   does not support a keyboard, display, or mouse.
	 */
	public NGramGUI(String title) throws HeadlessException {
		super(title);	
	}
	
	/*
	 * @author Anthony Gough
	 * Method to setup the required components for the GUI - includes all the
	 * required JComponents for the effective operation and display of the GUI
	 */
	private void createAndShowGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		/* Create the search button */
		btnSearch = createButton("Search", Color.GREEN, Color.BLACK);
		
		/* Create the Graph and text toggle button */
		btnGraph = createButton("Graph Results", Color.BLUE, Color.WHITE); 
		btnText = createButton("Text Results", Color.DARK_GRAY, Color.WHITE);
		
		/* Create Reset and Exit button */
		btnExit = createButton("Close", Color.BLACK, Color.WHITE);
		btnReset = createButton("Reset", Color.RED, Color.WHITE);
		
		/* Create the panels */
		pnlDisplay = createPanel(Color.WHITE);
		pnlSearch = createPanel(Color.WHITE);
		pnlSouth = createPanel(Color.WHITE);
		pnlResetClose = createPanel(Color.WHITE);
		pnlOutputControl = createPanel(Color.WHITE);
		
		/* Create a regular text field. */
        phraseField = createTextField(0, JTextField.LEFT);
        
        /* Setup the main input text field. */
        phraseFieldLabel = createLabel(phraseFieldHint, phraseField);
        pnlSearch.add(Box.createHorizontalGlue()); 
        /* Place an empty border around the search button/search text field */
        createEmptyBorder(pnlSearch,VERTICAL_SIZE,LATERAL_BORDER,VERTICAL_SIZE,LATERAL_BORDER);
		
		/* create a formatted text field for the number of results required plus label*/
        resultNumber = createFomattedTextField();		
		numberResultsLabel = createLabel(numResultsString, resultNumber);
		
		/* ********** Layout all the panel architecture ********* */
		
		/* The graph and text results buttons */
		layoutWestPanel();	
		/* Close and reset buttons */
		layoutEastPanel();
		/* Panel for the user to enter and invoke a search */
		layoutNorthPanel();
		/* Panel for the display of text and graphical results*/
		layoutCenterPanel();
		
		/* Place border around results panel and GUI Button control panels */
		addBordersToPanels(pnlDisplay);
		addBordersToPanels(pnlOutputControl);
		addBordersToPanels(pnlResetClose);
		
		/* Initialise the state of the graph and text buttons to inactive*/
		resetGraphTextButtonState(false, false);
	
		/* Add panels to the content pane and draw the GUI */
		this.getContentPane().add(pnlDisplay,BorderLayout.CENTER);
		this.getContentPane().add(pnlSearch,BorderLayout.NORTH);
		this.getContentPane().add(pnlSouth,BorderLayout.SOUTH);
		this.getContentPane().add(pnlResetClose,BorderLayout.EAST);
		this.getContentPane().add(pnlOutputControl, BorderLayout.WEST);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setLocation(new Point(INITIAL_LOCATION, INITIAL_LOCATION));
		this.pack();
		this.setVisible(true);
		this.repaint();
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to update the state of a component based on the
	 * current state of the GUI
	 * @param component - component to change the enabled status of
	 * @param state - New state for the component
	 */
	private void updateComponentState(JComponent component, boolean state) {
		component.setEnabled(state);		
	}	
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to create a JFormattedTextField 
	 * Used to create the number of results required text field
	 */
	private JFormattedTextField createFomattedTextField() {
		try {
            /* Create a MaskFormatter for Number Entries - Only all 1 number to be entered*/
            formatter = new MaskFormatter("#");
        } catch (ParseException e) {
        	showErrorDialog(e.getMessage(), GENERIC_ERROR);
        }
		JFormattedTextField jFormattedText = new JFormattedTextField(formatter);
		
		/* Set the attributes of the JFormattedTextField */
		jFormattedText.setPreferredSize(new Dimension(LARGE_SIZE, VERTICAL_SIZE));
		jFormattedText.setHorizontalAlignment(JTextField.CENTER);
		jFormattedText.setBorder(newBorder(jFormattedText, Color.BLUE, DEFAULT_WIDTH));
		jFormattedText.setText(MAXIMUM_NUMBER_OF_QUERIES);
		return jFormattedText;		
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to set a border around a component
	 * @param component - Component to have border applied to
	 * @param colour - Colour of the border to apply
	 * @param thickness - Thickness of the border to apply
	 * @return - a Border object 
	 */
	private Border newBorder(JComponent component, Color colour, int thickness) {
		Border border = BorderFactory.createLineBorder(colour, thickness);
		return border;
	}
	

	/**
	 * @author Anthony Gough
	 * Private helper method to add same border to all the required panels in 
	 * the GUI
	 * @param panel - Panel to apply the border to
	 */
	private void addBordersToPanels(JPanel panel) {
		Border loweredBevel;
		loweredBevel = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel.setBorder(loweredBevel);
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to create a JTextField
	 * @param columns - Number of columns for text field
	 * @param alignment - alignment of the text in the text field
	 * @return a JTextField object
	 */
	private JTextField createTextField(int columns, int alignment) {
		JTextField jTextField = new JTextField(columns);
		jTextField.setHorizontalAlignment(alignment);
		
		/* Add a tool tip for the JTextField */
		jTextField.setToolTipText(phraseFieldHint);
		
		/* Add ActionListener so user hits enter will execute query */
		jTextField.addActionListener(this);
		return jTextField;
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to create an empty border around a panel
	 * @param panel - panel to apply the border to
	 * @param top - top dimension
	 * @param left - left dimension
	 * @param bottom - bottom dimension
	 * @param right - right dimension
	 */
	private void createEmptyBorder(JPanel panel, int top, int left, int bottom, int right) {
		panel.setBorder(BorderFactory.createEmptyBorder(top,left,bottom,right));	           
	}	

	
	/**
	 * @author Anthony Gough
	 * Private helper method to create a JLabel and associate it the a JTextField
	 * @param narration - text for the label
	 * @param association - JTextField to associate the label with
	 * @return - a new JLabel
	 */
	private JLabel createLabel(String narration, JTextField association) {
		JLabel jLabel = new JLabel(narration);
		jLabel.setLabelFor(association);
		return jLabel;
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to create a JButton
	 * @param str - the text to be added to the button
	 * @param backGround - Background colour for the button
	 * @param foreGround - Foreground colour for the button
	 * @return - A new JButton object
	 */
	private JButton createButton(String str, Color backGround, Color foreGround) {
		JButton button = new JButton(str); 
		button.setBackground(backGround);
		button.setForeground(foreGround);
		
		/* Set an ActionListener for the button */
		button.addActionListener(this);
		return button; 
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to create a new JPanel and set the required
	 * attributes for the panel
	 * @param colour - colour for the JPanel
	 * @return - A new JPanel
	 */
	private JPanel createPanel(Color colour) {
		JPanel newPanel = new JPanel();
		newPanel.setBackground(colour);
		return newPanel;
	}
	
	
	/**
	 * @author Michael Sachs
	 * Private helper method to layout the search components required in the North Panel
	 * of the GUI - layout the Search button and search text field and label using a 
	 * Horizontal BoxLayout
	 */
	private void layoutNorthPanel() {
		BoxLayout searchLayout = new BoxLayout(pnlSearch, BoxLayout.X_AXIS);
		pnlSearch.setLayout(searchLayout);
		pnlSearch.add(phraseFieldLabel);
		
		/* Place a filler between the label and the textfield */		
        pnlSearch.add(filler(FILLER_LATERAL,VERTICAL_SIZE)); 
		pnlSearch.add(phraseField);
		
		/* Place a filler between the textfield and the search button */
		pnlSearch.add(filler(FILLER_LATERAL,VERTICAL_SIZE)); 
		pnlSearch.add(btnSearch);  		
	}
	
	
	/**
	 * @author Anthony Gough
	 * Create a BoxLayout for the Center Panel to hold the Result Chart
	 * for text initially and this panel will also hold the Graph results when
	 * invoked 
	 */
	private void layoutCenterPanel() {
		pnlDisplay.setLayout(new BoxLayout(pnlDisplay, BoxLayout.PAGE_AXIS));
		  
		/* Instantiate a new Result Panel to display text results from query */
		results = new ResultPanel();
		  
		/* Add the Result Panel to the center of the BorderLayout */
		pnlDisplay.add(results);	
		this.pack();
		this.setVisible(true);
	}
	
	
	/**
	 * @author Michael Sachs
	 * Private helper method to layout the East panel of the GUI
	 * The East panel holds the reset and close buttons for the GUI
	 * in a Vertical BoxLayout
	*/
	private void layoutEastPanel() {
		BoxLayout buttonLayout = new BoxLayout(pnlResetClose, BoxLayout.Y_AXIS);
		pnlResetClose.setLayout(buttonLayout);

		/* Add a Filler to place the buttons in the panel closer together */
        pnlResetClose.add(filler(LARGE_SIZE,LATERAL_BORDER));
        
        /* Add the Reset Button */
        btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlResetClose.add(btnReset);
        
        /* Place a new filler before the Exit Button added to the Panel */
        pnlResetClose.add(filler(LARGE_SIZE,LATERAL_BORDER));
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlResetClose.add(btnExit);		
	}

	
	/**
	 * @author Michael Sachs
	 * Private helper method to create a filler to place between components 
	 * Set the minimum, maximum and preferred sizes to enable resizing of the
	 * GUI without losing the the logical grouping of components in the panel
	 * @param xSize - The x axis size of the filler component
	 * @param ySize - The y axis size of the filler component
	 * @return Box.Filler with the minimum, maximum and preferred sizes
	 */
	private Box.Filler filler(int xSize, int ySize) {
		Dimension minSize = new Dimension(xSize, ySize);
		Dimension prefSize = new Dimension(xSize, ySize);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, ySize);
		return new Box.Filler(minSize, prefSize, maxSize);			
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to layout the West Panel using a GridBagLayout
	 * Method relies on setting and resetting GridBagConstraints to enable the
	 * logical grouping of components
	 */
	private void layoutWestPanel() {
		GridBagConstraints constraints;
		GridBagLayout resultControlsLayout = new GridBagLayout();
		pnlOutputControl.setLayout(resultControlsLayout);
		
		/* Set the GridBagConstraints for the Number of results label */
		constraints = setGridBagConstraints(WEIGHT_ZERO,WEIGHT_ONE,GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_START );
		constraints.insets = new Insets(ALIGNMENT_ZERO,ALIGNMENT_TEN,ALIGNMENT_ZERO,ALIGNMENT_ZERO);
		
		/* Add the label to the panel using the constraints defined */
		addToPanel(pnlOutputControl, numberResultsLabel,constraints,POSITION_ZERO,POSITION_ZERO,
						WIDTH_ONE,WIDTH_ONE); 
		
		/* Set the GridBagConstraints for the JFormattedTextField for entry of number of results */
		constraints = setGridBagConstraints(WEIGHT_ZERO,WEIGHT_ONE,GridBagConstraints.HORIZONTAL, 
						GridBagConstraints.PAGE_START);
		constraints.insets = new Insets(ALIGNMENT_ZERO,ALIGNMENT_TWO,ALIGNMENT_ZERO,ALIGNMENT_ZERO);
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.ipadx = -80;
		
		/* Add the JFormattedTextField for the number of results required to the panel */
		addToPanel(pnlOutputControl, resultNumber,constraints,POSITION_ONE,POSITION_ZERO,WIDTH_ONE,WIDTH_ONE);

		/* Set the constraints for the Graph Results button and add the button to the panel */
		constraints = setGridBagConstraints(WEIGHT_ZERO,WEIGHT_Y,GridBagConstraints.HORIZONTAL, 
						GridBagConstraints.NORTHWEST);
		addToPanel(pnlOutputControl, btnGraph, constraints,POSITION_ZERO,POSITION_ONE,WIDTH_TWO,WIDTH_ONE);
		
		/* Set the constraints for the Text Results button and add the button to the panel */
		constraints = setGridBagConstraints(WEIGHT_ZERO,WEIGHT_Y,GridBagConstraints.HORIZONTAL, 
						GridBagConstraints.NORTHWEST);
		addToPanel(pnlOutputControl, btnText, constraints,POSITION_ZERO,POSITION_TWO,WIDTH_TWO,WIDTH_ONE);
	}
	
	
	/**
	 * Private helper method to set the GridBagConstraints defined for laying out the West Panel
	 * @author - Provided in Practical 7 - adapted for the requirements of the GUI
	 * @param x_weight - weightx to distribute the space among the columns
	 * @param y_weight - weighty to distribute the space among the rows
	 * @param fill - how to resize the component
	 * @param anchor - where to place the component
	 * @return - a GridBagConstraints object defined by input parameters for the GridBagLayout
	 */
	private GridBagConstraints setGridBagConstraints(int x_weight, double y_weight, int fill, int anchor) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = anchor;
		constraints.weightx = x_weight;
		constraints.weighty = y_weight;
		return constraints;
	}
	
	
	/**
     * A convenience method to add a component to given grid bag
     * layout locations. 
     * @author 	Cay Horstmann 
     * @param jp - the JPanel to add the component to
     * @param c - the component to add
     * @param constraints - the grid bag constraints to use
     * @param xpos - the x grid position
     * @param ypos - the y grid position
     * @param width - the grid width
     * @param height - the grid height
     */
   private void addToPanel(JPanel jp,Component c, GridBagConstraints constraints, int xpos, int ypos,
		   int width, int height) {  
      constraints.gridx = xpos;
      constraints.gridy = ypos;
      constraints.gridwidth = width;
      constraints.gridheight = height; 
      constraints.fill = GridBagConstraints.HORIZONTAL;
      jp.add(c, constraints);
   }
	

   /**
    * @author Anthony Gough
    * Creates and show the application GUI
    */
	@Override
	public void run() {
		createAndShowGUI();
	}
	
	/**
	 * @author Anthony Gough
	 * Method required to be implemented by ActionListener Interface - NGramGUI class 
	 * implements this interface. An instance of the event handler class is registered as a listener
	 * on a number of components using the component's addActionListener method.
	 * When the action event occurs, that object's actionPerformed method is invoked.
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		Object src=evt.getSource();
		
		/** User hits enter on the JTextBox or hit Search Button*/
		if (src==phraseField || src == btnSearch) {
			searchForResults();
			
		/** Reset button */
		} else if (src==btnReset) {
			checkThread();
			
		/** Close button */
		} else if (src==btnExit) {
			dispose();
			System.exit(0);
			
		/** Text Results button */
		} else if (src==btnText) {
			showTextResults();
			
		/** Graph Results button */
		} else if (src==btnGraph) {
			showGraphResults();
		}		
	}
	
	
	/**
	 * @author Anthony Gough
	 * Check if thread has been instantiated - if not instantiated when user hits
	 * reset button this will generate an exception - If thread has 
	 * been instantiated then interrupt the thread. Clear the GUI
	 */
	private void checkThread() {
		try {
			thread.interrupt();
		} catch (NullPointerException e) {
			//thread has not been instantiated
		} finally {
			clearGUI();
		}
	}
	
	
	/**
	 * @author Michael Sachs
	 * Helper method to toggle the state of the graph and text buttons
	 * based on the current state of the GUI
	 * @param graphState - boolean value to either enable/disable Graph Button 
	 * @param textState - boolean value to either enable/disable Text Button 
	 */
	private void resetGraphTextButtonState(boolean graphState, boolean textState) {
		updateComponentState(btnGraph, graphState);
		updateComponentState(btnText, textState);	
	}

		
	/**
	 * @author Anthony Gough
	 * Private helper method to reset the GUI to its original state with the
	 * Search TextBox being active, clear any queries in the Search text box, reset maximum
	 * number of queries to 5, clear any text results from NGram service, hide the ChartPanel
	 * if active and disable the graph and text buttons
	 */
	private void clearGUI() {
		if (!results.isVisible()) {
			showTextResults();
		}
		phraseField.setText(EMPTY);
		resultNumber.setText(MAXIMUM_NUMBER_OF_QUERIES);
		results.clearTextBoxResults();
		updateComponentState(btnText, false);
		updateComponentState(btnGraph, false);
		
		/* re-enable search box/button/number of results */
		updateSearchComponents(true);
	}
	
	
	/**
     * @author Anthony Gough
     * Private helper method to set the state of graph button/search button/result number
     * text box/search text box
     * @param state - boolean value representing if enabled/disabled
     */
	private void updateSearchComponents(boolean state) {
    	updateComponentState(btnSearch, state);
    	updateComponentState(resultNumber, state);
    	updateComponentState(phraseField, state);					
	}
	
	
	/**
	 * @author Michael Sachs
	 * Main routine for setting up the GUI to receive results from the NGram Service
	 * Show the ResultsPanel if not currently visible. Invokes methods to validate
	 * the query strings placed into the search box and check valid number of results 
	 * requested - if valid will invoke a thread to query the NGram Service
	 */
	private void searchForResults() {
		
		/* Check is graph panel is active - if so toggle with the Result Panel*/
		if (CHART_CREATED && chart.isVisible()) {
			showTextResults();
		}

		/* Container for a new query that user enters into text box */
		List<String> newQuery = new ArrayList<String>();		
		
		/* validate user input */
		if (!validateRequestedNumberOfResults()) return;
		if (!validatePhraseField()) return;
		
		/* Create the queries from the user input */
		newQuery = makeQueries();
		
		/* Ensure a maximum of 5 queries only */
		if (!validNumberOfQueries(newQuery)) return;
			
		/* Clear the Result Panel if valid queries */
		results.clearTextBoxResults();
		
		/* send queries to NGram service */
		queryNGramService(newQuery); 		
	}
	
	/**
	 * @author Anthony Gough, Michael Sachs
	 * Private helper method invoked from searchForResults.
	 * Spawns a separate thread to send and receive query results from the
	 * NGram service. Allows the GUI to remain responsive while the queries
	 * are being processed and then returned to the GUI.
	 * @param newQuery - a validated list of queries to process
	 */
	private void queryNGramService(final List<String> newQuery){ 
	        Runnable task = new Runnable() {
	        	
	            @Override 
	            public void run() { 
	            	
	            	/* Predicate to store if valid results have been returned */
	            	boolean atLeastOneResult = false;
	            	
	            	/* Predicate to test if last query in the List of queries to process */
	            	boolean last = false;

	            	/* Store the number of queries request by the user - important in the 
	            	 * case where user may change this value */
	            	resultsRequested = Integer.parseInt(resultNumber.getText());	        		
	            	
	            	/* Disable components on the GUI to prevent invoking additional events
	            	 * Disable search components, number of results text box, graph and text button */
	            	updateSearchComponents(false);
	            	resetGraphTextButtonState(false, false);
	            	
		            /* Iterate through the validated query contexts */
	            	for (String query : newQuery) { 
	            		/* Check if last query to process */
	            		if (query == newQuery.get(newQuery.size()-1)) {
	            			last = true;
	            		}
	            		
	            		/* Query the NGram Service */
		                try { 
			            	store.getNGramsFromService(query, resultsRequested); 
			            	
			            		/* If the thread has not been interrupted */
			                	if (!Thread.interrupted()) {
			            			
				                	/* Progressively display the results to the GUI 		                		
			                		 * Record if at least one query returned results */
			                		if (displayTestResults(query, last) && !atLeastOneResult) {
			                			atLeastOneResult = true;
			                		}
				                	
			                	} else {
			                		/* User has requested the thread to cease */
			                		return;
			                	}
		                /* Error querying the NGram Service */
		                } catch (NGramException e) {
		    				showErrorDialog(e.getMessage(), GENERIC_ERROR);
		    			}		            	
	            	}
	    			
	        		/* update the container for the last valid query list */
	        		currentQuery = newQuery;        		
	        		
	        		if (atLeastOneResult) {
	        			/* enable the graph button */
	        			updateComponentState(btnGraph, true);			
	        		}
	        		/* Re-enable search button and number of results text box and search text box */
	        		updateSearchComponents(true);
	            }
	        }; 
	        thread = new Thread(task, "ServiceThread");
	        thread.start();
	    }	
	
	

	/**
	 * @author Anthony Gough
	 * Private helper method to ensure user has not entered more than 5
	 * queries into the search box - will invoke method to throw exception
	 * and output to dialog box the error
	 * @param queries - List of queries constructed from the search input text box
	 * @return - true if less than 5 queries entered otherwise return false
	 */
	private boolean validNumberOfQueries(List<String> queries) {
		if (queries.size() >= ONE_QUERY && queries.size() <= FIVE_QUERIES) {
			return true;
		} else {
			handleInvalidInputs(INVALID_NUMBER_OF_QUERIES);
		}
		return false;
	}
	
	/**
	 * @author Michael Sachs
	 * Method to validate that the number of results requested is between
	 * 1 and 5 - if a valid number of results selected return true
	 * otherwise throw an NGramException and show dialog explaining cause of
	 * error
	 * @return true if valid number of results requested otherwise false
	 */
	private Boolean validateRequestedNumberOfResults() {
		/* Use try catch for a NumberFormatException - required results left empty */
		try {
			Integer numResults = Integer.parseInt(resultNumber.getText());
			if (numResults < ONE_RESULT || numResults > FIVE_RESULTS) {
				handleInvalidInputs(INVALID_NUMBER_OF_REQUESTS);					
				return false;
			}		
		} catch (NumberFormatException e) {
			showErrorDialog(INVALID_NUMBER_OF_REQUESTS, ERR_TITLE);
			return false;
		}
		return true;
	}
	
	
	/**
	 * @author Michael Sachs
	 * Private helper method to validate the string input into the JTextArea
	 * that it does not contain invalid characters. Will invoke method to
	 * throw NGram Exception if invalid characters and show dialog explaining the error
	 * Outputs the error dialog as well to text area
	 * @return true if no invalid characters otherwise return false
	 */
	private Boolean validatePhraseField() {	
		
		/* Get the user input */
		String input = phraseField.getText();
		if (input.length()==NO_INPUT) {
			handleInvalidInputs(NO_QUERY_MSG);
			return false;
		}
		
		/* Test for valid characters enter in the search text box */
		Boolean valid = Pattern.matches(REGEX, input);
		if (!valid) {
			showErrorInTextArea(ERR_INVALID_QUERY);
			handleInvalidInputs(ERR_INVALID_QUERY);
			return false;
		}		
		return true;
	}

	/**
	 * @author Michael Sachs
	 * Private helper method to throw an exception if user enters invalid parameters 
	 * into the GUI for processing and show a Dialog explaining the reason for the error
	 * @param message - message to be displayed in Dialog Box
	 */
	private void handleInvalidInputs(String message) {
		try {
			throw new NGramException(message);
		} catch (NGramException e) {
			showErrorDialog(message, ERR_TITLE);	
		}
	}
	
	/**
	 * @author Anthony Gough
	 * Private helper method to show a JOptionPane message
	 * @param message - Message to display to user
	 * @param title - title of the message for dialog
	 */
	private void showErrorDialog(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, 
				JOptionPane.ERROR_MESSAGE);
	}

	
	/**
	 * @author Anthony Gough
	 * Private helper method to show error message in the JTextArea
	 * after clearing the JTextArea 
	 * @param message - message to be displayed in the JTextArea
	 */
	private void showErrorInTextArea(String message) {
		results.clearTextBoxResults();
		results.setTextBoxText(message);		
	}
	
	
	/**
	 * @author Michael Sachs
	 * Private helper method to split the individual context strings
	 * and place them into a list - removes any blank/null queries
	 * @return - a List containing all the sanitised queries from the text box
	 */
	private List<String> makeQueries() {
		String[] phrases = phraseField.getText().split(",");		
		List<String> processQueryList = new ArrayList<String>();
		for (String phrase : phrases) {
			phrase = phrase.replaceAll(EXCESSIVE_WHITESPACE_BETWEEN_WORDS, " ").trim();			
			processQueryList.add(phrase);
		}
		
		/* Remove any zero length strings caused by consecutive commas */
		processQueryList.removeAll(Arrays.asList("", null));
		return processQueryList;
	}
	

	/**
	 * @author Michael Sachs
	 * Method to show the results returned after query of the NGram Service
	 * Will either show returned results or output that no results for specific
	 * phrase has been returned
	 * @param NGramQuery - the context sent to the NGram Service
	 * @param lastQuery - true if last query in list to process
	 * @return - true if there are results returned from service otherwise return false
	 */
	private boolean displayTestResults(String NGramQuery, boolean lastQuery) {	
		boolean validResults = false;
		String output = "";
			
		/** Query the NGramStore */
		NGramContainer node = store.getNGram(NGramQuery);
		
		/** If key is present in the store output node data */
		if (node != null) {
			output = RESULTS_HEADER + NGramQuery + NEWLINE;
			output = output + NEWLINE + node.toString() + NEWLINE;		
			validResults = true;

		} else {
			/** No data in the store for this key */
			output = output + NO_RESULTS_HEADER + NGramQuery + NEWLINE + NEWLINE;				
		}		
		
		/* trim last new line if last query */
		if (lastQuery) {
			results.setTextBoxText(output.trim());
		} else {
			results.setTextBoxText(output);
		}		

		return validResults;		
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to hide the Chart results and show the text
	 * results from the last query - will maintain the current size of the GUI
	 * if it has been resized by the user
	 */
	private void showTextResults() {
		/* Get the current dimensions of the GUI */
		Dimension currentSize = getCurrentDimensions();
		
		/* Swap the chart and results panel */
		toggleDisplayComponent(chart, results);

		/* Reset the search button and graph button */
		resetGraphTextButtonState(true, false);
		
		/* reset GUI to current size */
		this.setPreferredSize(currentSize);			
		this.pack();
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to toggle between the chart panel and the result panel
	 * @param currentPanel - The panel to be hidden
	 * @param hiddenPanel - The panel to be made visible
	 */
	private void toggleDisplayComponent(JPanel currentPanel, JPanel hiddenPanel) {
		currentPanel.setVisible(false);
		hiddenPanel.setVisible(true);
	}

	/**
	 * @author Michael Sachs
	 * Private helper method to set the text results panel to not visible
	 * and enable the graphical representation of the query results
	 * If chart has never been instantiated then create a new ChartPanel
	 */
	private void showGraphResults() {
		Dimension currentSize = getCurrentDimensions();

		/* Instantiate a new chart panel if has not yet been created */
		if (!CHART_CREATED) {
			chart = new ChartPanel(resultsRequested, 
					currentQuery, store);
			pnlDisplay.add(chart, BorderLayout.CENTER);	
			CHART_CREATED = true;			
		} else {
			chart.refreshChart(currentQuery, store, resultsRequested);
		}
		
		/* swap the text panel for the chart panel */
		toggleDisplayComponent(results, chart);
		
		/* Make the graph button inactive/text button active */
		resetGraphTextButtonState(false, true);
		
		/* reset GUI to current size */
		this.setPreferredSize(currentSize);
		this.pack();
	}
	
	/**
	 * @author Anthony Gough
	 * Private helper method to get the current JFrame size as user
	 * may have resized the window.
	 * @return - a Dimension object representing the current size of the JFrame
	 */
	private Dimension getCurrentDimensions() {
		int currentWidth = this.getWidth();
		int currentHeight = this.getHeight();
		Dimension currentDimension = new Dimension(currentWidth, currentHeight);
		return currentDimension;
	}
}

