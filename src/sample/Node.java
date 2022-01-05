package sample;

import java.util.Comparator;

public class Node{
    private Node left,right;
    private int frequency;
    private String character;

    public Node(String character) {
        this.character = character;
        this.frequency = 1;
        this.left = null;
        this.right = null;
    }

    public Node(int frequency,Node left, Node right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public Node(String character, Node left, Node right, int frequency) {
        this.left = left;
        this.right = right;
        this.frequency = frequency;
        this.character = character;
    }

    public boolean isLeafNode() {
        if(this.left==null && this.right==null)
            return true;
        else
            return false;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getCharacter() {
        return character;
    }




}
