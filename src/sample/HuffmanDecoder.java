package sample;

import java.io.*;

public class HuffmanDecoder {

    private static int numOfNodes;
    private static String encodedTree;
    private static String n="";
    private static int NUM_BYTES;

    public static void main(String[] args) throws IOException {

        File fileinput = new File("E:\\Java Projects\\HuffmanCompression\\output.bin");
        FileInputStream fileInputStream = new FileInputStream(fileinput);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        int bytesRead=-1;
        StringBuilder content = new StringBuilder();
        StringBuilder readFile = new StringBuilder();

        while((bytesRead=bufferedInputStream.read()) != -1){
            //convert integer value to string of bits
            String newByte = Integer.toBinaryString(bytesRead);
            content.append(newByte);
            content.reverse();

            //reverse truncation of trailing zeros
            while(content.length()<8){
                content.append(0);
            }
            readFile.append(content.toString());
            content.delete(0,content.length());
        }

        encodedTree = readFile.toString();

        NUM_BYTES = Integer.parseInt(encodedTree.substring(0,8),2);
        encodedTree = encodedTree.substring(8);
        //Final character in encoded string
        for(int i=0;i<NUM_BYTES;i++){
            n+="ÿ";
        }

//        Pad
//        int length = encodedTree.length();
//        while(length%8 != 0){
//            encodedTree+="0";
//            length++;
//        }
        Node headNode = readNode();



        String decoded = decode(headNode,encodedTree);
        System.out.println(decoded);

//        printInorder(headNode);

        bufferedInputStream.close();
        System.out.println("Doneeeee");
    }

    public static Node readNode() {

        char c = encodedTree.charAt(0);
        encodedTree = encodedTree.substring(1);
        if (c == '1') {
                numOfNodes--;
                String nodeData="";
                for(int i=0;i<NUM_BYTES;i++) {
                    nodeData += String.valueOf((char) Integer.parseInt(encodedTree.substring(0, 8), 2));
                    encodedTree = encodedTree.substring(8);
                }
                return new Node(nodeData, null, null, 0);
        }
        else{
                Node leftChild = readNode();
                Node rightChild = readNode();
                return new Node(0, leftChild, rightChild);
            }

    }

    public static void printInorder(Node node)
    {
        if (node == null)
            return;

        /* first recur on left child */
        printInorder(node.getLeft());

        /* then print the data of node */
        System.out.print(node.getCharacter() + " ");

        /* now recur on right child */
        printInorder(node.getRight());
    }

    public static String decode(Node node,String encoded){
        Node decodingNode = node;
        StringBuilder decoded = new StringBuilder();
        boolean flag=false;
        for (char c : encoded.toCharArray()){
            if(c=='0'){
                decodingNode = decodingNode.getLeft();
                if(decodingNode.getLeft()==null && decodingNode.getRight()==null){
                    if(!decodingNode.getCharacter().equals(n)) {
                        decoded.append(decodingNode.getCharacter());
                        decodingNode = node;
                    }
                }
            }
            if(c=='1'){
                decodingNode = decodingNode.getRight();
                if(decodingNode.getLeft()==null && decodingNode.getRight()==null){
                    if(!decodingNode.getCharacter().equals(n)) {
                        decoded.append(decodingNode.getCharacter());
                        decodingNode = node;
                    }
                    else if(flag){
                        break;
                    }
                    else {
                        flag=true;
                        decodingNode = node;
                    }
                }
            }
        }
        return decoded.toString();
    }

}
