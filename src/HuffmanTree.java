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

    char end; // TODO: is this needed?

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

    // saveCharEncode: encodes the raw text using the tree, then saves the resulting binary string to a file
    // postconditions: a binary string file (1's and 0's) will be saved to the disk, the length of the string will be returned
    public int saveCharEncode(String fileName)
    {
        String toEncode = raw + end;
        String encoded = "";
        String curr = "";
        HuffmanNode temp = root;

        for (int i = 0; i < toEncode.length(); i++)
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
                        //TODO: figure out exceptions and throw "Character not found on either children nodes"
                    }
                }
                else
                {
                    //TODO: throw "Node has no children but character hasn't been found yet"
                }
            }
        }

        System.out.println(encoded);
        // TODO: learn how to save this to a text file instead of printing to console

        return(encoded.length());
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
            // TODO: figure out how to throw exceptions
        }

        for (int i = 0; i < 255; i++) // TODO: fix this by initializing the list with 0's
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
                end = (char) i;
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
        HuffmanTree test = new HuffmanTree("a bb ccc dddd eeeee");
        test.printCodes();
        test.printTree();
        System.out.print(test.saveCharEncode("test.txt"));
    }

}