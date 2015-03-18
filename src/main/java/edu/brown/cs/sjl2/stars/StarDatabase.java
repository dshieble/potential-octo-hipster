package edu.brown.cs.sjl2.stars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.brown.cs.sjl2.kd.KDTree;


/**
 * This class represents a Star Database. It is created from a CSV containing
 * Star ID, Name, X, Y, Z on each line. The name is optional, but must be an
 * empty string at the least. StarDatabse makes use of the class @link KDTree to
 * efficiently store and retrieve the nearest neighbors to a point or star or
 * the list of star id's within a certain distance of a point or star.
 * @author sjl2
 *
 */
public final class StarDatabase {

  private KDTree<Star> stars;
  private static final int KSTAR = 3;

  /**
   * Indices represents the different constants needed for indices from commands
   *  that will be needed.
   *
   * @author sjl2
   *
   */
  public enum Indices {
    ID(0), NAME(1), X(2), Y(3), Z(4);

    private int value;

    Indices(int value) {
      this.value = value;
    }

    /**
     * Obtain the value of the Index.
     * @return Returns the value of the Index
     */
    public int getValue() {
      return this.value;
    }
  }

  /**
   * Constructor for the object Star Database (StarDB). The database uses the
   * class KDTree to store a stars database. This allows users to find the k
   * nearest neighbors of a star or point efficiently.
   * @param db a File that is a csv file with the form ID, Name, X, Y, Z per
   * line
   * @throws IOException If readers suffer fatal errors
   */
  public StarDatabase(File db) throws IOException {

    ArrayList<Star> starsList = new ArrayList<Star>();
    BufferedReader csv = new BufferedReader(
        new InputStreamReader(new FileInputStream(db), "UTF8"));

    String line = csv.readLine(); // Remove Header
    line = csv.readLine();

    String[] words;

    // Parse Each Line
    try {
      while (line != null) {
        words = line.split(",");
        String starID = words[Indices.ID.getValue()];
        String name = words[Indices.NAME.getValue()];
        double x = Double.parseDouble(words[Indices.X.getValue()]);
        double y = Double.parseDouble(words[Indices.Y.getValue()]);
        double z = Double.parseDouble(words[Indices.Z.getValue()]);
        starsList.add(new Star(starID, name, x, y, z));
        line = csv.readLine();
      }

      // Efficient Initialization
      stars = new KDTree<Star>(KSTAR, starsList);

    } catch (NumberFormatException ex) {
      csv.close();
      throw new IllegalArgumentException(
          ".csv file entered is not formatted correctly.");
    }

    // Close Buffer
    csv.close();
  }

  /**
   * Finds the k nearest neighbors to the test point (x,y,z). k must be
   * non-negative.
   * @param k a non-negative number of neighbors to be found
   * @param x x-coordinate
   * @param y y-coordinate
   * @param z z-coordinate
   * @return An ArrayList of the star ID's of the nearest neighbors.
   *
   */
  public ArrayList<String> neighbors(int k, double x, double y, double z) {
    if (k < 0) {
      throw new IllegalArgumentException("ERROR: k must be non-negative");
    }

    ArrayList<String> neighbors = new ArrayList<String>();
    ArrayList<Star> n = stars.neighbors(k, new double[] {x, y, z});

    for (int i = 0; i < Math.min(k, n.size()); i++) {
      neighbors.add(n.get(i).getID());
    }

    return neighbors;
  }

  /**
   * Uses neighbors(int k, double x, double y, double z) to find the nearest
   * neighbor to the Star star. This does not include itself.
   * @param k The number of neighbors to include
   * @param star The reference star for the neighbors
   * @return Returns an ArrayList of the Neighbors' Star IDs
   */
  public ArrayList<String> neighbors(int k, Star star) {
    double[] coords = star.getCoordinates();
    // Search for it's k neighbors (not including itself)
    ArrayList<String> n = neighbors(k + 1, coords[0], coords[1], coords[2]);
    n.remove(0); // Remove closest element (star)
    return n;

  }

  /**
   * Finds the k nearest neighbor to the star with the name name.
   * @param k The number of neighbors to be returned.
   * @param name Name of the star to be searched for
   * @return Returns an ArrayList of the Star IDs for the k nearest neighbors.
   */
  public ArrayList<String> neighbors(int k, String name) {
    if (name.length() == 0) {
      throw new IllegalArgumentException("name must not be an empty string.");
    }

    for (Star s: stars) {
      if (name.equals(s.getName())) {
        return neighbors(k, s);
      }
    }

    // NAME DOES NOT EXIST
    throw new IllegalArgumentException("Star not in database");
  }

  /**
   * Finds all of the stars within a radius r of the point (x,y,z). This
   * includes points exactly r away from the center.
   * @param r The radius of inclusion.
   * @param x x-coordinate
   * @param y y-coordinate
   * @param z z-coordinate
   * @return Returns an ArrayList of the Star ID's that are within the radius
   * (inclusive)
   */
  public ArrayList<String> radius(double r, double x, double y, double z) {
    if (r < 0) {
      throw new IllegalArgumentException("radius must be non-negative.");
    }

    ArrayList<String> toReturn = new ArrayList<>();
    ArrayList<Star> st = stars.radius(r, new double[] {x, y, z});

    for (Star s: st) {
      toReturn.add(s.getID());
    }

    return toReturn;
  }

  /**
   * Finds all points within a radius r of the Star star. This includes points
   * exactly r away from the star and does NOT include star.
   * @param r The radius of inclusion
   * @param star The star at the center of the radius.
   * @return Returns an ArrayList of the neighbors' Star IDs
   */
  public ArrayList<String> radius(double r, Star star) {
    double[] coords = star.getCoordinates();
    ArrayList<String> neighbors = radius(r, coords[0], coords[1], coords[2]);
    // Remove the center star
    neighbors.remove(0);
    return neighbors;
  }

  /**
   * Finds all points within a radius r of the star with the name name. This
   * includes points exactly r away from star and does NOT include the star.
   * @param r The radius of inclusion
   * @param name The name of the star at the center of the radius
   * @return Returns an ArrayList of the neighbors' Star IDs
   */
  public ArrayList<String> radius(double r, String name) {
    if (name.length() == 0) {
      throw new IllegalArgumentException("Name must not be an empty string.");
    }

    for (Star s : stars) {
      if (name.equals(s.getName())) {
        return radius(r, s);
      }
    }

    // Name DOES NOT EXIST
    throw new IllegalArgumentException("Star not in database.");
  }
}
