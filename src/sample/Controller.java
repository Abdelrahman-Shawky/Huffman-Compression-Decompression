package sample;


import java.io.*;
import java.util.*;

public class Controller {

    HashMap<String, String> prefixHashMap = new HashMap<>();
    String test;
    int NUM_BYTES = 5;

    public void buildNodes() throws IOException {

        HashMap<String, Integer> nodesHashMap = new HashMap<>();

        File file = new File("E:\\Java Projects\\HuffmanCompression\\test.txt");
        FileInputStream in = new FileInputStream(file);
        BufferedInputStream buffer = new BufferedInputStream(in);

        StringBuilder nBytes = new StringBuilder();
        StringBuilder content = new StringBuilder();
        byte line[] = new byte[NUM_BYTES];
        while((buffer.read(line)) != -1){
            for(int i=0; i<NUM_BYTES; i++){
                nBytes.append((char)line[i]);
                line[i]=0;
            }
            content.append(nBytes);
            nodesHashMap.putIfAbsent(nBytes.toString(), 0);
            nodesHashMap.put(nBytes.toString(),nodesHashMap.get(nBytes.toString())+1);
            //Reset nBytes to read next N bytes
            nBytes.delete(0,nBytes.length());
        }
        //Insert Character to know end of encoded string
        StringBuilder n = new StringBuilder();
        for(int i=0;i<NUM_BYTES;i++){
            n.append("ÿ");
        }
        nodesHashMap.put(n.toString(),1);
        test = content.toString();

        //Priority Queue to build tree
        PriorityQueue<Node> nodesPriorityQueue = new PriorityQueue<>(4, new NodeComparator());

        for(HashMap.Entry<String,Integer> entry : nodesHashMap.entrySet()){
            Node node = new Node(entry.getKey());
            node.setFrequency(entry.getValue());
            nodesPriorityQueue.add(node);
        }
        while(nodesPriorityQueue.size()>1)
        {
            Node node1 = nodesPriorityQueue.poll();
            Node node2 = nodesPriorityQueue.poll();
            Node nodeResult = new Node(node1.getFrequency()+node2.getFrequency(),node1,node2);
            nodesPriorityQueue.add(nodeResult);
        }

        Node headNode = nodesPriorityQueue.poll();
        StringBuilder prefix = new StringBuilder();
        buildPrefix(headNode, prefix);
        System.out.println(prefixHashMap);

        encode(headNode);

        System.out.println("Doneeeee");
    }

    //https://www.journaldev.com/
    public void buildPrefix(Node node, StringBuilder prefix) {
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

    private void encodeTree(Node headNode, StringBuilder stringBuilder, StringBuilder s) throws IOException {

        if(headNode.isLeafNode()){
            stringBuilder.append(1);
            s.append(1);
            for(Character c : headNode.getCharacter().toCharArray()){
                String value = Integer.toBinaryString(c);
                int length = value.length();
                //Padding with zeros so each node takes 1 byte
                while(length<8){
                    stringBuilder.append(0);
                    length++;
                }
                stringBuilder.append(Integer.toBinaryString(c));
                s.append(c);
            }
        }
        else{
            stringBuilder.append(0);
            s.append(0);
            encodeTree(headNode.getLeft(),stringBuilder,s);
            encodeTree(headNode.getRight(),stringBuilder,s);
        }
    }

    public String encode(Node headNode) throws IOException {
        FileOutputStream file = new FileOutputStream("output.bin");
        BufferedOutputStream output = new BufferedOutputStream(file);

        StringBuilder treeEncoded = new StringBuilder();
        StringBuilder treeEncodedString = new StringBuilder();

        //write number of nodes to first byte
        String numBytes = Integer.toBinaryString(NUM_BYTES);
        int length = numBytes.length();
        while(length<8){
            treeEncoded.append(0);
            length++;
        }
        treeEncoded.append(numBytes);

        encodeTree(headNode, treeEncoded,treeEncodedString);
        System.out.println(treeEncodedString);

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

        //lost trailing zero
        output.write(treeBitSet.toByteArray());

        output.close();
        /*
        File fileInput = new File("E:\\Java Projects\\HuffmanCompression\\output.bin");
        FileInputStream fileInputStream = new FileInputStream(fileInput);
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

        bufferedInputStream.close();

         */
        return coded.toString();
    }

}
