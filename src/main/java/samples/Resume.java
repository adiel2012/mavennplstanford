/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CacheMap;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilterReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acastano
 */
public class Resume {

    public static void main(String[] args) {

        //  http://stackoverflow.com/questions/22434081/sentiments-scores-stanford-core-nlp
//            PrintWriter xmlOut = new PrintWriter(filename);
        Properties props = new Properties();
        HashMap<String, Integer> resume = new HashMap<String, Integer>();

        props.setProperty("annotators",
                "tokenize, ssplit, pos, lemma, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String message = getMessage();
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
                System.out.println("Oración: " + partText);
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    // Retrieve and add the lemma for each word into the
                    // list of lemmas
                    //System.out.println("       "+token.get(CoreAnnotations.LemmaAnnotation.class));
                    String lemmatized = token.get(CoreAnnotations.LemmaAnnotation.class);

                    if (resume.containsKey(lemmatized)) {
                        Integer times = resume.get(lemmatized) + 1;
                        resume.put(lemmatized, times);
                    } else {
                        resume.put(lemmatized, 1);
                    }

                }
            }

            ArrayList<PairValue> lista = new ArrayList<>();
            for (Map.Entry<String, Integer> entrySet : resume.entrySet()) {
                String key = entrySet.getKey();
                int value = entrySet.getValue();
                lista.add(new PairValue(key, value));
            }

            lista.sort(new Comparator<PairValue>() {
                @Override
                public int compare(PairValue o1, PairValue o2) {
                    return o1.count - o2.count;
                }
            });
            
            for (PairValue par : lista) {
                System.out.println(par.string+" "+par.count);
            }

        //-------------------------------------------------------
        }
    }

    private static String getMessage() {
        String res = "";
        try {
            
            File f = new File("sample.txt");
            Scanner scan = new Scanner(f);
            while(scan.hasNext()){
                res += scan.next() ;
            }
            
            scan.close();
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Resume.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    

    public static class PairValue implements Comparable<PairValue> {

        private int count = 0;
        private String string;

        public PairValue(String string, int count) {
            this.string = string;
            this.count = count;
        }

        /**
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * @param count the count to set
         */
        public void setCount(int count) {
            this.count = count;
        }

        /**
         * @return the string
         */
        public String getString() {
            return string;
        }

        /**
         * @param string the string to set
         */
        public void setString(String string) {
            this.string = string;
        }

        @Override
        public int compareTo(PairValue o) {
            return count - o.count;
        }

    }

    class ValueComparator implements Comparator<String> {

        Map<String, Double> base;

        public ValueComparator(Map<String, Double> base) {
            this.base = base;
        }

    // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }

//    public static List<String> lemmatize_word(String documentText, StanfordCoreNLP pipeline) {
////        List<String> lemmas = new ArrayList<>();
//        // Create an empty Annotation just with the given text
//        Annotation document = new Annotation(documentText);
//        // run all Annotators on this text
//        pipeline.annotate(document);
//        // Iterate over all of the sentences found
//        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//        for (CoreMap sentence : sentences) {
//            // Iterate over all tokens in a sentence
//            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//                // Retrieve and add the lemma for each word into the
//                // list of lemmas
//                lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
//            }
//        }
//        return lemmas;
//    }
    public static List<String> lemmatize(String documentText, StanfordCoreNLP pipeline) {
        List<String> lemmas = new ArrayList<>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }
        return lemmas;
    }

}
