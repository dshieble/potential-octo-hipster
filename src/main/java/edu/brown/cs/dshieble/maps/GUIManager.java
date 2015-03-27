package edu.brown.cs.dshieble.maps;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;
import edu.brown.cs.sjl2.autocorrect.Autocorrect;
import edu.brown.cs.sjl2.kd.KDTree;
import freemarker.template.Configuration;

public class GUIManager {

  private Autocorrect autocorrect;
  private KDTree<Node> tree;
  private TrafficManager tm;
  private String db;

  private static final int SUGGESTIONS = 5;
  private static final int DEFAULT_PORT = 8585;
  private static final int TRAFFIC_PORT = 8080;
  private static final int STATUS = 500;
  private static final Gson GSON = new Gson();

  public GUIManager(String db, TrafficManager tm, int port) {
    this.tm = tm;
    this.db = db;
    try (PathFinder p = new PathFinder(db, tm)) {
      this.tree = new KDTree<Node>(2, new ArrayList<>(p.getAllNodes()));
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (port == TRAFFIC_PORT) {
      port = DEFAULT_PORT;
    }

    runSparkServer(port);
  }

  public GUIManager(String db, TrafficManager tm) {
    this.tm = tm;
    this.db = db;
    try (PathFinder p = new PathFinder(db, tm)) {
      this.tree = new KDTree<Node>(2, new ArrayList<>(p.getAllNodes()));
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    runSparkServer(DEFAULT_PORT);
  }

  public void update() {
    // TODO

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
    Spark.get("/anchor", new AnchorHandler());
    Spark.post("/ways", new WaysHandler());
    Spark.post("/closest", new ClosestHandler());
    Spark.post("/path", new PathHandler());
    Spark.post("/suggestions", new SuggestionsHandler());
  }

  /**
   * Default Handler for maps.
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
   * TODO
   *
   * @author sjl2
   *
   */
  private class AnchorHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      double[] extrema = new double[] { 26.0, 59.4, -4.8, 34.7 };
      // min lat
      // max lat
      // min long
      // max long

      // TODO get actual extrema from path finder

      return GSON.toJson(extrema);
    }
  }

  // TODO
//  private class Tile {
//    private List<Way> ways;
//
//    Tile(List<Way> ways) {
//      this.ways = ways;
//    }
//  }

  private class WaysHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      double maxLat = GSON.fromJson(qm.value("maxLat"), Double.class);
      double minLat = GSON.fromJson(qm.value("minLat"), Double.class);

      double maxLong = GSON.fromJson(qm.value("maxLong"), Double.class);
      double minLong = GSON.fromJson(qm.value("minLong"), Double.class);

      List<Way> ways;

      try (PathFinder p = new PathFinder(db, tm)) {
        ways = p.getWaysWithin(minLat, maxLat, minLong, maxLong);

      } catch (ClassNotFoundException | SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();

        ways = new ArrayList<>();
      }

      return GSON.toJson(ways);
    }
  }

  /**
   * TODO
   *
   * @author sjl2
   *
   */
  private class ClosestHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      // TODO
      QueryParamsMap qm = req.queryMap();
      Double latitude = GSON.fromJson(qm.value("lat"), Double.class);
      Double longitude = GSON.fromJson(qm.value("lng"), Double.class);

      ArrayList<Node> n = tree.neighbors(1, new double[] { latitude, longitude });
      if (!n.isEmpty()) {
        return GSON.toJson(n.get(0));
      } else {
        return GSON.toJson(null);
      }
    }
  }

  /**
   * TODO
   *
   * @author sjl2
   *
   */
  private class PathHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      String input = qm.value("rawText");

      List<String> suggestions =
          autocorrect.getNSuggestions(SUGGESTIONS, input);

      return GSON.toJson(suggestions);
    }
  }


  private class SuggestionsHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      String input = qm.value("rawText");

      List<String> suggestions =
          autocorrect.getNSuggestions(SUGGESTIONS, input);

      return GSON.toJson(suggestions);
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
