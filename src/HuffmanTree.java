// File: HuffmanTree.java
// Description: Contains the definition for the Huffman class, and the main function of the program
// Author: Cody Stuck

import java.util.ArrayList;
import java.util.Collections;

public class HuffmanTree
{
    // subclasses

    // HuffmanNode: node class to be used in the HuffmanTree
    private class HuffmanNode implements Comparable<HuffmanNode>
    {
        // members
        public int frequency;
        public String element;
        public HuffmanNode right, left;

        // with leaves constructor
        HuffmanNode(String newElement, int newFrequency, HuffmanNode newLeft, HuffmanNode newRight)
        {
            this.element = newElement;
            this.frequency = newFrequency;
            this.left = newLeft;
            this.right = newRight;
        }

        // without leaves constructor
        HuffmanNode(String newElement, int newFrequency)
        {
            this.element = newElement;
            this.frequency = newFrequency;
            this.left = null;
            this.right = null;
        }

        // comparator
        @Override
        public int compareTo(HuffmanNode nodeToCompare)
        {
            return(this.frequency - nodeToCompare.frequency);
        }
    }

    // members
    HuffmanNode root;
    ArrayList<Integer> frequencyList;
    ArrayList<HuffmanNode> frequencyNodes;
    String raw;

    // accessors

    // printCodes: prints the codes assigned to every character
    // postconditions: the codes of each character is printed to the console
    public void printCodes()
    {
        printCodesHelper(root, "");
    }

    // printTree: prints the Huffman tree in post order
    // postconditions: every node in the tree is printed
    public void printTree()
    {
        printTreeHelper(root);
    }

    // printCharEncode: encodes the raw text using the tree, then saves the resulting binary string to a file
    // postconditions: a binary string file (1's and 0's) will be output to console, the length of the string will be returned
    public String getEncode()
    {
        String toEncode = raw;
        String encoded = "";
        String curr = "";
        HuffmanNode temp = root;

        for (int i = 0; i < raw.length(); i++)
        {
            temp = root;
            curr = Character.toString(toEncode.charAt(i));

            while (!temp.element.equals(curr))
            {
                if (temp.left != null || temp.right != null) // make sure that the node has at least one child
                {
                    if (temp.left.element.contains(curr))
                    {
                        temp = temp.left;
                        encoded += "0";
                    }
                    else if (temp.right.element.contains(curr))
                    {
                        temp = temp.right;
                        encoded += "1";
                    }
                    else
                    {
                        throw new RuntimeException("Character not found on either children nodes");
                    }
                }
                else
                {
                    throw new RuntimeException("Node has no children but character hasn't been found yet");
                }
            }
        }

        return(encoded);
    }

    // testDecode: tests the decoding function by encoding the given text before decoding back to plaintext
    // postconditions: outputs an encoded and re-decoded string
    public String testDecode()
    {
        String encodedString = this.getEncode();
        String decoded = "";
        HuffmanNode temp = root;
        int codeIndex = 0;

        // step through the binary string as if it were binary code
        while (codeIndex < encodedString.length())
        {
            temp = root;
            while (!(temp.left == null && temp.right == null))
            {
                if (encodedString.charAt(codeIndex) == '0')
                {
                    temp = temp.left;
                } else if (encodedString.charAt(codeIndex) == '1')
                {
                    temp = temp.right;
                } else {
                    throw new RuntimeException("Found character that was not '0' or '1'");
                }
                codeIndex++;
            }

            decoded += temp.element;
        }
        return decoded;
    }

    // helpers

    // printCodesHelper: recursive helper function for printCodes
    // preconditions: t is the parent node to run the recursive function on
    // postconditions: the codes of every character are printed recursively
    private void printCodesHelper(HuffmanNode t, String code)
    {
        // make sure that the node exists
        if (t != null)
        {
            printCodesHelper(t.left, code + "1");
            printCodesHelper(t.right, code + "0");

            if(t.left == null && t.right == null)
            {
                System.out.print(t.element + ": " + code + "\n");
            }
        }
    }

    // printTreeHelper: recursively prints the tree
    // preconditions: root is the parent node to run the recursive function on
    // postconditions: the node will be printed, then the nodes below will be recursively printed
    private void printTreeHelper(HuffmanNode parent)
    {
        if (parent != null)
        {
            printTreeHelper(parent.left);
            printTreeHelper(parent.right);
            System.out.print(parent.frequency + " (" + parent.element + ")\n");
        }
    }

    // constructor
    // preconditions: rawText is the string to be encoded
    // postconditions: a Huffman tree will be constructed from the text to
    HuffmanTree(String rawText)
    {
        raw = rawText;
        root = null;
        frequencyList = new ArrayList<Integer>(Collections.nCopies(256, 0));
        frequencyNodes = new ArrayList<HuffmanNode>(0);

        String tempString = "";

        // make sure that the input string is not empty
        if (rawText.length() <= 0)
        {
            throw new RuntimeException("Input String cannot be empty!");
        }

        for (int i = 0; i < 255; i++)
        {
            frequencyList.set(i, 0);
        }

        // get frequencies of all ascii characters
        for (int i = 0; i < rawText.length(); i++)
        {
            int asciiVal = rawText.charAt(i);
            frequencyList.set(asciiVal, frequencyList.get(asciiVal) + 1);
        }

        // create list of nodes of frequencies
        for (int i = 0; i < 256; i++)
        {
            if (frequencyList.get(i) != 0) {
                tempString = Character.toString((char) i);

                frequencyNodes.add(new HuffmanNode(tempString, frequencyList.get(i), null, null));
            } else if (i == 0) {
                tempString = Character.toString((char) i);
                frequencyNodes.add(new HuffmanNode(tempString, 1, null, null));
            }
        }

        // create tree
        while(frequencyNodes.size() > 1)
        {
            Collections.sort(frequencyNodes);

            for (int i = 0; i < frequencyNodes.size(); i++)
            {
                System.out.print(frequencyNodes.get(i).element);
                System.out.print(":");
                System.out.print(frequencyNodes.get(i).frequency);
                System.out.print("\n");
            }
            System.out.print("\n");

            frequencyNodes.add(new HuffmanNode(
                        frequencyNodes.get(0).element + frequencyNodes.get(1).element,
                        frequencyNodes.get(0).frequency + frequencyNodes.get(1).frequency,
                        frequencyNodes.get(1),
                        frequencyNodes.get(0)));

            frequencyNodes.remove(0);
            frequencyNodes.remove(0);
        }

        root = frequencyNodes.get(0);
    }

    // driver function for HuffmanTree class
    public static void main(String args[])
    {
        // this string can be replaced with any test string!
        String testString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla feugiat convallis quam, nec laoreet massa. Pellentesque consectetur eleifend ante non dignissim. Fusce convallis diam a lacus tristique, nec posuere sapien luctus. Pellentesque aliquam tincidunt elementum. Mauris dictum, felis ut rhoncus cursus, lacus nibh congue quam, in vehicula lacus tellus nec purus. Cras mattis non eros ac vestibulum. Nullam sagittis quam ex, ac imperdiet leo rhoncus pellentesque. Cras lobortis ex vitae tellus rutrum ornare. Cras posuere sit amet neque nec imperdiet. Aliquam erat volutpat. Vivamus consequat tortor vel nisl interdum ultrices. Integer luctus sed augue et accumsan.\n" +
                "\n" +
                "Aliquam fringilla tempor urna sed aliquet. Cras volutpat felis ut erat ullamcorper laoreet. In a nulla pharetra, imperdiet nulla nec, dignissim quam. Sed ut tincidunt dolor. Vestibulum sodales aliquet ultrices. Vestibulum iaculis nunc id ligula bibendum, nec pellentesque elit convallis. Vestibulum faucibus urna libero, nec placerat lacus posuere sed. Nulla ac tortor imperdiet, sollicitudin ex vitae, hendrerit felis. Quisque rutrum iaculis quam in maximus. Cras laoreet hendrerit nulla, consequat efficitur lacus placerat et.\n" +
                "\n" +
                "Praesent finibus posuere luctus. Pellentesque leo magna, sollicitudin finibus libero vel, mattis feugiat eros. Curabitur id quam erat. Maecenas sit amet dictum lorem, pretium tempus nulla. Pellentesque mollis finibus metus ut consectetur. Morbi convallis dui ultrices ex accumsan, eget bibendum purus congue. Cras tristique tristique ante, id semper leo facilisis tempor. Maecenas eu felis id mi convallis mattis id eget tortor. Vestibulum non lorem erat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.\n" +
                "\n" +
                "Nam tristique risus ac fermentum dignissim. Sed sed accumsan sem, id commodo enim. Nullam sit amet mi egestas, molestie enim sed, imperdiet elit. Nullam auctor dapibus risus eu tempor. Donec aliquet, mauris sit amet vestibulum volutpat, eros libero posuere odio, et euismod turpis tellus sed magna. Mauris bibendum velit pulvinar nulla lobortis, non vestibulum elit finibus. Nam commodo ipsum nec ultrices blandit. Duis a porta lectus. Nulla ultrices nunc nec interdum imperdiet. Nullam aliquam tempor congue.\n" +
                "\n" +
                "Ut tincidunt metus et purus pharetra vestibulum. Morbi placerat malesuada scelerisque. Cras faucibus dolor non consequat luctus. In eleifend nibh ac metus elementum porta. Praesent purus elit, dictum vel arcu sit amet, vulputate viverra turpis. Sed ultrices tincidunt nisi quis eleifend. Morbi iaculis mollis diam ac pretium. Integer quis ultricies odio. Fusce dignissim ante eu aliquam porttitor. Mauris porttitor arcu nisi, sit amet tincidunt ligula venenatis in. Duis volutpat efficitur augue, ut feugiat purus euismod sit amet. Duis ut ipsum lacus.";

        System.out.println("Testing on string:\n" + testString);
        HuffmanTree test = new HuffmanTree(testString);
        System.out.println();

        System.out.println("Printing trees...");
        test.printTree();
        System.out.println();

        System.out.println("Printing codes...");
        test.printCodes();
        System.out.println();

        System.out.println("Printing encoded output and compression results...");
        String encodedString = test.getEncode();
        System.out.println(encodedString);
        System.out.println("Compressed to " + encodedString.length() + " bits from " + (testString.length() * 8) + " bits (original string size computed from the assumption that each character is 1 byte large)");
        System.out.println();

        System.out.println("Testing decoding...");
        System.out.println("This should be the original string!\n" + test.testDecode());
    }

}