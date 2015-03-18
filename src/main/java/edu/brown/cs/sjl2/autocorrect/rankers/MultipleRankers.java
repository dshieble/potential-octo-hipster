package edu.brown.cs.sjl2.autocorrect.rankers;

import java.util.List;

/**
 * MultipleRankers allows for the combination of several rankers to come
 * together and rank as one.
 *
 * @author sjl2
 *
 */
public interface MultipleRankers extends WordRanker {

  /**
   * Add a Ranker to the rankers to be used for sorting. This ranker will be
   * applied after all of the current rankers.
   * @param ranker A Ranker to be used in sorting. It will be applied in the
   * order it was added.
   */
  void addRanker(WordRanker ranker);

  /**
   * Add Rankers to the rankers used for sorting. These rankers will be used
   * in their respective order AFTER the current rankers that have already been
   * added.
   * @param rankers Rankers to be used in sorting. They will be applied in the
   * order they were added.
   */
  void addRankers(List<WordRanker> rankers);
}
