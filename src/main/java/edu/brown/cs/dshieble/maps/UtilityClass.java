package edu.brown.cs.dshieble.maps;

/**
 * Class of static methods to find haverstine distance.
 *
 * @author sjl2
 *
 */
public final class UtilityClass  {

  public static final double EARTH = 6371000;

  private UtilityClass() {
    // Private Hidden Constructor
  }

  /**
   * Gets haverstine distance between two nodes.
   * @param n1 The first node.
   * @param n2 The second node.
   * @return Returns haverstine distance.
   */
  public static double getDistance(
      final Node n1, final Node n2) {
    double lat1 = Math.toRadians(n1.getLat());
    double lat2 = Math.toRadians(n2.getLat());
    double latDiff =  Math.toRadians(n1.getLat() - n2.getLat());
    double longDiff = Math.toRadians(n1.getLong() - n2.getLong());
    double a = Math.pow(Math.sin(latDiff / 2), 2)
        + Math.cos(lat2) * Math.cos(lat1)
        * Math.pow(Math.sin(longDiff / 2), 2);
    return 2 * EARTH * Math.asin(Math.sqrt(a));
  }

  /**
   * The haverstine distance between two nodes based on their latitudes and
   * longitudes.
   * @param la1 Point 1 Latitude
   * @param lo1 Point 1 Longitude
   * @param la2 Point 2 Latitude
   * @param lo2 Point 2 Longitude
   * @return Returns haverstine distance.
   */
  public static double getDistance(
      double la1, double lo1, double la2, double lo2) {
    double lat1 = Math.toRadians(la1);
    double lat2 = Math.toRadians(la2);
    double latDiff =  Math.toRadians(la1 - la2);
    double longDiff = Math.toRadians(lo1 - lo2);
    double a = Math.pow(Math.sin(latDiff / 2), 2)
        + Math.cos(lat2) * Math.cos(lat1)
        * Math.pow(Math.sin(longDiff / 2), 2);
    return 2 * EARTH * Math.asin(Math.sqrt(a));
  }
}
