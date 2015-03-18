package edu.brown.cs.sjl2.autocorrect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Dictionary Trie is a Trie that can store words efficiently. It is implemented
 * with a Sentinel Trie which does not house a letter. The children hold all
 * the letters that begin added words. This path continues down. The Dictionary
 * Trie is built with an inner class Trie. This is so that the Main Trie can
 * have a size.
 *
 * @author sjl2
 *
 */
public class DictionaryTrie {

  private int size = 0;
  private Trie root = new Trie("", ""); // Sentinel Trie for Root

  /**
   * Constructor of the Dictionary Trie where it is built from an array of
   * words.
   * @param words The words to build the trie from.
   */
  public DictionaryTrie(Collection<String> words) {
    addWords(words);
  }

  /**
   * Constructor of the Dictionary Trie where it is built from a File of words.
   * @param dictionaryFile The File that hosts a corpus to build the dictionary
   * from.
   * @throws IOException On bad input file or flawed buffering.
   */
  public DictionaryTrie(File dictionaryFile) throws IOException {
    addFile(dictionaryFile);
  }

  /**
   * Constructor of the Dictionary Trie where it is built from a file name. The
   * file must lead to an existing file that contains a corpus of text to use
   * generate the dictionary from.
   * @param fileName The name of the file to build the trie from.
   * @throws IOException Throws an IOException on bad input file.
   */
  public DictionaryTrie(String fileName) throws IOException {
    addFile(new File(fileName));
  }

  /**
   * Constructor of the DictionaryTrie where it is built from multiple files.
   * @param files The list of files to add to the DictionaryTrie
   * @throws IOException Throws IOException on bad file inputs.
   */
  public DictionaryTrie(List<File> files) throws IOException {
    for (File f : files) {
      addFile(f);
    }
  }


  /**
   * Represents a Trie. A Trie contains a letter that represents the node of the
   * trie. The prefix represents the path to that node from the root. Frequency
   * is the number of times that "word" is stored in the trie. Positive number
   * frequencies represent words that are stored. 0 represents that it is simply
   * a prefix and not a word. Words can be prefixes. Children are all of the
   * Tries that follow the letter. Previous words is used to determine the
   * frequencies of the words that precede this word in the corpus that built
   * the trie.
   *
   * @author sjl2
   *
   */
  public final class Trie {
    private String letter;
    private String prefix;
    private int freq;
    private HashMap<String, Trie> children;
    private HashMap<String, Integer> previousWords;

    /**
     * Child node Constructor. The size is unused here.
     * @param letter
     */
    private Trie(String letter, String prefix) {
      this.letter = letter;
      this.prefix = prefix;
      this.freq = 0;
      this.children = new HashMap<String, Trie>();
      this.previousWords = new HashMap<String, Integer>();
    }

    /**
     * Getter for root letter of the Trie.
     * @return String The letter represented in the node.
     */
    public String getLetter() {
      return letter;
    }

    /**
     * Getter for the prefix of the node. This is equivalent to the letter path
     * to the node.
     * @return Returns the prefix to this node's letter as a String.
     */
    public String getPrefix() {
      return prefix;
    }

    /**
     * Gets the word associated with the node. Returns null if the node is not
     * associated with a word. Be sure to check before using it.
     * @return Returns the word associated with this node or null if there is no
     * word associated with this node.
     */
    public String getWord() {
      if (!isWord()) {
        return null;
      }
      return prefix + letter;
    }

    /**
     * Used for determining if the root letter completes a word. You must been
     * keeping track of the path to this letter for the complete word.
     * @return boolean whether the root letter completes a word.
     */
    public boolean isWord() {
      return freq > 0;
    }

    /**
     * Getter for the frequency of the word associated with the node.
     * @return Returns the frequency of the word. If the node is not associated
     * with a word, then 0 is returned.
     */
    public int getFrequency() {
      return freq;
    }

    /**
     * Getter for the frequency of the word associated with the node.
     * @return Returns the frequency of the word. If the node is not associated
     * with a word, then 0 is returned.
     */
    private int getPreviousFrequency(String prev) {
      Integer f = previousWords.get(prev);
      return f == null ? 0 : f;
    }

    /**
     * Gets a copy of the Child Trie that contains the letter letter. If the
     * child does not exist it returns null.
     * @param childLetter The String letter that the child should contain.
     * @return Returns a copy of the child Trie that contains the letter.
     * Null if the child does not exist.
     */
    public Trie getChildCopy(String childLetter) {
      Trie child = children.get(childLetter);
      return (child != null ? child.copyOf() : null);
    }

    /**
     * Gets the Child Trie that contains the letter letter. If the child does
     * not exist it returns null.
     * @param childLetter The String letter that the child should contain.
     * @return The child Trie that contains the letter. Null if the child DNE.
     */
    private Trie getChild(String childLetter) {
      return children.get(childLetter);
    }

    /**
     * Getter for the children of the root letter. Returns a copy of the
     * children.
     * @return Returns the children of the root letter as a hashmap.
     */
    public HashMap<String, Trie> getChildren() {
      HashMap<String, Trie> kids = new HashMap<String, Trie>();
      for (Trie child : children.values()) {
        kids.put(child.getLetter(), child.copyOf());
      }

      return kids;
    }

    /**
     * Lists copies of the node's children.
     * @return Returns a list of trie that represent all the children of the
     * node.
     */
    public List<Trie> listChildren() {
      List<Trie> kids = new ArrayList<Trie>();
      for (Trie child : children.values()) {
        kids.add(child.copyOf());
      }
      return kids;
    }

    /**
     * Adds the letter to the child of the node.
     * @param childLetter The letter to be added to the child
     * @param wordPrefix The prefix of the word being added.
     * @return Returns the Child that was added, or the existing child if it
     * existed.
     */
    private Trie addChild(String childLetter, String wordPrefix) {
      Trie child = getChild(childLetter);
      if (child == null) {
        // Didn't Exist
        child = new Trie(childLetter, wordPrefix + letter);
        children.put(childLetter, child);
      }
      return child;
    }

    /**
     * Adds remaining word to a Trie. It is assumed the word is missing the
     * prefix that is the result of the path it took to get to this part of the
     * trie.
     * @param word String the rest of the word being added.
     * @return Returns boolean true if the word previously existed. False
     * otherwise
     */
    private boolean addWord(String word, String prev) {
      if (word.isEmpty()) {
        // Finished off a word!
        if (!letter.isEmpty()) {
          // Not at Root
          freq++;

          // Work with the previous word.
          if (!prev.isEmpty()) {
            Integer f = previousWords.get(prev);
            if (f ==  null) {
              previousWords.put(prev, 1);
            } else {
              previousWords.put(prev, f + 1);
            }
          }
        }

        return freq > 1;

      } else {
        String firstLetter = word.substring(0, 1); // First Letter
        word = word.substring(1); // Rest of Word
        Trie child = addChild(firstLetter, getPrefix());
        return child.addWord(word, prev);
      }
    }


    /**
     * Prints out the letter stored in the trie followed by an asterick if it
     * ends a word. Note: an asterick does not denote that there are no more
     * words stored in the children.
     * @return A String representing a Trie
     */
    @Override
    public String toString() {
      StringBuilder s =
          new StringBuilder(letter + (isWord() ? "*" + freq : ""));
      if (children.size() > 0) {
        s.append("{ ");
        for (Trie c : children.values()) {
          s.append(c + " ");
        }
        s.append("}");
      }

      return s.toString();
    }

    /**
     * Creates a deep copy of the Trie. This is to prevent the original data
     * from being perverted when children and roots and distributed by its
     * functions.
     * @return Returns a Trie that is a copy of the this class. The idea is that
     * the class will not be manipulated when the copy is changed.
     */
    public Trie copyOf() {
      Trie copy = new Trie(this.letter, this.prefix);
      copy.freq = this.freq;
      copy.children = getChildren();
      copy.previousWords = new HashMap<String, Integer>(this.previousWords);
      return copy;
    }
  }


  /**
   * Getter for the size of the Trie. This only works on the root node. All
   * @return Returns an int the size of the Trie (number of words);
   */
  public int getSize() {
    return size;
  }

  /**
   * Getter for the Root of the Trie. The root is a sentinel node and cannot be
   * used. Start with the children immediately.
   * @return Returns the Root node (A Sentinel Trie with no letter value).
   */
  public Trie getRoot() {
    return root.copyOf();
  }

  /**
   * Getter for the children of the root. These children are all tries that
   * begin the storage of essential data (no information stored at root).
   * @return Returns a list of the Tries that are the children of the root.
   */
  public List<Trie> getRoots() {
    return new ArrayList<Trie>(root.getChildren().values());
  }

  private boolean addWord(String word, String prev) {

    word = word.toLowerCase();
    prev = prev.toLowerCase();

    boolean existed = root.addWord(word, prev);

    if (!existed) {
      size++;
    }

    return existed;
  }

  /**
   * Determines if the String word is a word in the DictionaryTrie.
   * @param word The word to test for inclusion in dictionary.
   * @return Returns boolean true if the word is in the dictionary.
   */
  public boolean isWord(String word) {
    Trie node = search(word);
    return node != null && node.isWord();
  }

  /**
   * Finds the node that holds the final letter of the word in the Trie. The
   * node will hold important information about the word. Note: word does not
   * necessarily need to be a word. It could be a prefix, and the node returned
   * will be the node at the end of that prefix.
   * @param word The string to be found in the Trie. This is case sensitive.
   * please input lowercase words.
   * @return Returns the Trie that is the final node of the string word. Null if
   * the word is not in the trie.
   */
  public Trie search(String word) {
    if (word.isEmpty()) {
      return root.copyOf();
    }

    String first = word.substring(0, 1);
    word = word.substring(1);

    Trie curr = root.getChild(first);

    while (curr != null && !word.isEmpty()) {
      first = word.substring(0, 1);
      word = word.substring(1);
      curr = curr.getChild(first);
    }

    return (curr != null ? curr.copyOf() : null);
  }

  /**
   * Determines the number of occasions that the word was added to the Trie.
   * Higher frequencies mean that the word was more heavily used in the text
   * sample that built the Trie.
   * @param word The word for to find frequency for.
   * @return Returns an int with the number of times the word was used in the
   * text that built the Trie.
   */
  public int getFrequency(String word) {
    word = word.toLowerCase();

    Trie trie = search(word);
    if (trie == null || word.isEmpty()) {
      return 0;
    } else {
      return trie.getFrequency();
    }
  }

  /**
   * Determines the number of occasions that the word was added to the Trie.
   * Higher frequencies mean that the word was more heavily used in the text
   * sample that built the Trie.
   * @param word The word for to find bigram frequency for.
   * @param prev The word to that preceded word.
   * @return Returns an int with the number of times the word was used in the
   * text that built the Trie.
   */
  public int getBigramFrequency(String word, String prev) {
    word = word.toLowerCase();
    prev = prev.toLowerCase();

    Trie trie = search(word);
    if (trie == null || word.isEmpty()) {
      return 0;
    } else {
      return trie.getPreviousFrequency(prev);
    }
  }

  private void addFile(File dictionaryFile)
    throws IOException {

    String prev = "";
    String line;
    String[] words;

    try {

      BufferedReader reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(dictionaryFile), "UTF8"));

      line = reader.readLine();

      while (line != null) {
        line = line.replaceAll("[\\p{Punct}]+", " ");
        words = line.split("[\\s]+");

        for (String w : words) {
          addWord(w.toLowerCase(), prev);
          prev = w;
        }

        line = reader.readLine();
      }

      reader.close();
    } catch (UnsupportedEncodingException e) {
      throw new UnsupportedEncodingException("DictionaryTrie: "
        + e.getMessage());
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("DictionaryTrie: " + e.getMessage());
    } catch (IOException e) {
      throw new IOException("DictionaryTrie: " + e.getMessage());
    }
  }

  private void addWords(Collection<String> words) {
    String prev = "";
    for (String w : words) {
      addWord(w.toLowerCase(), prev);
      prev = w;
    }
  }

  @Override
  public String toString() {
    return (root != null ? root.toString() : "");
  }

}
