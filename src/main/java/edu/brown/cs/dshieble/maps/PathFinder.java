package edu.brown.cs.dshieble.maps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This class uses the input database to find the shortest distance between two
 * nodes.
 * @author dshieble
 *
 */
public class PathFinder implements AutoCloseable {
  private static final int THREE = 3;
  private static final int FOUR = 4;
  private static final int FIVE = 5;

  /**
   * connection to db.
   */
  private Connection conn;

  /**
   * connection to Traffic.
   */
  private TrafficManager tm;

  /**
   * Constructor for the PathFinder class.
   * @param db The database to build the nodes from
   * @param trafficManager The traffic Manager to use
   */
  public PathFinder(final String db, TrafficManager trafficManager) {
    this.tm = trafficManager;
    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + db;
      conn = DriverManager.getConnection(urlToDB);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(
          "Could not find SQLite JDBC Class: " + e.getMessage());
    } catch (SQLException e) {
      throw new RuntimeException(
          "Could not connect to database " + db + ": " + e.getMessage());
    }

  }

  /**
   * Finds a path from the first node to the final node.
   * @param startId the first node id
   * @param endId the final node
   * @param usingTraffic True if we are using traffic, false to ignore
   * @return a list representing a path
   */
  public final List<Node> findPath(final String startId,
      final String endId, final boolean usingTraffic) {

    Set<String> explored = new HashSet<String>();
    if (startId == null || endId == null) {
      return null;
    }
    double[] latLong = getLatLong(startId);
    double[] latLongEnd = getLatLong(endId);
    Node d = new Node(latLong[0], latLong[1],
        startId,
        null, null,
        null, 0, 1);
    PriorityQueue<Node> pq = new PriorityQueue<Node>(1,
        new NodeComparator());
    pq.add(d);
    while (!pq.isEmpty()) {
      Node head = pq.remove();
      if (head.getID().equals(endId)) {
        //System.out.println(head);
        return head.getPath();
      } else if (!explored.contains(head.getID())) {
        explored.add(head.getID());
        Set<Node> nodes = findNodes(head, usingTraffic, latLongEnd);
        for (Node n : nodes) {
          if (!explored.contains(n.getID())) {
            pq.add(n);
          }
        }
      }
    }
    assert pq.isEmpty();
    return new ArrayList<Node>();
  }


  /**
   * Finds all nodes that share an edge with this node.
   * @param node the node
   * @param usingTraffic true if traffic is to be factored in. False otherwise.
   * @param target The lat long of the target. Used to find heuristic
   * @return a list of nodes
   */
  public final Set<Node> findNodes(
      final Node node, final boolean usingTraffic,
      final double[] target) {
    Set<Node> output = new HashSet<Node>();
    String query =
        "SELECT node.id, node.latitude, node.longitude, W.id, W.name "
            + "FROM node INNER JOIN "
            + "(SELECT id, name, end FROM way WHERE start = ?) AS W "
            + "ON W.end = node.id;";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setString(1, node.getID());
      // Execute the query and retrieve a ResultStatement
      try (ResultSet rs = prep.executeQuery()) {
        //Add the results to a list
        while (rs.next()) {
          String id = rs.getString(1);
          double lat = rs.getDouble(2);
          double lon = rs.getDouble(THREE);
          String wayId = rs.getString(FOUR);
          String wayName = rs.getString(FIVE);
          if (id != node.getID()) {
            double heur = 0; //UtilityClass.getDistance(lat, lon,
            //target[0], target[1]);
            double multiplier = 1;
            if (usingTraffic) {
              multiplier = tm.getTrafficLevel(wayId);
              //System.out.println(multiplier);
            }
            Node n = new Node(lat, lon, id, node,
                wayName, wayId, heur, multiplier);
            //System.out.println(id);
            //System.out.println(multiplier);
            output.add(n);
          }
        }
      } catch (SQLException e1) {
        throw new RuntimeException(
            "Could not obtain neighbor nodes." + e1.getMessage());
      }
    } catch (SQLException e2) {
      throw new RuntimeException(
          "Could not obtain neighbor nodes." + e2.getMessage());
    }

    return output;
  }

  /**
   * returns the heuristic value.
   * @param node the node we are recording heuristic fir
   * @param target the final node
   * @return the heuristic value (euclidian)
   */
  public double getHeuristic(Node node, Node target) {
    return node.getDistance(target);
  }

  /**
   * Getter for the Latitude and Longitude of a Node using ID.
   * @param id of a node
   * @return a 2 element array containg the latitude, longtiude of that node
   */
  public double[] getLatLong(String id) {
    // TODO Auto-generated method stub
    double[] latlong = new double[2];
    String latlongquery = "SELECT latitude, longitude FROM node WHERE id = ?";
    try (PreparedStatement prep = conn.prepareStatement(latlongquery)) {
      prep.setString(1, id);
      // Execute the query and retrieve a ResultStatement
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          latlong[0] = rs.getDouble(1);
          latlong[1] = rs.getDouble(2);
        }
      } catch (SQLException e1) {
        throw(e1);
      }
    } catch (SQLException e2) {
      throw new RuntimeException(
          "Could not obtain Lat, Long for " + id + ": " + e2.getMessage());
    }
    return latlong;
  }

  /**
   * Getter for the name of a way based on its id.
   * @param id the id of the way
   * @return the name of the way
   */
  public final String getName(final String id) {
    String name = null;
    String query = "SELECT name FROM way WHERE id = ?";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setString(1, id);
      // Execute the query and retrieve a ResultStatement
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          name = rs.getString(1);
        }
      } catch (SQLException e1) {
        throw(e1);
      }
    } catch (SQLException e2) {
      throw new RuntimeException(
          "Could not obtain name for " + id + ": " + e2.getMessage());
    }
    return name;
  }

  /**
   * Getter for Intersection using two street names.
   * @param name1 A Street Name
   * @param name2 A second street name that intersects the first.
   * @return Returns the id of the node that is at the intersection of the two
   * streets. Throws a runtime exception if there is no intersection.
   */
  public final String getIntersection(final String name1, final String name2) {
    String name = null;
    String[] queries =
    {
      "SELECT A.start FROM "
        + "(SELECT start FROM way WHERE name = ?) AS A "
        + "INNER JOIN "
        + "(SELECT end FROM way WHERE name = ?) AS B "
        + "ON A.start = B.end;",
      "SELECT A.end FROM "
        + "(SELECT end FROM way WHERE name = ?) AS A "
        + "INNER JOIN "
        + "(SELECT start FROM way WHERE name = ?) AS B "
        + "ON A.end = B.start;",
      "SELECT A.start FROM "
        + "(SELECT start FROM way WHERE name = ?) AS A "
        + "INNER JOIN "
        + "(SELECT start FROM way WHERE name = ?) AS B "
        + "ON A.start = B.start;",
      "SELECT A.end FROM "
        + "(SELECT end FROM way WHERE name = ?) AS A "
        + "INNER JOIN "
        + "(SELECT end FROM way WHERE name = ?) AS B "
        + "ON A.end = B.end;"
    };

    for (int i = 0; i < queries.length; i++) {
      try (PreparedStatement prep = conn.prepareStatement(queries[i])) {
        prep.setString(1, name1);
        prep.setString(2, name2);
        // Execute the query and retrieve a ResultStatement
        try (ResultSet rs = prep.executeQuery()) {
          if (rs.next()) {
            name = rs.getString(1);
            break;
          }
        } catch (SQLException e1) {
          throw(e1);
        }
      } catch (SQLException e2) {
        throw new RuntimeException(name1 + " and " + name2 + " do not form an "
            + "intersection. ");
      }
    }
    return name;
  }

  /**
   * Getter for all the nodes in the database.
   * @return a list of all nodes
   */
  public final List<Node> getAllNodes() {
    List<Node> list = new ArrayList<Node>();
    String query = "SELECT id, latitude, longitude FROM node";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      // Execute the query and retrieve a ResultStatement
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          String id = rs.getString(1);
          double lat = rs.getDouble(2);
          double lon = rs.getDouble(THREE);
          Node n = new Node(lat, lon, id, null, null, null, 0, 1);
          list.add(n);
        }
      } catch (SQLException e1) {
        throw(e1);
      }
    } catch (SQLException e2) {
      throw new RuntimeException("Could not get all nodes: " + e2.getMessage());
    }
    return list;
  }

  /**
   * Getter for all the unique street names in the database.
   * @return Returns a list of strings of all the streets
   */
  public final List<String> getStreetNames() {
    List<String> names = new ArrayList<>();
    String query = "SELECT DISTINCT name FROM way;";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          names.add(rs.getString(1));
        }
      } catch (SQLException e1) {
        throw (e1);
      }
    } catch (SQLException e2) {
      throw new RuntimeException("Could not get all street names: "
          + e2.getMessage());
    }

    return names;
  }

  /**
   * Gets all ways within a certain bounding box. Used to find ways for tiles.
   * @param lat1 The minimum Latitude of a box
   * @param lat2 Max lat of a box
   * @param lon1 min longitude of a box
   * @param lon2 max longitude of a box
   * @return a list of all ways whose starts or ends are in the tile
   */
  public final List<Way> getWaysWithin(double lat1, double lat2,
      double lon1, double lon2) {
    List<Way> list = new ArrayList<Way>();
    String query = "SELECT start, end, way.id, way.name FROM way INNER JOIN "
        + "(SELECT id FROM node "
        + "WHERE latitude >= ? "
        + "AND latitude <= ? "
        + "AND longitude >= ? "
        + "AND longitude <= ?"
        + ") "
        + "AS A ON way.start = A.id";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      // Execute the query and retrieve a ResultStatement
      prep.setDouble(1, lat1);
      prep.setDouble(2, lat2);
      prep.setDouble(THREE, lon1);
      prep.setDouble(FOUR, lon2);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          Node start = idToNode(rs.getString(1));
          Node end = idToNode(rs.getString(2));
          String id = rs.getString(THREE);
          String name = rs.getString(FOUR);
          Way w = new Way(start, end, name, id);
          list.add(w);
        }
      } catch (SQLException e1) {
        throw(e1);
      }
    } catch (SQLException e2) {
      throw new RuntimeException(
          "Could not get ways within bounding box: " + e2.getMessage());
    }
    return list;
  }

  /**
   * Finds a node from an id.
   * @param id of a node
   * @return that node
   */
  public Node idToNode(String id) {
    Node node = null;
    String query = "SELECT latitude, longitude FROM node WHERE id = ?";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      // Execute the query and retrieve a ResultStatement
      prep.setString(1, id);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          double lat = rs.getDouble(1);
          double lon = rs.getDouble(2);
          node = new Node(lat, lon, id, null, null, null, 0, 1);
        }
      } catch (SQLException e1) {
        throw(e1);
      }
    } catch (SQLException e2) {
      throw new RuntimeException(
          "Could not obtain node for " + id + ": " + e2.getMessage());
    }
    return node;
  }

  /**
   * Getter for the extrema of a maps database.
   * 0 - min lat
   * 1 - max lat
   * 2 - min lon
   * 3 - max lon
   * @return a 4 elements double array where
   */
  public double[] getMaxMin() {
    double[] output = new double[FOUR];
    String query = "SELECT  "
        + "MIN(latitude), MAX(latitude), MIN(longitude), MAX(longitude) "
        + "FROM node";
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      // Execute the query and retrieve a ResultStatement
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          for (int i = 0; i < output.length; i++) {
            output[i] = rs.getDouble(i + 1);
          }
        }
      } catch (SQLException e1) {
        throw(e1);
      }
    } catch (SQLException e2) {
      throw new RuntimeException(
          "Could not obtain extrema of database: " + e2.getMessage());
    }
    return output;
  }

  @Override
  /**
   * Closes and cleans up any resources.
   */
  public final void close() {
    try {
      conn.close();
    } catch (SQLException e) {
      throw new RuntimeException(
          "Could not close PathFinder: " + e.getMessage());
    }
  }
}
