package edu.brown.cs.dshieble.maps;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * The Main Class for the Bacon project.
 *  It handles the commandline input.
 * @author dshieble
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
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public static void main(final String[] args) {
    //gui inputs
    if (args.length == 2) {
      if (args[0].equals("--gui")) {
        String file = args[1];
//        try {
          //GuiManager.makeGUI(file);
//        } catch (SQLException|ClassNotFoundException e) {
//          System.out.println("ERROR: database error");
//        }
      } else {
        System.out.println("ERROR: Arguements are: "
            + "[--gui] <sql database>");
      }
    //bad input
    } else if (args.length != 1) {
      System.out.println("ERROR: Arguements are:"
          + "[--gui] <sql database>");
    //matching names
    } else {
      try {
       CommandReader.readCommands(args[0]);
      } catch (IOException|SQLException e) {
        System.exit(0); // error printed in CommandReader
      }
    }

  }
}
