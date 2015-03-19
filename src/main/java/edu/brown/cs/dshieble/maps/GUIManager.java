package edu.brown.cs.dshieble.maps;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
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
  private PathFinder pathfinder;

  private static final int SUGGESTIONS = 5;
  private static final int DEFAULT_PORT = 8585;
  private static final int STATUS = 500;
  private static final Gson GSON = new Gson();

  public GUIManager(
      KDTree<Node> tree,
      PathFinder pathfinder,
      Autocorrect autocorrect,
      int port) {
    this.tree = tree;
    this.pathfinder = pathfinder;

    runSparkServer(port);
  }

  public GUIManager(
      KDTree<Node> tree,
      PathFinder pathfinder,
      Autocorrect autocorrect) {
    this.tree = tree;
    this.pathfinder = pathfinder;

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
    Spark.get("/anchor", new AnchorHandler());
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
      int width = Math.ceil(extrema[1] - extrema[0]);
      int height = Math.ceil(extrema[3] - extrema[2]);
      Tile[][] grid = new Tile[height][width];

      // TODO get actual extrema from path finder

      return GSON.toJson(extrema);
    }
  }

  private class Tile {
    private List<Way> ways;

    Tile(List<Way> ways) {
      this.ways = ways;
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
      Node coordinate = GSON.fromJson(qm.value("coordinate"), Node.class);

      // TODO

      return GSON.toJson(coordinate);
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
