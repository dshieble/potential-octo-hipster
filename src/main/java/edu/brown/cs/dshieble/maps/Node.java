package edu.brown.cs.dshieble.maps;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.sjl2.kd.KDimensional;

/**
 * This class represents an actor and stores the parent, weight, id and name.
 * @author dshieble
 *
 */
public class Node implements KDimensional {

  /**
   * latitude of node
   */
  private double lat;
  /**
   * longitude of node
   */
  private double lon;
  /**
   * actor's parent.
   */
  private Node parent;
  /**
   * distance from start to this node
   */
  private double weight;
  /**
   * actor's name.
   */
  private String name;
  /**
   * actor's id.
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
   * heuristic guess
   */
  private double heuristic;
  /**
   * @param la the latitude of this node
   * @param lo the longitude of this node
   * @param i this node's id
   * @param p the parent node
   * @param rN the name of the road to this node
   * @param rI the id of the road to this node
   * @param heur the heuristic value from this node
   */
  public Node(final double la, final double lo,
      final String i, final Node p,
      final String rN, final String rI, final double heur,
      final int multiplier) {
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
      this.weight = multiplier*getDistance(parent) + parent.getWeight();
    }
  }

  /**
   *
   * @return A list of actors that form a pathto this actor
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
  * @return this actor's id
  */
  public final String getID() {
    return id;
  }

//  /**
//  *
//  * @return this actor's name
//  */
//  public final String getName() {
//    return name;
//  }

  /**
  *
  * @return the name of the movie this actor costarred in with its parent
  */
  public final String getEdgeName() {
    return edgeName;
  }

  /**
  *
  * @return the id of the movie this actor costarred in with its parent
  */
  public final String getEdgeId() {
    return edgeId;
  }

  /**
  *
  * @return this actor's parent
  */
  public final Node getParent() {
    return parent;
  }

  /**
   *
   * @return the weight of the path to this actor
   */
  public final double getWeight() {
    return weight;
  }

  /**
  *
  * @return the weight of the path to this actor
  */
  public final double getHeuristic() {
    return heuristic;
  }


  
  /**
   *
    * @param o the actor to compare to
   * @return true if the actors id is equivalent
   */
  @Override
  public final boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Node)) {
      return false;
    }
    Node actor = (Node) o;
    return actor.getID().equals(id);
  }

  /**
  *
  * @return string version of object
  */
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
