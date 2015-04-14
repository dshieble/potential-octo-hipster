package edu.brown.cs.dshieble.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Traffic Thread is a Thread class used to obtain traffic from the traffic
 * server. It is called by a traffic manager with the concurrent datastructures
 * initialized for it to update while the manager queries it.
 *
 * @author sjl2
 *
 */
public class TrafficThread extends Thread {

  private static final int TIME_COUNT = 1000;
  private static final int ELEMENT_LENGTH = 3;
  private static final int PAUSE = 500; // milliseconds

  /**
   * the unix time.
   */
  private AtomicInteger time;

  /**
   * stores traffic data.
   */
  private ConcurrentHashMap<String, Double> map;

  /**
   * stores the locations that were just updated because there is
   * no ConcurrentHashSet.
   */
  private ConcurrentHashMap<String, Boolean> updated;

  /**
   * the port number.
   */
  private int port;

  /**
   * Constructor for the Traffic Thread. The threads purpose is to repeatedly
   * update traffic for the traffic manager. Use the manager to access the
   * information it updates.
   * @param port The port to read the traffic data from
   * @param time The time of update
   * @param map The map to hold the traffic values
   * @param updated The ways that are updated
   */
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
    try {
      Timer t = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          updateTraffic();
        }
      };

      t.scheduleAtFixedRate(task, 0, PAUSE);
    } catch (RuntimeException e) {
      System.out.println("ERROR: " + e.getMessage());
    }

  }

  /**
   * Updates the concurrent hash map with traffic data for a traffic manager
   * that created the thread.
   */
  public void updateTraffic() {
    updated.clear();
    try {
      String urlString = "http://localhost:" + port + "?last=" + time;
      time.getAndSet((int) (System.currentTimeMillis() / TIME_COUNT));
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
        for (int i = 0; i < sArray.length; i++) {
          try {
            String[] element = sArray[i].split("\"");
            if (element.length == ELEMENT_LENGTH) {
              String key = element[1]
                  .replaceAll("\\]", "")
                  .replaceAll("\\[", "");
              double value = Double.parseDouble(element[2]
                  .split(",")[1]
                  .trim()
                  .replaceAll("\\]", "")
                  .replaceAll("\\[", ""));
              map.put(key, value);
              updated.put(key, true);
            }
          } catch (NumberFormatException e) {
            throw new RuntimeException(e.getMessage());
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }
    } catch (RuntimeException e) {
      //traffic fail quietly!
      throw(e);
    } catch (ProtocolException e1) {
      throw new RuntimeException("Protocol Error: " + e1.getMessage());
    } catch (IOException e1) {
      throw new RuntimeException("IO Error: " + e1.getMessage());
    }
  }
}
