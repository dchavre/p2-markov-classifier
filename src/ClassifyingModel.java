import java.util.*;
import java.util.regex.*;
import java.io.*;
import compsci201.Ignore;

/**
 * AUTHOR: YOU, please modify
 */

public class ClassifyingModel extends BaseMarkovModel{

    // declare more needed instance variables here
    private Map<List<String>, List<String>> myMap;

    public ClassifyingModel(int size) {
        super(size);
        myMap = new HashMap<>();
    }

    public ClassifyingModel(){
        this(2);
    }

    /**
     * Returns the number of times token follows context in this model
     * @param context is an N-gram, a list of myOrder strings
     * @param token possibly follows the context in trained model
     * @return # occurrences of token following context in trained model
     */
    private int tokenInContextCount(List<String> context, String token) {
        int count = 0;
        for(String s : myMap.get(context)) {
            if (s.equals(token)) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * Use regular expression to tokenize rather
     * than split. Any alphabetic sequence followed by 
     * punctuation. So separation can be whitespace or number
     * for example.
     * @return list of tokens
     */

    @Ignore
    @Override
    public List<String> tokenize(String text){
        List<String> tokens = new ArrayList<>();
        String includePunc = "[A-Za-z]+|[.,!?;:]";
        Pattern pattern = Pattern.compile(includePunc);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    /**
     * Tokenize text and add <START>/<END> tags. Functionally
     * the same as BaseMarkovModel.updateWordSequence,
     * but that adds the padded text to an instance variable rather
     * than returning it. So code copied here
     * @param text to be processed
     * @return tokenized and pre/post padded sequence of tokens
     */
     private List<String> createTokenizedText(String text) {
        List<String> padded = new ArrayList<>();
        List<String> tokens = tokenize(text);

        for(int k=0; k < myModelSize; k++){
            padded.add("<START>");
        }
        padded.addAll(tokens);
        for(int k=0; k < myModelSize; k++){
            padded.add(END);
        }

        return padded;
    }

    /**
     * Return the log likelihood that text matches this trained model.
     * The text will be tokenized, then each n-gram/follow in text
     * "compared" in probabilistic way to the trained model's data.
     * Return the log probability of a match based on this comparison.
     * @param text is to be tokenized and matched against this model
     * @param smoother value used for Laplace smoothing
     * @return the normalized log probability of a match
     */
    public double calculateMatchProbability(String text, double smoother){
        
        List<String> padded = createTokenizedText(text);
       
        double probTotal = 0.0;
        for(int k=0; k < padded.size() - myModelSize; k++) {   
            double prob = 0.5; // replace with appropriate calculation/smoothed
            probTotal += Math.log(prob);
        }
        return probTotal; // must be normalized before returning
    }

    @Override
    public void processTraining(){
        // modify vocabulary instance variable
        
        for(int k=0; k < myWordSequence.size()-myModelSize; k++) {
            List<String> current = myWordSequence.subList(k, k+myModelSize);
            String next = myWordSequence.get(k+myModelSize);

            // update ALL instance variables appropriately
            
            
            myMap.putIfAbsent(current, new ArrayList<>());
            myMap.get(current).add(next);
        }
    }

    public int vocabularySize(){
        return 2; // must use instance variables appropriately
    }

    public static void main(String[] args) throws IOException {
        ClassifyingModel mm = new ClassifyingModel(3);
        String dirName = "data/shakespeare";
        mm.trainDirectory(dirName);
        System.out.printf("trained model for %s, vocab size = %d, tokens = %d\n",
                          dirName,mm.vocabularySize(),mm.tokenSize());
    }
}