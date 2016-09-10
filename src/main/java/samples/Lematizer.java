/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

/**
 *
 * @author acastano
 */
import java.io.*;
import java.util.*;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.*;
import edu.stanford.nlp.util.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Lematizer {

    public static void main(String[] args)   {

        //  http://stackoverflow.com/questions/22434081/sentiments-scores-stanford-core-nlp
//            PrintWriter xmlOut = new PrintWriter(filename);
        Properties props = new Properties();
        props.setProperty("annotators",
                "tokenize, ssplit, pos, lemma, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String message = "I am working the greatest worker";
        Annotation annotation = new Annotation(message);
        pipeline.annotate(annotation);
//            pipeline.xmlPrint(annotation, xmlOut);
        List<CoreMap> sentences = annotation.get(
                CoreAnnotations.SentencesAnnotation.class);
        if (sentences != null && sentences.size() > 0) {

            int longest = 0;
//        Annotation annotation = pipeline.process(line);
            Long textLength = 0L;
            int sumOfValues = 0;

            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                String partText = sentence.toString();
                System.out.println("Oración: "+partText);
                int i = 0;
                for (String word : lemmatize(partText, pipeline)) {
                    System.out.println((++i)+" "+word+ " LEMMA:"+lemmatize(word, pipeline));
                }
            }

        }

        //-------------------------------------------------------
    }
    
    
    

    public static List<String> lemmatize(String documentText, StanfordCoreNLP pipeline) {
        List<String> lemmas = new ArrayList<>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
            // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
        return lemmas;
    }
}
