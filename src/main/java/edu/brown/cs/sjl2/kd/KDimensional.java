package edu.brown.cs.sjl2.kd;

/**
 * KDimensional is an interface that represents any object that has a connection
 * to a coordinate in k-dimensions. Any object that implements KDimensional
 * should use these methods and represent coordinates as a double[] where
 * the format of the array is {x, y, z, ...}. KDimensionals may have an ID, but
 * must have a getID() in order to have additional sorting. If IDs do not make
 * sense with your class,
 *
 * @author sjl2
 *
 */
public interface KDimensional {

  /**
   * Returns the ID of a KDimensional. Should be -1 if implementing class does
   * not have a private variable for ID. ALlows for sorting by ID.
   * @return Returns an int of the KDimensional's ID or -1 if it does not have
   * one.
   */
  String getID();

  /**
   * Returns the coordinate value of the dimension n. Dimensions start at 0 and
   * continue increasing until k-1. Geometrically, the dimensions map 0 to x,
   * 1 to y, 2 to z, etc.
   * @param n the dimension obtained begins at zero. n must be non-negative.
   * @return returns the double of the coordinate value of dimension n.
   */
  double getDimension(int n);

  /**
   * Returns the positive distance between a KD and a coordinate along the nth
   * dimension.
   * @param coordinates A Double Array of coordinates with k elements.
   * @param n The nth dimension to compare the distances.
   * @return Returns the dimensional distance between this KD and the
   * coordinates.
   */
  double getDimensionalDistance(double[] coordinates, int n);

  /**
   * In KDimensional's, k represents the number of dimensions (length of
   * double[] coordinates.
   * @return returns the number of dimensions in the K-Space.
   */
  int getK();

  /**
   * Returns the distance to another KDimensional Object kd.
   * @param kd another KDimensional object. kd must not be null.
   * @return returns the double that represents the distance between the two
   * KDimensionals.
   */
  double getDistance(KDimensional kd);

  /**
   * Returns the distance to an arbitrary coordinate. The coordinates to not
   * necessarily need to have the same number of dimensions. Assumes a value of
   * zero for the dimensions that are not used by the KDimensional or vice
   * versa.
   * @param coordinates The Coordinates to find the distance to
   * @return returns the distance between the given coordinates and the
   * KDimensional's coordinates.
   */
  double getDistance(double[] coordinates);

  /**
   * A getter that returns the coordinates of a KDimensional in the form of a
   * double array.
   * @return Returns a double[] representing coordinates as {x, y, z .. etc.}
   */
  double[] getCoordinates();

}
