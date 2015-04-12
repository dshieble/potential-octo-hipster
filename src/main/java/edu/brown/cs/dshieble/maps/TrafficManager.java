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
import java.util.Set;
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
  private ConcurrentHashMap<String, Double> map;

  /**
   * stores the locations that were just updated - bcuz there is
   * no ConcurrentHashSet
   */
  private ConcurrentHashMap<String, Boolean> updated;

  /**
   * the port number
   */
  private int port;

  /**
   * simple constructor that initializes the threadsafe variables
   */
  public TrafficManager(int p) {
    port = p;
    time = new AtomicInteger(0);
    map = new ConcurrentHashMap<String, Double>();
    updated = new ConcurrentHashMap<String, Boolean>();
  }

  /**
   * Updates the concurrent hash map with traffic data
   */
  public void updateTraffic() {
    updated.clear();
    try {
      String urlString = "http://localhost:" + port + "?last=" + time;
      time.getAndSet((int) (System.currentTimeMillis() / 1000));
      URL url = new URL(urlString);
      HttpURLConnection connection =
          (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
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
              double value = Double.parseDouble(element[2]
                  .split(",")[1]
                  .trim()
                  .replaceAll("\\]","")
                  .replaceAll("\\[",""));
              map.put(key, value);
              updated.put(key, true);
            }
          } catch (NumberFormatException e) {
            //do nothing
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (Exception e) { //traffic fail quietly!
      e.printStackTrace();
    }
  }

  /**
   *
   * @return returns a set of all way ids whose traffic was updated
   * in last iteration
   */
  public Set<String> getUpdated() {
    return updated.keySet();
  }

  /**
   * @return simple getter
   */
  public ConcurrentHashMap<String, Double> getMap() {
    return map;
  }

  /**
   *
   * @param id the way id
   * @return the traffic of that way
   */
  public double getTrafficLevel(String id) {
    return map.get(id) != null ? map.get(id) : 1.0;
  }

}
