package edu.brown.cs.dshieble.maps;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.sjl2.kd.KDimensional;

/**
 * This class represents an actor and stores the parent, weight, id and name.
 * @author dshieble
 *
 */
public class Way implements KDimensional {

  /**
   * start node
   */
  private Node start;
  /**
   * end node
   */
  private Node end;
  /**
   * actor's name.
   */
  private String name;
  /**
   * actor's id.
   */
  private String id;

  /**
   * 
   * @param s start node
   * @param e end node
   * @param n name of way
   * @param i id of way
   */
  public Way(Node s, Node e, String n, String i) {
    this.start = s;
    this.end = e;
    this.name = n;
    this.id = i;
  }


  @Override
  /**
   * KDimensional Class method
   * @param n the dimension to find
   * @return the value of the nth dimension
   */
  public final double getDimension(int n) {
    return start.getDimension(n);
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
    return start.getDistance(kd);
  }

  @Override
  /**
   * 
   * @return the distance between this node and some coordinates, using the
   * Haversine formula
   */
  public final double getDistance(double[] coordinates) {
    return start.getDistance(coordinates);

  }

  @Override
  /**
   * Returns the latitude and longitude in an array
   */
  public final double[] getCoordinates() {
    return start.getCoordinates();
  }

  @Override
  /**
  *
  * @return this actor's id
  */
  public final String getID() {
    return id;
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
    if (!(o instanceof Way)) {
      return false;
    }
    Way w = (Way) o;
    return w.getID().equals(id);
  }

//  /**
//  *
//  * @return string version of object
//  */
// public final String toString() {
//   if (parent != null) {
//     return " Parent: " + parent.getID() + " Weight: " + weight
//            + " ID: " + id + " Edge Name: " + edgeName;
//   } else {
//     return " Parent: null" + " Weight: " + weight
//            + " ID: " + id + " Edge Name: null";
//   }
// }
}