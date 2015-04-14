package edu.brown.cs.dshieble.maps;

/**
 * The Main Class for the Bacon project.
 *  It handles the commandline input.
 * @author dshieble
 *
 * cs032_traffic_server 8080 /course/cs032/data/maps/smallMaps.sqlite3
 *
 */
public final class Main {

  private static final int TRAFFIC_PORT = 8080;

  /**
   * Prevents this class from being instantiated.
   */
  private Main() {

  }

  /**
   * Mainline of code. Parses user input and finds path between actors.
   * @param args CL args
   */
  public static void main(final String[] args) {

    if (args.length == 2) {
      if (args[0].equals("--gui")) {
        String file = args[1];
        TrafficManager t = new TrafficManager(TRAFFIC_PORT);
        new GUIManager(file, t);

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
