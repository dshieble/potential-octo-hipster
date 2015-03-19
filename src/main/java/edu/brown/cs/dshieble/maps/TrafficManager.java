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
    
  }
  
  public String getTraffic() {
    String url = "http://localhost:8080?last=0";
    return url;
  }
}
