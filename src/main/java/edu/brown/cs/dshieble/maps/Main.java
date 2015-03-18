package edu.brown.cs.dshieble.maps;

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
//    if (args.length == 2) {
//      if (args[1].equals("--gui")) {
//        String file = args[0];
//        try {
//          GuiManager.makeGUI(file);
//        } catch (SQLException|ClassNotFoundException e) {
//          System.out.println("ERROR: database error");
//        }
//      } else {
//        System.out.println("ERROR: Arguements are: "
//            + "<name 1> <name 2> <sql database>");
//      }
//    //bad input
//    } else if (args.length != 3) {
//      System.out.println("ERROR: Arguements are:"
//          + " <name 1> <name 2> <sql database>");
//    //matching names
//    } else if (args[0].equals(args[1])) {
//      System.out.println("ERROR: Actor names cannot be the same");
//    //Acceptable input
//    } else {
//      try (PathFinder p = new PathFinder(args[2])) {
//        List<Actor> list = p.findActorPath(args[0], args[1]);
//        if (list == null) {
//          System.out.println("ERROR: Actor not found");
//        } else if (list.size() == 0) {
//          System.out.println(args[0] + " -/- " + args[1]);
//        } else {
//          Iterator<Actor> it = list.iterator();
//          it.next(); //First one we dont print
//          while (it.hasNext()) {
//            Actor a = it.next();
//            System.out.println(a.getParent().getName() + " -> " + a.getName()
//                          + " : " + a.getMovieName());
//          }
//        }
//      } catch (SQLException|ClassNotFoundException e) {
//        System.out.println("ERROR: database error");
//      }
//    }
  }
}
