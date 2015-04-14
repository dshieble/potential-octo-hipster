package edu.brown.cs.dshieble.maps;

import edu.brown.cs.sjl2.kd.KDimensional;

/**
 * This class represents an actor and stores the parent, weight, id and name.
 * @author dshieble
 *
 */
public class Way implements KDimensional {

  /**
   * start node.
   */
  private Node start;
  /**
   * end node.
   */
  private Node end;
  /**
   * Street name.
   */
  private String name;
  /**
   * Way ID.
   */
  private String id;

  /**
   * Constructor for a Way representing a one-way street in maps.
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
   * KDimensional Class method.
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
   * @return dimensional distance
   */
  public final double getDimensionalDistance(double[] coordinates, int n) {
    return start.getDimensionalDistance(coordinates, n);
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

  /**
   *
   * @return start node
   */
  public Node getStart() {
    return start;
  }

  /**
   *
   * @return end node
   */
  public Node getEnd() {
    return end;
  }

  /**
  *
  * @return string version of object
  */
  @Override
  public final String toString() {
    return "Name: " + name + " ID: " + id
         + " Start: " + start.getID() + " End: " + end.getID();
  }

}
