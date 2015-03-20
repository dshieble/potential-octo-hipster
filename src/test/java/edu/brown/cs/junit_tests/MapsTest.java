package edu.brown.cs.junit_tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.brown.cs.dshieble.maps.*;
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
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3");
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
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3");
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
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3");
    double[] ll0 = {41.82, -71.4};
    double[] ll3 = {41.82, -71.4003};
    double[] ll5 = {41.8206, -71.4003};
    Node Node0 = new Node(ll0[0], ll0[1], "/n/0", null, null, null, 0, 1);
    Node Node3 = new Node(ll3[0], ll3[1], "/n/3", null, null, null, 0, 1);
    Node Node5 = new Node(ll5[0], ll5[1], "/n/5", null, null, null, 0, 1);
    Set<Node> nodes0 = p.findNodes(Node0);
    assertTrue(nodes0.size() == 2);
    for (Node n : nodes0) {
      assertTrue(n.getID().equals("/n/1") || n.getID().equals("/n/3"));
    }
    
    Set<Node> nodes3 = p.findNodes(Node3);
    assertTrue(nodes3.size() == 2);
    for (Node n : nodes3) {
      assertTrue(n.getID().equals("/n/0") || n.getID().equals("/n/4"));
    }
 
    Set<Node> nodes5 = p.findNodes(Node5);
    assertTrue(nodes5.size() == 2);
    for (Node n : nodes5) {
      assertTrue(n.getID().equals("/n/2") || n.getID().equals("/n/4"));
    }
  }

  //KDimensional interface
  
  
  
  @Test 
  public void getPathTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3");
    List<Node> path04 = p.findPath("/n/0", "/n/4");
    assertTrue(path04.get(0).getID().equals("/n/0"));
    assertTrue(path04.get(1).getID().equals("/n/1"));
    assertTrue(path04.get(2).getID().equals("/n/4"));

    List<Node> path00 = p.findPath("/n/0", "/n/0");
    assertTrue(path00.get(0).getID().equals("/n/0"));
    
    List<Node> path35 = p.findPath("/n/3", "/n/5");
    assertTrue(path35.get(0).getID().equals("/n/3"));
    assertTrue(path35.get(1).getID().equals("/n/4"));
    assertTrue(path35.get(2).getID().equals("/n/5"));
    
    List<Node> path05 = p.findPath("/n/0", "/n/5");
    assertTrue(path05.get(0).getID().equals("/n/0"));
    assertTrue(path05.get(1).getID().equals("/n/1"));
    assertTrue(path05.get(2).getID().equals("/n/2"));
    assertTrue(path05.get(3).getID().equals("/n/5"));

  }
  
  @Test 
  public void getIntersectionTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3");
    //System.outprintln(p.getIntersection("Chihiro Ave", "Sootball Ln"));
    assertTrue(p.getIntersection("Chihiro Ave", "Sootball Ln").equals("/n/1"));
    assertTrue(p.getIntersection("Chihiro Ave", "Radish Spirit Blvd").equals("/n/0"));
    assertTrue(p.getIntersection("Kamaji Pl", "Chihiro Ave").equals("/n/2"));
    assertTrue(p.getIntersection("Chihiro Ave", "Yubaba St") == null);
    assertTrue(p.getIntersection("Yubaba St", "Sootball Ln").equals("/n/4"));

  }
  
  @Test 
  public void getAllNodesTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3");
    List<Node> nodes = p.getAllNodes();
    assertTrue(nodes.size() == 6);
    assertTrue(nodes.get(0).getID().equals("/n/0"));
    assertTrue(nodes.get(1).getID().equals("/n/1"));
    assertTrue(nodes.get(2).getID().equals("/n/2"));
    assertTrue(nodes.get(3).getID().equals("/n/3"));
    assertTrue(nodes.get(4).getID().equals("/n/4"));
    assertTrue(nodes.get(5).getID().equals("/n/5"));

    //System.out.println(Arrays.toString(nodes.toArray()));
  }
  
  @Test 
  public void getWaysWithin() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3");
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
//    for (Way w: ways3) {
//      System.out.println(w.getID());
//    }
//    assertTrue(ways3.size() == 0);
    
    
    //System.out.println(Arrays.toString(ways.toArray()));

  }
  
  @Test 
  public void maxMinTest() throws ClassNotFoundException, SQLException {
    PathFinder p = new PathFinder("/course/cs032/data/maps/smallMaps.sqlite3");
    //System.outprintln(p.getIntersection("Chihiro Ave", "Sootball Ln"));
    double[] maxMin = p.getMaxMin();
    assertTrue(maxMin[0] == 41.82);
    assertTrue(maxMin[1] == 41.8206);
    assertTrue(maxMin[2] == -71.4003);
    assertTrue(maxMin[3] == -71.4);
  }
  
  @Test 
  public void runTest() throws ClassNotFoundException, SQLException, InterruptedException {
//    System.out.println("Printing: ");
//    TrafficManager t = new TrafficManager();
//    Map<String, Integer> wayToTraffic = t.getTraffic();
//    for (String s : wayToTraffic.keySet()) {
//      System.out.println(s);
//      System.out.println(wayToTraffic.get(s));
//    }
//    Thread.sleep(4);
//    Map<String, Integer> wayToTraffic2 = t.getTraffic();
//    for (String s : wayToTraffic2.keySet()) {
//      System.out.println(s);
//      System.out.println(wayToTraffic.get(s));
//    }
    
  }

  
//  
//  @Test
//  public void nameTest() throws ClassNotFoundException, SQLException {
//    String name1 = "Will Smith";
//    String name2 = "Arnold Schwarzenegger";
//    String name3 = "Samuel L. Jackson";
//    String name7 = "Dave ad";
//
//    PathFinder p = new PathFinder("/course/cs032/data/bacon/smallBacon.sqlite3");
//    assertTrue(!p.nameMatch(name1, name2));
//    assertTrue(p.nameMatch(name2, name3));
//    assertTrue(p.nameMatch(name7, name2));
//    assertTrue(!p.nameMatch(name7, name3));
//    assertTrue(!p.nameMatch(name1, name7));
//    p.close();
//  }
//  
//  @Test
//  public void findActorsTest1() throws ClassNotFoundException, SQLException {
//    PathFinder p = new PathFinder("/course/cs032/data/bacon/smallBacon.sqlite3");
//    Actor a1 = new Actor("Will Smith", "/m/0147dk", null, 0, null, null);
//    Actor a2 = new Actor("Stephen Root", "/m/0309lm", a1, 0.2, "Jersey Girl", null);
//    Actor a3 = new Actor("Robert Downey Jr.", "/m/0k6nymn", a2, 0.9, "The Soloist", null);
//    //1
//    Actor[] l1 = p.findActors(a1).toArray(new Actor[1]);
//    assertTrue(l1.length == 1);
//    assertTrue(l1[0].equals(a2));
//    //2
//    Actor[] l2 = p.findActors(a2).toArray(new Actor[1]);
//    assertTrue(l2.length == 1);
//    assertTrue(l2[0].equals(a3));
//    //3
//    Actor[] l3 = p.findActors(a3).toArray(new Actor[1]);
//    assertTrue(l3[0] == null);
//    p.close();
//  }
//  
//  @Test
//  public void findActorsTest2() throws ClassNotFoundException, SQLException {
//    PathFinder p = new PathFinder("/course/cs032/data/bacon/smallBacon.sqlite3");
//    Actor a1 = new Actor("Arnold Schwarzenegger", "/m/0tc7",
//        null, 0, null, null);
//    Actor a2 = new Actor("Steve Coogan", "/m/01nfys", a1, 0,
//        "Around the the World in 80 Days", null);
//    Actor a3 = new Actor("Cate Blanchett", "/m/0154qm", a2, 0,
//        "Hot Fuzz", null);
//    Actor a4 = new Actor("Betty White", "/m/025mb_", a3, 0, "Ponyo", null);
//    Actor a5 = new Actor("Willow Smith", "/m/03gq433", a4, 0,
//        "The Lorax", null);
//    //1
//    Actor[] l1 = p.findActors(a1).toArray(new Actor[1]);
//    assertTrue(l1.length == 1);
//    assertTrue(l1[0].equals(a2));
//    //2
//    Actor[] l2 = p.findActors(a2).toArray(new Actor[1]);
//    assertTrue(l2.length == 1);
//    assertTrue(l2[0].equals(a3));
//    //3
//    Actor[] l3 = p.findActors(a3).toArray(new Actor[1]);
//    assertTrue(l3.length == 1);
//    assertTrue(l3[0].equals(a4));
//    //4
//    Actor[] l4 = p.findActors(a4).toArray(new Actor[1]);
//    assertTrue(l4.length == 1);
//    assertTrue(l4[0].equals(a5));
//    //5
//    Actor[] l5 = p.findActors(a5).toArray(new Actor[1]);
//    assertTrue(l5[0] == null);
//    p.close();  
//  }
//
//  @Test
//  public void findPathTest1() throws ClassNotFoundException, SQLException { 
//    PathFinder p = new PathFinder("/course/cs032/data/bacon/smallBacon.sqlite3");
//    Actor a1 = new Actor("Arnold Schwarzenegger", "/m/0tc7", null, 0, null, null);
//    Actor a2 = new Actor("Steve Coogan", "/m/01nfys", a1, 0.5,
//        "Around the World in 80 Days", null);
//    Actor a3 = new Actor("Cate Blanchett", "/m/0154qm", a2, 0.5,
//        "Hot Fuzz", null);
//    Actor a4 = new Actor("Betty White", "/m/025mb_", a3, 0.5,
//        "Ponyo", null);
//    Actor a5 = new Actor("Willow Smith", "/m/03gq433", a4, 0.5,
//        "The Lorax", null);
//    //1
//    Actor[] l = p.findActorPath(a1.getName(), a5.getName()).toArray(new Actor[5]);
//    Actor[] expected = {a1, a2, a3, a4, a5};
//    for (int i = 1; i < expected.length; i++) {
//      assertTrue(l[i].equals(expected[i]));
//      assertTrue(l[i].getWeight() == expected[i].getWeight());
//      assertTrue(l[i].getParent().equals(expected[i].getParent()));
//      assertTrue(l[i].getMovieName().equals(expected[i].getMovieName()));
//    }
//
//    p.close();  
//  }
//
//  @Test
//  public void findPathTest2() throws ClassNotFoundException, SQLException { 
//    PathFinder p = new PathFinder(
//        "/course/cs032/data/bacon/smallBacon.sqlite3");
//    Actor a6 = new Actor("Will Smith", "/m/0147dk", null, 0, null, null);
//    Actor a7 = new Actor("Stephen Root", "/m/0309lm", a6, 0.5,
//        "Jersey Girl", null);
//    Actor a8 = new Actor("Robert Downey Jr.", "/m/0k6nymn", a7, 0.5,
//        "The Soloist", null);
//    
//    Actor[] l = p.findActorPath(a6.getName(), a8.getName()).toArray(new Actor[3]);
//    Actor[] expected = {a6, a7, a8};
//    for (int i = 1; i < expected.length; i++) {
//      assertTrue(l[i].equals(expected[i]));
//      assertTrue(l[i].getWeight() == expected[i].getWeight());
//      assertTrue(l[i].getParent().equals(expected[i].getParent()));
//      assertTrue(l[i].getMovieName().equals(expected[i].getMovieName()));
//    }
//    p.close();  
//  }
//
//  @Test
//  public void findPathTest3() throws ClassNotFoundException, SQLException { 
//    PathFinder p = new PathFinder("/course/cs032/data/bacon/smallBacon.sqlite3");
//    Actor a1 = new Actor("Arnold Schwarzenegger", "/m/0tc7", null, 0,
//        null, null);
//    Actor a2 = new Actor("Robert Downey Jr.", "/m/0k6nymn", null, 0.5,
//        "The Soloist", null);
//    
//    assertTrue(p.findActorPath(a1.getName(), a2.getName()).size() == 0);
//    p.close();  
//  }
//
//  @Test
//  public void getIDNameTest() throws ClassNotFoundException, SQLException {
//    PathFinder p = new PathFinder("/course/cs032/data/bacon/smallBacon.sqlite3");
//    String name1 = "Arnold Schwarzenegger";
//    String name2 = "Pulp Fiction";
//    String id1 = "/m/0tc7";
//    String id2 = "/m/0f4_l";
//    assertTrue(p.getID(name1).equals(id1));
//    assertTrue(p.getID(name2).equals(id2));
//    assertTrue(p.getName(id1).equals(name1));
//    assertTrue(p.getName(id2).equals(name2));
//    p.close();  
//  }
//  
//  
//  @Test
//  public void getActorMovieNamesTest() throws ClassNotFoundException, SQLException {
//    PathFinder p = new PathFinder("/course/cs032/data/bacon/smallBacon.sqlite3");
//    String name1 = "Arnold Schwarzenegger";
//    String name2 = "Pulp Fiction";
//    String[][] movies = p.getActorMovieNames(p.getID(name1));
//    String[][] actors = p.getActorMovieNames(p.getID(name2));
//    assertTrue(movies.length == 1);
//    assertTrue(actors.length == 2);
//    assertTrue(actors[0][0].equals("John Travolta") || actors[1][0].equals("John Travolta"));
//    assertTrue(actors[0][0].equals("Samuel L. Jackson") || actors[1][0].equals("Samuel L. Jackson"));
//    p.close();  
//  }
//
//  @Test
//  public void getAllNamesTest() throws ClassNotFoundException, SQLException {
//    PathFinder p = new PathFinder("/course/cs032/data/bacon/smallBacon.sqlite3");
//    assertTrue(p.getActorNames().length == 33);
//    p.close();  
//  }

}
