import java.util.List;
import twitter4j.*;

private Twitter mi_twitter;

//Acceder a tu Twitter
public TwitterJ(String userName,String password){
	try{
		mi_twitter = new Twitter(userName,password);
		mi_twwiter.verifyCredentials();
		} catch (TwitterException ex){
			System.out.println("Error: "+ex.getMessage());
			}		
}

// Nuevo Tweet
public void nuevoTweet(String tweet) {
 
  try {
    this.mi_twitter.update(tweet);    
  } catch (Exception ex) {
    System.out.println("Error: " + ex.getMessage());    
  }
}

// Devuelve el Estao Actual
public String getEstado(){
  String estadoActual = "";
 
  try {
    List<Status> statusList = mi_twitter.getUserTimeline();
    estadoActual = String.valueOf(statusList.get(0).getText());    
  } catch (TwitterException ex) {
    System.out.println("Error:"+ex.getMessage());    
  }
 
  return "Mi Estado es: "+estadoActual;    
}

//Nos devolvera Contacto de Twitter
public void listaDeContactos() {
  try {
    List<User> friends = mi_twitter.getFriends();    
    System.out.println("Lista de Contactos\n");
 
    for (User f : friends) {
      System.out.println("Usuario: "+f.getScreenName());
      System.out.println("Estado: "+f.getStatusText()+"\n");
    }    
  } catch (Exception e) {}
}

//Main
public static void main(String args[]){
  TwitterJ t = new TwitterJ("mi_user","Mi_password"); 
 
  t.nuevoTweet("Ejemplo para Lineadecodigo.com");    
  t.listaDeContactos();
 
  System.out.println(t.getEstado());    
}

//Marial de Consulta
https://stackoverflow.com/questions/2943161/get-tweets-of-a-public-twitter-profile
http://twitter4j.org/en/code-examples.html
http://www.socialseer.com/twitter-programming-in-java-with-twitter4j/how-to-retrieve-more-than-100-tweets-with-the-twitter-api-and-twitter4j/
https://stackoverflow.com/questions/33863262/how-to-get-tweets-of-a-specific-user-using-twitter4j
https://stackoverflow.com/questions/29508538/get-tweets-by-particular-user-using-twitter4j-java-api
https://www.taringa.net/posts/info/14332194/Programacion-tu-aplicacion-Twitter.html
https://picodotdev.github.io/blog-bitix/2016/04/usar-twitter-desde-java-con-twitter4j/
http://www.barriblog.com/2010/07/lo-que-siempre-quiso-saber-del-api-de-twitter-y-nunca-se-atrevio-a-preguntar/
https://repositorio.uam.es/bitstream/handle/10486/662510/roales_gonzalez_natalia_tfg.pdf?sequence=1
https://repositorio.uam.es/bitstream/handle/10486/662352/otero_rodriguez_adrian_tfg.pdf?sequence=1
--
https://stackoverflow.com/questions/13545936/twitter4j-search-for-public-tweets
https://stackoverflow.com/questions/5455555/twitter4j-how-to-get-tweet-text-with-anchor-to-hash-tag-and-urls
http://www.tothenew.com/blog/get-tweets-posted-by-user-using-twitter4j/
https://forum.processing.org/two/discussion/9800/twitter-4j-get-tweets-by-user-id-seems-impossible

----------------------------
Twitter4j Link Descarga
http://twitter4j.org/archive/twitter4j-4.0.4.zip

