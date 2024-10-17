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
    public static String outputStart = "";
    public static int windowSize;
    // Method to read and parse the text file
    public static BinarySearchTree readTextFile() {
        
        Scanner keyboard = new Scanner(System.in);
        BinarySearchTree distributionTree = new BinarySearchTree();
        
        System.out.println("Enter Window Size:");
        windowSize = keyboard.nextInt();
        
        try (Scanner scan = new Scanner(new File("java_projects//merchant.txt"))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (int i = 0; i <= line.length() - windowSize - 1; i++) {
                    String window = line.substring(i, i + windowSize);
                    if (i == 0 && outputStart == "") {
                        outputStart = window;
                    }
                    distributionTree.insert(window, line.charAt(i + windowSize));
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

    static int getInputLength(){
        String filePath = "java_projects//merchant.txt";
        int characterCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int charInt;
            

            while ((charInt = reader.read()) != -1) {
                characterCount++;
            }

            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return characterCount;
    }

    public static void generateOuput(){             
        BinarySearchTree tree = readTextFile();
        tree.search("act");
        String output = outputStart;
        System.out.println(output);
        for(int i = 0; i <= getInputLength(); i++){
            System.out.println(i);
            String searchKey = output.substring(i, i + windowSize );
            Node node = tree.search(searchKey);
            char c = ' ';
            if (node != null) {
                c = getRandomChar(node);
                output = output + c;
            } else {
                output = output + c;
            }
            
        }
        try {

            // Create a FileWriter object
            // to write in the file
            FileWriter fWriter = new FileWriter(
                "java_projects//output.txt");

            // Writing into file
            // Note: The content taken above inside the
            // string
            fWriter.write(output);

            // Printing the contents of a file
            System.out.println(output);

            // Closing the file writing connection
            fWriter.close();

            // Display message for successful execution of
            // program on the console
            System.out.println(
                "File is created successfully with the content.");
        }

        // Catch block to handle if exception occurs
        catch (IOException e) {

            // Print the exception
            System.out.print(e.getMessage());
        }
    }
    

    public static void main(String[] args) {
       
        generateOuput();
        
        
        
        
    }
}
