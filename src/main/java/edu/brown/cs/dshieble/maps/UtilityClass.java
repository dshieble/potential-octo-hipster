package edu.brown.cs.dshieble.maps;

public final class UtilityClass  {

  public static final double earth = 6371000;

  public static double getDistance(
      final Node n1, final Node n2) {
    double lat1 = Math.toRadians(n1.getLat());
    double lat2 = Math.toRadians(n2.getLat());
    double latDiff =  Math.toRadians(n1.getLat() - n2.getLat());
    double longDiff = Math.toRadians(n1.getLong() - n2.getLong());
    double a = Math.pow(Math.sin(latDiff / 2), 2)
        + Math.cos(lat2) * Math.cos(lat1)
        * Math.pow(Math.sin(longDiff / 2), 2);
    return 2 * earth * Math.asin(Math.sqrt(a));
  }

  public static double getDistance(
      double la1, double lo1, double la2, double lo2) {
    double lat1 = Math.toRadians(la1);
    double lat2 = Math.toRadians(la2);
    double latDiff =  Math.toRadians(la1 - la2);
    double longDiff = Math.toRadians(lo1 - lo2);
    double a = Math.pow(Math.sin(latDiff / 2), 2)
        + Math.cos(lat2) * Math.cos(lat1)
        * Math.pow(Math.sin(longDiff / 2), 2);
    return 2 * earth * Math.asin(Math.sqrt(a));
  }
} 
