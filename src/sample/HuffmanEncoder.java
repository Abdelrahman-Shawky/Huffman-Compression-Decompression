package sample;


import java.io.*;
import java.util.*;

public class HuffmanEncoder {

    private static HashMap<String, String> prefixHashMap = new HashMap<>();
    private static String test;
    private static int NUM_BYTES;
    private static int BLOCK_SIZE = 1024;
    private static String PATH;
    private static String encodedTree;
    private static String n="";
    private static double INPUT_FILE_SIZE;
    private static double OUTPUT_FILE_SIZE;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String comp_decomp = input.substring(0,1);
//        System.out.println(comp_decomp);
        long endTime;
        long startTime;
        long timeElapsed = 0;
        double time = 0;

        if(comp_decomp.equals("c")){
            PATH = input.substring(2,input.length()-2);
//            System.out.println(PATH);
            NUM_BYTES = Integer.parseInt(input.substring(input.length()-1));
//            System.out.println(NUM_BYTES);
            startTime = System.currentTimeMillis();
            buildNodes();
            endTime = System.currentTimeMillis();
            timeElapsed = endTime - startTime;
            time = timeElapsed;
            System.out.println("Compression Time = " + time/1000 + "s");
            double ratio = (INPUT_FILE_SIZE/OUTPUT_FILE_SIZE)*100;
            System.out.println("Compression Ratio = " + ratio + "%");

        }
        else if(comp_decomp.equals("d")){
            PATH = input.substring(2);
//            System.out.println(PATH);
            startTime = System.currentTimeMillis();
            decodeFile();
            endTime = System.currentTimeMillis();
            timeElapsed = endTime - startTime;
            time = timeElapsed;
            System.out.println("Decompression Time = " + timeElapsed + "ms");
            System.out.println("Decompression Time = " + time/1000 + "s");

        }
    }

    public static void buildNodes() throws IOException {

        HashMap<String, Integer> nodesHashMap = new HashMap<>();
        
        File file = new File(PATH);
        INPUT_FILE_SIZE = file.length();
        FileInputStream in = new FileInputStream(file);
        BufferedInputStream buffer = new BufferedInputStream(in);


            StringBuilder nBytes = new StringBuilder();
            StringBuilder content = new StringBuilder();
            byte line[] = new byte[NUM_BYTES];
            while ((buffer.read(line)) != -1) {
                for (int i = 0; i < NUM_BYTES; i++) {
                    nBytes.append((char) line[i]);
                    line[i] = 0;
                }
                content.append(nBytes);
                nodesHashMap.putIfAbsent(nBytes.toString(), 0);
                nodesHashMap.put(nBytes.toString(), nodesHashMap.get(nBytes.toString()) + 1);
                nBytes.delete(0, nBytes.length());

            }
            //Insert Character to know end of encoded string
            String n = "";
            for (int i = 0; i < NUM_BYTES; i++) {
                n+="ÿ";
            }
            nodesHashMap.put(n, 1);
            test = content.toString();
            content.delete(0,content.length());

            //Priority Queue to build tree
            PriorityQueue<Node> nodesPriorityQueue = new PriorityQueue<>(4, new NodeComparator());

            for (HashMap.Entry<String, Integer> entry : nodesHashMap.entrySet()) {
                Node node = new Node(entry.getKey());
                node.setFrequency(entry.getValue());
                nodesPriorityQueue.add(node);
            }
            nodesHashMap.clear();

            while (nodesPriorityQueue.size() > 1) {
                Node node1 = nodesPriorityQueue.poll();
                Node node2 = nodesPriorityQueue.poll();
                Node nodeResult = new Node(node1.getFrequency() + node2.getFrequency(), node1, node2);
                nodesPriorityQueue.add(nodeResult);
            }

            Node headNode = nodesPriorityQueue.poll();
            StringBuilder prefix = new StringBuilder();
            buildPrefix(headNode, prefix);
            prefix.delete(0,prefix.length());
//        System.out.println(prefixHashMap);

            encode(headNode);
//        }

//        System.out.println("Doneeeee");
        buffer.close();
    }

    //https://www.journaldev.com/
    public static void buildPrefix(Node node, StringBuilder prefix) {
        if (node != null) {
            if (node.getLeft() == null && node.getRight() == null) {
                prefixHashMap.put(node.getCharacter(), prefix.toString());
            } else {
                prefix.append("0");
                buildPrefix(node.getLeft(), prefix);
                prefix.deleteCharAt(prefix.length() - 1);

                prefix.append("1");
                buildPrefix(node.getRight(), prefix);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    private static void encodeTree(Node headNode, StringBuilder stringBuilder) throws IOException {

        if(headNode.isLeafNode()){
            stringBuilder.append(1);
            for(Character c : headNode.getCharacter().toCharArray()){
                String value = Integer.toBinaryString(c);
                int length = value.length();
                //Padding with zeros so each node takes 1 byte
                while(length<8){
                    stringBuilder.append(0);
                    length++;
                }
                stringBuilder.append(Integer.toBinaryString(c));
            }
        }
        else{
            stringBuilder.append(0);
            encodeTree(headNode.getLeft(),stringBuilder);
            encodeTree(headNode.getRight(),stringBuilder);
        }
    }

    public static void encode(Node headNode) throws IOException {
        System.out.println("- A " + prefixHashMap.get("A"));
        System.out.println("- B " + prefixHashMap.get("B"));
        String FILE_NAME = PATH.substring(PATH.lastIndexOf('\\')+1);
        String NEW_FILE_NAME = "6353." + NUM_BYTES + "." + FILE_NAME + ".hc";
        String TARGET_PATH = PATH.substring(0,PATH.lastIndexOf('\\')) + NEW_FILE_NAME;
        FileOutputStream file = new FileOutputStream(TARGET_PATH);
        BufferedOutputStream output = new BufferedOutputStream(file);

        StringBuilder treeEncoded = new StringBuilder();
//        StringBuilder treeEncodedString = new StringBuilder();

        //write number of nodes to first byte
        String numBytes = Integer.toBinaryString(NUM_BYTES);
        int length = numBytes.length();
        while(length<8){
            treeEncoded.append(0);
            length++;
        }
        treeEncoded.append(numBytes);

        encodeTree(headNode, treeEncoded);
//        System.out.println(treeEncodedString);

        BitSet treeBitSet = new BitSet();
        int count=0;
        for(Character c : treeEncoded.toString().toCharArray()){
            if(c.equals('1')){
                treeBitSet.set(count);
            }
            else{
                treeBitSet.clear(count);
            }
            count++;
        }
        treeEncoded.delete(0,treeEncoded.length());
//        output.write(treeBitSet.toByteArray());

        StringBuilder coded = new StringBuilder();
        StringBuilder nBytes = new StringBuilder();
        //Encode Content
        int n = 0;
        for(char c : test.toCharArray()){
            n++;
            if(n % NUM_BYTES != 0){
                if(test.length()-n != 0) {
                    nBytes.append(c);
                    continue;
                }
            }
            nBytes.append(c);
            coded.append(prefixHashMap.get(nBytes.toString()));
            nBytes.setLength(0);
        }
        coded.append(prefixHashMap.get("ÿ"));

//        BitSet bitSet = new BitSet(coded.length());
        for(Character c : coded.toString().toCharArray()){
            if(c.equals('1')){
                treeBitSet.set(count);
            }
            else{
                treeBitSet.clear(count);
            }
            count++;
        }
//        System.out.println(coded);
        coded.delete(0,coded.length());

        //lost trailing zero
        output.write(treeBitSet.toByteArray());

        output.close();
        File outputFile = new File(TARGET_PATH);
        OUTPUT_FILE_SIZE = outputFile.length();

    }


    //Decoder


    public static void decodeFile() throws IOException {
        File fileinput = new File(PATH);
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

        String FILE_NAME = PATH.substring(PATH.lastIndexOf('\\')+1,PATH.length()-3);
        String NEW_FILE_NAME = "extracted." + FILE_NAME;
//        System.out.println(NEW_FILE_NAME);
        String TARGET_PATH = PATH.substring(0,PATH.lastIndexOf('\\')) + NEW_FILE_NAME;
        FileOutputStream file = new FileOutputStream(TARGET_PATH);
        BufferedOutputStream output = new BufferedOutputStream(file);

        for(Character c : decoded.toCharArray())
        {
            output.write(c);
        }
        output.close();


//        System.out.println(decoded);

//        printInorder(headNode);

        bufferedInputStream.close();
//        System.out.println("Doneeeee");
    }

    public static Node readNode() {

        char c = encodedTree.charAt(0);
        encodedTree = encodedTree.substring(1);
        if (c == '1') {
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
