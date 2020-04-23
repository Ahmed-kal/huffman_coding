package graph;

import guru.nidi.graphviz.attribute.Records;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import logic.BranchNode;
import logic.LeafNode;
import logic.TreeNode;
import java.io.File;
import java.io.IOException;
import static guru.nidi.graphviz.attribute.Records.label;
import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.model.Factory.mutGraph;

public class GraphCreator {

    public static File Create(TreeNode root, String fileName) {
        MutableGraph mutableGraph = mutGraph("Huffman Tree").setDirected(true).add(createNode(root));
        try {
            fileName = fileName.substring(0, fileName.length()-4);
            File createdGraph = new File(fileName + "_HuffmanTreeGraph.png");
            Graphviz.fromGraph(mutableGraph).render(Format.PNG).toFile(createdGraph);
            return createdGraph;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Node createNode(TreeNode n) {
        if(n instanceof LeafNode) {
            LeafNode ln = (LeafNode)n;
            return Factory.node(n.toString()).with(label( ""),   Records.of(rec(ln.getCharacter() + ""), rec(ln.getFrequency() + "")));
        }
        BranchNode bn = (BranchNode)n;
        return Factory.node(n.toString()).with(label(bn.getFrequency() + ""), Shape.CIRCLE).link(createNode(bn.getLeftChild()), createNode(bn.getRightChild()));
    }

}
