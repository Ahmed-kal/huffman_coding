package logic;

public class BranchNode extends TreeNode {

    private TreeNode leftChild, rightChild;

    public BranchNode(int frequency, TreeNode leftChild, TreeNode rightChild) {
        super(frequency);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }
}
