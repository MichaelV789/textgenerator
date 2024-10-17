import java.util.*;
import java.io.*;

class Node {
    String key;
    Node left, right;
    CharDistribution counters = new CharDistribution();

    public Node(String key) {
        this.key = key;
        left = right = null;
    }

    void addChar(char c) {
        counters.occurs(c);
    }
}

// Binary search tree implementation with key and value pair
class BinarySearchTree {
    Node root;

    BinarySearchTree() {
        root = null;
    }

    boolean empty() {
        return root == null;
    }

    void insert(String key, char value) {
        root = insertRec(root, key);
        Node node = search(key);
        if (node != null) {
            node.addChar(value);
        }
    }

    Node insertRec(Node root, String key) {
        if (root == null) {
            root = new Node(key);
            return root;
        }
        if (key.compareTo(root.key) < 0) {
            root.left = insertRec(root.left, key);
        } else if (key.compareTo(root.key) > 0) {
            root.right = insertRec(root.right, key);
        }
        return root;
    }

    Node search(String key) {
        return searchRec(root, key);
    }

    Node searchRec(Node root, String key) {
        if (root != null) {
            if (root.key.compareTo(key) == 0)
                return root;
            else if (root.key.compareTo(key) < 0)
                return searchRec(root.right, key);
            else
                return searchRec(root.left, key);
        }
        return null;
    }

    void inorder() {
        inorderRec(root);
        System.out.println("\n");
    }

    void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.println(root.key + " ");
            inorderRec(root.right);
        }
    }
}

class CharDistribution {
    private int[] counters = new int[27]; // 26 letters + space

    public CharDistribution() {
        Arrays.fill(counters, 0); // Initialize all counts to zero
    }

    void occurs(char c) {
        if (c >= 'a' && c <= 'z') {
            counters[c - 'a']++; // Increment the appropriate index for lowercase letters
        } else if (c == ' ') {
            counters[26]++; // Space character
        } else if (c == '\n') {
            counters[26]++; // Treat newline as space
        }
    }

    void printDistribution() {
        for (int i = 0; i < 26; i++) {
            System.out.println((char) ('a' + i) + ": " + counters[i]);
        }
        System.out.println("space: " + counters[26]);
    }

    char getRandomChar() {
        int totalCount = 0;
        for (int i = 0; i < 26; i++) {
            totalCount += counters[i];
        }
        if (totalCount == 0) {
            return ' '; // No characters recorded, return space
        }
        int randomIndex = (int) (Math.random() * totalCount);
        for (int i = 0; i < 26; i++) {
            randomIndex -= counters[i];
            if (randomIndex < 0) {
                return (char) ('a' + i);
            }
        }
        return ' '; // Default to space
    }
}

public class MIVIPA2_3 {

    // Method to read and parse the text file
    public static BinarySearchTree readTextFile() {
        int windowSize;
        Scanner keyboard = new Scanner(System.in);
        BinarySearchTree distributionTree = new BinarySearchTree();
        
        System.out.println("Enter Window Size:");
        windowSize = keyboard.nextInt();
        
        try (Scanner scan = new Scanner(new File("merchant.txt"))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (int i = 0; i <= line.length() - windowSize; i++) {
                    String window = line.substring(i, i + windowSize);
                    distributionTree.insert(window, line.charAt(i + windowSize - 1));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } finally {
            keyboard.close();
        }
        return distributionTree;
    }

    // Get a random character from a node
    static char getRandomChar(Node node) {
        if (node == null) {
            System.out.println("No such node found.");
            return ' ';
        }
        return node.counters.getRandomChar();
    }

    public static void main(String[] args) {
        // this is a git test
        // delta test
        BinarySearchTree tree = readTextFile();
        Node node = tree.search("the");
        tree.inorder();
        if (node != null) {
            char c = getRandomChar(node);
            System.out.println("Random character from 'the' window: " + c);
            tree.root.counters.printDistribution();
        } else {
            System.out.println("The word 'the' was not found in the text.");
        }
    }
}
