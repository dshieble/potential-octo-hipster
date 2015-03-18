package edu.brown.cs.sjl2.stars;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class StarDatabaseTest {

  private StarDatabase fullDB;
  private StarDatabase tenStars;
  private StarDatabase oneStar;
  private StarDatabaseNaive fullTest;
  private StarDatabaseNaive tenTest;
  private StarDatabaseNaive oneTest;

  @Before
  public void setUp() throws Exception {
    fullDB = new StarDatabase(
        new File("/course/cs032/data/stars/stardata.csv"));
    tenStars = new StarDatabase(
        new File("/course/cs032/data/suites/files/stars/ten-star.txt"));
    oneStar = new StarDatabase(
        new File("/course/cs032/data/suites/files/stars/one-star.txt"));
    fullTest = new StarDatabaseNaive(
        new File("/course/cs032/data/stars/stardata.csv"));
    tenTest = new StarDatabaseNaive(
        new File("/course/cs032/data/suites/files/stars/ten-star.txt"));
    oneTest = new StarDatabaseNaive(
        new File("/course/cs032/data/suites/files/stars/one-star.txt"));
  }

  @Test
  public void noNeighborsTest() {
    assertTrue("noNeighbors1 failed.",
        fullDB.neighbors(0, 0 , 0 , 0).equals(new ArrayList<Integer>()));
    assertTrue("noNeighbors2 failed.",
        fullDB.neighbors(0, "Sol").equals(new ArrayList<Integer>()));
    assertTrue("noNeighbors3 failed.", tenStars.neighbors(0, 0 , 0 , 0).equals(
        new ArrayList<Integer>()));
    assertTrue("noNeighbors4 failed.",
        tenStars.neighbors(0, "Sol").equals(new ArrayList<Integer>()));
    assertTrue("noNeighbors5 failed.", oneStar.neighbors(0, 0 , 0 , 0).equals(
        new ArrayList<Integer>()));
    assertTrue("noNeighbors6 failed.",
        oneStar.neighbors(0, "Lonely Star").equals(new ArrayList<Integer>()));
  }

  @Test
  public void nearestTest() {
    assertTrue("nearest1 failed.", fullDB.neighbors(1, 0, 0 , 0).equals(
        fullTest.neighbors(1, 0, 0, 0)));
    assertTrue("nearest2 failed.", fullDB.neighbors(1, 100.0, 0 , 0).equals(
        fullTest.neighbors(1, 100.0, 0, 0)));
    assertTrue("nearest3 failed.", fullDB.neighbors(1, 20.0, 20.0 , 20.0).equals(
        fullTest.neighbors(1, 20.0, 20.0, 20.0)));
    assertTrue("nearest4 failed.", fullDB.neighbors(1, -20.0, -20.0 , -20.0).equals(
        fullTest.neighbors(1, -20.0, -20.0, -20.0)));
    assertTrue("nearest5 failed.", tenStars.neighbors(1, 0, 0 , 0).equals(
        tenTest.neighbors(1, 0, 0, 0)));
    assertTrue("nearest6 failed.", tenStars.neighbors(1, 100.0, 0 , 0).equals(
        tenTest.neighbors(1, 100.0, 0, 0)));
    assertTrue("nearest7 failed.", tenStars.neighbors(1, 20.0, 20.0 , 20.0).equals(
        tenTest.neighbors(1, 20.0, 20.0, 20.0)));
    assertTrue("nearest8 failed.", tenStars.neighbors(1, -20.0, -20.0 , -20.0).equals(
        tenTest.neighbors(1, -20.0, -20.0, -20.0)));
  }

  @Test
  public void nearest5Test() {
    assertTrue("nearest5(1) failed.", fullDB.neighbors(5, 0, 0 , 0).equals(
        fullTest.neighbors(5, 0, 0, 0)));
    assertTrue("nearest5(2) failed.", fullDB.neighbors(5, 100.0, 0 , 0).equals(
        fullTest.neighbors(5, 100.0, 0, 0)));
    assertTrue("nearest5(3) failed.", fullDB.neighbors(5, 20.0, 20.0 , 20.0).equals(
        fullTest.neighbors(5, 20.0, 20.0, 20.0)));
    assertTrue("nearest5(4) failed.", fullDB.neighbors(5, -20.0, -20.0 , -20.0).equals(
        fullTest.neighbors(5, -20.0, -20.0, -20.0)));
    assertTrue("nearest5(5) failed.", tenStars.neighbors(5, 0, 0 , 0).equals(
        tenTest.neighbors(5, 0, 0, 0)));
    assertTrue("nearest5(6) failed.", tenStars.neighbors(5, 100.0, 0 , 0).equals(
        tenTest.neighbors(5, 100.0, 0, 0)));
    assertTrue("nearest5(7) failed.", tenStars.neighbors(5, 20.0, 20.0 , 20.0).equals(
        tenTest.neighbors(5, 20.0, 20.0, 20.0)));
    assertTrue("nearest5(8) failed.", tenStars.neighbors(5, -20.0, -20.0 , -20.0).equals(
        tenTest.neighbors(5, -20.0, -20.0, -20.0)));
  }

  @Test
  public void neighborsNameTest() {
    assertTrue("nearestName1 failed.",
        tenStars.neighbors(5, "Barnard's Star").equals(
            tenTest.neighbors(5, "Barnard's Star")));
    assertTrue("nearestName2 failed.", tenStars.neighbors(5, "Sol").equals(
        tenTest.neighbors(5, "Sol")));
    assertTrue("nearestName3 failed.",
        tenStars.neighbors(5, "Rigel Kentaurus A").equals(
        tenTest.neighbors(5, "Rigel Kentaurus A")));
    assertTrue("nearestName4 failed.",
        tenStars.neighbors(10, "Barnard's Star").equals(
        tenTest.neighbors(10, "Barnard's Star")));
    assertTrue("nearestName5 failed.", tenStars.neighbors(10, "Sol").equals(
        tenTest.neighbors(10, "Sol")));
    assertTrue("nearestName6 failed.",
        tenStars.neighbors(10, "Rigel Kentaurus A").equals(
        tenTest.neighbors(10, "Rigel Kentaurus A")));
    assertTrue("nearestName7 failed.",
        fullDB.neighbors(5, "Barnard's Star").equals(
        fullTest.neighbors(5, "Barnard's Star")));
    assertTrue("nearestName8 failed.",
        fullDB.neighbors(5, "Sol").equals(
        fullTest.neighbors(5, "Sol")));
    assertTrue("nearestName9 failed.",
        fullDB.neighbors(5, "Rigel Kentaurus A").equals(
        fullTest.neighbors(5, "Rigel Kentaurus A")));
    assertTrue("nearestName10 failed.",
        fullDB.neighbors(10, "Barnard's Star").equals(
        fullTest.neighbors(10, "Barnard's Star")));
    assertTrue("nearestName11 failed.",
        fullDB.neighbors(10, "Sol").equals(
        fullTest.neighbors(10, "Sol")));
    assertTrue("nearestName12 failed.",
        fullDB.neighbors(10, "Rigel Kentaurus A").equals(
        fullTest.neighbors(10, "Rigel Kentaurus A")));
  }

  @Test
  public void overflowTest() {
    assertTrue("overflow1 failed.", tenStars.neighbors(11, 0, 0 , 0).equals(
        tenTest.neighbors(11, 0, 0, 0)));
    assertTrue("overflow2 failed.", tenStars.neighbors(10, 0, 0 , 0).equals(
        tenTest.neighbors(10, 0, 0, 0)));
    assertTrue("overflow3 failed.", tenStars.neighbors(20, 0, 0 , 0).equals(
        tenTest.neighbors(20, 0, 0, 0)));
    assertTrue("overflow4 failed.", oneStar.neighbors(0, 0, 0 , 0).equals(
        oneTest.neighbors(0, 0, 0, 0)));
    assertTrue("overflow5 failed.", oneStar.neighbors(1, 0, 0 , 0).equals(
        oneTest.neighbors(1, 0, 0, 0)));
    assertTrue("overflow6 failed.", oneStar.neighbors(2, 0, 0 , 0).equals(
        oneTest.neighbors(2, 0, 0, 0)));
  }

  @Test
  public void radius0Test() {
    assertTrue("zeroRadius1 failed.",
        fullDB.radius(0, 0.1 , 0 , 0).equals(new ArrayList<Integer>()));
    assertTrue("zeroRadius2 failed.", tenStars.radius(0, 1.0, 0 , 0).equals(
        new ArrayList<Integer>()));
    assertTrue("zeroRadius3 failed.",
        tenStars.radius(0, "Sol").equals(new ArrayList<Integer>()));
    assertTrue("zeroRadius4 failed.", oneStar.radius(0, 0, 1.0, 0).equals(
       new ArrayList<Integer>()));
  }

  @Test
  public void radius1Test() {
    assertTrue("radiusOne1 failed.", fullDB.radius(1, 0, 0 , 0).equals(
        fullTest.radius(1, 0, 0, 0)));
    assertTrue("radiusOne2 failed.", fullDB.radius(1, 100.0, 0 , 0).equals(
        fullTest.radius(1, 100.0, 0, 0)));
    assertTrue("radiusOne3 failed.", fullDB.radius(1, 20.0, 20.0 , 20.0).equals(
        fullTest.radius(1, 20.0, 20.0, 20.0)));
    assertTrue("radiusOne4 failed.",
        fullDB.radius(1, -20.0, -20.0 , -20.0).equals(
        fullTest.radius(1, -20.0, -20.0, -20.0)));
    assertTrue("radiusOne5 failed.", tenStars.radius(1, 0, 0 , 0).equals(
        tenTest.radius(1, 0, 0, 0)));
    assertTrue("radiusOne6 failed.", tenStars.radius(1, 100.0, 0 , 0).equals(
        tenTest.radius(1, 100.0, 0, 0)));
    assertTrue("radiusOne7 failed.",
        tenStars.radius(1, 20.0, 20.0 , 20.0).equals(
        tenTest.radius(1, 20.0, 20.0, 20.0)));
    assertTrue("radiusOne8 failed.",
        tenStars.radius(1, -20.0, -20.0 , -20.0).equals(
        tenTest.radius(1, -20.0, -20.0, -20.0)));
  }

  @Test
  public void radius10Test() {
    assertTrue("radiusFive1 failed.", fullDB.radius(10, 0, 0, 0).equals(
        fullTest.radius(10, 0, 0, 0)));
    assertTrue("radiusFive2 failed.", fullDB.radius(10, 100.0, 0, 0).equals(
        fullTest.radius(10, 100.0, 0, 0)));
    assertTrue("radiusFive3 failed.", fullDB.radius(10, 20.0, 20.0, 20.0).equals(
        fullTest.radius(10, 20.0, 20.0, 20.0)));
    assertTrue("radiusFive4 failed.",
        fullDB.radius(10, -20.0, -20.0, -20.0).equals(
        fullTest.radius(10, -20.0, -20.0, -20.0)));
    assertTrue("radiusFive5 failed.", tenStars.radius(10, 0, 0, 0).equals(
        tenTest.radius(10, 0, 0, 0)));
    assertTrue("radiusFive6 failed.", tenStars.radius(10, 100.0, 0, 0).equals(
        tenTest.radius(10, 100.0, 0, 0)));
    assertTrue("radiusFive7 failed.",
        tenStars.radius(10, 20.0, 20.0, 20.0).equals(
        tenTest.radius(10, 20.0, 20.0, 20.0)));
    assertTrue("radiusFive8 failed.",
        tenStars.radius(10, -20.0, -20.0, -20.0).equals(
        tenTest.radius(10, -20.0, -20.0, -20.0)));
  }

  @Test
  public void radiusNameTest() {
    assertTrue("radiusName1 failed.",
        tenStars.radius(5, "Barnard's Star").equals(
        tenTest.radius(5, "Barnard's Star")));
    assertTrue("radiusName2 failed.", tenStars.radius(5, "Sol").equals(
        tenTest.radius(5, "Sol")));
    assertTrue("radiusName3 failed.",
        tenStars.radius(5, "Rigel Kentaurus A").equals(
        tenTest.radius(5, "Rigel Kentaurus A")));
    assertTrue("radiusName4 failed.",
        tenStars.radius(10, "Barnard's Star").equals(
        tenTest.radius(10, "Barnard's Star")));
    assertTrue("radiusName5 failed.", tenStars.radius(10, "Sol").equals(
        tenTest.radius(10, "Sol")));
    assertTrue("radiusName6 failed.",
        tenStars.radius(10, "Rigel Kentaurus A").equals(
        tenTest.radius(10, "Rigel Kentaurus A")));
    assertTrue("radiusName8 failed.", fullDB.radius(10, "Sol").equals(
        fullTest.radius(10, "Sol")));
    assertTrue("radiusName9 failed.",
        fullDB.radius(10, "Rigel Kentaurus A").equals(
        fullTest.radius(10, "Rigel Kentaurus A")));
  }

}
