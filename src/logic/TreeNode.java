package logic;

public abstract class TreeNode implements Comparable<TreeNode>{
    private int frequency;

    public TreeNode(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(TreeNode TreeNode) {
        return this.frequency - TreeNode.getFrequency();
    }
}
