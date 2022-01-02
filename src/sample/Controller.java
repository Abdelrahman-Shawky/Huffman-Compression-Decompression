package sample;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Controller {

//    String test = "DDDDCBBAAA";
//    String test = "ABA11$A";
//    String test = "GBBCT10.SEQ         Genetic Sequence Data Bank\n" +
//            "                        December 15 2021\n" +
//            "\n" +
//            "                NCBI-GenBank Flat File Release 247.0\n" +
//            "\n" +
//            "                     Bacterial Sequences (Part 10)\n" +
//            "\n" +
//            "     101 loci,   247874669 bases, from      101 reported sequences\n" +
//            "\n" +
//            "\n" +
//            "LOCUS       AP013063             5225577 bp    DNA     circular BCT 27-JAN-2017";
//    String test = "GBBCT10.SEQ         Genetic Sequence Data Bank\n" +
//            "                        December 15 2021\n" +
//            "\n" +
//            "                NCBI-GenBank Flat File Release 247.0\n" +
//            "\n" +
//            "                     Bacterial Sequences (Part 10)\n" +
//            "\n" +
//            "     101 loci,   247874669 bases, from      101 reported sequences\n" +
//            "\n" +
//            "\n" +
//            "LOCUS       AP013063             5225577 bp    DNA     circular BCT 27-JAN-2017\n" +
//            "DEFINITION  Serratia marcescens SM39 DNA, complete genome.\n" +
//            "ACCESSION   AP013063\n" +
//            "VERSION     AP013063.1\n" +
//            "DBLINK      BioProject: PRJDB1121\n" +
//            "            BioSample: SAMD00061009\n" +
//            "KEYWORDS    .\n" +
//            "SOURCE      Serratia marcescens SM39\n" +
//            "  ORGANISM  Serratia marcescens SM39\n" +
//            "            Bacteria; Proteobacteria; Gammaproteobacteria; Enterobacterales;\n" +
//            "            Yersiniaceae; Serratia.\n" +
//            "REFERENCE   1\n" +
//            "  AUTHORS   Iguchi,A., Nagaya,Y., Pradel,E., Ooka,T., Ogura,Y., Katsura,K.,\n" +
//            "            Kurokawa,K., Oshima,K., Hattori,M., Parkhill,J., Sebaihia,M.,\n" +
//            "            Coulthurst,S.J., Gotoh,N., Thomson,N.R., Ewbank,J.J. and Hayashi,T.\n" +
//            "  TITLE     Genome evolution and plasticity of Serratia marcescens, an\n" +
//            "            important multidrug-resistant nosocomial pathogen\n" +
//            "  JOURNAL   Genome Biol Evol 6 (8), 2096-2110 (2014)\n" +
//            "   PUBMED   25070509\n" +
//            "  REMARK    DOI:10.1093/gbe/evu160\n" +
//            "REFERENCE   2  (bases 1 to 5225577)\n" +
//            "  AUTHORS   Hayashi,T., Iguchi,A. and Ogura,Y.\n" +
//            "  TITLE     Direct Submission\n" +
//            "  JOURNAL   Submitted (27-MAY-2013) Contact:Tetsuya Hayashi Kyushu University,\n" +
//            "            Department of Bacteriology, Faculty of Medical Sciences; 3-1-1\n" +
//            "            Maedashi, Higashi-ku, Fukuoka 812-8582, Japan\n" +
//            "COMMENT     ##Genome-Assembly-Data-START##\n" +
//            "            Assembly Method       :: phred/phrap/consed package\n" +
//            "            Genome Coverage       :: 12.6x\n" +
//            "            Sequencing Technology :: Sanger\n" +
//            "            ##Genome-Assembly-Data-END##\n" +
//            "FEATURES             Location/Qualifiers\n" +
//            "     source          1..5225577\n" +
//            "                     /organism=\"Serratia marcescens SM39\"\n" +
//            "                     /mol_type=\"genomic DNA\"\n" +
//            "                     /strain=\"SM39\"\n" +
//            "                     /db_xref=\"taxon:1334564\"\n" +
//            "     gene            208..276\n" +
//            "                     /gene=\"thrL\"\n" +
//            "                     /locus_tag=\"SM39_0001\"\n" +
//            "     CDS             208..276\n" +
//            "                     /gene=\"thrL\"\n" +
//            "                     /locus_tag=\"SM39_0001\"\n" +
//            "                     /codon_start=1\n" +
//            "                     /transl_table=11\n" +
//            "                     /product=\"thr operon leader peptide\"\n" +
//            "                     /protein_id=\"BAO32072.1\"\n" +
//            "                     /translation=\"MRNISLKTTIITTTDTTGNGAG\"\n" +
//            "     gene            357..2816\n" +
//            "                     /gene=\"thrA\"\n" +
//            "                     /locus_tag=\"SM39_0002\"\n" +
//            "     CDS             357..2816\n" +
//            "                     /gene=\"thrA\"\n" +
//            "                     /locus_tag=\"SM39_0002\"\n" +
//            "                     /codon_start=1\n" +
//            "                     /transl_table=11\n" +
//            "                     /product=\"fused aspartokinase I and homoserine\n" +
//            "                     dehydrogenase I\"\n" +
//            "                     /protein_id=\"BAO32073.1\"\n" +
//            "                     /translation=\"MRVLKFGGTSVANAERFLRVADIMESNARQGQVATVLSAPAKIT\n" +
//            "                     NHLVAMIDKTVAGQDILPNISDAERIFADLLSGLAQALPGFEYDRLKSVVDQEFAQLK\n" +
//            "                     QVLHGVSLLGQCPDSVNAAIICRGEKLSIAIMEGVFRAKGYPVTVINPVEKLLAQGHY\n" +
//            "                     LESTVDIAESTLRIAAAAIPADHIVLMAGFTAGNDKGELVVLGRNGSDYSAAVLAACL\n" +
//            "                     RADCCEIWTDVDGVYTCDPRTVPDARLLKSMSYQEAMELSYFGAKVLHPRTITPIAQF\n" +
//            "                     QIPCLIKNTSNPQAPGTLIGKDSTDDAMPVKGITNLNNMAMINVSGPGMKGMVGMAAR\n" +
//            "                     VFAVMSRAGISVVLITQSSSEYSISFCVPQGELLRARRALEEEFYLELKDGVLDPLDV\n" +
//            "                     MERLAIISVVGDGMRTLRGISARFFSALARANINIVAIAQGSSERSISVVVSNESATT\n" +
//            "                     GVRVSHQMLFNTDQVIEVFVIGVGGVGGALIEQIYRQQPWLKQKHIDLRVCGIANSRV\n" +
//            "                     MLTNVHGIALDSWRDELAGAQEPFNLGRLIRLVKEYHLLNPVIVDCTSSQAVADQYVD\n" +
//            "                     FLADGFHVVTPNKKANTSSMNYYQQLRAAAAGSHRKFLYDTNVGAGLPVIENLQNLLN\n" +
//            "                     AGDELVRFSGILSGSLSFIFGKLDEGLSLSAATLQARANGYTEPDPRDDLSGMDVARK\n" +
//            "                     LLILAREAGYKLELSDIEVESVLPPSFDASGDVDQFLARLPELDKEFARNVANAAEQG\n" +
//            "                     KVLRYVGLIDEGRCKVRIEAVDGNDPLYKVKNGENALAFYSRYYQPLPLVLRGYGAGN\n" +
//            "                     DVTAAGVFADLLRTLSWKLGV\"\n" +
//            "     gene            2818..3747\n" +
//            "                     /gene=\"thrB\"\n" +
//            "                     /locus_tag=\"SM39_0003\"\n" +
//            "     CDS             2818..3747\n" +
//            "                     /gene=\"thrB\"\n" +
//            "                     /locus_tag=\"SM39_0003\"\n" +
//            "                     /codon_start=1\n" +
//            "                     /transl_table=11\n" +
//            "                     /product=\"homoserine kinase\"\n" +
//            "                     /protein_id=\"BAO32074.1\"\n" +
//            "                     /translation=\"MVKVYAPASIGNVSVGFDVLGAAVSPIDGTLLGDCVSVEAAETF\n" +
//            "                     SLQNAGRFVSKLPAEPKENIVYQCWERFCQEIGREVPVAMRLEKNMPIGSGLGSSACS\n" +
//            "                     VVAGLMAMNEFCDRPLDKTTLLGLMGELEGRISGSVHYDNVAPCYLGGLQLMLEEEGI\n" +
//            "                     ISQEVPCFDDWLWVMAYPGIKVSTAEARAILPAQYRRQDCISHGRYLAGFIHACHTRQ\n" +
//            "                     PQLAAKLMQDVIAEPYRTRLLPGFAEARKAAQEIGALACGISGSGPTLFAVCNDGATA\n" +
//            "                     QRMAAWLQQHYLQNDEGFVHICRLDTAGARLLG\"\n" +
//            "     gene            3751..5040\n" +
//            "                     /gene=\"thrC\"\n" +
//            "                     /locus_tag=\"SM39_0004\"\n" +
//            "     CDS             3751..5040\n" +
//            "                     /gene=\"thrC\"\n" +
//            "                     /locus_tag=\"SM39_0004\"\n" +
//            "                     /codon_start=1\n" +
//            "                     /transl_table=11\n" +
//            "                     /product=\"threonine synthase\"\n" +
//            "                     /protein_id=\"BAO32075.1\"\n" +
//            "                     /translation=\"MKLYNLKDHNEQVSFAQAIKQGLGKQQGLFFPLELPEFELTEID\n" +
//            "                     QLLEQDFVTRSSRILSAFIGEEVPEAALKKRVQAAFEFPAPVAKVTEDVSCLELFHGP\n" +
//            "                     TLAFKDFGGRFMAQMLAEVAGDQPVTILTATSGDTGAAVAHAFYGLKNVRVVILYPQG\n" +
//            "                     KISPLQEKLFCTLGGNIHTVAIDGDFDACQALVKQAFDDRELKDALHLNSANSINISR\n" +
//            "                     LLAQICYYFEAVAQLPQEARNQLVISVPSGNFGDLTAGLLAKSLGLPVKRFIAATNAN\n" +
//            "                     DTVPRFLTSGQWQPHATVATLSNAMDVSQPNNWPRVEELFRRKVWQLKELGHAAVSDE\n" +
//            "                     TTRETMRELAELGYISEPHAAIAYRALRDRLQEGEFGLFLGTAHPAKFKESVEAILGQ\n" +
//            "                     ELPLPKALALRADLPLLSHTLPAGFAELRKFLMALPA\"\n" +
//            "     gene            complement(5087..5863)\n" +
//            "                     /locus_tag=\"SM39_0005\"\n" +
//            "     CDS             complement(5087..5863)\n" +
//            "                     /locus_tag=\"SM39_0005\"\n" +
//            "                     /codon_start=1\n" +
//            "                     /transl_table=11\n" +
//            "                     /product=\"hypothetical protein\"\n" +
//            "                     /protein_id=\"BAO32076.1\"\n" +
//            "                     /translation=\"MLVIISPAKTLDYDSPLATERFTQPEMLDKSKQLIKICRELTPA\n" +
//            "                     QIASLMSISDKLAGLNAARFSEWQPKFTPDNARQALLAFKGDVYTGLQAQDFSEADFD\n" +
//            "                     FAQQHLRMLSGLYGVLRPLDLMMPYRLEMGIRLENPKGKDLYSFWGDKITQKLNEALE\n" +
//            "                     QQGDDVVVNLASDEYFKAVKPAKLHGALIKPVFLDEKNGKYKVISFYAKKARGLMSRF\n" +
//            "                     IIKNRLTRSEQLVDFNLEGYAFDEAASQGNELVFKRPEQA\"\n" +
//            "     gene            complement(5970..7406)\n" +
//            "                     /locus_tag=\"SM39_0006\"\n" +
//            "     CDS             complement(5970..7406)";

    HashMap<String, String> prefixHashMap;
    String test;
    int NUM_BYTES = 1;
    public String readFile() throws IOException {
        File file = new File("E:\\Java Projects\\HuffmanCompression\\test.txt");
//        BufferedReader br = new BufferedReader(new FileReader(file));
        FileInputStream in = new FileInputStream(file);
        BufferedInputStream buffer = new BufferedInputStream(in);

//        String test;
//        List<Byte> content = new ArrayList<>();
        StringBuilder content = new StringBuilder();
        byte line[] = new byte[NUM_BYTES];
//        int character;
        while((buffer.read(line)) != -1){
            for(byte b : line){
                content.append((char)b);
//                b=0;
            }


//            content.append(System.lineSeparator());
            System.out.println(content);
        }
//        content.deleteCharAt(content.length()-1);
//        test=br.readLine();
//        System.out.println(content.toString());


        return content.toString();

    }

    public void buildNodes() throws IOException {
//        test = readFile();

        HashMap<String, Integer> nodesHashMap = new HashMap<>();

        File file = new File("E:\\Java Projects\\HuffmanCompression\\test.txt");
        FileInputStream in = new FileInputStream(file);
        BufferedInputStream buffer = new BufferedInputStream(in);

        StringBuilder nBytes = new StringBuilder();
        StringBuilder content = new StringBuilder();
        byte line[] = new byte[NUM_BYTES];
        while((buffer.read(line)) != -1){
            for(int i=0; i<NUM_BYTES; i++){
//                System.out.println(line);
                nBytes.append((char)line[i]);
                line[i]=0;
            }
            content.append(nBytes.toString());
            nodesHashMap.putIfAbsent(nBytes.toString(), 0);
            nodesHashMap.put(nBytes.toString(),nodesHashMap.get(nBytes.toString())+1);
//            System.out.println(nBytes.toString());
//            nBytes.append(System.lineSeparator());
//            System.out.println(nBytes);
            nBytes.delete(0,nBytes.length());
        }
        test = content.toString();

//        String nBytes="";
//        int n = 0;
////        System.out.println(test);
//        for(char c : test.toCharArray()){
//            n++;
//            if(n % NUM_BYTES != 0){
//                if(test.length()-n != 0){
//                    nBytes+=c;
//                    continue;
//                }
//            }
//            nBytes+=c;
//            nodesHashMap.putIfAbsent(nBytes, 0);
//            nodesHashMap.put(nBytes,nodesHashMap.get(nBytes)+1);
//            System.out.println(nBytes);
//            nBytes="";
//        }
        PriorityQueue<Node> nodesPriorityQueue = new PriorityQueue<>(4, new NodeComparator());

        for(HashMap.Entry<String,Integer> entry : nodesHashMap.entrySet()){
            Node node = new Node(entry.getKey());
            node.setFrequency(entry.getValue());
            nodesPriorityQueue.add(node);
//            System.out.println("Entry: " + entry.getKey() + " Value: " + entry.getValue());
        }
//        System.out.println(nodesPriorityQueue.peek().getCharacter());
        while(nodesPriorityQueue.size()>1)
        {
            Node node1 = nodesPriorityQueue.poll();
//            System.out.println("1 --> " + node1.getFrequency());
            Node node2 = nodesPriorityQueue.poll();
//            System.out.println("2 --> " + node2.getFrequency());
            Node nodeResult = new Node(node1.getFrequency()+node2.getFrequency(),node1,node2);
//            System.out.println(nodeResult.getFrequency());
            nodesPriorityQueue.add(nodeResult);
        }
//        System.out.println(nodesPriorityQueue.peek().getFrequency());

        prefixHashMap = new HashMap<>();

        Node headNode = nodesPriorityQueue.poll();
        String prefix="";
        buildPrefix(headNode, prefix);

        String coded = encode();
//        System.out.println(coded);
        String decoded = decode(headNode, coded);
//        System.out.println(decoded);
    }

    //https://www.journaldev.com/
    public void buildPrefix(Node node, String prefix) {
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

    public String encode() throws IOException {
        String coded = "";
        String nBytes="";
        int n = 0;
        for(char c : test.toCharArray()){
            n++;
            if(n % NUM_BYTES != 0){
                if(test.length()-n != 0) {
                    nBytes+=c;
                    continue;
                }
            }
            nBytes+=c;
//            System.out.println(nBytes);
            coded += prefixHashMap.get(nBytes);
            nBytes="";
        }

        BitSet huffmanCodeBit = new BitSet(coded.length());

        for (int i = 0; i < coded.length(); i++) {
            if(coded.charAt(i) == '1')
                huffmanCodeBit.set(i);
        }
//        File path = new File("out.txt");
//        ObjectOutputStream outputStream = null;
//        try {
//            outputStream = new ObjectOutputStream(new FileOutputStream(path));
//            outputStream.writeObject(huffmanCodeBit);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        FileOutputStream file = new FileOutputStream("output.txt");
        BufferedOutputStream output = new BufferedOutputStream(file);
        BitSet bitSet = new BitSet(coded.length());
        int counter=0;
        for(Character c : coded.toCharArray()){
            if(c.equals('1')){
                bitSet.set(counter);
            }
            else{
                bitSet.clear(counter);
            }
            System.out.println(c);
            counter++;
        }
        //lost trailing zero
        System.out.println(bitSet.length());
        output.write(bitSet.toByteArray());
        System.out.println(coded);
        System.out.println(bitSet.toByteArray()[0]);
        System.out.println(bitSet.toString());
        StringBuilder s = new StringBuilder();

        for(int i=0;i<bitSet.length();i++) {
            if(bitSet.get(i)){
                s.append("1");
            }
            else{
                s.append("0");
            }
        }
        System.out.println(s.toString());
        output.close();

//        File file = new File("out2.txt");
//        byte[] data = coded.getBytes(StandardCharsets.UTF_8);
//
//        try (RandomAccessFile stream = new RandomAccessFile(file, "rw");
//             FileChannel channel = stream.getChannel())
//        {
//            ByteBuffer buffer = ByteBuffer.allocate(data.length);
//            buffer.put(data);
//            buffer.flip();
//            channel.write(buffer);
//
//            System.out.println("Successfully written data to the file");
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

        return coded;
    }

    public String decode(Node node,String encoded){
        Node decodingNode = node;
//        String decoded = "";
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
                    decoded.append(decodingNode.getCharacter());
                    decodingNode = node;
                }
            }
        }
        return decoded.toString();
    }
}
