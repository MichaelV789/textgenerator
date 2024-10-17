import java.util.*;
import java.io.*;

// Node for the HashMap (handles chaining in case of hash collisions)
class HashMapNode {
    String key;
    CharDistribution counters;
    HashMapNode next;

    public HashMapNode(String key) {
        this.key = key;
        this.counters = new CharDistribution();
        this.next = null;
    }
    
    void addChar(char c) {
        counters.occurs(c);
    }
}

// Custom HashMap class
class CustomHashMap {
    private HashMapNode[] table;
    private int capacity;

    public CustomHashMap(int capacity) {
        this.capacity = capacity;
        this.table = new HashMapNode[capacity];
    }

    // Hash function to map the string key to an index in the array
    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // Insert a key-value pair into the hash map
    public void insert(String key, char value) {
        int index = hash(key);
        HashMapNode newNode = new HashMapNode(key);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            // Handle collision using chaining
            HashMapNode current = table[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    current.addChar(value);
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        newNode.addChar(value);
    }

    // Search for a key in the hash map and return the corresponding node
    public HashMapNode search(String key) {
        int index = hash(key);
        HashMapNode current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.next;
        }
        return null; // Key not found
    }
}

class CharDistribution {
    private int[] counters = new int[28]; // 26 letters + space + newline

    public CharDistribution() {
        Arrays.fill(counters, 0); // Initialize all counts to zero
    }

    void occurs(char c) {
        if (c >= 'a' && c <= 'z') {
            counters[c - 'a']++; // Increment the appropriate index for lowercase letters
        } else if (c == ' ') {
            counters[26]++; // Space character
        } else if (c == '\n') {
            counters[27]++; // Newline character
        }
    }

    void printDistribution() {
        for (int i = 0; i < 26; i++) {
            System.out.println((char) ('a' + i) + ": " + counters[i]);
        }
        System.out.println("space: " + counters[26]);
        System.out.println("newline: " + counters[27]);
    }

    char getRandomChar() {
        int totalCount = 0;
        for (int i = 0; i < 28; i++) { // Include all 28 possible characters (a-z, space, newline)
            totalCount += counters[i];
        }
        if (totalCount == 0) {
            System.out.println("No characters recorded for this node.");
            return ' ';
        }
        int randomIndex = (int) (Math.random() * totalCount);

        for (int i = 0; i < 26; i++) {
            randomIndex -= counters[i];
            if (randomIndex < 0) {
                return (char) ('a' + i);
            }
        }

        // Handle space and newline separately
        randomIndex -= counters[26];
        if (randomIndex < 0) {
            return ' ';
        }

        randomIndex -= counters[27];
        if (randomIndex < 0) {
            return '\n';
        }

        return ' '; // Default case, though it shouldn't be reached
    }
}

public class MIVIPA3 {
    public static String outputStart = "";
    public static int windowSize;

    // Method to read and parse the entire text file as a block
    public static CustomHashMap readTextFile() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter Window Size:");
        windowSize = keyboard.nextInt();

        CustomHashMap distributionMap = new CustomHashMap(1000); // Capacity of 1000 buckets

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("java_projects//merchant.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');  // Keep newline characters
            }
        } catch (IOException e) {
            System.out.println("File not found or error reading file: " + e.getMessage());
        }

        // Process the entire content as a single block
        String text = content.toString();

        if (text.length() >= windowSize) {
            for (int i = 0; i <= text.length() - windowSize; i++) {
                String window = text.substring(i, i + windowSize);

                if (i == 0 && outputStart.equals("")) {
                    outputStart = window;
                }

                // Check if there's a character after the window and insert it
                if (i + windowSize < text.length()) {
                    distributionMap.insert(window, text.charAt(i + windowSize));
                }
            }
        }

        return distributionMap;
    }

    // Get a random character from a node
    static char getRandomChar(HashMapNode node) {
        if (node == null) {
            System.out.println("No such node found.");
            return ' ';
        }
        return node.counters.getRandomChar();
    }

    static int getInputLength() {
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

    public static void generateOutput() {
        CustomHashMap tree = readTextFile();
        String output = outputStart;
        System.out.println(output);
        int spaceCount = 0;

        for (int i = 0; i <= getInputLength() - windowSize; i++) {
            if (i + windowSize > output.length()) {
                System.out.println("Reached the end of the output generation.");
                break;
            }

            String searchKey = output.substring(i, i + windowSize);
            HashMapNode node = tree.search(searchKey);
            char c = ' ';

            if (node != null) {
                c = getRandomChar(node);
                spaceCount = 0; // Reset space count if valid character is found
            } else {
                System.out.println("Invalid key: " + searchKey);
                spaceCount++;
                if (spaceCount > 10) { // Arbitrary threshold for too many spaces
                    System.out.println("Too many spaces generated, stopping...");
                    break;
                }
            }

            output = output + c;
        }

        try {
            FileWriter fWriter = new FileWriter("java_projects//output2.txt");
            fWriter.write(output);
            System.out.println(output);
            fWriter.close();
            System.out.println("File is created successfully with the content.");
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    public static void main(String[] args) {
        generateOutput();
    }
}
