// File: HNode.java
// Description: Contains the definition of the HNode class
// Author: Cody Stuck

// HNode: node data structure for use in HTree
public class HNode
{
    // members
    private string bitstring;
    private char character;

    // methods

    // accessors

    // getBitString: returns the binary bit string contained in the node
    string getBitString()
    {
        return(this.bitstring);
    }

    // getChar: returns the character contained in the node
    char getChar()
    {
        return(this.character);
    }

    // mutators

    // helpers

    // constructor
    HNode(newBitString, newCharacter)
    {
        this.bitstring = newBitString;
        this.character = newCharacter;
    }


}
