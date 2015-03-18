package edu.brown.cs.sjl2.stars;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import edu.brown.cs.sjl2.kd.KDimensional;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StarsTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  /**
   * Tests Star constructor and getter to make sure it instantiates itself and
   * private variables correctly.
   */
  @Test
  public void constructorGetterTest() {
    String id = "0";
    String name = "Frank";
    double x = 0.0;
    double y = -1.0;
    double z = 1.0;
    double[] coordinates = new double[] {x, y, z};

    Star test = new Star(id, name, x, y, z);

    for (int i = 0; i < 3; i++) {
      assertTrue(String.format("getDimension(%d) Failed.", i),
          test.getDimension(i) == coordinates[i]);
    }

    assertTrue("getK failed.", test.getK() == 3);
    assertTrue("getCoordinates failed",
        Arrays.equals(test.getCoordinates(), coordinates));
    assertTrue("getName failed.", test.getName().equals(name));
    assertTrue("getID failed.", test.getID() == id);
  }

  /**
   * Test all of the getDistance formulas for both KDs and Stars.
   */
  @Test
  public void getDistanceTest() {
    double dist = Math.pow(0.7,2) + Math.pow(2.0,2) + Math.pow(1.9,2);
    dist = Math.sqrt(dist);
    KDimensional kd1 = new Star("0", "Frank", 0.0, -1.0, 1.4);
    KDimensional kd2 = new Star("0", "Sue", 0.7, 1.0, -0.5);
    Star star1 = new Star("0", "Frank", 0.0, -1.0, 1.4);
    Star star2 = new Star("", "Sue", 0.7, 1.0, -0.5);

    // Formula Test
    assertTrue("KDimensional get Distance failed.",
        kd1.getDistance(kd2) == dist);
    assertTrue("Star getDistance failed.",star1.getDistance(star2) == dist);

    // Switch Positions
    assertTrue("Communative 1 getDistance failed.",
        kd2.getDistance(kd1) == dist);
    assertTrue("Communative 2 getDistance failed.",
        star2.getDistance(star1) == dist);

    // Zero Distance
    assertTrue("Zero Distance failed", star1.getDistance(star1) == 0.0);
    assertTrue("Zero Distance failed", kd1.getDistance(kd1) == 0.0);

    // getDistance(coordinates)
    assertTrue("getDistance(coordinates) failed.",
        star1.getDistance(star2.getCoordinates()) == dist);
    assertTrue("getDistance(coordinates) failed.",
        kd1.getDistance(kd2.getCoordinates()) == dist);

    // getDistance(x,y,z)
    assertTrue("getDistance(x,y,z) failed.",
        star1.getDistance(0.7, 1.0, -0.5) == dist);
  }

  /**
   * Tests equals.
   */
  @Test
  public void equalsTest() {
    Star star1 = new Star("0", "Frank", 0.0, -1.0, 1.4);
    Star star2 = new Star("0", "Sue", 0.7, 1.0, -0.5);
    Star star3 = new Star("0", "Frank", 0.0, -1.0, 1.4);
    Star star4 = new Star("0", "Frank", 0.0, -1.0, 1.3);
    assertTrue(star1.equals(star1));
    assertTrue(star1.equals(star3));
    assertTrue(star3.equals(star1));
    assertFalse(star1.equals(star2));
    assertFalse(star2.equals(star1));
    assertFalse(star3.equals(star4));

  }

  /**
   * Tests toString.
   */
  @Test
  public void toStringTest() {
    Star star1 = new Star("10", "Frank", 0.0, -1.0, 1.4);
    assertTrue(star1.toString().equals(Integer.toString(10)));
  }

  /**
   * Tests hashCode.
   */
  @Test
  public void hashCodeTest() {
    Star star1 = new Star("0", "Frank", 0.0, -1.0, 1.4);
    Star star2 = new Star("0", "Frank", 0.0, -1.0, 1.4);
    assertTrue(star1.hashCode() == star2.hashCode());

  }

}
