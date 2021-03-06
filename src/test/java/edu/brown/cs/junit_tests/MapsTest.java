package edu.brown.cs.junit_tests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Set;

import java.sql.SQLException;

import edu.brown.cs.dshieble.maps.*;
import edu.brown.cs.sjl2.kd.KDTree;
import static org.junit.Assert.*;


public class MapsTest {

  public boolean nearlyEqual(double n1, double n2) {
    return Math.abs(n1 - n2) < 1;
  }

  @Test
  public void testTest() {
    assertTrue(1 == 1);
  }

  @Test
  public void getLatLongTest()  throws ClassNotFoundException, SQLException {
    String id0 = "/n/0";
    String id1 = "/n/1";
    String id2 = "/n/2";
    String id3 = "/n/3";
    String id4 = "/n/4";
    String id5 = "/n/5";
    double[] e0 = {41.82, -71.4};
    double[] e1 = {41.8203, -71.4};
    double[] e2 = {41.8206, -71.4};
    double[] e3 = {41.82, -71.4003};
    double[] e4 = {41.8203, -71.4003};
    double[] e5 = {41.8206, -71.4003};
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    assertTrue(Arrays.equals(p.getLatLong(id0), e0));
    assertTrue(Arrays.equals(p.getLatLong(id1), e1));
    assertTrue(Arrays.equals(p.getLatLong(id2), e2));
    assertTrue(Arrays.equals(p.getLatLong(id3), e3));
    assertTrue(Arrays.equals(p.getLatLong(id4), e4));
    assertTrue(Arrays.equals(p.getLatLong(id5), e5));
  }

  @Test
  public void distanceTest() {
    double[] ll0 = {41.82, -71.4};
    double[] ll1 = {41.8203, -71.4};
    double[] ll2 = {41.8206, -71.4};
    double[] ll3 = {41.82, -71.4003};
    double[] ll4 = {41.8203, -71.4003};
    double[] ll5 = {41.8206, -71.4003};
    Node Node0 = new Node(ll0[0], ll0[1], null, null, null, null, 0, 1);
    Node Node1 = new Node(ll1[0], ll1[1], null, null, null, null, 0, 1);
    Node Node2 = new Node(ll2[0], ll2[1], null, null, null, null, 0, 1);
    Node Node3 = new Node(ll3[0], ll3[1], null, null, null, null, 0, 1);
    Node Node4 = new Node(ll4[0], ll4[1], null, null, null, null, 0, 1);
    Node Node5 = new Node(ll5[0], ll5[1], null, null, null, null, 0, 1);
    int d01 = 33;
    int d35 = 67;
    int d24 = 42;
    assertTrue(nearlyEqual(Node0.getDistance(Node1), d01)) ;
    assertTrue(nearlyEqual(Node3.getDistance(Node5), d35)) ;
    assertTrue(nearlyEqual(Node2.getDistance(Node4), d24)) ;
  }

  @Test
  public void getNameTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    assertTrue(p.getName("/w/0").equals("Chihiro Ave"));
    assertTrue(p.getName("/w/1").equals("Chihiro Ave"));
    assertTrue(p.getName("/w/3").equals("Sootball Ln"));
    assertTrue(p.getName("/w/6").equals("Yubaba St"));
  }

  @Test
  public void getIntersectionNode() {
    assertTrue(1 == 1);
  }

  @Test
  public void getNodesTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    double[] ll0 = {41.82, -71.4};
    double[] ll3 = {41.82, -71.4003};
    double[] ll5 = {41.8206, -71.4003};
    Node Node0 = new Node(ll0[0], ll0[1], "/n/0", null, null, null, 0, 1);
    Node Node3 = new Node(ll3[0], ll3[1], "/n/3", null, null, null, 0, 1);
    Node Node5 = new Node(ll5[0], ll5[1], "/n/5", null, null, null, 0, 1);
    Set<Node> nodes0 = p.findNodes(Node0, false, ll0);


    assertTrue(nodes0.size() == 2);
    for (Node n : nodes0) {
      assertTrue(n.getID().equals("/n/1") || n.getID().equals("/n/3"));
    }

    Set<Node> nodes3 = p.findNodes(Node3, false, ll0);
    assertTrue(nodes3.size() == 1);
    for (Node n : nodes3) {
      assertTrue(n.getID().equals("/n/4"));
    }

    Set<Node> nodes5 = p.findNodes(Node5, false, ll0);
    assertTrue(nodes5.size() == 0);
  }




  @Test
  public void getPathTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    List<Node> path04 = p.findPath("/n/0", "/n/4", false);
    assertTrue(path04.get(0).getID().equals("/n/0"));
    assertTrue(path04.get(1).getID().equals("/n/1"));
    assertTrue(path04.get(2).getID().equals("/n/4"));

    List<Node> path00 = p.findPath("/n/0", "/n/0", false);
    assertTrue(path00.get(0).getID().equals("/n/0"));

    List<Node> path35 = p.findPath("/n/3", "/n/5", false);
    assertTrue(path35.get(0).getID().equals("/n/3"));
    assertTrue(path35.get(1).getID().equals("/n/4"));
    assertTrue(path35.get(2).getID().equals("/n/5"));

    List<Node> path05 = p.findPath("/n/0", "/n/5", false);
    assertTrue(path05.get(0).getID().equals("/n/0"));
    assertTrue(path05.get(1).getID().equals("/n/1"));
    assertTrue(path05.get(2).getID().equals("/n/2"));
    assertTrue(path05.get(3).getID().equals("/n/5"));

  }

  @Test
  public void getIntersectionTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    //System.outprintln(p.getIntersection("Chihiro Ave", "Sootball Ln"));
    assertTrue(p.getIntersection("Chihiro Ave", "Sootball Ln").equals("/n/1"));
    assertTrue(p.getIntersection("Chihiro Ave", "Radish Spirit Blvd").equals("/n/0"));
    assertTrue(p.getIntersection("Kamaji Pl", "Chihiro Ave").equals("/n/2"));
    assertTrue(p.getIntersection("Chihiro Ave", "Yubaba St") == null);
    assertTrue(p.getIntersection("Yubaba St", "Sootball Ln").equals("/n/4"));
  }

  @Test
  public void getAllNodesTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    List<Node> nodes = p.getAllNodes();
    assertTrue(nodes.size() == 6);
    assertTrue(nodes.get(0).getID().equals("/n/0"));
    assertTrue(nodes.get(1).getID().equals("/n/1"));
    assertTrue(nodes.get(2).getID().equals("/n/2"));
    assertTrue(nodes.get(3).getID().equals("/n/3"));
    assertTrue(nodes.get(4).getID().equals("/n/4"));
    assertTrue(nodes.get(5).getID().equals("/n/5"));
  }

  @Test
  public void getWaysWithin() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    double lat1 = 41.82;
    double lat2 = 41.8205;
    double lon1 = -71.41;
    double lon2 = -71.4;

    List<Way> ways = p.getWaysWithin(lat1, lat2, lon1, lon2);

    assertTrue(ways.size() == 6);
    assertTrue(ways.get(0).getID().equals("/w/0"));
    assertTrue(ways.get(1).getID().equals("/w/1"));
    assertTrue(ways.get(2).getID().equals("/w/2"));
    assertTrue(ways.get(3).getID().equals("/w/3"));
    assertTrue(ways.get(4).getID().equals("/w/5"));
    assertTrue(ways.get(5).getID().equals("/w/6"));

    double lat10 = 41.82;
    double lat20 = 41.8205;
    double lon10 = -71.4001;
    double lon20 = -71.4;

    List<Way> ways2 = p.getWaysWithin(lat10, lat20, lon10, lon20);
    assertTrue(ways2.size() == 4);
    assertTrue(ways2.get(0).getID().equals("/w/0"));
    assertTrue(ways2.get(1).getID().equals("/w/1"));
    assertTrue(ways2.get(2).getID().equals("/w/2"));
    assertTrue(ways2.get(3).getID().equals("/w/3"));

    double lat100 = 41.828;
    double lat200 = 41.8205;
    double lon100 = -71.4001;
    double lon200 = -71.4;

    List<Way> ways3 = p.getWaysWithin(lat100, lat200, lon100, lon200);
  }

  @Test
  public void maxMinTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    //System.outprintln(p.getIntersection("Chihiro Ave", "Sootball Ln"));
    double[] maxMin = p.getMaxMin();
    assertTrue(maxMin[0] == 41.82);
    assertTrue(maxMin[1] == 41.8206);
    assertTrue(maxMin[2] == -71.4003);
    assertTrue(maxMin[3] == -71.4);
  }

  @Test
  public void nearestNeighborTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3",
        null);
    KDTree<Node> kd = new KDTree<Node>(2, new ArrayList<Node>(p.getAllNodes()));
    double[] one = {41.82, -71.3};
    double[] two = {41.8205, -71.3};
    double[] three = {41.8202, -71.40016};

    assertTrue(kd.neighbors(1, one).get(0).getID().equals("/n/0"));
    assertTrue(kd.neighbors(1, two).get(0).getID().equals("/n/2"));
    assertTrue(kd.neighbors(1, three).get(0).getID().equals("/n/4"));

  }

}
