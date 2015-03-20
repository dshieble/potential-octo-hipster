package edu.brown.cs.sjl2.stars;

import edu.brown.cs.sjl2.kd.KDimensional;

/**
 * Star represents a star in the star database. A star has an ID, name, and a 3D
 * coordinate in space. This coordinate allows for Star to implement
 * KDimensional
 *
 * @author sjl2
 *
 */
public class Star implements KDimensional, Comparable<Star> {

  private String id;
  private String name;
  private double x;
  private double y;
  private double z;
  private static final int K = 3;
  private static final int WORD = 32;

  /**
   * Constructor for object star. Stars data cannot be changed after
   * construction.
   * @param id Star ID in database
   * @param name Name of Star
   * @param x Star's x-coordinate
   * @param y Star's y-coordinate
   * @param z Star's z-coordinate
   */
  public Star(String id, String name, double x, double y, double z) {
    this.id = id;
    this.name = name;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Constructor for object star. Stars data cannot be changed after
   * construction.
   * @param id Star ID in database
   * @param name Name of Star
   * @param coordinates Star's coordinates. coordinates must be an array of
   * the format double[] {x, y, z}.
   */
  public Star(String id, String name, double[] coordinates) {
    this.id = id;
    this.name = name;
    this.x = coordinates[0];
    this.y = coordinates[1];
    this.z = coordinates[2];
  }

  /**
   * Getter for private variable id.
   * @return Returns the star's ID
   */
  @Override
  public String getID() {
    return id;
  }

  /**
   * Getter for private variable name.
   * @return Returns the name of the star
   */
  public String getName() {
    return this.name;
  }

  @Override
  public double getDimension(int n) {
    return getCoordinates()[n];
  }

  @Override
  public double getDimensionalDistance(double[] coordinates, int n) {
    return Math.abs(getDimension(n) - coordinates[n]);
  }
  @Override
  public int getK() {
    return K;
  }

  @Override
  public double[] getCoordinates() {
    return new double[] {x, y, z};
  }

  @Override
  public double getDistance(KDimensional kd) {
    return getDistance(kd.getCoordinates());
  }

  @Override
  public double getDistance(double[] coordinates) {
    double sum = 0.0;
    double[] myCoordinates = getCoordinates();
    int len1 = myCoordinates.length;
    int len2 = coordinates.length;
    double[] longer;
    double[] shorter;

    if (len1 > len2) {
      longer = myCoordinates;
      shorter = coordinates;
    } else {
      longer = coordinates;
      shorter = myCoordinates;
    }

    for (int i = 0; i < shorter.length; i++) {
      sum += Math.pow(shorter[i] - longer[i], 2);
    }
    for (int j = shorter.length; j < longer.length; j++) {
      sum += Math.pow(longer[j], 2);
    }

    return Math.sqrt(sum);
  }

  /**
   * Another method to obtain distance between two stars. neighbor must be
   * non-null.
   * @param neighbor a star to compare distances
   * @return the distance between this star and neighbor
   */
  public double getDistance(Star neighbor) {
    return Math.sqrt(Math.pow(x - neighbor.x, 2)
        + Math.pow(y - neighbor.y, 2)
        + Math.pow(z - neighbor.z, 2));
  }

  /**
   * Finds distance between the star and the point (x,y,z).
   * @param x2 x-coordinate
   * @param y2 y-coordinate
   * @param z2 z-coordinate
   * @return the distance between the star and the point (x,y,z)
   */
  public double getDistance(double x2, double y2, double z2) {
    return Math.sqrt(Math.pow(this.x - x2, 2.0)
        + Math.pow(this.y - y2, 2)
        + Math.pow(this.z - z2, 2));
  }

  /**
   * String representation of star.
   * @return returns the ID of the star as a string
   */
  @Override
  public String toString() {
    return id;
  }

  /**
   * Determines if object o is equivalent to this star. Determines by values and
   * not whether they are the same object.
   * @param o object being tested
   * @return returns true iff object o is a star and has the same name, id, and
   * coordinates.
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Star)) {
      return false;
    }

    Star star = (Star) o;
    return id == star.id
        && name.equals(star.name)
        && x == star.x
        && y == star.y
        && z == star.z;
  }

  @Override
  public int hashCode() {
    long xLong = Double.doubleToLongBits(this.x);
    long yLong = Double.doubleToLongBits(this.y);
    long zLong = Double.doubleToLongBits(this.z);

    return (int) (xLong ^ (xLong >> WORD)
        ^ yLong ^ (yLong >> WORD) ^ (zLong >> WORD));
  }

  @Override
  public int compareTo(Star o) {
    return id.compareTo(o.getID());
  }

}
