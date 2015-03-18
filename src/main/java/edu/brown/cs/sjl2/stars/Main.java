package edu.brown.cs.sjl2.stars;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;


/**
 * Main serves as the entry point for using the Stars Database via command line
 * and the GUI.
 *
 * @author sjl2
 *
 */
public final class Main {
  /**
   * Begins execution of the stars project.
   *
   * @param args Command Line arguments for Stars. One should be the name of
   * the file containing csv of stars. Another will be the optional flag --gui
   * that will activate the Spark server
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private File db;
  private StarDatabase starDB;

  // Constants
  private static final int PORT = 8585;
  private static final int STATUS = 500;

  /**
   * ArgLength represents the length of args after splitting.
   *
   * @author sjl2
   *
   */
  public enum ArgLength {
    NAME(4), POINT(5);

    private int value;

    ArgLength(int value) {
      this.value = value;
    }

    /**
     * Getter for the value of the enum ArgLength.
     * @return Returns the length of the command
     */
    public int getValue() {
      return this.value;
    }
  }

  /**
   * Indices represents the different constants needed for indices from commands
   *  that will be needed.
   *
   * @author sjl2
   *
   */
  public enum Indices {
    FUNCTION(0), RK(1), NAME(3), X(2), Y(3), Z(4);

    private int value;

    Indices(int value) {
      this.value = value;
    }

    /**
     * Obtain the value of the Index.
     * @return Returns the value of the Index
     */
    public int getValue() {
      return this.value;
    }
  }

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    OptionParser parser = new OptionParser();

    parser.accepts("gui");
    OptionSpec<File> fileSpec = parser.nonOptions().ofType(File.class);
    OptionSet options = parser.parse(args);

    db = options.valueOf(fileSpec);
    if (db == null) {
      System.out.println("ERROR: Please specify a star file");
      return;
    }

    // Initialize Database
    try {
      starDB = new StarDatabase(db);
    } catch (IOException e) {
      System.out.println("ERROR: Fatal: DB could not be initialized.");
      return;
    }

    if (options.has("gui")) {
      // To the GUI!
      runSparkServer();
    } else {
      // Process commands
      try {
        controlLoop();
        return;
      } catch (UnsupportedEncodingException e) {
        System.out.println("ERROR: Fatal: " + e.getMessage());
      } catch (IOException e) {
        System.out.println("ERROR: Fatal: " + e.getMessage());
      }

    }
  }

  /**
   * Executes the REPL for command line use of the stars database. Commands are
   * entered by typing the function and argument wanted and hitting entered.
   * The loop ends on a ctrl+D.
   *
   * @throws IOException Throws IOException fatal IO issues.
   */
  private void controlLoop() throws IOException {
    BufferedReader cmd = new BufferedReader(
        new InputStreamReader(System.in, "UTF8"));

    // Neighbors/Radius Arguments
    int k;
    double r;
    String name;
    double x;
    double y;
    double z;

    boolean useName; // Boolean for whether to useName or Coordinates
    ArrayList<String> neighbors; // Holds the resulting stars for display

    String command = cmd.readLine(); // Copy of users command
    String[] inputs;

    while (command != null && !command.isEmpty()) {
      System.out.println(command); // Print Command
      command = command.trim(); // Trim Outside Whitespace
      inputs = command.split("\\s+|\""); // Split on whitespace or quotes

      try {
        // Initialize
        useName = false;
        name = "";
        x = 0;
        y = 0;
        z = 0;
        if (inputs.length >= ArgLength.NAME.getValue() && inputs[2].isEmpty()) {
          // Name since we split on ""'s.
          StringBuilder nameBuilder = new StringBuilder();
          for (int i = Indices.NAME.getValue(); i < inputs.length; i++) {
            nameBuilder.append(inputs[i] + " ");
          }
          name = nameBuilder.toString().trim();
          useName = true;
        } else if (inputs.length == ArgLength.POINT.getValue()) {
          // Use Coordinates
          x = Double.parseDouble(inputs[Indices.X.getValue()]);
          y = Double.parseDouble(inputs[Indices.Y.getValue()]);
          z = Double.parseDouble(inputs[Indices.Z.getValue()]);
        } else {
          throw new IllegalArgumentException("Incorrect number of arguments.");
        }

        if (inputs[Indices.FUNCTION.getValue()].equals("neighbors")) {
          // NEIGHBORS
          k = Integer.parseInt(inputs[Indices.RK.getValue()]);

          if (useName) {
            neighbors = starDB.neighbors(k, name);
          } else {
            neighbors = starDB.neighbors(k, x, y, z);
          }

        } else if (inputs[0].equals("radius")) {
          // RADIUS
          r = Double.parseDouble(inputs[Indices.RK.getValue()]);

          if (useName) {
            neighbors = starDB.radius(r, name);
          } else {
            neighbors = starDB.radius(r, x, y, z);
          }

        } else {
          throw new IllegalArgumentException(
              String.format("Command %s does not exist.", inputs[0]));
        }

        // Print Results
        for (String i : neighbors) {
          System.out.println(i);
        }
      } catch (NumberFormatException e) {
        System.out.println(
            String.format("ERROR: Number Format: %s", e.getMessage()));
      } catch (IllegalArgumentException e) {
        System.out.println(
            String.format("ERROR: Illegal Argument: %s", e.getMessage()));
      }

      command = cmd.readLine();
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer() {
    Spark.setPort(PORT);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/stars", new FrontHandler(), freeMarker);
    Spark.post("/neighbors", new NeighborsHandler(), freeMarker);
    Spark.post("/radius", new RadiusHandler(), freeMarker);
  }

  /**
   * Default Handler for stars. Provides html for users looking for
   * localhost/stars.
   *
   * @author sjl2
   *
   */
  private class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String neighbors = "";
      Map<String, Object> variables =
          ImmutableMap.of("title", "Stars: Query the database",
              "db", db,
              "neighbors", neighbors);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handler for neighbor posts. Any submissions through the form for nearest
   * neighbor will be handled here.
   *
   * @author sjl2
   *
   */
  private class NeighborsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      ArrayList<String> ids;
      StringBuilder neighborsBuilder = new StringBuilder();
      String neighbors;

      try {
        int n = Integer.parseInt(qm.value("number"));

        boolean isName = Boolean.parseBoolean(qm.value("isName"));
        if (isName) {
          // Name
          String name = qm.value("name");
          name = name.trim();
          ids = starDB.neighbors(n, name);
        } else {
          // Coordinates
          double x = Double.parseDouble(qm.value("x"));
          double y = Double.parseDouble(qm.value("y"));
          double z = Double.parseDouble(qm.value("z"));
          ids = starDB.neighbors(n, x, y, z);
        }

        for (String id : ids) {
          neighborsBuilder.append(id);
          neighborsBuilder.append('\n');
        }
        neighbors = neighborsBuilder.toString();
      } catch (NumberFormatException e) {
        neighbors = "ERROR: Could not parse coordinates. " + e.getMessage();
      } catch (IllegalArgumentException e) {
        neighbors = "ERROR: " + e.getMessage();
      }
      Map<String, Object> variables =
          ImmutableMap.of("title", "Stars: Query the database",
              "db", db,
              "neighbors", neighbors);

      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handler for radius posts. Any submissions through the form for radius will
   * be handled here.
   *
   * @author sjl2
   *
   */
  private class RadiusHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      ArrayList<String> ids;
      StringBuilder neighborsBuilder = new StringBuilder();
      String neighbors;
      try {
        double r = Double.parseDouble(qm.value("radius"));

        boolean isName = Boolean.parseBoolean(qm.value("isName"));
        if (isName) {
          // Name
          String name = qm.value("name");
          name = name.trim();
          ids = starDB.radius(r, name);
        } else {
          // Coordinates
          double x = Double.parseDouble(qm.value("x"));
          double y = Double.parseDouble(qm.value("y"));
          double z = Double.parseDouble(qm.value("z"));
          ids = starDB.radius(r, x, y, z);
        }

        for (String id : ids) {
          neighborsBuilder.append(id);
          neighborsBuilder.append('\n');
        }
        neighbors = neighborsBuilder.toString();
      } catch (NumberFormatException ex) {
        neighbors = "ERROR: Could not parse double. " + ex.getMessage();
      } catch (IllegalArgumentException ex) {
        neighbors = "ERROR: " + ex.getMessage();
      }


      Map<String, Object> variables =
          ImmutableMap.of("title", "Stars: Query the database",
              "db", db,
              "neighbors", neighbors);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handler for printing exceptions. Allows for easier debugging by having any
   * exceptions thrown while using the GUI to go right to browser and not just
   * be displayed on the command line.
   *
   * @author sjl2
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(STATUS);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }


}
