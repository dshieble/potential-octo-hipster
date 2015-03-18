package edu.brown.cs.sjl2.stars;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator to used to compare two stars. Determines which of two stars is
 * nearer or farther from a reference star.
 *
 * @author sjl2
 *
 */
public class StarDistanceComparator implements Comparator<Star>, Serializable {

  private static final long serialVersionUID = 1L;
  private double x;
  private double y;
  private double z;
  private int multiplier;

  /**
   * Constructor for the StarDistanceComparator. Initiate with leastFirst true
   * if you want the comparator to order the closest stars first. False if you
   * would like to compare the farthest stars first.
   * @param leastFirst Boolean that is true if comparing nearest star.
   * @param origin A Star that is the reference to compare the distances between
   * two stars.
   */
  public StarDistanceComparator(boolean leastFirst, Star origin) {
    double[] coordinates = origin.getCoordinates();
    this.x = coordinates[0];
    this.y = coordinates[1];
    this.z = coordinates[2];

    if (leastFirst) {
      multiplier = 1;
    } else {
      multiplier = -1;
    }
  }

/**
 * Constructor for the StarDistanceComparator. Initiate with leastFirst true
 * if you want the comparator to order the closest stars first. False if you
 * would like to compare the farthest stars first.
 * @param leastFirst Boolean to determine if comparator is nearest or farthest
 * @param x double X-Coordinate
 * @param y double Y-Coordinate
 * @param z double Z-Coordinate
 */
  public StarDistanceComparator(boolean leastFirst,
      double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;

    if (leastFirst) {
      multiplier = 1;
    } else {
      multiplier = -1;
    }
  }

  @Override
  public int compare(Star s1, Star s2) {
    double dist1 = s1.getDistance(x, y, z);
    double dist2 = s2.getDistance(x, y, z);
    double diff = dist1 - dist2;
    if (diff > 0) {
      return multiplier;
    } else if (diff < 0) {
      return -1 * multiplier;
    } else {
      return s1.getID().compareTo(s2.getID());
    }
  }
}
