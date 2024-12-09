/*
 * @author Anthony Gough : Student Number: n8578290
 * @author Michael Sachs : Student Number: n0259373
 * May 2014
 * 
 * The NGramNode class specifies a container to hold NGram results, separately holding the 
 * context phrase, a list of predicted words, and a corresponding list of probabilities
 */

package assign2.ngram;

import java.text.DecimalFormat;

public class NGramNode implements NGramContainer {
	

	/* Format for output of probabilities */	 
	private static final String DecFormat="#.######";
	
	/* Private instance variables */
	private String[] predictions;
	private Double[] probabilities;
	private String context;
	
	/* Private class constants */
	private static final String NEWLINE = "\n";
	private static final String EMPTY = "";
	private static final String SPACE = " ";
	private static final Double ZERO = 0.0;
	private static final Double ONE = 1.0;
	
	/* Private class constants for NGramExceptions */	
	private static final String ERR_WORD = "Invalid Word Array: cannot be null or empty or " + 
					"contain null or empty elements.";
	private static final String ERR_CONTEXT = "Invalid Context Parameter: Cannot be null or empty.";
	private static final String ERR_PREDICTIONS = "Invalid Predictions Array: cannot be null or empty or " + 
					"contain null or empty elements.";
	private static final String ERR_PROBABILITIES = "Invalid Probabilities Array: cannot be null or " + 
					"contain at least one entry which is null , zero, negative or greater than 1.0";
	private static final String ERR_SIZE = "Probabilities array size is different to Predictions array size";
	private static final String ERR_INVALID = "Invalid Array Parameters";	
	
	
	/**
	 * @author Anthony Gough
	 * <p>Constructor for the NGramNode class</p>
	 * <p>
	 * Conditions that will throw a NGramException when instantiating a new
	 * NGramNode object - words is null or empty or contains at least one empty or null string OR
	 * predictions is null or empty or contains at least one empty or null string OR
	 * probabilities is null or contains at least one entry which is null , zero, negative or 
	 * greater than 1.0 OR  the predictions.length is different from probabilities.length</p>
	 * <p>
	 * The constructor sets the following instance variables:
	 * @param words - An array of words that makes up the context phrase
	 * @param predictions - array of next words in the phrase as predicted by the model
	 * @param probabilities - an array of probabilities for each of the corresponding predicted words
	 * @throws NGramException - Throws NGramException if violation of the 
	 * conditions named above. Exception thrown by respective methods called from constructor</p>
	 * <p>
	 * The constructor will create a new NGramNode which is a container holding the context phrase as
	 * an Array of words, array of next words in the phrase as predicted by the model 
	 * and an array of probabilities for the predictions
 	 *  - the context property is a string made up of values contained in the words array</p>
	 */
	public NGramNode(String[] words, String[] predictions, Double[] probabilities) throws NGramException {
		
		/* setter for words string array - will validate words */
		setContext(words);
		
		/* Validate the predictions and probabilities arrays */
		validatePredictionsProbabilities(predictions, probabilities);
	}	
	

	/**
	 * @author Michael Sachs
	 * <p>Constructor for the NGramNode class</p>
	 * <p>
	 * Conditions that will throw a NGramException when instantiating a new
	 * NGramNode object - context is null or empty OR
	 * predictions is null or empty or contains at least one empty or null string OR
	 * probabilities is null or contains at least one entry which is null , zero, negative or 
	 * greater than 1.0 OR the predictions.length is different from probabilities.length</p>
	 * <p>
	 * The constructor sets the following instance variables:
	 * @param context - string containing the context phrase
	 * @param predictions - array of next words in the phrase as predicted by the model
	 * @param probabilities - a list of probabilities for each of the corresponding predicted words
	 * @throws NGramException - Throws NGramException if violation of the 
	 * conditions named above. Exception thrown by respective methods called from constructor</p>
	 * <p>
	 * The constructor will create a new NGramNode which is a container holding the context phrase as
	 * a string, array of next words in the phrase as predicted by the model and an array of probabilities 
 	 *  for each of the predictions</p>
	 */
	public NGramNode(String context, String[] predictions, Double[] probabilities) throws NGramException {
		
		/* setter for context string will validate context */
		setContext(context);
		
		/* Check the predictions and probabilities arrays */
		validatePredictionsProbabilities(predictions, probabilities);
	}	
	
		
	/* @author Michael Sachs
	 * (non-Javadoc)
	 * @see assign2.ngram.NGramContainer#getContext()
	 */
	@Override
	public String getContext() {
		return this.context;
	}
	

	/* @author Michael Sachs
	 * (non-Javadoc)
	 * @see assign2.ngram.NGramContainer#setContext()
	 */
	@Override
	public void setContext(String context) throws NGramException {
		if (contextInvalid(context)) {
			throw new NGramException(ERR_CONTEXT);
		} else {
			this.context = context;
		}
	}
	

	/* @author Michael Sachs
	 * (non-Javadoc)
	 * @see assign2.ngram.NGramContainer#setContext()
	 */
	@Override
	public void setContext(String[] words) throws NGramException {
		
		/* Validate the words array */
		if (stringArrayInvalid(words)) {
			throw new NGramException(ERR_WORD);
		} else {
			this.context = createContextString(words);
		}		
	}
	

	/* @author Michael Sachs
	 * (non-Javadoc)
	 * @see assign2.ngram.NGramContainer#getPredictions()
	 */
	@Override
	public String[] getPredictions() {
		return this.predictions;
	}
	

	
	 /* @author Anthony Gough
	 * (non-Javadoc)
	 * @see assign2.ngram.NGramContainer#setPredictions(java.lang.String[])
	 */
	@Override
	public void setPredictions(String[] predictions) throws NGramException {
		
		/* Validate the predictions array */
		if (stringArrayInvalid(predictions)) {
			throw new NGramException(ERR_PREDICTIONS);
		}
		this.predictions = predictions;		
	}
	

	/* @author Anthony Gough
	 * (non-Javadoc)
	 * @see assign2.ngram.NGramContainer#getProbabilities(java.lang.Double[])
	 */
	@Override
	public Double[] getProbabilities() {
		return probabilities;
	}
	

	/* @author Anthony Gough
	 * (non-Javadoc)
	 * @see assign2.ngram.NGramContainer#setProbabilities(java.lang.Double[])
	 */
	@Override
	public void setProbabilities(Double[] probabilities) throws NGramException {
		
		/* Validate probabilities array */
		if (probabilityArrayInValid(probabilities)) {
			throw new NGramException(ERR_PROBABILITIES);
		}
		this.probabilities = probabilities;
	}
	
	
	/* @author Anthony Gough
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = new String();
		String formatProbability = new String();
		for (int i=0; i < predictions.length; i++) {
			formatProbability = customFormat(DecFormat,probabilities[i]);
			result = result + context + " | " + predictions[i] + " : " + formatProbability + NEWLINE;
		}
		return result;
	}
	
	
	/**
	 * @author Michael Sachs
	 * Private helper method used by both constructors to validate common array inputs
	 * for the predictions array and probabilities array - if arrays are valid then 
	 * arrays are set for the NGramNode object	 * 	
	 * @param predictionArray - Array of strings containing predictions from NGram Service
	 * @param probabilityArray - Array of doubles containing the probabilities for each prediction
	 * @throws NGramException - if the length of the predictions array is different to the length of the
	 * 						    probabilities array
	 */
	private void validatePredictionsProbabilities(String[] predictionArray, Double[] probabilityArray) 
			throws NGramException {
		
		/* Validate predictions and probability array lengths are equal */		
		if(equalArrayLength(predictionArray, probabilityArray)) {
			
			/* Validate/Set the prediction array input */
			setPredictions(predictionArray);
				
			/* Validate probability array input */
			setProbabilities(probabilityArray);			
		
		} else {
			throw new NGramException(ERR_SIZE);
		}
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to test if a string array is null or empty or any
	 * elements of the array contain null or empty values 	 * 
	 * @param testArray - String array to be tested for null/empty values
	 * @return true if the array is null or empty or any elements are null/empty
	 */
	private Boolean stringArrayInvalid(String[] testArray) {
		
		/** Check array not null/empty */
		if (testArray == null || testArray.length==ZERO) {
			return true;
		}
		
		/** check no empty/null elements in array */
		for (int i=0; i < testArray.length; i++) {
			if (testArray[i]==null || testArray[i]==EMPTY) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @author Anthony Gough
	 * Helper method to check that the elements of probability array are valid based on interface
	 * specifications
	 * Checks every element of the array that it is not  null , zero, negative or greater than 
	 * 1.0 and that the array is not null or empty	 * 
	 * @param testArray - testArray is array to be validated
	 * @return true is there are any invalid elements in array or if array is null
	 */
	private Boolean probabilityArrayInValid(Double[] testArray) {
		
		/** Check not null */
		if (testArray == null) {
			return true;
		}
		
		/** check no empty/null elements in array */
		for (int i=0; i < testArray.length; i++) {
			if (testArray[i] == null || testArray[i] <= ZERO || testArray[i] > ONE) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to check if the predictions array is the same length
	 * as the probability array	 * 
	 * @param predictionsArray - String array containing the predictions returned from service
	 * @param probabilityArray - Double array containing the probability for each prediction
	 * @return - true if the 2 arrays compared are of same length
	 * @throws NGramException - if either of the parameters to method are null
	 */
	private Boolean equalArrayLength(String[] predictionsArray,	Double[] probabilityArray) 
						throws NGramException {
		if (predictionsArray!=null && probabilityArray!=null) {
			return predictionsArray.length == probabilityArray.length;
		} else {
			throw new NGramException(ERR_INVALID);
		}
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to format probability into required string format	 * 
	 * @param pattern - Decimal format pattern specified
	 * @param value - value to be formatted
	 * @return - returns string formatted as per the string pattern specified
	 */
	private String customFormat(String pattern, double value) {
	      DecimalFormat myFormatter = new DecimalFormat(pattern);
	      return myFormatter.format(value);
	}
	
	
	/**
	 * @author Michael Sachs	
	 * Private helper method to check if the context string is null or empty.	 * 
	 * @param context - string containing the context phrase
	 * @return - true if the context string is null or empty otherwise false
	 */
	private Boolean contextInvalid(String context) {
		return (context == null || context == EMPTY);		
	}
	
	
	/**
	 * @author Michael Sachs
	 * Private helper method to create a single context string if the NGramNode object
	 * is created with a string array in the constructor.	 * 
	 * @param words - String array of words passed into the constructor
	 * @return - outputs a string representation of words each separated by a space
	 */
	private String createContextString(String[] words) {
		String context = EMPTY;
		for (String word : words) {
			context += word + SPACE;
		}
		/* Remove the last space and return result */
		return context.trim();
	}
}
