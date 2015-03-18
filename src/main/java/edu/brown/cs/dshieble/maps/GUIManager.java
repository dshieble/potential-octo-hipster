package edu.brown.cs.dshieble.maps;

import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
import edu.brown.cs.sjl2.bacon.Main.ExceptionPrinter;
import edu.brown.cs.sjl2.kd.KDTree;

public class GUIManager {

  private KDTree<Node> tree;
  private PathFinder pathfinder;

  public GUIManager(int port, KDTree<Node> tree, PathFinder pathfinder) {
    this.tree = tree;
    this.pathfinder = pathfinder;

    runSparkServer(port);
  }


  private void runSparkServer(int port) {
    Spark.setPort(port);
    Spark.setPort(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
  }
}
