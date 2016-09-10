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

public class TwitterSentiment {

    public static void main(String[] args)  {

        Date final_date = Date.from(Instant.now());
        Date initial_date = addDays(final_date, -10);
        Date tempdate = (Date) initial_date.clone();

        String t4jquery = "trump";

        while (tempdate.before(final_date)) {
            PrintWriter xmlOut = null;
            try {
                System.out.println("------------------------------------------------");
                tempdate = addDays(tempdate, 1);
                System.out.println(tempdate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                System.out.println(dateFormat.format(tempdate));
                String filename = System.getProperty("user.dir") + "\\xmls\\" + dateFormat.format(tempdate) + ".xml";
                System.err.println(filename);
                String[] twittermessages = get_twits(tempdate, t4jquery);
                String message = "";
                for (String twittermessage : twittermessages) {
                    message += twittermessage + ".";
                }   //  http://stackoverflow.com/questions/22434081/sentiments-scores-stanford-core-nlp
                xmlOut = new PrintWriter(filename);
                Properties props = new Properties();
                props.setProperty("annotators",
                        "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
                StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
                Annotation annotation = new Annotation(message);
                pipeline.annotate(annotation);
                pipeline.xmlPrint(annotation, xmlOut);
                List<CoreMap> sentences = annotation.get(
                        CoreAnnotations.SentencesAnnotation.class);
                if (sentences != null && sentences.size() > 0) {
                    
                    int longest = 0;
//        Annotation annotation = pipeline.process(line);
                    Long textLength = 0L;
                    int sumOfValues = 0;
                    
                    for (CoreMap sentence : sentences) {
                        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                        int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                        String partText = sentence.toString();
                        if (partText.length() > longest) {
                            textLength += partText.length();
                            sumOfValues = sumOfValues + sentiment * partText.length();
                            
                            System.out.println(sentiment + " " + partText);
                        }
                    }
                    System.out.println("Overall: " + (double)sumOfValues/textLength);
                    
//                CoreMap sentence = sentences.get(0);
//                Tree tree = sentence.get(TreeAnnotation.class);
//                PrintWriter out = new PrintWriter(System.out);
//                out.println("The first sentence parsed is:");
//                tree.pennPrint(out);
                }
                
                //-------------------------------------------------------
                /*     PrintWriter xmlOut = new PrintWriter("xmlOutput.xml");
                Properties props = new Properties();
                props.setProperty("annotators",
                "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
                StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
                Annotation annotation = new Annotation(
                "It is a wonderful day. the cat runs");
                pipeline.annotate(annotation);
                pipeline.xmlPrint(annotation, xmlOut);
                // An Annotation is a Map and you can get and use the
                // various analyses individually. For instance, this
                // gets the parse tree of the 1st sentence in the text.
                List<CoreMap> sentences = annotation.get(
                CoreAnnotations.SentencesAnnotation.class);
                if (sentences != null && sentences.size() > 0) {
                
                CoreMap sentence = sentences.get(0);
                Tree tree = sentence.get(TreeAnnotation.class);
                PrintWriter out = new PrintWriter(System.out);
                out.println("The first sentence parsed is:");
                tree.pennPrint(out);
                
             }*/
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TwitterSentiment.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TwitterSentiment.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                xmlOut.close();
            }
        }
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    private static String[] get_twits(Date tempdate, String querystr) {

        ArrayList<String> res = new ArrayList<String>();
        try {
            twitter4j.Twitter twitter = getTwitter();

            Query query = new Query(querystr);
            query.setCount(5000);
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                //System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                res.add("@" + status.getUser().getScreenName() + ":" + status.getText());
            }
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterSentiment.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res.toArray(new String[]{});
    }

    public static twitter4j.Twitter getTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("sB9TX9gJ9V4nB8Jh8ObKFd2FS")
                .setOAuthConsumerSecret("RPwx1Zb3DrWpqSA9g5V7GtXeidZES6xL5VnihnveSdREBA6Lgu")
                .setOAuthAccessToken("767789639843414021-3Svwm5jrJBXUkRLf0f2CAbKwSHLXH9B")
                .setOAuthAccessTokenSecret("KYUG7JbavSma9oaONPYU0ZIt1leAYFkPut75jBfWHrLTR");
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();

    }
}
