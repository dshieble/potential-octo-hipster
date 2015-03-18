package edu.brown.cs.sjl2.autocorrect;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
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
  private List<File> dictionaryFiles;
  private Autocorrect autocorrect;

  // Constants
  private static final int PORT = 8585;
  private static final int STATUS = 500;
  private static final int N = 5;
  private static final Gson GSON = new Gson();

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    OptionParser parser = new OptionParser();

    OptionSpec<Integer> ledSpec =
      parser.accepts("led").withRequiredArg().ofType(Integer.class);
    parser.accepts("prefix");
    parser.accepts("whitespace");
    parser.accepts("smart");
    parser.accepts("gui");

    OptionSpec<File> fileSpec = parser.nonOptions().ofType(File.class);


    OptionSet options = parser.parse(args);

    dictionaryFiles = options.valuesOf(fileSpec);

    if (dictionaryFiles.isEmpty()) {
      System.out.println("ERROR: Please specify a text file");
      return;
    }

    try {
      Autocorrect.Builder builder = new Autocorrect.Builder(dictionaryFiles);


      boolean led = options.has("led");

      if (led) {
        Integer editDistance = options.valueOf(ledSpec);
        if (editDistance == null) {
          System.out.println("ERROR: Please include maximum edit distance "
              + "immediately following --led");
          return;
        }
        // Add LED Suggestion Generator
        builder.useLED(led, editDistance);
      }

      builder.usePrefix(options.has("prefix"));
      builder.useWhitespace(options.has("whitespace"));
      builder.useSmartRank(options.has("smart"));


      autocorrect = builder.build();

    } catch (Exception ex) {
      System.out.println("ERROR: " + ex.getMessage());
      return;
    }

    if (options.has("gui")) {
      // To the GUI!
      runSparkServer();
    } else {
      // Process commands
      try {
        controlLoop();
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

    System.out.println("Ready");
    String input = cmd.readLine();
    while (input != null && !input.isEmpty()) {
      List<String> suggestions = autocorrect.getNSuggestions(N, input);
      for (String s : suggestions) {
        System.out.println(s);
      }
      System.out.println("");
      input = cmd.readLine();
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
    Spark.get("/autocorrect", new FrontHandler(), freeMarker);
    Spark.post("/suggestions", new SuggestionsHandler());
  }

  /**
   * Default Handler for stars. Provides html for users looking for
   * localhost/autocorrect.
   *
   * @author sjl2
   *
   */
  private class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      List<String> suggestions = new ArrayList<String>();
      Map<String, Object> variables =
        ImmutableMap.of("title", "Auto-Correct",
          "dictionaries", dictionaryFiles,
          "suggestions", suggestions);
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * A handler for posts to Autocorrect. Shuttles input from the GUI into
   * Autocorrect. Then shuttles solutions back to front-end.
   *
   * @author sjl2
   *
   */
  private class SuggestionsHandler implements Route {
    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();

      String input = qm.value("rawText");

      List<String> suggestions = autocorrect.getNSuggestions(N, input);

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
