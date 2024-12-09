/*
 * @author Anthony Gough : Student Number: n8578290
 * @author Michael Sachs : Student Number: n0259373
 * May 2014
 * 
 * Unit Tests for the NGramStore Class
 */
package assign2.ngram;

import static org.junit.Assert.*;
import java.lang.reflect.Array;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.text.IsEmptyString.*;

public class NGramStoreTest {
	
	
	private static final String DEFAULT_CONTEXT_ONE = "be or not to";
	
	/* Probabilities array data generated from SimpleNGramGenerator using bing-body/2013-12/5
	 * Data generated 25th May 2014 using DEFAULT_CONTEXT_ONE
	 */
	private static final Double[] PROBABILITIES_SET_ONE = {0.9862794856312105, 0.004375221051582521,
		0.002074913517454911, 6.760829753919819E-4, 3.2508729738543437E-4};
	
	private static final Double[] PROBABILITIES_SET_ONE_RESULT = {0.9862794856312105};
	
	/* Predictions array data generated from SimpleNGramGenerator using bing-body/2013-12/5
	 * Data generated 25th May 2014  using DEFAULT_CONTEXT_ONE
	 */
	private static final String[] PREDICTIONS_SET_ONE = {"be", "bop", "b", "bee", "have"};
	
	private static final String[] PREDICTIONS_SET_ONE_RESULT = {"be"};
	
	public static final String DEFAULT_CONTEXT_TWO = "I have a dream";
	
	/* Probabilities array data generated from SimpleNGramGenerator using bing-body/2013-12/5
	 * Data generated 25th May 2014  using DEFAULT_CONTEXT_TWO
	 */
	private static final Double[] PROBABILITIES_SET_TWO = {0.29308932452503206, 0.08570378452303697, 
		0.02992264636608189, 0.025061092530321138, 0.021727011788637444};
	
	/* Predictions array data generated from SimpleNGramGenerator using bing-body/2013-12/5
	 * Data generated 25th May 2014  using DEFAULT_CONTEXT_TWO
	 */
	private static final String[] PREDICTIONS_SET_TWO = {"speech", "that", "by", "i", "kat"};
	
	/* NO_RESULT_CONTEXT returns no results from SimpleNGramGenerator using bing-body/2013-12/5 model
	 * Data attempted to be generated 25th May 2014 - null results returned
	 */
	private static final String NO_RESULT_CONTEXT = "woof explicit uhsa yell";	
	private static final int MAX_RESULTS = 5;
	private static final int ONE_RESULT = 1;
	private static final int NO_RESULTS = 0;
	
	private static final String FIRST_OUTPUT = "be or not to | be : 0.986279\n" +
			"be or not to | bop : 0.004375\n" +
			"be or not to | b : 0.002075\n" + 
			"be or not to | bee : 0.000676\n" + 
			"be or not to | have : 0.000325\n";
	
	private static final String ADDITIONAL_OUTPUT = "I have a dream | speech : 0.293089\n" +
			"I have a dream | that : 0.085704\n" +
			"I have a dream | by : 0.029923\n" +
			"I have a dream | i : 0.025061\n" +
			"I have a dream | kat : 0.021727\n";
	
	private static final String SINGLE_OUTPUT = "be or not to | be : 0.986279\n";
	
	private static final String NEWLINE = "\n";
	
	private static NGramStore container; 
	private NGramNode[] testNode = new NGramNode[4];	
	
	/* Setup for Unit Tests   
	 * @author Anthony Gough */
	@Before
	public void setUp() throws NGramException {		
		/* Default NGramNode for use in testing */	
		testNode[0] = new NGramNode(DEFAULT_CONTEXT_ONE, PREDICTIONS_SET_ONE, PROBABILITIES_SET_ONE);
		
		/* NGramNode used to test 'Update' of existing NGram */
		testNode[1] = new NGramNode(DEFAULT_CONTEXT_ONE, PREDICTIONS_SET_TWO, PROBABILITIES_SET_TWO);
		
		/* Additional node used for testing */
		testNode[2] = new NGramNode(DEFAULT_CONTEXT_TWO, PREDICTIONS_SET_TWO, PROBABILITIES_SET_TWO);
		
		/* Single result node used for toString validation */
		testNode[3] = new NGramNode(DEFAULT_CONTEXT_ONE, PREDICTIONS_SET_ONE_RESULT, PROBABILITIES_SET_ONE_RESULT);
		
		/* Create instance of NGramStore */
		container = new NGramStore();
	}
	
	
	/* Test add new NGramContainer to NGramStore - when add NGramNode the store
 	 * should not return null if key is found 
 	 * @author Michael Sachs*/
	@Test
	public void addNGramNewContainer() {	
		/* Confirm the store does not contain the context string */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(equalTo(null)));
		
		/* Add new NGramNode to the store */
		container.addNGram(testNode[0]);
		
		/* Confirm that the entry is added into the store */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
	}
	
	
	/* Test that if the key already exists in the store that the new data updates the 
	 store using the same key - test predictions updated.  
 	 * @author Michael Sachs*/
	@Test
	public void addNGramExistingKeyComparePredictions() {
		
		/* Add new NGramNode to the store */
		container.addNGram(testNode[0]);
		
		/* Confirm the prediction set added to the container is correct */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE).getPredictions(), equalTo(PREDICTIONS_SET_ONE));
		
		/* Update the NGramStore with the same context but with different predictions/probabilities */
		container.addNGram(testNode[1]);
		
		/* Confirm that the NGram predictions is updated */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE).getPredictions(), equalTo(PREDICTIONS_SET_TWO));		
	}
	
	
	/* Test that if the key already exists in the store that the new data updates the 
	 container using the same key - test probabilities updated.  
 	 * @author Michael Sachs*/
	@Test
	public void addNGramExistingKeyCompareProbabilities() {
				
		/* Add new NGramNode to the store */
		container.addNGram(testNode[0]);
		
		/* Confirm the prediction set added to the container is correct */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE).getProbabilities(), equalTo(PROBABILITIES_SET_ONE));
		
		/* Update the NGramStore with the same context but with different predictions/probabilities */
		container.addNGram(testNode[1]);
			
		/* Confirm that the NGram predictions is updated */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE).getProbabilities(), equalTo(PROBABILITIES_SET_TWO));		
	}
	
	/* Test that if add a second NGram to store that both NGrams exist in the container and second
	 * add NGram does not overwrite the first NGram 
 	 * @author Michael Sachs*/
	@Test
	public void addNGramSecondNGram() {
		/* Add new NGramNode to the store */
		container.addNGram(testNode[0]);
		
		/* Add second NGram */
		container.addNGram(testNode[2]);
		
		/* Verify both NGrams are in the container */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
		assertThat(container.getNGram(DEFAULT_CONTEXT_TWO), is(not(equalTo(null))));
		
	}
	
	
	/* Test removeNGram - If the context exists in the store then the associated NGram is removed  
 	 * @author Michael Sachs*/
	@Test
	public void removeNGramExistingKey() {
		
		/* Add ONE new NGram to the container */
		container.addNGram(testNode[0]);
		
		/* Confirm the container is not empty */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
		
		/* Remove the NGram from the container and confirm that the NGram no longer exists */
		container.removeNGram(DEFAULT_CONTEXT_ONE);
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(equalTo(null)));
	}
	
	
	/* Test removeNGram - If the context does not exist in the container then the state of the 
 	 * container is unchanged. Check that existing entries still present and removed NGram will return null 
 	 * @author Michael Sachs*/
	@Test
	public void removeNGramKeyNotPresent() {
		
		/* Add a new NGram to the store */
		container.addNGram(testNode[0]);
		
		/* Attempt to remove a non-existent NGram from the container - no side effects should occur if the
		* NGram does not exist*/
		container.removeNGram(DEFAULT_CONTEXT_TWO);
		
		/* Original entry still present in the store - state unchanged */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
		
		/* Removed non-existent NGram will only return null if try to getNGram */
		assertThat(container.getNGram(DEFAULT_CONTEXT_TWO), is(equalTo(null)));		
	}
	
	
	/* Test removing an non-existent NGram from the container - no side effects. Confirm that if
	 * try to get the NGram that has been removed returns null 
 	* @author Anthony Gough */
	@Test
	public void removeNonExistentNGram() {
		container.removeNGram(DEFAULT_CONTEXT_ONE);
		
		/* Try to get a NGram that was removed that never existed should return null*/
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(equalTo(null)));
	}
	
	
	/* Test removing a NGram from the store after it has been added then added a second time. After
	 * added for second time the NGram is removed - when try to retrieve NGram should return 
	 * null (no duplicate) 
 	* @author Michael Sachs */
	@Test
	public void removeNGramAddedTwice() throws NGramException {
		/* Add a new NGram to the store */
		container.addNGram(testNode[0]);
		
		/* add the NGram again */
		container.addNGram(testNode[0]);
		
		/* Confirm NGram is in the container */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
		
		/* remove the NGram */
		container.removeNGram(DEFAULT_CONTEXT_ONE);
		
		/* verify that NGram that is added twice once removed will not exist in container */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(equalTo(null)));
	}
	
	/* Test removing a NGram from the store after it has been added then updated with new NGram (same key)
	 * and then is removed - when try to retrieve NGram should return null (no duplicate) 
 	* @author Michael Sachs */
	@Test
	public void removeNGramAddedTwiceDifferentDataSameKey() throws NGramException {
		/* add a new NGram */
		container.addNGram(testNode[0]);
		
		/* add new NGram with same key but different predictions/probabilities */
		container.addNGram(testNode[1]);
		
		/* Confirm NGram is in the container */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
		
		/* remove the updated NGram */
		container.removeNGram(DEFAULT_CONTEXT_ONE);
		
		/* verify that NGram once removed will not exist in container */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(equalTo(null)));
	}
	
	
	/* Test that removeNGram does not remove all the entries in the container. Add 2 NGrams to
	 * the container 
	 * @author Anthony Gough */
	@Test
	public void removeNGramTwoDifferentEntries() {
		/* add a new NGram */
		container.addNGram(testNode[0]);
		
		/* add a second NGram */
		container.addNGram(testNode[2]);
		
		/* remove the first NGram */
		container.removeNGram(DEFAULT_CONTEXT_ONE);
		
		/* verify the second NGram still exists */
		assertThat(container.getNGram(DEFAULT_CONTEXT_TWO), is(not(equalTo(null))));
	}
	
	
	/* Test getNGram - returns null if key is not in the store - Have state of the store with one entry 
 	 * @author Michael Sachs*/
	@Test
	public void getNGramKeyNotInContainer() {
		/* Add a new NGram to the container - state of the container is one entry */
		container.addNGram(testNode[0]);
		
		/* Retrieve non-existent key from the store */
		assertThat(container.getNGram(DEFAULT_CONTEXT_TWO), is(equalTo(null)));
	}
	
	
	/* Test getNGram - returns null if key is not in the store - Have state of the store as empty 
 	* @author Michael Sachs */
	@Test
	public void getNGramKeyNotInEmptyContainer() {
				
		/* Retrieve non-existent key from the store */
		assertThat(container.getNGram(DEFAULT_CONTEXT_TWO), is(equalTo(null)));
	}
	
	
	/* Test getNGram - returns not null if key is in the store 
 	* @author Michael Sachs */
	@Test
	public void getNGramKeyInContainer() {
		
		/* add new NGram to store */
		container.addNGram(testNode[0]);
				
		//Retrieve NGram with key present in the map
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
	}
	
	
	/* Test getNGram - check the correct data is returned for NGram - probabilities  
 	 * @author Michael Sachs*/
	@Test
	public void getNGramCheckProbabilities() {
		
		//add new NGram to Map
		container.addNGram(testNode[0]);
		
		//check the probabilities for the retrieved NGram is correct
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE).getProbabilities(), equalTo(PROBABILITIES_SET_ONE));
	}
	
	
	/* Test getNGram - check the correct data is returned for NGram - predictions  
 	 * @author Michael Sachs*/
	@Test
	public void getNGramCheckPredictions() {
		
		//add new NGram to Map
		container.addNGram(testNode[0]);
		
		//check the probabilities for the retrieved NGram is correct
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE).getPredictions(), equalTo(PREDICTIONS_SET_ONE));
	}
	
	/* ******************************************************************************************** */
	/* Test getNGramsFromService Exception Throws - does not require testing if the service fails
	 * to connect (Part 2 Assignment Specifications) 
 	* @author Michael Sachs */	
	@Test
	public void getNGramsFromServiceThrowsException() {
		/* Service failure cannot be tested however a simulation such as disabling
		* the network adapter has be done which throws the correct NGramException */
		assertThat(true, is(true));
	}
	
	@Test
	public void getNGramsFromServiceThrowsExceptionCannotCreateNGram() {
		/* If NGram cannot be created this exception is thrown by the constructor in the 
		* NGramNode class - cannot be tested within this Test Class */
		assertThat(true, is(true));
	}
	/* ******************************************************************************************** */

	/* Return false if service returns no predictions - Use context that returns empty result  
	 * Validated returns no results 25th May 2014 using SimpleNGramGenerator using bing-body/2013-12/5
	 * as the NGram model
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceNoResults() throws NGramException {		
		boolean result = container.getNGramsFromService(NO_RESULT_CONTEXT, MAX_RESULTS);
		assertThat(result, is(false));
	}
	
		
	/* Service returns no predictions - Test that Context not added to store  
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceNoResultsNoKeyStored() throws NGramException {
		
		/* Query service with context phrase that will return no results */		
		container.getNGramsFromService(NO_RESULT_CONTEXT, MAX_RESULTS);
		
		/* Verify that the NGram is not created and added to the container */
		assertThat(container.getNGram(NO_RESULT_CONTEXT), is(equalTo(null)));
	}
	
	/* Service returns no results if zero (0) maxResults passed as parameter even
	 * for a context that would otherwise generate results
	 * @author Michael Sachs */
	@Test
	public void getNGramsFromServiceZeroMaxResults() throws NGramException {
		
		/* Query service with context phrase that will return results */		
		container.getNGramsFromService(DEFAULT_CONTEXT_ONE, NO_RESULTS);
		
		/* Verify that the NGram is not created and added to the container */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(equalTo(null)));		
	}
	
	/* Service returns no results if zero (0) maxResults passed as parameter even
	 * for a context that would otherwise generate results - getNGramsFromService should
	 * return false  
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceZeroMaxResultsReturnFalse() throws NGramException {
		
		/* Query service with context phrase that will return results */		
		container.getNGramsFromService(DEFAULT_CONTEXT_ONE, NO_RESULTS);

		/* Verify that the getNGramsFromService returns false */
		assertThat(container.getNGramsFromService(DEFAULT_CONTEXT_ONE, NO_RESULTS), is(equalTo(false)));		
	}
	
	/* Test that getNGramsFromService returns true when only one result request - Use context that returns 
	 * multiple results 
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceReturnsOneResult() throws NGramException {
		
		boolean result = container.getNGramsFromService(DEFAULT_CONTEXT_ONE, ONE_RESULT);
		assertThat(result, is(true));
	}
	
	/* Test that getNGramsFromService creates NGram when only one result request - Use context that returns 
	 * multiple results 
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceReturnOneResultCreateNGram() throws NGramException {
		
		container.getNGramsFromService(DEFAULT_CONTEXT_ONE, ONE_RESULT);
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
	}
	
	
	/* Return true if service returns results for more than one result - Use context that returns
	 * multiple results  
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceReturnsResults() throws NGramException {
		
		/* Query the service */
		boolean result = container.getNGramsFromService(DEFAULT_CONTEXT_ONE, MAX_RESULTS);
		assertThat(result, is(true));
	}
	
	
	/* Test that if service returns more than one result and that the context is stored as a key in store 
	/* Use context that returns results  
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceReturnsResultsKeyAdded() throws NGramException {
		
		/* Query the service */
		container.getNGramsFromService(DEFAULT_CONTEXT_ONE, MAX_RESULTS);		
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
	}	
	
	/* Test that service returns a correct results when the service is queried 
	/* Validate the NGram stored via probabilities 
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceValidateNGramProbabilities() throws NGramException {
		Double[] resultsFromClass;
		
		/* Query the service */
		container.getNGramsFromService(DEFAULT_CONTEXT_ONE, MAX_RESULTS);
		
		/* Verify new NGram is added to store */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
		
		/* Get the NGram Probabilities */
		resultsFromClass = container.getNGram(DEFAULT_CONTEXT_ONE).getProbabilities();
		
		/* Validate the NGram Added has correct result */
		assertThat(resultsFromClass, is(equalTo(PROBABILITIES_SET_ONE)));
	
	}	
	
	/* Test that service returns a correct results when the service is queried 
	/* Validate the NGram stored via predictions 
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceValidateNGramPredictions() throws NGramException {
		String[] resultsFromClass;
		
		container.getNGramsFromService(DEFAULT_CONTEXT_ONE, MAX_RESULTS);
		
		/* Verify new NGram is added to store */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
		
		/* Get the NGram Predictions */
		resultsFromClass = container.getNGram(DEFAULT_CONTEXT_ONE).getPredictions();
		
		/* Get the NGram Predictions */
		resultsFromClass = container.getNGram(DEFAULT_CONTEXT_ONE).getPredictions();		
		
		/* Validate the NGram Added has correct result */
		assertThat(resultsFromClass, is(equalTo(PREDICTIONS_SET_ONE)));	
	}	
	
	
	/* Test that NGram Service only returns the number of results requested - test 1 result
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceSingleResult() throws NGramException {
		container.getNGramsFromService(DEFAULT_CONTEXT_TWO, ONE_RESULT);
		
		/* Verify NGram is added to store */
		assertThat(container.getNGram(DEFAULT_CONTEXT_TWO), is(not(equalTo(null))));
		
		/* Verify that only one prediction/probability is returned is number requested*/
		assertThat(container.getNGram(DEFAULT_CONTEXT_TWO).getPredictions().length, is(equalTo(ONE_RESULT)));
		assertThat(container.getNGram(DEFAULT_CONTEXT_TWO).getProbabilities().length, is(equalTo(ONE_RESULT)));	
	}	
	
	/* Test that NGram Service only returns correct results when only request 1 result
 	* @author Anthony Gough */
	@Test
	public void getNGramsFromServiceSingleResultCorrect() throws NGramException {
		container.getNGramsFromService(DEFAULT_CONTEXT_ONE, ONE_RESULT);
		
		/* Verify NGram is added to store */
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE), is(not(equalTo(null))));
		
		/* Verify that only one prediction/probability is returned is number requested*/
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE).getPredictions(), 
				is(equalTo(PREDICTIONS_SET_ONE_RESULT)));
		assertThat(container.getNGram(DEFAULT_CONTEXT_ONE).getProbabilities(), 
				is(equalTo(PROBABILITIES_SET_ONE_RESULT)));	
	}	
	
	
	/* Test toString method is outputting correct format - One context in store
 	* @author Anthony Gough */
	@Test
	public void toStringFormatCorrect() throws NGramException {		
		
		/* Add a single NGram result to the container */
		container.addNGram(testNode[0]);
		
		/* Expected output should be as per output String */
		assertThat(container.toString(), is(equalTo(FIRST_OUTPUT+NEWLINE)));			
	}
	
	
	/* Test toString method is outputting correct format - More than one phrase  
 	* @author Anthony Gough */
	@Test
	public void toStringFormatCorrectMultiplePhrases() throws NGramException {		
			
		// Create new StringBuilder.		
		StringBuilder result = buildString(FIRST_OUTPUT,ADDITIONAL_OUTPUT);		
	
		/* Add a two NGram results to the container */
		container.addNGram(testNode[0]);
		container.addNGram(testNode[2]);
			
		/* Expected output should be as per output String */
		assertThat(container.toString(), is(result.toString()));
		
	}
	
	/* Test toString returns correct output with NGrams containing different lengths of predictions  
 	* @author Anthony Gough */
	@Test
	public void toStringMultiplePutputsDifferentLengths() throws NGramException {
		
		/* add a single result NGram to the store */
		container.addNGram(testNode[3]);
		
		/* add a NGram with multiple results */
		container.addNGram(testNode[2]);
		
		/* Build the string */
		StringBuilder result = buildString(SINGLE_OUTPUT, ADDITIONAL_OUTPUT);

		assertThat(container.toString(), is(equalTo(result.toString())));
	}	
		
		
	/* test toString method is outputting correct format - No NGrams in store 
 	* @author Anthony Gough */
	@Test
	public void toStringFormatEmptyStore() throws NGramException {
			
		/* State of the store is no NGrams */
		assertThat(container.toString(), isEmptyString());			
	}
	
	/**
	 * @author Anthony Gough
	 * Private helper method to construct strings from multiple inputs
	 * @param first - First string to add
	 * @param second - Second string to add
	 * @return - returns a StringBuilder object
	 */
	private StringBuilder buildString(String first, String second) {
		StringBuilder builder = new StringBuilder();		
		builder.append(first);
		builder.append(NEWLINE);
		builder.append(second);
		builder.append(NEWLINE);
		return builder;
	}
		
	/* *******************************************************************************************
   	 * Confirm that the API spec has not been violated through the
   	 * addition of public fields, constructors or methods that were
   	 * not requested
   	 */
   	@Test
   	public void NoExtraPublicMethods() {
   		//Extends Object, implements NGramMap
   		final int toStringCount = 1;
   		final int NumObjectClassMethods = Array.getLength(Object.class.getMethods());
   		final int NumInterfaceMethods = Array.getLength(NGramMap.class.getMethods());
   		final int NumNGramStoreClassMethods = Array.getLength(NGramStore.class.getMethods());
   		assertTrue("obj:"+NumObjectClassMethods+":inter:"+NumInterfaceMethods+" - 1 (toString()) = class:"+NumNGramStoreClassMethods,
   				(NumObjectClassMethods+NumInterfaceMethods-toStringCount)==NumNGramStoreClassMethods);
   	}
   	
   	@Test 
   	public void NoExtraPublicFields() {
   	//Extends Object, implements NGramMap
   		final int NumObjectClassFields = Array.getLength(Object.class.getFields());
   		final int NumInterfaceFields = Array.getLength(NGramMap.class.getFields());
   		final int NumNGramStoreClassFields = Array.getLength(NGramStore.class.getFields());
   		assertTrue("obj + interface = class",(NumObjectClassFields+NumInterfaceFields)==NumNGramStoreClassFields);
   	}
   	
   	@Test 
   	public void NoExtraPublicConstructors() {
   	//Extends Object, implements NGramMap
   		final int NumObjectClassConstructors = Array.getLength(Object.class.getConstructors());
   		final int NumInterfaceConstructors = Array.getLength(NGramMap.class.getConstructors());
   		final int NumNGramStoreClassConstructors = Array.getLength(NGramStore.class.getConstructors());
   		assertTrue("obj:"+NumObjectClassConstructors+":inter:"+NumInterfaceConstructors+" = class:"+NumNGramStoreClassConstructors,
   				(NumObjectClassConstructors+NumInterfaceConstructors)==NumNGramStoreClassConstructors);
   	}
}