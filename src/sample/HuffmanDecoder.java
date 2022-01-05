package sample;

import java.io.*;
import java.util.HashMap;
import java.util.Stack;

public class HuffmanDecoder {

    private static int numOfNodes;
    private static String encoded;
    private static HashMap<String, String> prefixHashMap;

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

        String result = readFile.toString();
        System.out.println(result);
        numOfNodes = Integer.parseInt(result.substring(0,8),2);
        System.out.println(numOfNodes);
        result = result.substring(8,result.length());
        System.out.println(result);
//        Node headNode = readNode(result,numOfNodes);
//        printInorder(headNode);
        System.out.println(result.charAt(3));
        encoded = result;
        Node headNode = readNode();
        System.out.println(encoded);
        String prefix = "";
//        buildPrefix(headNode,prefix);
//        encoded = encoded.split("11011")[1];
        System.out.println(encoded);
        String decoded = decode(headNode,encoded);
        System.out.println(decoded);

        printInorder(headNode);



        bufferedInputStream.close();


    }

    public static Node readNode() throws IOException {

        System.out.println(encoded);
        Node leftChild=null;
        Node rightChild=null;
        if(numOfNodes==0){
            return new Node(0, leftChild, rightChild);
        }
            if (encoded.charAt(0) == '1') {
                numOfNodes--;
                System.out.println(numOfNodes);
                String nodeData = String.valueOf((char) Integer.parseInt(encoded.substring(1, 9), 2));
                encoded = encoded.substring(8);
                System.out.println(nodeData);
                return new Node(nodeData, null, null, 0);
            } else{
                encoded = encoded.substring(1);
                leftChild = readNode();
                encoded = encoded.substring(1);
                rightChild = readNode();
                return new Node(0, leftChild, rightChild);
            }

    }

    public static void buildPrefix(Node node, String prefix) {
        if (node != null) {
            if (node.getLeft() == null && node.getRight() == null) {
                prefixHashMap.put(node.getCharacter(), prefix);
            } else {
                prefix += "0";
                buildPrefix(node.getLeft(), prefix);
                prefix = prefix.substring(0, prefix.length() - 1);

                prefix += "1";
                buildPrefix(node.getRight(), prefix);
                prefix = prefix.substring(0, prefix.length() - 1);
            }
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
        for (char c : encoded.toCharArray()){
            if(c=='0'){
                decodingNode = decodingNode.getLeft();
                if(decodingNode.getLeft()==null && decodingNode.getRight()==null){
                    decoded.append(decodingNode.getCharacter());
                    decodingNode = node;
                }
            }
            if(c=='1'){
                decodingNode = decodingNode.getRight();
                if(decodingNode.getLeft()==null && decodingNode.getRight()==null){
//                    if(!decodingNode.getCharacter().equals("ÿ")) {
                        System.out.println(decodingNode.getCharacter());
                        decoded.append(decodingNode.getCharacter());
                        decodingNode = node;
//                    }
//                    else{
//                        return decoded.toString();
//                    }
                }
            }
        }

        return decoded.toString();
    }

}
