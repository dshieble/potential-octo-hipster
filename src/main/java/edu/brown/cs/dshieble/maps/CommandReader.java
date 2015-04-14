package edu.brown.cs.dshieble.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.sjl2.kd.KDTree;


/**
 * This class interacts with the user's input, and prints the
 * output to the screen.
 * @author dshieble
 *
 */
public final class CommandReader {

  /**
   * Prevents this class from being instantiated.
   */
  private CommandReader() {

  }

  /**
   * Method called by main class that reads command line prompts.
   * @param tm - the triemanager that ranks input words
   * @param usePrefix - the options
   * @param levenshtein - the options
   * @param useWhitespace - the options
   * @param useSmart - the options
   */
  public static void readCommands(String file) {
    try (PathFinder p = new PathFinder(file, null)) {
      try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in))) {
        KDTree<Node> kd = new KDTree<Node>(2, new ArrayList<Node>(p.getAllNodes()));
        String command = null;
        while ((command = reader.readLine())  != null) {
          if (command.length() == 0) {
            break; //Break execution on empty input
          }
          String[] quoteArr = command.split("\"");
          //If lat long input type
          String fromId = null;
          String toId = null;
          if (quoteArr.length == 1) {
            String[] spaceArr = command.split(" ");
            if (spaceArr.length != 4) {
              throw new IOException("Incorrect number of Arguments.");
            }
            double[] from = new double[2];
            double[] to = new double[2];
            for (int i = 0; i < from.length + to.length; i ++) {
              try {
                double d = Double.parseDouble(spaceArr[i]);
                if (i < 2) {
                  from[i] = d;
                } else {
                  to[i-2] = d;
                }
              } catch (NumberFormatException e) {
                throw new IOException("An expected double was not found.");
              }
            }

            fromId = kd.neighbors(1, from).get(0).getID();
            toId = kd.neighbors(1, to).get(0).getID();

          //if quotation input type
          } else {
            if (quoteArr.length != 8) {
              throw new IOException("Incorrect Number of Inputs.");
            } else {
              String s1 = quoteArr[1];
              String c1 = quoteArr[3];
              String s2 = quoteArr[5];
              String c2 = quoteArr[7];
              fromId = p.getIntersection(s1, c1);
              toId = p.getIntersection(s2, c2);
            }
          }
          if (fromId == null || toId == null) {
            throw new IOException();
          }
          //Print the REPL stuff
          List<Node> list = p.findPath(fromId, toId, false);
          if (list.size() == 0 || list.size() == 1) {
            System.out.println(fromId + " -/- " + toId);
          } else {
            for (Node n : list) {
              if (n.getID() != fromId) {
                System.out.println(n.getParent().getID() + " -> " + n.getID()
                  + " : " + n.getEdgeId());
              }
            }
          }
        }
      } catch (IOException ioe) {
        System.out.println("ERROR: REPL Exception: " + ioe.getMessage());
      }
    } catch (RuntimeException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }
}
