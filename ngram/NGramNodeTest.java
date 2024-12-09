/*
 * @author Anthony Gough : Student Number: n8578290
 * @author Michael Sachs : Student Number: n0259373
 * May 2014
 * 
 * Unit Tests for the NGramNode Class
 */

package assign2.ngram;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.microsoft.research.webngram.service.GenerationService;
import com.microsoft.research.webngram.service.NgramServiceFactory;
import com.microsoft.research.webngram.service.GenerationService.TokenSet;

import static org.hamcrest.CoreMatchers.*;

public class NGramNodeTest {

	public static final String KEY = "068cc746-31ff-4e41-ae83-a2d3712d3e68";
	private static final String DEFAULT_CONTEXT = "Friends Romans Countrymen lend me";
	private static final String NULL_STRING = null;
	private static final String EMPTY_STRING = "";
	private static final String SINGLE_CHARACTER = "a";
	private static final String[] SINGLE_WORD_ARRAY = {"mine"};
	private static final String SINGLE_WORD = "mine";
	private static final String DELIMITER = " ";
	private static final String[] SINGLE_PREDICTION = {"lend"};
	private static final String[] NULL_STRING_ARRAY = null;
	private static final String[] STRING_ARRAY_CONTAINS_EMPTY_STRING = {"Friends", "", "Countrymen", "lend", "me"};
	private static final String[] STRING_ARRAY_CONTAINS_NULL_STRING = {"Friends", null, "Countrymen", "lend", "me"};
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final Double[] NULL_DOUBLE_ARRAY = null;
	private static final Double[] DOUBLE_ARRAY_CONTAINS_NULL = {0.05, 0.02, null, 0.03, 0.25};
	private static final Double[] DOUBLE_ARRAY_CONTAINS_ZERO = {0.05, 0.02, 0.00, 0.03, 0.25};
	private static final Double[] DOUBLE_ARRAY_CONTAINS_NEGATIVE = {0.05, 0.02, -0.01, 0.03, 0.25};
	private static final Double[] DOUBLE_ARRAY_CONTAINS_VALUE_OVER_ONE = {0.05, 0.02, 1.01, 0.03, 0.25};
	private static final String[] SHORT_PREDICTIONS_RESULT = {"the", "to", "my", "open"};
	private static final Double[] SHORT_PROBABILITIES_RESULT = {0.05, 0.02, 0.08, 0.12};
	private static final Double EQUAL_TO_ONE = 1.00;
	private static final int FIVE_RESULTS = 5;
	
	private static String[] predictions;
	private static Double[] probabilities;
	private static String[] words;
	private static final String DEFAULT_MODEL = "bing-body/2013-12/5";
	
	
	/* Declare array of testNodes for the Unit Tests */
	private NGramNode[] testNode = new NGramNode[2]; 
	
	/* Setup before the JUnit Tests - get return results from NGram Service */
	@BeforeClass
	public static void setupNGram() throws NGramException {
		NgramServiceFactory factory = NgramServiceFactory.newInstance(KEY);
		GenerationService service = factory.newGenerationService();

		TokenSet tokenSet = service.generate(KEY, DEFAULT_MODEL, DEFAULT_CONTEXT, FIVE_RESULTS, null);
		List<Double> logProbs = tokenSet.getProbabilities();
		List<Double> probs = new ArrayList<Double>();
		int numberOfWords = tokenSet.getWords().size();
		
		/* Convert log results into probabilities */
		for (Double x : logProbs) {
			probs.add(Math.pow(10.0,x));
		}
		
		predictions = tokenSet.getWords().toArray(new String[numberOfWords]);
		probabilities = probs.toArray(new Double[probs.size()]);
		words = DEFAULT_CONTEXT.split(DELIMITER);
	}
	
	
	/* Create 2 new testNodes - 1 from each constructor 
	 * @author Anthony Gough */
	@Before
	public void setUp() throws NGramException {		
		testNode[0] = new NGramNode(words, predictions, probabilities);
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, predictions, probabilities);
	}

	/* NGramException thrown from first constructor - words array is null  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorOneParamWordsIsNull() throws NGramException {
		testNode[0] = new NGramNode(NULL_STRING_ARRAY, predictions, probabilities);
	}
	
	/* NGramException thrown from first constructor - words array is empty  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)	
	public void constructorONEParamWordsIsEmpty() throws NGramException {
		testNode[0] = new NGramNode(EMPTY_STRING_ARRAY, predictions, probabilities);
	}

	/* NGramException thrown from first constructor - words array contains a null string  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)	
	public void constructorOneParamWordsHasOneNullString() throws NGramException {
		testNode[0] = new NGramNode(STRING_ARRAY_CONTAINS_NULL_STRING,  predictions, probabilities);
	}
	
	/* NGramException thrown from first constructor - words array contains an empty string  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)	
	public void constructorOneParamWordsHasOneEmptyString() throws NGramException {
		testNode[0] = new NGramNode(STRING_ARRAY_CONTAINS_EMPTY_STRING,  predictions, probabilities);
	}
	
	/* NGramException thrown from first constructor - words array contains one empty string and
	 * one null string 
	 * @author Anthony Gough*/
	@Test(expected = NGramException.class)	
	public void constructorOneParamWordsHasOneEmptyOneNullString() throws NGramException {
		String[] words = {"Friends", NULL_STRING, EMPTY_STRING, "lend", "me"};
		testNode[0] = new NGramNode(words,  predictions, probabilities);
	}	
	
	/* NGramException thrown from first constructor predictions array is null  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorOneParamPredictionsIsNull() throws NGramException{
		testNode[0] = new NGramNode(words, NULL_STRING_ARRAY, probabilities);
	}
	
	/* NGramException thrown from first constructor predictions array is empty  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)	
	public void constructorOneParamPredictionsIsEmpty() throws NGramException {
		testNode[0] = new NGramNode(words, EMPTY_STRING_ARRAY, probabilities);
	}

	/* NGramException thrown from first constructor predictions array contains a null string  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)	
	public void constructorOneParamPredictionsHasOneNullString() throws NGramException {
		testNode[0] = new NGramNode(words, STRING_ARRAY_CONTAINS_NULL_STRING, probabilities);
	}
	
	/* NGramException thrown from first constructor predictions array contains an empty string  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)	
	public void constructorOneParamPredictionsHasOneEmptyString() throws NGramException {
		testNode[0] = new NGramNode(words, STRING_ARRAY_CONTAINS_EMPTY_STRING, probabilities);
	}
	
	/* NGramException thrown from first constructor predictions array contains an empty string
	 * and a null string  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)	
	public void constructorOneParamPredictionsHasNullAndEmptyString() throws NGramException {
		String[] predictions = {"Friends", NULL_STRING, EMPTY_STRING, "lend", "me"};
		testNode[0] = new NGramNode(words, predictions, probabilities);
	}
	
	/* NGramException thrown from first constructor probabilities is null  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorOneParamProbabilitiesIsNull() throws NGramException{
		testNode[0] = new NGramNode(words, predictions, NULL_DOUBLE_ARRAY);
	}
	
	/* NGramException thrown from first constructor probabilities contains a null entry  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)	
	public void constructorOneParamProbabilitiesHasNullEntry() throws NGramException {
		testNode[0] = new NGramNode(words, predictions, DOUBLE_ARRAY_CONTAINS_NULL);
	}
	
	/* NGramException thrown from first constructor probabilities contains a zero entry  
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)	
	public void constructorOneParamProbabilitiesHasZeroEntry() throws NGramException {
		testNode[0] = new NGramNode(words, predictions, DOUBLE_ARRAY_CONTAINS_ZERO);
	}	
	
	/* NGramException thrown from first constructor probabilities contains a negative entry   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)	
	public void constructorOneParamProbabilitiesHasNegativeEntry() throws NGramException {
		testNode[0] = new NGramNode(words, predictions, DOUBLE_ARRAY_CONTAINS_NEGATIVE);
	}
	
	/* NGramException thrown from first constructor probabilities contains a value just greater than 1   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)	
	public void constructorOneParamProbabilitiesHasEntryGreaterThanOne() throws NGramException {
		testNode[0] = new NGramNode(words, predictions, DOUBLE_ARRAY_CONTAINS_VALUE_OVER_ONE);
	}
	
	/* Test that 1 is an acceptable probability and does not thrown an exception   
	 * @author Michael Sachs */
	public void constructorOneParamProbabilitiesHasEntryEqualToOne() throws NGramException {
		Double[] probabilities = {EQUAL_TO_ONE};
		String[] words = {"the"};
		String[] predictions = {"to"};
		testNode[0] = new NGramNode(words, predictions, probabilities);
		assertThat(testNode[0].getPredictions(), is(equalTo(predictions)));
		assertThat(testNode[0].getProbabilities(), is(equalTo(probabilities)));
	}	
	
	/* NGramException thrown from first constructor predictions.length not equal to probabilities.length   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorOneParamPredictionsAndProbabilitiesLengthNotEqual() throws NGramException {
		
		/* default predictions length is 5 so set probabilities length to 4 */
		testNode[0] = new NGramNode(words, predictions, SHORT_PROBABILITIES_RESULT);
	}
	
	/* NGramException thrown from first constructor predictions.length not equal to probabilities.length   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorOneParamPredictionsProbabilitiesLengthNotEqual() throws NGramException {
		
		/* default probabilities length is 5 so set predictions length to 4 */		
		testNode[0] = new NGramNode(words, SHORT_PREDICTIONS_RESULT, probabilities);
	}	
	
	/* NGramException thrown from second constructor context is null   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorTwoParamContextIsNull() throws NGramException {
		testNode[1] = new NGramNode(NULL_STRING, predictions, probabilities);
	}
	
	/* NGramException thrown from second constructor context is empty   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorTwoParamContextsIsEmpty() throws NGramException {
		testNode[1] = new NGramNode(EMPTY_STRING, predictions, probabilities);
	}
	
	/* NGramException thrown from second constructor predictions array is null   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorTwoParamPredictionsIsNull() throws NGramException {
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, NULL_STRING_ARRAY, probabilities);
	}
	
	/* NGramException thrown from second constructor predictions array is empty   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorTwoParamPredictionsIsEmpty() throws NGramException {
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, EMPTY_STRING_ARRAY, probabilities);
	}

	/* NGramException thrown from second constructor predictions array contains null string   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorTwoParamPredictionsHasOneNullString() throws NGramException {
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, STRING_ARRAY_CONTAINS_NULL_STRING, probabilities);
	}
	
	/* NGramException thrown from second constructor predictions array contains empty string   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorTwoParamPredictionsHasOneEmptyString() throws NGramException{
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, STRING_ARRAY_CONTAINS_EMPTY_STRING, probabilities);
	}
	
	/* NGramException thrown from second constructor predictions array contains an empty string
	 * and a null string   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)	
	public void constructorTwoParamPredictionsHasNullAndEmptyString() throws NGramException {
		String[] predictions = {"lend", EMPTY_STRING, NULL_STRING, "me", "your"};
		testNode[1] = new NGramNode(words, predictions, probabilities);
	}
	
	/* NGramException thrown from second constructor probabilities array is null   
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void constructorTwoParamProbabilitiesIsNull() throws NGramException{
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, predictions, NULL_DOUBLE_ARRAY);
	}
	
	/* NGramException thrown from second constructor probabilities array contains null value   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorTwoParamProbabilitiesHasNullEntry() throws NGramException{
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, predictions, DOUBLE_ARRAY_CONTAINS_NULL);
	}
	
	/* NGramException thrown from second constructor probabilities array contains 0 value   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorTwoParamProbabilitiesHasZeroEntry() throws NGramException {
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, predictions, DOUBLE_ARRAY_CONTAINS_ZERO);
	}
	
	/* NGramException thrown from second constructor probabilities array contains negative value   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorTwoParamProbabilitiesHasNegativeEntry() throws NGramException {
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, predictions, DOUBLE_ARRAY_CONTAINS_NEGATIVE);
	}
	
	/* NGramException thrown from second constructor probabilities array contains value greater than one   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorTwoParamProbabilitiesHasEntryGreaterThanOne() throws NGramException {
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, predictions, DOUBLE_ARRAY_CONTAINS_VALUE_OVER_ONE);
	}
	
	/* Test that 1 is an acceptable probability and does not thrown an exception   
	 * @author Anthony Gough */
	public void constructorTwoParamProbabilitiesHasEntryEqualToOne() throws NGramException {
		Double[] probabilities = {EQUAL_TO_ONE};
		String[] predictions = {"to"};
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, predictions, probabilities);
		assertThat(testNode[1].getPredictions(), is(equalTo(predictions)));
		assertThat(testNode[1].getProbabilities(), is(equalTo(probabilities)));
	}	
	
	/* NGramException thrown from second constructor probabilities array length smaller than predictions array
	 * length   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorTwoParamPredictionsAndProbabilitiesLengthNotEqual() throws NGramException {
		/* default predictions length is 5 so set probabilities length to 4 */
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, predictions, SHORT_PROBABILITIES_RESULT);
	}

	/* NGramException thrown from second constructor predictions array length smaller than probabilities array
	 * length   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void constructorTwoParamPredictionsProbabilitiesLengthNotEqual() throws NGramException {
		/* default probabilities length is 5 so set predictions length to 4 */
		testNode[1] = new NGramNode(DEFAULT_CONTEXT, SHORT_PREDICTIONS_RESULT, probabilities);
	}	
	
	/* Test getContext method - Constructor 1 - parameter is String array   
	 * @author Anthony Gough */
	@Test
	public void getContextMethodReturnsContextPhraseFromStringArray() throws NGramException {
		assertThat(testNode[0].getContext(), is(equalTo(DEFAULT_CONTEXT)));
	}
	
	/* Test getContext method - Constructor 2 - parameter is String    
	 * @author Michael Sachs */
	@Test
	public void getContextMethodReturnsContextPhraseFromString() throws NGramException {
		assertThat(testNode[1].getContext(), is(equalTo(DEFAULT_CONTEXT)));
	}
	
	/* NGramException thrown - conditions for the class setter method - Parameter is null String   
	 * @author Michael Sachs*/
	@Test(expected = NGramException.class)
	public void setContextParamContextIsNull() throws NGramException {
		testNode[1].setContext(NULL_STRING);
	}
	
	/* NGramException thrown - conditions for the class setter method - Parameter is empty String    
	 * @author Michael Sachs*/
	@Test(expected = NGramException.class)
	public void setContextParamContextIsEmpty() throws NGramException {
		testNode[1].setContext(EMPTY_STRING);
	}
	
	/* Test that setContext will set the context correctly for a context string   
	 * @author Michael Sachs*/
	public void setContextValidContextString() throws NGramException {
		testNode[1].setContext(DEFAULT_CONTEXT);
		assertThat(testNode[1].getContext(), is(equalTo(DEFAULT_CONTEXT)));
	}
	
	/* Test that setContext will set context correctly for a string of length 1    
	 * @author Michael Sachs*/
	public void setContextValidContextStringLengthOne() throws NGramException {
		testNode[1].setContext(SINGLE_CHARACTER);
		assertThat(testNode[1].getContext(), is(equalTo(SINGLE_CHARACTER)));
	}	
	
	/* NGramException thrown - conditions for the class setter method - Parameter is null String array    
	 * @author Michael Sachs*/
	@Test(expected = NGramException.class)
	public void setContextParamWordsIsNull() throws NGramException {
		testNode[0].setContext(NULL_STRING_ARRAY);
	}
	
	/* NGramException thrown - conditions for the class setter method - Parameter is empty String array    
	 * @author Michael Sachs*/
	@Test(expected = NGramException.class)
	public void setContextParamWordsIsEmpty() throws NGramException {
		testNode[0].setContext(EMPTY_STRING_ARRAY);
	} 
	
	/* NGramException thrown - conditions for the class setter method - String array contains empty string    
	 * @author Michael Sachs*/
	@Test(expected = NGramException.class)
	public void setContextParamWordsContainEmptyString() throws NGramException {
		testNode[0].setContext(STRING_ARRAY_CONTAINS_EMPTY_STRING);
	} 
		
	/* NGramException thrown - conditions for the class setter method - String array contains null string    
	 * @author Michael Sachs*/
	@Test(expected = NGramException.class)
	public void setContextParamWordsContainsNullString() throws NGramException {
		testNode[0].setContext(STRING_ARRAY_CONTAINS_NULL_STRING);
	} 
	
	/* Test setContext sets the correct context - using a String array - should create string from array
	 * in the order the words are indexed into the array    
	 * @author Michael Sachs*/
	@Test
	public void setContextParamStringArray() throws NGramException {
		testNode[0].setContext(words);
		assertThat(testNode[0].getContext(), is(equalTo(DEFAULT_CONTEXT)));
	} 	
	
	/* Test setContext sets the correct context - using a String array of length 1 - should create 
	 * string from array - in this case a single word    
	 * @author Michael Sachs*/
	@Test
	public void setContextParamStringArrayOneElement() throws NGramException {
		testNode[0].setContext(SINGLE_WORD_ARRAY);
		assertThat(testNode[0].getContext(), is(equalTo(SINGLE_WORD)));
		assertThat(testNode[0].getContext(), is(equalTo(SINGLE_WORD_ARRAY[0])));
	} 	
		
	/* Test getPredictions method - from using Constructor 1   
	 * @author Michael Sachs */
	@Test
	public void getPredictionsTestConstructorOne() {
		assertThat(testNode[0].getPredictions(), is(equalTo(predictions)));
	}
	
	/* Test getPredictions method - from using Constructor 2   
	 * @author Michael Sachs */
	@Test
	public void getPredictionsTestConstructorTwo() {
		assertThat(testNode[1].getPredictions(), is(equalTo(predictions)));
	}	
	
	/* NGramException thrown - predictions array is null    
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void setPredictionsParamPredictionsIsNull() throws NGramException{
		testNode[0].setPredictions(NULL_STRING_ARRAY);
	}
	
	/* NGramException thrown - predictions array is empty    
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void setPredictionParamPredictionsIsEmpty() throws NGramException{
		testNode[0].setPredictions(EMPTY_STRING_ARRAY);
	}
	
	/* NGramException thrown - predictions array contains a null entry    
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void setPredictionsParamPredictionsHasOneNullString() throws NGramException{
		testNode[0].setPredictions(STRING_ARRAY_CONTAINS_NULL_STRING);
	}
	
	/* NGramException thrown - predictions array contains a empty entry    
	 * @author Michael Sachs */
	@Test(expected = NGramException.class)
	public void setPredictionParamPredictionsHasOneEmptyString() throws NGramException{
		testNode[0].setPredictions(STRING_ARRAY_CONTAINS_EMPTY_STRING);
	}
	
	/* Test the setPredictions method sets expected value - more than one prediction    
	 * @author Michael Sachs */
	@Test
	public void setPredictionsCorrectFromConstructorOne() throws NGramException {
		testNode[0].setPredictions(predictions);
		assertThat(testNode[0].getPredictions(), is(equalTo(predictions)));
	}
	
	/* Test the setPredictions method sets expected value - only ONE prediction    
	 * @author Michael Sachs */
	@Test
	public void setPredictionsCorrectSInglePrediction() throws NGramException {
		testNode[0].setPredictions(SINGLE_PREDICTION);
		assertThat(testNode[0].getPredictions(), is(equalTo(SINGLE_PREDICTION)));
	}
	
	/* Test the getProbabilities method returns expected value - from using Constructor 1   
	 * @author Michael Sachs */
	@Test
	public void getProbabilitiesCorrectFromConstructorOne() {
		assertThat(testNode[0].getProbabilities(), is(equalTo(probabilities)));
	}
	
	/* Test the getProbabilities method returns expected value - from using Constructor 2   
	 * @author Anthony Gough */
	@Test
	public void getProbabilitiesCorrectFromConstructorTwo() {
		assertThat(testNode[1].getProbabilities(), is(equalTo(probabilities)));
	}
	
	/* NGramException thrown for setProbabilities - probabilities array is null   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void setProbabilitiesParamProbabilitiesNull() throws NGramException{
		testNode[0].setProbabilities(NULL_DOUBLE_ARRAY);
	}	
	
	/* NGramException thrown for setProbabilities - probabilities array contains a null entry   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void setProbabilitiesParamProbabilitiesHasNullEntry() throws NGramException{
		testNode[0].setProbabilities(DOUBLE_ARRAY_CONTAINS_NULL);
	}
	
	/* NGramException thrown for setProbabilities - probabilities array contains a zero entry   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void setProbabilitiesParamProbabilitiesHasZeroEntry() throws NGramException{
		testNode[0].setProbabilities(DOUBLE_ARRAY_CONTAINS_ZERO);
	}
	
	/* NGramException thrown for setProbabilities - probabilities array contains a negative entry   
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void setProbabilitiesParamProbabilitiesHasNegativeEntry() throws NGramException{
		testNode[0].setProbabilities(DOUBLE_ARRAY_CONTAINS_NEGATIVE);
	}
	
	/* NGramException thrown for setProbabilities - probabilities array contains a value greater than one  
	 * @author Anthony Gough */
	@Test(expected = NGramException.class)
	public void setProbabilitiesParamProbabilitiesHasEntryGreaterThanOne() throws NGramException{
		testNode[0].setProbabilities(DOUBLE_ARRAY_CONTAINS_VALUE_OVER_ONE);
	}
	
	/* Test that no exception is thrown when the probability is equal to 1.0   
	 * @author Anthony Gough */
	@Test
	public void setProbabilitiesParamProbabilitiesContainesOne() throws NGramException{
		Double[] probabilities = {EQUAL_TO_ONE};
		testNode[0].setProbabilities(probabilities);
		assertThat(testNode[0].getProbabilities(), is(equalTo(probabilities)));
	}		
	
	@Test
	/* test toString method creates string in correct format   
	 * @author Anthony Gough */
	public void toStringFormatCorrect() throws NGramException {
		String output = "My hovercraft is | full : 0.066563\n" +
			"My hovercraft is | far : 0.028824\n" +
			"My hovercraft is | sinking : 0.024525\n";
		String context = "My hovercraft is";
		String[] predictions = {"full","far","sinking"};
		Double[] probabilities = {0.066563121545421,0.0288241254545121,0.0245245151547};
		testNode[1] = new NGramNode(context, predictions,probabilities);
		assertThat(testNode[1].toString(), is(equalTo(output)));
	}
	
	/* Tests instructed to be included as part of the submission */
	/*-----------------------------------------------------------*/
	/*
   	 * Confirm that the API spec has not been violated through the
   	 * addition of public fields, constructors or methods that were
   	 * not requested
   	 */
   	@Test
   	public void NoExtraPublicMethods() {
   		//Extends Object, implements NGramContainer
   		final int toStringCount = 1;
   		final int NumObjectClassMethods = Array.getLength(Object.class.getMethods());
   		final int NumInterfaceMethods = Array.getLength(NGramContainer.class.getMethods());
   		final int NumNGramNodeClassMethods = Array.getLength(NGramNode.class.getMethods());
   		assertTrue("obj:"+NumObjectClassMethods+":inter:"+NumInterfaceMethods+" - 1 (toString()) = class:"+NumNGramNodeClassMethods,
   				(NumObjectClassMethods+NumInterfaceMethods-toStringCount)==NumNGramNodeClassMethods);
   	}
   	
   	@Test 
   	public void NoExtraPublicFields() {
   	//Extends Object, implements NGramContainer
   		final int NumObjectClassFields = Array.getLength(Object.class.getFields());
   		final int NumInterfaceFields = Array.getLength(NGramContainer.class.getFields());
   		final int NumNGramNodeClassFields = Array.getLength(NGramNode.class.getFields());
   		assertTrue("obj + interface = class",(NumObjectClassFields+NumInterfaceFields)==NumNGramNodeClassFields);
   	}
   	
   	@Test 
   	public void NoExtraPublicConstructors() {
   	//Extends Object, implements NGramContainer
   		final int ExtraConsCount =1;
   		final int NumObjectClassConstructors = Array.getLength(Object.class.getConstructors());
   		final int NumInterfaceConstructors = Array.getLength(NGramContainer.class.getConstructors());
   		final int NumNGramNodeClassConstructors = Array.getLength(NGramNode.class.getConstructors());
   		assertTrue("obj:"+NumObjectClassConstructors+":inter:"+NumInterfaceConstructors+" 1 (extra) = class:"+NumNGramNodeClassConstructors,
   				(NumObjectClassConstructors+NumInterfaceConstructors+ExtraConsCount)==NumNGramNodeClassConstructors);
   	}
	

       @Test
       public void TOSTRING_ComplexObject() throws NGramException {
     	   	  DecimalFormat df = new DecimalFormat(NGramContainer.DecFormat);
    	   	  String test = "be or not to | be : 0.136059\n" + "be or not to | mention : 0.066563\n" + 
    	   			  		"be or not to | exceed : 0.032759\n" + "be or not to | say : 0.028824\n" +
    	   			  		"be or not to | the : 0.024524\n";
    	   	  testNode[0].setContext("be or not to");
    	   	  testNode[0].setPredictions(new String[]{"be","mention","exceed","say","the"});
    	   	  testNode[0].setProbabilities(new Double[]{0.13605912332,0.066563234345,0.03275912314,0.028823899932,0.0245242343});
    	   	  String str = testNode[0].toString(); 
    	      assertEquals(test,str);
       }

}