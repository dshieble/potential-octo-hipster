package edu.brown.cs.dshieble.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TrafficThread extends Thread {

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

  public TrafficThread(
      int port,
      AtomicInteger time,
      ConcurrentHashMap<String, Double> map,
      ConcurrentHashMap<String, Boolean> updated) {

    this.port = port;
    this.time = time;
    this.map = map;
    this.updated = updated;
  }

  @Override
  public void run() {
    Timer t = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        updateTraffic();
      }
    };

    t.scheduleAtFixedRate(task, 0, 500);
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
}