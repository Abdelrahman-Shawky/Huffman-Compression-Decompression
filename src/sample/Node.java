package sample;

import java.util.Comparator;

public class Node{
    private Node left,right;
    private int frequency;
    private char character;

    public Node(char character) {
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

    public char getCharacter() {
        return character;
    }




}
