/*
* @author Anthony Gough : Student Number: n8578290
* @author Michael Sachs : Student Number: n0259373
* May 2014
*/
/**<p>
* Class to generate a JPanel to hold a JTextArea inside a 
* JScrollPane.  ResultPanel may be loaded into a GUI layout
* and be used to display textual output
* </p>
*/

package assign2.gui;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ResultPanel extends JPanel {

	/* Private class constants */
	private static final long serialVersionUID = 2405956237207773568L;	
	private static final int MARGIN = 10;
	private static final int TOP_MARGIN = 2;
	
	/* JTextArea to hold textual results */
	private JTextArea textArea;
	
	/* JScrollPane to enclose the JTextArea to make content
	 * more readable by enabling scroll bars once size of text
	 * exceeds default size of JTextArea
	 */
	private JScrollPane scrollPane;

	/**
	 * @author Anthony Gough
	 * <p>No Argument constructor for the creation of a JPanel that holds a
	 * JScrollPane that contains a JTextArea inside a BoxLayout </p>
	 */
	public ResultPanel() {
		setBackground(Color.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		/* Create an un-editable JTextArea */
		textArea = createTextArea();		
		
		/* Add a JTextArea to the JScrollPane */
		scrollPane = new JScrollPane(textArea);
		add(scrollPane);
	}

	/**
	 * @author Anthony Gough
	 * Simple setter to update the text results inside the JTextBox
	 * @param results - the text that is written to the
	 * 					JTextBox
	 */
	public void setTextBoxText(String results) {
		textArea.append(results);
	}
	

	/** 
	 * @author Anthony Gough
	 * Simple public method to clear the JTextBox of any text 
	 */
	public void clearTextBoxResults() {
		textArea.setText(null);
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to create a JTextArea	 * 	 
	 * @return - a JTextArea object
	 */
	private JTextArea createTextArea() {
		textArea = new JTextArea(10,10);
		textArea.setEditable(false);
		/* Set margins around the text area so data spaced away from panel */
		textArea.setMargin(new Insets(TOP_MARGIN,MARGIN,MARGIN,MARGIN));
		return textArea;
	}
}
