package edu.brown.cs.sjl2.stars;

import static org.junit.Assert.assertTrue;
import edu.brown.cs.sjl2.kd.KDTree;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class KDTreeTest {

  KDTree<Star> tree;
  static ArrayList<Star> stars;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    stars = new ArrayList<Star>();

    for (int i = 0; i < 200; i++) {
      stars.add(
          new Star(Integer.toString(i),
              "",
              Math.random(),
              Math.random(),
              Math.random()));
    }
  }

  @Before
  public void setUp() throws Exception {
    tree = new KDTree<Star>(3, stars);
  }

  @Test
  public void addTestEmpty() {
    KDTree<Star> empty = new KDTree<Star>(3);
    Star s = new Star(Integer.toString(0), "", 0, 0, 0);
    empty.add(s);
    assertTrue(empty.getSize() == 1);
  }

  @Test
  public void addTestFull() {
    KDTree<Star> empty = new KDTree<Star>(3);
    Star s = new Star("0", "", 0, 0, 0);
    empty.add(s);
    assertTrue(empty.getSize() == 1);
    empty.add(s); // No Effect
    assertTrue(empty.getSize() == 1);
  }

  @Test
  public void searchTest() {
    KDTree<Star> empty = new KDTree<Star>(3);
    Star s = new Star("0", "", 0, 0, 0);
    empty.add(s);
    assertTrue(empty.search(s).equals(s));
  }

  @Test
  public void searchTest2() {
    KDTree<Star> empty = new KDTree<Star>(3);
    Star s = new Star("0", "", 0, 0, 0);
    Star s2 = new Star("1", "", 0, 0, 0);
    empty.add(s);
    empty.add(s2);
    assertTrue(empty.search(s2).equals(s2));
  }

  @Test
  public void searchTest3() {
    KDTree<Star> empty = new KDTree<Star>(3);
    Star s = new Star("0", "", 0, 0, 0);
    Star s2 = new Star("1", "", 0, 0, 0);
    empty.add(s);
    assertTrue(empty.search(s2) == null);
  }

  @Test
  public void iterTest() {
    KDTree<Star> unbalanced = new KDTree<Star>(3);
    String id;
    for (int i = 1; i <= 100; i++) {
      id = Integer.toString(i);
      unbalanced.add(new Star(id, "", 1, 1, 1));
    }
    int idSum = 0;
    int sumAct = 101*50;
    Iterator<Star> iter = unbalanced.iterator();

    while(iter.hasNext()) {
      idSum += Integer.parseInt(iter.next().getID());
    }

    assertTrue(idSum == sumAct);
  }




}
