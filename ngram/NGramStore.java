/*
 * @author Anthony Gough : Student Number: n8578290
 * @author Michael Sachs : Student Number: n0259373
 * May 2014
 * 
 * NGramStore class stores a collection of NGrams, each held in an object of type NGramContainer. 
 * The Abstract model of the collection is a map, with the context string as the key. 
 * Each context string is retrieves its associated container object.
 */

package assign2.ngram;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Iterator;

import com.microsoft.research.webngram.service.GenerationService;
import com.microsoft.research.webngram.service.NgramServiceFactory;
import com.microsoft.research.webngram.service.GenerationService.TokenSet;

public class NGramStore implements NGramMap {
	
	/* Private instance variables */
	private NGramNode node;
	
	/* Container for the NGram Store */
	private static Map<String, NGramContainer> container;
	
	
	/* Constants for the class */
	private static final String Key = "068cc746-31ff-4e41-ae83-a2d3712d3e68"; 
	private static final String DEFAULT_MODEL = "bing-body/2013-12/5"; 
	private static final String NEWLINE = "\n";
	private static final int NO_RESULTS = 0;
	private static final String ERR_SERVICE_FAULT = "NGram Service is not available - Try again later.";
	private static final String ERR_CREATING_NGRAM_NODE = "Error creating a new NGramNode object";

	/**
	 * @author Anthony Gough
	 * <p>Constructor for the NGramStore class</p>
	 * <p>
	 * Parameterless constructor. Only initialises a LinkedHashMap as the abstract container 
	 * for the NGramStore with the context string being the key to retrieve the associated container 
	 * object</p>
	 */
	public NGramStore() {
		container = new LinkedHashMap<String, NGramContainer>();
	}
	

	/*
	 * author Michael Sachs
	 * @see assign2.ngram.NGramMap#addNGram(assign2.ngram.NGramContainer)
	 */
	@Override
	public void addNGram(NGramContainer ngram) {
		/* Add node to container - if key (context string) already exists in container
		*  then the container will be updated with new node */
		container.put(ngram.getContext(), ngram);
	}
	

	/*
	 * @author Anthony Gough
	 * (non-Javadoc)
	 * @see assign2.ngram.NGramMap#removeNGram(java.lang.String)
	 */
	@Override
	public void removeNGram(String context) {
		if (container.containsKey(context)) {
			container.remove(context);
		}		
	}

	/*
	 * @author Michael Sachs
	 * @see assign2.ngram.NGramMap#getNGram(java.lang.String)
	 */
	@Override
	public NGramContainer getNGram(String context) {
		if (container.containsKey(context)) {
			return container.get(context);
		} else {
			return null;
		}
	}

	/*
	 * @author Michael Sachs/Anthony Gough
	 * @see assign2.ngram.NGramMap#getNGramsFromService(java.lang.String)
	 */
	@Override
	public boolean getNGramsFromService(String context, int maxResults) throws NGramException {
		
		try {
			NgramServiceFactory factory = NgramServiceFactory.newInstance(Key);
			GenerationService service = factory.newGenerationService();
	
			/* Call the NGram Service */
			TokenSet tokenSet = service.generate(Key, DEFAULT_MODEL, context, maxResults, null);
				
			/* Determine number of words returned from service */
			int numberOfWords = tokenSet.getWords().size();
			
			/* NGram node cannot be created if search returned no results */
			if (numberOfWords == NO_RESULTS) {
				return false;
			}
			
			/* Get the predictions and probabilities and store into respective arrays */
			String[] predictions = processWordResults(tokenSet, numberOfWords);
			Double[] probabilities = processProbabilities(tokenSet);
			
			/* Create a new NGram node */
			createStoreNewNode(context, predictions, probabilities);			
		
		/* Service failed to connect/Generated an exception */
		} catch (Exception e) {
			throw new NGramException(ERR_SERVICE_FAULT);
		}
		return true;
	}

	/*
	 * @author Anthony Gough
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// Create new StringBuilder.
		StringBuilder builder = new StringBuilder();

		/* Iterate over the NGram Container and output every node in the store */
		Iterator<String> it = container.keySet().iterator();
	    while(it.hasNext()){
	    	String key = it.next();
	    	builder.append(container.get(key).toString());
	    	builder.append(NEWLINE);
	    }
	    return builder.toString();
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to create an array of predictions returned
	 * from the NGram Service
	 * @param tokens - TokenSet object returned from the NGram Service
	 * @param resultNumber - Number of results returned from the NGram Service
	 * @return - Returns the predictions from the service as an array
	 */
	private String[] processWordResults(TokenSet tokens, int resultNumber) {
		return tokens.getWords().toArray(new String[resultNumber]);
	}
	
	
	/**
	 * @author Anthony Gough
	 * Private helper method to create an array of probabilities returned
	 * from the NGram Service - reuse same List to create the probabilities
	 * array
	 * @param tokens - TokenSet object returned from the NGram Service
	 * @return - Returns the probabilities of each prediction as an array
	 */
	private Double[] processProbabilities(TokenSet tokens) {
		List<Double> logOfProbabilities = tokens.getProbabilities();
		
		/* initialize an Iterator for the List */
		ListIterator<Double> listIterator = logOfProbabilities.listIterator();
		
		/* Iterate over list and convert log of probability */
        while (listIterator.hasNext()) {
            Double result = listIterator.next();
            listIterator.set(Math.pow(10.0, result));
        }
		/* Return an array of probabilities */
		return logOfProbabilities.toArray(new Double[logOfProbabilities.size()]);
	}
	
	
	/**
	 * @author Michael Sachs
	 * Private Helper method to create the NGram node when valid results returned
	 * from the NGram Service
	 * @param key - The context string used as a key to store the node in the container
	 * @param predictions - String array containing the predictions returned from the NGram service
	 * @param probabilities - Array of probabilities for the predictions
	 * @throws NGramException
	 */
	private void createStoreNewNode(String key, String[] predictions, Double[] probabilities) 
								throws NGramException {
		try {
			
			/* Create a new node and store into container */
			node = new NGramNode(key , predictions, probabilities);		
			this.addNGram(node);
			
		/* Throw an exception if error trying to create a new NGramNode object */
		} catch (Exception e) {
			throw new NGramException(ERR_CREATING_NGRAM_NODE);
		}		
	}
}
