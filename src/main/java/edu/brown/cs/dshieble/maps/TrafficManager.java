package edu.brown.cs.dshieble.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that manages the Traffic data
 * @author dshieble
 *
 */

public class TrafficManager {

  /**
   * the unix time
   */
  private AtomicInteger time;

  /**
   * stores traffic data
   */
  private ConcurrentHashMap<String, Integer> map;

  /**
   * simple constructor that initlaizes the threadsafe variables
   */
  public TrafficManager() {
    time = new AtomicInteger(0);
    map = new ConcurrentHashMap<String, Integer>();
  }


  /**
   * Updates the concurrent hash map with traffic data
   */
  public void updateTraffic() {
    ConcurrentHashMap<String, Integer> output =
        new ConcurrentHashMap<String, Integer>();
    String urlString = "http://localhost:8080?last=" + time;
    time.getAndSet((int) (System.currentTimeMillis() / 1000));
      try {
        URL url = new URL(urlString);
        HttpURLConnection connection =
            (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
 
        // read the output from the server
        try (BufferedReader reader =
          new BufferedReader(
          new InputStreamReader(connection.getInputStream()))) {
          String line = reader.readLine();
          if (line == null) {
            throw new IOException();
          }                    
          String[] sArray = line.split("\\], \\[");
          for (int i = 0; i < sArray.length; i ++) {
            try {
              String[] element = sArray[i].split("\"");
              if (element.length == 3) {
                String key = element[1]
                    .replaceAll("\\]","")
                    .replaceAll("\\[","");
                int value = Integer.parseInt(element[2]
                    .split(",")[1]
                    .trim()
                    .replaceAll("\\]","")
                    .replaceAll("\\[",""));
                output.put(key, value);
              }
            } catch (NumberFormatException e) {
              //do nothing
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    map = output;
    
//    
//    for (String s : map.keySet()) {
//      System.out.println(s);
//      System.out.println(map.get(s));
//    }
    
  }

  /**
   * 
   * @return simple getter
   */
  public ConcurrentHashMap<String, Integer> getMap() {
    return map;
  }

  /**
   * 
   * @param map simple setter
   */
  public void setMap(ConcurrentHashMap<String, Integer> map) {
    this.map = map;
  }

  /**
   * 
   * @param id the way id
   * @return the traffic of that way
   */
  public int getTrafficLevel(String id) {
    return map.get(id);
  }

}
