/*
* @author Anthony Gough : Student Number: n8578290
* @author Michael Sachs : Student Number: n0259373
* May 2014
* 
* <p>
* Class is main entry point into SearchSuggestion application
* </p>
*/
package assign2.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Suggestions {
	
	/** Main method invokes the SearchSuggestion Application 
	 *  Initial thread schedules the GUI creation */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new NGramGUI("NGRAM Lookup V1.0"));
	}
}


