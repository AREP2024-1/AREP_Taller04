package edu.eci.arep.ASE.app.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HTTPConnection {

    private static final String USER_AGENT = "Mozilla/5.0";
    private final String apiUrl;
    private final String apiKey;
    private static final Map<String, String> cache = new ConcurrentHashMap();

    /*
     * Construye una instancia de la clase HTTPConnection con la URL de la API y la clave API proporcionadas.
     * @param apiUrl La URL de la API a la que nos vamos a conectar.
     * @param apiKey La clave API.
     */
    public HTTPConnection(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    /*
     * Proporcina la informacion de la pelicula que se desea buscar.
     * @param queryTitleMovie Título de la película que se va a buscar.
     * @return Informacion de la pelicula buscada .
     */
    public String getDatosPelicula(String queryTitleMovie) throws IOException {

        URL obj = new URL(apiUrl+"?apikey="+apiKey+"&t="+queryTitleMovie);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        return null;
    }

    /*
     * Proporciona la informacion de la pelicula que se desea buscar, si esta no se encuentra en el cache, 
     * la busca en la API y la guarda en el cache.
     * @param queryMovie Título de la película que se va a buscar.
     * @return Informacion de la pelicula buscada.
     */
    public String cacheMovie(String queryMovie) throws IOException {
        if(!cache.containsKey(queryMovie)){
            String movieData = getDatosPelicula(queryMovie);
            cache.put(queryMovie, movieData);
            return movieData;
        }else{
            System.out.println("The data of the movie " + queryMovie + " is in the cache");
            return cache.get(queryMovie);
        }

    }

    
}
