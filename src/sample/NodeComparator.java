package sample;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        if(node1.getFrequency() < node2.getFrequency()){
            return -1;
        }
        if(node1.getFrequency() > node2.getFrequency()){
            return 1;
        }
        return 0;
    }
}
