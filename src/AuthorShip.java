import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * @author: Owen Astrachan
 * @date: February 15, 2026
 * @version 1.1, improved example from Fall
 * 
 * Train what is essentially an order-zero Markov model by counting 
 * word frequencies and then using these frequencies to calculate
 * maximum likelihood estimates for "unknown" texts
 */

public class AuthorShip {
    
    private Map<String, Integer> myAuthorProfile;
    private int myTotalWords;
    private final double ALPHA_SMOOTH = 0.1;
    
    public AuthorShip() {
        this.myAuthorProfile = new HashMap<>();
        this.myTotalWords = 0;
    }
    
    /**
     * Tokenize text into words, here splitting on whitespace
     */
    private List<String> tokenizeWhite(String text) {
        List<String> tokens = new ArrayList<>();
        String[] strs = text.split("\\s+");
        tokens.addAll(Arrays.asList(strs));
        return tokens;
    }

    /**
     * Tokenize based on word endings
     * @param text
     * @return
     */

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
     * Build word frequency profile for author X from training folder
     */

    public void trainDirectory(String dirName) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(dirName))){
            List<Path> files = paths.filter(Files::isRegularFile)
                                    .collect(Collectors.toList());

            for(Path each : files) {
                String text = Files.readString(each);
                List<String> words = tokenize(text);
                myTotalWords += words.size();
                for(String word : words) {
                    myAuthorProfile.put(word, myAuthorProfile.getOrDefault(word,0)+1);
                }
            }
        }       
    }   
    
    /**
     * Calculate log-likelihood of text given author X's word distribution
     */
    private double calculateLogLikelihood(List<String> words) {
        if (words.isEmpty()) {
            return Double.NEGATIVE_INFINITY;
        }
        
        double logLikelihood = 0.0;
        int vocabSize = myAuthorProfile.size();
        
        for (String word : words) {
            int wordCount = myAuthorProfile.getOrDefault(word, 0);
            double probability = 
                (double)(wordCount + ALPHA_SMOOTH) / (myTotalWords + ALPHA_SMOOTH*vocabSize);
            logLikelihood += Math.log(probability);
        }
        
        return logLikelihood;
    }

    private double analyze(String text){
        List<String> words = tokenizeWhite(text);
        double likelihood = calculateLogLikelihood(words);
        double normalizedScore = likelihood/words.size();
        return normalizedScore;
    }

    public void analyzeUnknowns(String testFolder) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(testFolder))){
            List<Path> files = paths.filter(Files::isRegularFile)
                                    .collect(Collectors.toList());

            Map<String, Double> map = new HashMap<>();
            for(Path each: files){
                String fileName = each.getFileName().toString();
                double value = analyze(Files.readString(each));
                map.put(fileName,value);
            }
            // sort map entries by log likelihood, with largest first, smallest last
            ArrayList<Map.Entry<String,Double>> list = new ArrayList<>(map.entrySet());
            Collections.sort(list, Map.Entry.comparingByValue(Comparator.reverseOrder()));
            for(int k=0; k < list.size(); k++){
                System.out.printf("%1.2f\t%s\n",list.get(k).getValue(),list.get(k).getKey());
                System.out.println("-".repeat(30));
            }
         }
    }


    public static void main(String[] args) throws IOException{
        AuthorShip analyzer = new AuthorShip();
        
        // Example folder paths - replace with your actual paths
        String trainingFolder = "data/cbronte";  // Folder with Author X's known works
        String testFolder = "identify";         // Folder with texts to analyze
        
        System.out.printf("training on %s\n",trainingFolder);
        analyzer.trainDirectory(trainingFolder);
        analyzer.analyzeUnknowns(testFolder);
    }
}