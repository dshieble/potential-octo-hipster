package edu.brown.cs.dshieble.maps;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.sjl2.kd.KDimensional;

/**
 * This class represents an node and stores the parent, weight, id and name.
 * @author dshieble
 *
 */
public class Node implements KDimensional {

  /**
   * latitude of node.
   */
  private double lat;
  /**
   * longitude of node.
   */
  private double lon;
  /**
   * node's parent.
   */
  private Node parent;
  /**
   * distance from start to this node.
   */
  private double weight;
  /**
   * node's id.
   */
  private String id;
  /**
   * name of movie on edge.
   */
  private String edgeName;
  /**
   * id of movie on edge.
   */
  private String edgeId;
  /**
   * heuristic guess.
   */
  private double heuristic;
  /**
   * Constructor for a Node that represents an intersection of two ways.
   * @param la the latitude of this node
   * @param lo the longitude of this node
   * @param i this node's id
   * @param p the parent node
   * @param rN the name of the road to this node
   * @param rI the id of the road to this node
   * @param heur the heuristic value from this node
   * @param multiplier the multiplier on the weight
   */
  public Node(final double la, final double lo,
      final String i, final Node p,
      final String rN, final String rI, final double heur,
      final double multiplier) {
    this.lat = la;
    this.lon = lo;
    this.id = i;
    this.parent = p;
    this.edgeName = rN;
    this.edgeId = rI;
    this.heuristic = heur;
    if (parent == null) {
      this.weight = 0;
    } else {
      this.weight = multiplier * getDistance(parent) + parent.getWeight();
    }
  }

  /**
   * @return A list of Nodes that represent the path.
   */
  public final List<Node> getPath() {
    List<Node> path;
    if (parent != null) {
      path = parent.getPath();
    } else {
      path = new ArrayList<Node>();
    }
    path.add(this);
    return path;
  }

  @Override
  /**
   * KDimensional Class method
   * @param n the dimension to find
   * @return the value of the nth dimension
   */
  public final double getDimension(int n) {
    if (n == 0) {
      return lat;
    } else {
      return lon;
    }
  }

  @Override
  /**
   *
   * @return the number of dimensions
   */
  public final int getK() {
    return 2;
  }

  @Override
  /**
   * @return dimensional distance
   */
  public final double getDimensionalDistance(double[] coordinates, int n) {
    double[] myCoor = {this.getLat(), this.getLong()};
    if (n == 0) {
      return UtilityClass.getDistance(myCoor[0], myCoor[1],
          myCoor[0], coordinates[1]);
    } else if (n == 1) {
      return UtilityClass.getDistance(myCoor[0], myCoor[1],
          coordinates[0], myCoor[1]);
    }
    return -1;
  }

  @Override
  /**
   *
   * @return the distance between this node and another node, using the
   * Haversine formula
   */
  public final double getDistance(KDimensional kd) {
    if (!(kd instanceof Node)) {
      return 0;
    } else {
      return UtilityClass.getDistance(this, (Node) kd);
    }
  }

  @Override
  /**
   *
   * @return the distance between this node and some coordinates, using the
   * Haversine formula
   */
  public final double getDistance(double[] coordinates) {
    assert coordinates.length == 2;
    return UtilityClass.getDistance(getLat(), getLong(),
        coordinates[0], coordinates[1]);
  }

  @Override
  /**
   * Returns the latitude and longitude in an array
   */
  public final double[] getCoordinates() {
    double[] coor = {lat, lon};
    return coor;
  }

  /**
   *
   * @return this node's latitude
   */
  public final double getLat() {
    return lat;
  }

  /**
   *
   * @return this node's longitude
   */
  public final double getLong() {
    return lon;
  }

  /**
   *
   * @return this node's id
   */
  @Override
  public final String getID() {
    return id;
  }

  /**
   *
   * @return the name of the movie this node costarred in with its parent
   */
  public final String getEdgeName() {
    return edgeName;
  }

  /**
   *
   * @return the id of the movie this node costarred in with its parent
   */
  public final String getEdgeId() {
    return edgeId;
  }

  /**
   *
   * @return this node's parent
   */
  public final Node getParent() {
    return parent;
  }

  /**
   *
   * @return the weight of the path to this node
   */
  public final double getWeight() {
    return weight;
  }

  /**
   *
   * @return the weight of the path to this node
   */
  public final double getHeuristic() {
    return heuristic;
  }



  /**
   *
   * @param o the node to compare to
   * @return true if the nodes id is equivalent
   */
  @Override
  public final boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Node)) {
      return false;
    }
    Node node = (Node) o;
    return node.getID().equals(id);
  }

  /**
   *@return hashcode of id
  */
  @Override
  public int hashCode() {
    return id.hashCode();
  }

  /**
   *
   * @return string version of object
   */
  @Override
  public final String toString() {
    if (parent != null) {
      return " Parent: " + parent.getID() + " Weight: " + weight
          + " ID: " + id + " Edge Name: " + edgeName;
    } else {
      return " Parent: null" + " Weight: " + weight
          + " ID: " + id + " Edge Name: null";
    }
  }
}
