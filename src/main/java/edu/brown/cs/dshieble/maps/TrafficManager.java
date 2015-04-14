package edu.brown.cs.dshieble.maps;

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
   * Constructor for the Traffic Manger. Links the rest of the application with
   * the thread that is repeatedly updating traffic.
   * @param p The port to look for the traffic.
   */
  public TrafficManager(int p) {
    time = new AtomicInteger(0);
    map = new ConcurrentHashMap<String, Double>();
    updated = new ConcurrentHashMap<String, Boolean>();

    Thread t = new TrafficThread(p, time, map, updated);
    t.start();

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
   * @return the traffic of that way or 1.0 if normal traffic
   */
  public double getTrafficLevel(String id) {
    return map.get(id) != null ? map.get(id) : 1.0;
  }

}
