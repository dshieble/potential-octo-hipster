package edu.brown.cs.dshieble.maps;

import java.sql.SQLException;

/**
 * The Main Class for the Bacon project.
 *  It handles the commandline input.
 * @author dshieble
 *
 * cs032_traffic_server 8080 /course/cs032/data/maps/smallMaps.sqlite3
 *
 *
 */
public final class Main {


  /**
   * Prevents this class from being instantiated.
   */
  private Main() {

  }

  /**
   * Mainline of code. Parses user input and finds path between actors.
   * @param args CL args
   * @throws Exception
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public static void main(final String[] args) {
    //gui inputs
//    TrafficManager t = new TrafficManager(8080);
//    while (true) {
//      t.updateTraffic();
//      //pause the program
//      try { Thread.sleep(1000);} catch (InterruptedException e) {break;}
//      Map<String, Integer> map = t.getMap();
//      for (String k : map.keySet()) {
//        System.out.println(k);
//        System.out.println(map.get(k));
//      }
//    }
    //System.exit(0);



    if (args.length == 2 ) {
      if (args[0].equals("--gui")) {
        String file = args[1];
        TrafficManager t = new TrafficManager(8080);
        new GUIManager(file, t);

//        try {
//          GUIManager g = new GUIManager(file, t);
//        } catch (SQLException|ClassNotFoundException e) {
//          System.out.println("ERROR: database error");
//          return;
//        }

//        while (true) {
//           t.updateTraffic();
//          //pause the program
//          try { Thread.sleep(250);} catch (InterruptedException e) {break;}
//          Map<String, Integer> map = t.getMap();
//          for (String k : map.keySet()) {
//            System.out.println(k);
//            System.out.println(map.get(k));
//          }
//        }
      } else {
        System.out.println("ERROR: Arguments are: "
            + "[--gui] <sql database>");
        return;
      }
    //bad input
    } else if (args.length != 1) {
      System.out.println("ERROR: Arguments are:"
          + "[--gui] <sql database>");
      return;
    //matching names
    } else {
      CommandReader.readCommands(args[0]);
    }

  }
}



//

//System.out.println("Printing: ");
//TrafficManager t = new TrafficManager();
//t.updateTraffic();
//System.exit(0);
//









//String ff = "/course/cs032/data/maps/smallMaps.sqlite3";
//double lat1 = 0;
//double lat2 = 410.8207;
//double lon1 = -71.4002;
//double lon2 = -0;
//
//try (PathFinder p = new PathFinder(ff)) {
//List<Way> l = p.getWaysWithin(lat1, lat2,
//    lon1, lon2);
//System.out.println(l.size());
//System.out.println(Arrays.toString(l.toArray()));
//} catch (Exception e) {
//throw(e);
//}
//System.exit(0);
