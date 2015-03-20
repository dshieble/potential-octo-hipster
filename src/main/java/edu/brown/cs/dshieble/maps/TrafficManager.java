package edu.brown.cs.dshieble.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A work in progress
 * @author dshieble
 *
 */

public class TrafficManager {

  public TrafficManager() {
    System.out.println(getTraffic());
  }
  
  public String getTraffic() {
    String urlString = "http://localhost:8080?last=0";    
    StringBuilder stringBuilder;
 
      try {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
       
        // just want to do an HTTP GET here
        connection.setRequestMethod("GET");
       
        connection.setReadTimeout(15*1000);
        connection.connect();
 
        // read the output from the server
        try (BufferedReader reader =
          new BufferedReader(
          new InputStreamReader(connection.getInputStream()))) {
          stringBuilder = new StringBuilder();
 
          String line = null;
          while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
          }
          return stringBuilder.toString();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        
      }
    return null;
  }
    
    
    
    
    
    
    
    
    
    
    

}
