package edu.brown.cs.sjl2.kd;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * KDTree is the implementation of a KDimensional tree to store points that are
 * associated with KDimensions (that is, extend the interface KDimensional). The
 *  class is iterable when needed, but the order is lost.
 *
 * @author sjl2
 *
 * @param <E> A Class that implements or implements an extension of KDimensional
 */
public class KDTree<E extends KDimensional> implements Iterable<E> {

  private int k;
  private Node root;
  private int size;
  private static final int DIVISOR = 5;

/**
 * A getter for the private variable size.
 * @return Returns the size of the kd tree.
 */
  public int getSize() {
    return size;
  }

  /**
   * A getter for the private variable k or the number of dimensions in the
   * tree.
   * @return Returns the number of dimensions in the tree, k.
   */
  public int getK() {
    return k;
  }

  /**
   * Private Class to KDTree. A substructure of the tree, a node represents a
   * point in the tree. It holds the invariant that it's left children are all
   * less than parent along a specified dimension and the right children are
   * greater. This dimension is determined by the depth (alternating between all
   *  dimensions).
   *
   * @author sjl2
   *
   */
  class Node {
    private int dim;
    private int depth;
    private E value;
    private Node left;
    private Node right;

    /**
     * Constructor foE r a Node in a KD Tree.
     * @param depth The level in the tree the node is. Used in determining the
     * dimension of to split on.
     * @param value The KDimensional of the node.
     * @param left The left child of Node. It is either another Node or null
     * @param right The right child of Node. It is either another Node or null
     */
    Node(int depth, E value, Node left, Node right) {
      this.dim = depth % k;
      this.depth = depth;
      this.value = value;
      this.left = left;
      this.right = right;
    }

    /**
     * Constructor for a node in a KD Tree. This creates a leaf of sorts (Both
     * children are instantiated to null).
     * @param depth The level of the node in the tree. Determines the dimension
     * to split on.
     * @param value The KDimensional of the node.
     */
    Node(int depth, E value) {
      this.dim = depth % k;
      this.depth = depth;
      this.value = value;
      this.left = null;
      this.right = null;
    }

    /**
     * Constructor for a node in a KD Tree from a collection of points. Builds
     * balanced tree recursively from the points.
     * @param depth The depth of the node
     * @param points The points to create the node and its children.
     */
    Node(int depth, List<E> points) {
      this.depth = depth;
      this.dim = depth % k;

      E median = medianNode(points);
      this.value = median;

      List<E> leftPoints = new ArrayList<>();
      List<E> rightPoints = new ArrayList<>();

      for (E kd : points) {
        if (kd.getDimension(dim) <= median.getDimension(dim)
            && kd.getID() != median.getID()) {
          leftPoints.add(kd);
        } else if (kd.getDimension(dim) > median.getDimension(dim)) {
          rightPoints.add(kd);
        }
      }

      if (leftPoints.size() > 0) {
        this.left = new Node(depth + 1, leftPoints);
      } else {
        this.left = null;
      }

      if (rightPoints.size() > 0) {
        this.right = new Node(depth + 1, rightPoints);
      } else {
        this.right = null;
      }
    }

    private E medianHelper(List<E> points, int k, int left, int right) {
      if (points.isEmpty()) {
        throw new IllegalArgumentException("Shit's broke yo");
      }
      int index = left;
      E pivot = points.get(index);
      if (right - left <= 1) {
        return pivot;
      }

      swap(points, index, right);

      int frontier = left;

      for (int i = left; i <= right; i++) {
        if (points.get(i).getDimension(dim) < pivot.getDimension(dim)) {
          swap(points, i, frontier);
          frontier++;
        }
      }

      swap(points, frontier, right);

      if (k < frontier) {
        return medianHelper(points, k, left, frontier - 1);
      } else if (k > frontier) {
        return medianHelper(points, k, frontier + 1, right);
      } else {
        return pivot;
      }

    }

    private void swap(List<E> list, int a, int b) {
      E temp = list.get(a);
      list.set(a, list.get(b));
      list.set(b, temp);
    }

    private E medianNode(List<E> points) {
      if (points.isEmpty()) {
        return null;
      }
      int n = points.size();
      int k = (int) Math.ceil(n / 2.0) - 1;
      return medianHelper(points, k, 0, n - 1);
    }

    /**
     * Adds a KDimensional to a Node by recursively adding to children until it
     * reaches a leaf (a null child).
     * @param point A Point to add. Must not be null.
     * @return Returns the Node with the added KDimensional
     */
    Node add(E point) {
      this.dim = depth % k;

      if (value == null) {
        //Add Leaf
        return new Node(0, point);
      } else if (point.getDimension(dim) < value.getDimension(dim)) {
        // Go Left
        if (left != null) {
          return new Node(depth, value, left.add(point), right);
        } else {
          return new Node(depth, value, new Node(depth + 1, point), right);
        }
      } else {
        // Go Right
        if (right != null) {
          return new Node(depth, value, left, right.add(point));
        } else {
          return new Node(depth, value, left, new Node(depth + 1, point));
        }
      }
    }

    /**
     * Getter for Node's value.
     * @return Returns the KDimensional of the node.
     */
    E getVal() {
      return value;
    }

    /**
     * Getter for Left Child Node.
     * @return Returns the Left Child of the node. Null if there is none.
     */
    Node getLeft() {
      return left;
    }

    /**
     * Getter for Right Child Node.
     * @return Returns the Right Child of the node. Null if there is none.
     */
    Node getRight() {
      return right;
    }

    /**
     * Getter for the Depth of the Node.
     * @return Returns the depth of the node as an int
     */
    int getDepth() {
      return depth;
    }
  }

  /**
   * Constructor for a KD Tree. Only requires the number of dimensions each
   * point will have. Points must be added.
   * @param k The number of dimensions in the KDimensional points stored
   */
  public KDTree(int k) {
    this.k = k;
    this.root = new Node(0, new ArrayList<>());
    this.size = 0;
  }

  /**
   * The recommended constructor for a KD Tree. Will build the tree out of the
   * points in kds to be more efficiently arranged/balanced. This makes
   * following functions more efficient.
   *
   * @param k The number of dimensions for the KDimensional points
   * @param kds An ArrayList of KDimensionals to be added effectively to the
   * KD Tree. kds must be non-null and NOT empty.
   */
  public KDTree(int k, List<E> kds) {
    this.k = k;

    // Check bad arguments
    if (kds == null) {
      throw new IllegalArgumentException("kds must not be null.");
    } else if (kds.isEmpty()) {
      throw new IllegalArgumentException("kds must not be an empty.");
    }

    root = new Node(0, kds);

    this.size = kds.size();
  }

  /**
   * NearestKD is a comparator that allows two KDimensionals to be compared by
   * who is closer to a reference point determined on instantiation.
   *
   * @author sjl2
   *
   */
  public class NearestKD<T extends KDimensional> implements Comparator<T> {

    private double[] point;

    /**
     * Constructor for NearestKD.
     * @param point Reference point to compare two points.
     */
    public NearestKD(double[] point) {
      this.point = point.clone();
    }

    /**
     * The getter for the private variable point.
     * @return Returns the reference point for NearestKD
     */
    public double[] getPoint() {
      return point.clone();
    }
    /**
     * Compares two points to determine which is closer to the reference point.
     * @param pt1 A KDimensional point
     * @param pt2 A KDimensional point
     * @return Returns 1 if pt1 is closer to reference, -1 if pt2 is closer.
     * Returns 0 if the two points are equally close.
     */
    @Override
    public int compare(T pt1, T pt2) {
      double dist1 = pt1.getDistance(point);
      double dist2 = pt2.getDistance(point);
      double diff = dist1 - dist2;

      if (diff > 0) {
        return 1;
      } else if (diff < 0) {
        return -1;
      } else {
        // Order by ID (IDs all -1 for KDs w/o IDs.
        return pt1.getID().compareTo(pt2.getID());
      }
    }
  }

  /**
   * Adds a KDimensional to the KD Tree if it is not in the Database.
   * @param kd A KDimensional to add to the KDTree
   */
  public void add(E kd) {
    if (search(kd) == null) {
      // Not in database
      root = root.add(kd);
      size += 1;
    }
  }

  /**
   * Searches the KDTree for a point, returns the point if it exists, null
   * otherwise.
   * @param point The point to search the KDTree for.
   * @return Returns the point if it is in the kd tree. Null otherwise.
   */
  public E search(E point) {
    Node curr = root;
    int dim;

    while (curr != null) {
      dim = curr.depth % k;
      if (curr.getVal() == null) {
        return null; // Empty Tree
      } else if (curr.getVal().equals(point)) {
        return curr.getVal();
      } else if (point.getDimension(dim) < curr.getVal().getDimension(dim)) {
        curr = curr.left;
      } else {
        curr = curr.right;
      }
    }
    return null;
  }


  /**
   * Recursive helper for Neighbors to find the k nearest neighbors to a set of
   * test coordinates.
   * @param n The number of neighbors to be found.
   * @param coordinates The coordinates of the reference point
   * @param node The current node being searched.
   * @param pq The priority queue to hold and sort the neighbors
   * @param maxDist The maximum distance in the priority queue. Updated when any
   * point is added.
   * @return Returns a Priority Queue with the neighbors sorted within. The
   * queue may have more or less than n as the method was implemented without a
   * bounded priority queue.
   */
  private PriorityQueue<E> neighborsHelper(int n, double[] coordinates,
      Node node, PriorityQueue<E> pq, double maxDist) {
    Node explore;
    Node unexplored;

    if (node == null) {
      return pq;
    }

    E currPoint = node.getVal();
    int dim = node.getDepth() % k;

    // Add point
    pq.add(currPoint);
    maxDist = Math.max(maxDist, currPoint.getDistance(coordinates));

    // Pick a direction
    if (coordinates[dim] < currPoint.getDimension(dim)) {
      // Check Left Child
      explore = node.getLeft();
      unexplored = node.getRight();
    } else {
      // Check Right Child
      explore = node.getRight();
      unexplored = node.getLeft();
    }

    pq = neighborsHelper(n, coordinates, explore, pq, maxDist);

    // Other Branch within bounds?
    double radius = Math.abs(currPoint.getDimension(dim) - coordinates[dim]);
    if (pq.size() < n || radius <= maxDist && unexplored != null) {
      pq = neighborsHelper(n, coordinates, unexplored, pq, maxDist);
    }

    return pq;
  }

  /**
   * Finds the nearest neighbors to the coordinate at coordinates.
   * @param n The number of nearest neighbors
   * @param coordinates The center of the test area
   * @return Returns an ArrayList of KDimensionals that are nearest ot the point
   * coordinates.
   */
  public ArrayList<E> neighbors(int n, double[] coordinates) {
    ArrayList<E> toReturn = new ArrayList<E>();

    if (n == 0) {
      return toReturn;
    }

    PriorityQueue<E> pq = new PriorityQueue<E>(n,
        new NearestKD<E>(coordinates));

    pq = neighborsHelper(n, coordinates, root, pq, 0.0);

    int len = pq.size();
    for (int i = 0; i < Math.min(n, len); i++) {
      toReturn.add(pq.poll());
    }
    return toReturn;
  }

  /**
   * Recursive helper for radius.
   * @param r The radius of inclusion
   * @param coordinates Test Coordinates
   * @param node Current Node
   * @param pq PriorityQueue to hold the points within radius.
   * @return Returns a priority queue with the points within the radius sorted.
   */
  private PriorityQueue<E> radiusHelper(double r, double[] coordinates,
      Node node, PriorityQueue<E> pq) {

    Node explore;
    Node unexplored;

    if (node == null) {
      return pq;
    }

    E currPoint = node.getVal();
    int dim = node.getDepth() % k;

    // Add point
    if (currPoint.getDistance(coordinates) <= r) {
      pq.add(currPoint);
    }

    // Pick a direction
    if (coordinates[dim] < currPoint.getDimension(dim)) {
      // Check Left Child
      explore = node.getLeft();
      unexplored = node.getRight();
    } else {
      // Check Right Child
      explore = node.getRight();
      unexplored = node.getLeft();
    }

    pq = radiusHelper(r, coordinates, explore, pq);

    // Other Branch within bounds?
    double radius = Math.abs(currPoint.getDimension(dim) - coordinates[dim]);
    if (radius <= r && unexplored != null) {
      pq = radiusHelper(r, coordinates, unexplored, pq);
    }

    return pq;
  }

  /**
   * Returns an ArrayList of KDimensionals within a radius r inclusive.
   * @param r Radius of inclusion
   * @param coordinates of center
   * @return Returns an ArrayList of KDimensionals within radius r inclusive
   */
  public ArrayList<E> radius(double r, double[] coordinates) {
    if (r < 0) {
      throw new IllegalArgumentException("r must be non-negative");
    }
    ArrayList<E> toReturn = new ArrayList<E>();

    PriorityQueue<E> pq = new PriorityQueue<E>(Math.max(size / DIVISOR, 1),
        new NearestKD<E>(coordinates));

    pq = radiusHelper(r, coordinates, root, pq);

    int len = pq.size();
    // Pop Priority Queue
    for (int i = 0; i < len; i++) {
      toReturn.add(pq.poll());
    }
    return toReturn;
  }

  @Override
  public Iterator<E> iterator() {
    Iterator<E> iter = new Iterator<E>() {

      private List<Node> curr = new ArrayList<Node>();
      private int count = 0;

      @Override
      public boolean hasNext() {
        return count < size;
      }

      @Override
      public E next() {
        if (count == 0) {
          curr.add(root);
        }

        // Remove Last Element (Least damage to list)
        Node n = curr.remove(curr.size() - 1);
        E val = null;
        Node left;
        Node right;

        if (n != null) {
          val = n.getVal();
          left = n.getLeft();
          right = n.getRight();
          if (left != null) {
            curr.add(left);
          }

          if (right != null) {
            curr.add(right);
          }
        }

        count++;
        return val;
      }

    };
    return iter;
  }


}
