package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
        sortedCharFreqList = new ArrayList<>();
        double [] numarray = new double[128];
        char [] chararray = new char[128];
        double count = 0;
        while (StdIn.hasNextChar()){
            int a = StdIn.readChar();
            numarray[a]+=1;
            chararray[a]=(char)a;
            count++;
        }
        for (int i =0; i<128; i++){
            if (numarray[i]==count){
                if (i==127){
                    CharFreq newchar = new CharFreq((char)0, 0);
                    sortedCharFreqList.add(newchar);
                }
                else{
                int nn = i+1;
                CharFreq newchar = new CharFreq((char)nn, 0);
                sortedCharFreqList.add(newchar);}
                CharFreq thischar = new CharFreq(chararray[i], 1.0);
                sortedCharFreqList.add(thischar);
            }
            else if (numarray[i]>0){
            CharFreq newchar = new CharFreq(chararray[i], numarray[i]/count);
            sortedCharFreqList.add(newchar);
        }}
        Collections.sort(sortedCharFreqList);
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    //https://github.com/Hbattini/Rutgers-DataStructures-Huffman/blob/main/HuffmanCoding.java
    //delete the link !
    public void makeTree() {
        huffmanRoot= new TreeNode();
        Queue<TreeNode> Source = new Queue<TreeNode>();
        Queue<TreeNode> Target = new Queue<TreeNode>();
        Queue<TreeNode> Hold = new Queue<TreeNode>();
        for(int i =0; i<sortedCharFreqList.size(); i++)
        {TreeNode a = new TreeNode(sortedCharFreqList.get(i),null,null);
        Source.enqueue(a);}
        TreeNode l = Source.dequeue();
        TreeNode r = Source.dequeue();
        double lp = l.getData().getProbOcc();
        double rp = r.getData().getProbOcc();
        CharFreq nc = new CharFreq(null,rp+lp);
        TreeNode y = new TreeNode(nc, l, r);
        Target.enqueue(y);
        huffmanRoot = y;
        while (!(huffmanRoot.getData().getProbOcc()==1)){
            if (Target.isEmpty()){
                TreeNode left = Source.dequeue();
                Hold.enqueue(left);}
            else if (Source.isEmpty()){
                TreeNode left = Target.dequeue();
                Hold.enqueue(left);}
            else if (Source.peek().getData().getProbOcc()<=Target.peek().getData().getProbOcc()){
                TreeNode left = Source.dequeue();
                Hold.enqueue(left);
            }
            else{
                TreeNode left = Target.dequeue();
                Hold.enqueue(left);}
            if (Target.isEmpty()){
                TreeNode right = Source.dequeue();
                double rightprob = right.getData().getProbOcc();
                TreeNode left = Hold.dequeue();
                double leftprob = left.getData().getProbOcc();
                CharFreq newchar = new CharFreq(null,rightprob+leftprob);
                TreeNode yes = new TreeNode(newchar, left, right);
                Target.enqueue(yes);
                huffmanRoot = yes;
            }
            else if (Source.isEmpty()){
                TreeNode right = Target.dequeue();
                double rightprob = right.getData().getProbOcc();
                TreeNode left = Hold.dequeue();
                double leftprob = left.getData().getProbOcc();
                CharFreq newchar = new CharFreq(null,rightprob+leftprob);
                TreeNode yes = new TreeNode(newchar, left, right);
                Target.enqueue(yes);
                huffmanRoot = yes;
            }
            else if (Source.peek().getData().getProbOcc()<=Target.peek().getData().getProbOcc()){
                TreeNode right = Source.dequeue();
                double rightprob = right.getData().getProbOcc();
                TreeNode left = Hold.dequeue();
                double leftprob = left.getData().getProbOcc();
                CharFreq newchar = new CharFreq(null,rightprob+leftprob);
                TreeNode yes = new TreeNode(newchar, left, right);
                Target.enqueue(yes);
                huffmanRoot = yes;
            }
            else{
                TreeNode right = Target.dequeue();
                double rightprob = right.getData().getProbOcc();
                TreeNode left = Hold.dequeue();
                double leftprob = left.getData().getProbOcc();
                CharFreq newchar = new CharFreq(null,rightprob+leftprob);
                TreeNode yes = new TreeNode(newchar, left, right);
                Target.enqueue(yes);
                huffmanRoot = yes;}
        }
        // TreeNode last = Target.dequeue();
        // double prob = last.getData().getProbOcc();
        // TreeNode left = Hold.dequeue();
        // double leftprob = left.getData().getProbOcc();
        // CharFreq newchar = new CharFreq(null,rightprob+leftprob);
        // TreeNode yes = new TreeNode(newchar, left, right);
        // huffmanRoot = yes;
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        //recursion
        encodings = new String[128];
        TreeNode r = huffmanRoot;
        String en = new String();
        encodings= amy(r, encodings, en);
    }

    private static String[] amy(TreeNode root, String[] encodings, String encode){
        if ((root.getLeft()==null)&&(root.getRight()==null)){
            encodings[(int)(root.getData().getCharacter())]=encode;
        }
        else{
        amy(root.getLeft(), encodings, encode+"0");
        amy(root.getRight(), encodings, encode+"1");
        }
        return encodings;
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String bs = new String();
        while(StdIn.hasNextChar()){
            bs += encodings[StdIn.readChar()];
        }
        writeBitString(encodedFile, bs);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        String bs = readBitString(encodedFile);
        String a = new String();
        TreeNode root = huffmanRoot;
        for (int i =0; i<bs.length(); i++){
            if (bs.charAt(i)=='0'){
                root = root.getLeft();
            }
            else if (bs.charAt(i)=='1'){
                root = root.getRight();
            }
            if (root.getData().getCharacter()!=null){
                a+=root.getData().getCharacter();
                root=huffmanRoot;
            }
        }
        StdOut.print(a);
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}