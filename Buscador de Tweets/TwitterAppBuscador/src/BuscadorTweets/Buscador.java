package BuscadorTweets;
import twitter4j.*;
import twitter4j.conf.*;
import java.util.*;

public class Buscador {
	
public static void main(String[] args) {
	ConfigurationBuilder config=new ConfigurationBuilder();
	config.setDebugEnabled(true)
			.setOAuthConsumerKey("")
			.setOAuthConsumerSecret("")
			.setOAuthAccessToken("")
			.setOAuthAccessTokenSecret("");
	TwitterFactory tf = new TwitterFactory(config.build());		
	Twitter twitter=tf.getInstance();
	try{
		Query cad=new Query("Argentina vs Uruguay");
		QueryResult resultados;
		resultados = twitter.search(cad);
		List<Status> tweets = resultados.getTweets();
		for (Status tweet:tweets){
			System.out.println("@"+tweet.getUser().getScreenName()+"-"+tweet.getText());
			}
		System.exit(0);
		} catch (TwitterException te){
		te.printStackTrace();
		System.out.println("Fallo la busqueda de Tweets: "+te.getMessage());
		System.exit(-1);
		}
	
  }

}

