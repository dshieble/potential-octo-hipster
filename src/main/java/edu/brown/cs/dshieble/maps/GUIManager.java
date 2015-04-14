package edu.brown.cs.dshieble.maps;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.brown.cs.dshieble.autocorrect.TrieManager;
import edu.brown.cs.sjl2.kd.KDTree;

import freemarker.template.Configuration;

import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Class for Running the GUI Server. Initializing the class will automatically
 * run the server.
 *
 * @author sjl2
 *
 */
public class GUIManager {

  private TrieManager autocorrect;
  private KDTree<Node> tree;
  private TrafficManager tm;
  private String db;

  private static final int DEFAULT_PORT = 8686;
  private static final int TRAFFIC_PORT = 8080;
  private static final int STATUS = 500;
  private static final Gson GSON = new Gson();

  /**
   * Constructor for a GUIManager that allows for the GUI port to be changed.
   * @param db String of the db file name
   * @param tm The Traffic Manager to update traffic
   * @param port The port to run the GUI server off of.
   */
  public GUIManager(String db, TrafficManager tm, int port) {
    this.tm = tm;
    this.db = db;
    try (PathFinder p = new PathFinder(db, tm)) {
      this.tree = new KDTree<Node>(2, new ArrayList<>(p.getAllNodes()));

      //initializeAutocorrect(p.getStreetNames());
      Collection<String> names = p.getStreetNames();
      autocorrect = new TrieManager(
          names.toArray(new String[names.size()]));
    } catch (RuntimeException e) {
      System.out.println("ERROR: " + e.getMessage());
    }

    if (port == TRAFFIC_PORT) {
      port = DEFAULT_PORT;
    }

    runSparkServer(port);
  }

  /**
   * Constructor for the GUIManager, runs the server automatically on the
   * default port.
   * @param db The string name of the database file
   * @param tm The traffic manager to update the file.
   */
  public GUIManager(String db, TrafficManager tm) {
    this.tm = tm;
    this.db = db;
    try (PathFinder p = new PathFinder(db, tm)) {
      this.tree = new KDTree<Node>(2, new ArrayList<>(p.getAllNodes()));

      //initializeAutocorrect(p.getStreetNames());
      Collection<String> names = p.getStreetNames();
      autocorrect = new TrieManager(
          names.toArray(new String[names.size()]));
    } catch (RuntimeException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
    runSparkServer(DEFAULT_PORT);
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

  private void runSparkServer(int port) {
    Spark.setPort(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    Spark.get("/maps", new FrontHandler(), freeMarker);
    Spark.post("/ways", new WaysHandler());
    Spark.post("/closest", new ClosestHandler());
    Spark.post("/path", new PathHandler());
    Spark.post("/intersections", new IntersectionHandler());
    Spark.post("/suggestions", new SuggestionsHandler());
    Spark.post("/traffic", new TrafficHandler());

  }

  /**
   * Default Handler for maps homepage.
   *
   * @author sjl2
   *
   */
  private class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables =
        ImmutableMap.of("title", "Maps");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handler for determining the ways within a tile.
   *
   * @author sjl2
   *
   */
  private class WaysHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      double maxLat = GSON.fromJson(qm.value("maxLat"), Double.class);
      double minLat = GSON.fromJson(qm.value("minLat"), Double.class);

      double maxLong = GSON.fromJson(qm.value("maxLong"), Double.class);
      double minLong = GSON.fromJson(qm.value("minLong"), Double.class);

      List<Way> ways = new ArrayList<>();

      try (PathFinder p = new PathFinder(db, tm)) {
        ways = p.getWaysWithin(minLat, maxLat, minLong, maxLong);
      } catch (RuntimeException e) {
        System.out.println("ERROR: " + e.getMessage());
      }

      return GSON.toJson(ways);
    }
  }

  /**
   * Handler for finding the closest node to a lat, long sent back from the
   * front end.
   *
   * @author sjl2
   *
   */
  private class ClosestHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      Double latitude = GSON.fromJson(qm.value("lat"), Double.class);
      Double longitude = GSON.fromJson(qm.value("lng"), Double.class);

      double[] latlon = new double[] {latitude, longitude};
      ArrayList<Node> n =
          tree.neighbors(1, latlon);

      if (!n.isEmpty()) {
        return GSON.toJson(n.get(0));
      } else {
        return GSON.toJson(null);
      }
    }
  }

  /**
   * Handler for determining the path between two nodes.
   *
   * @author sjl2
   *
   */
  private class PathHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      String startID = qm.value("start");
      String endID = qm.value("end");

      List<Node> path = new ArrayList<>();
      try (PathFinder p = new PathFinder(db, tm)) {
        path = p.findPath(startID, endID, true);
      } catch (RuntimeException e) {
        System.out.println("ERROR: " + e.getMessage());
      }

      return GSON.toJson(path);
    }
  }

  /**
   * Handler for finding the intersections between the source and the target.
   *
   * @author sjl2
   *
   */
  private class IntersectionHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      System.out.println("Finding Intersection.");
      QueryParamsMap qm = req.queryMap();

      String source1 = qm.value("source1");
      String source2 = qm.value("source2");

      String target1 = qm.value("target1");
      String target2 = qm.value("target2");




      List<Node> inter = new ArrayList<Node>();
      try (PathFinder p = new PathFinder(db, tm)) {
        String i1 = p.getIntersection(source1, source2);
        String i2 = p.getIntersection(target1, target2);
        if (i1 != null) {
          inter.add(p.idToNode(i1));
        } else {
          inter.add(null);
        }
        if (i1 != null) {
          inter.add(p.idToNode(i2));
        } else {
          inter.add(null);
        }

      } catch (RuntimeException e) {
        System.out.println("ERROR: " + e.getMessage());
      }

      return GSON.toJson(inter);
    }
  }

  /**
   * Handler for returning autocorrect suggestions for an input.
   *
   * @author sjl2
   *
   */
  private class SuggestionsHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      String[] input = new String[] {qm.value("rawText")};
      String[] suggestions = autocorrect.getSuggestions(input,
              true,
              0,
              true,
              false);

      return GSON.toJson(suggestions);
    }
  }

  /**
   * Receives a list of way ids, returns a map from way id to traffic level.
   *
   * @author dshieble
   */
  private class TrafficHandler implements Route {

    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      Type stringListType = new TypeToken<List<String>>() { } .getType();

      List<String> ways = GSON.fromJson(qm.value("ids"), stringListType);

      List<Double> out = new ArrayList<>();

      for (String w : ways) {
        out.add(tm.getTrafficLevel(w));
      }

      Map<String, Object> variables =
          ImmutableMap.of("index", qm.value("index"), "traffic", out);

      return GSON.toJson(variables);
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
