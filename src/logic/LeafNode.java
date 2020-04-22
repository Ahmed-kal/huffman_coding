package logic;

public class LeafNode extends TreeNode {
    private char character;

    public LeafNode(char c) {
        super(1);
        this.character = c;

    }

    public char getCharacter() {
        return character;
    }

    public void incrementFrequency() {
        setFrequency(getFrequency() + 1);
    }


}
