package logic;

import graph.GraphCreator;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;


public class HuffmanCoding {

    //The root of the huffman tree.
    private TreeNode root;
    //Character to binary code mapping of the huffman tree.
    private Map<Character, String> codeWords;
    //the character set used for reading and writing.
    private Charset charset;


    public HuffmanCoding(String fileName) {
        this.charset = Charset.defaultCharset();
        root = buildHuffmanTree(readFile(fileName));
        buildMap();
    }

    public HuffmanCoding(String fileName, String charset) {
        this.charset = Charset.forName(charset);
        root = buildHuffmanTree(readFile(fileName));
        buildMap();
    }

    public HuffmanCoding(TreeNode root){
        this.charset = Charset.defaultCharset();
        this.root = root;
        buildMap();
    }

    public HuffmanCoding(TreeNode root, String charset) {
        this.charset = Charset.forName(charset);
        this.root = root;
        buildMap();
    }

    //returns the available character sets on the operating system.
    public Set<String> getAvailableCharsets() {
        return Charset.availableCharsets().keySet();
    }

    //opens the given file and reads all the bytes into byte array.
    private byte[] read(String fileName) {
        try (InputStream is = new FileInputStream(fileName)) {
            return is.readAllBytes();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //sets the character set if it's a valid character set. returns whether the operation was successful.
    public boolean setCharset(String charsetName) {
        Charset charset = Charset.availableCharsets().get(charsetName);
        if(charset != null) {
            this.charset = charset;
            return true;
        }
        return false;
    }

    //opens the given file and reads all the characters into a list of characters.
    private List<Character> readFile(String fileName) {
        ArrayList<Character> chars = new ArrayList<>();
        try(BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(fileName), charset))) {
            int c;
            while((c = br.read()) != -1) {
                chars.add((char) c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chars;
    }

    //returns an List of LeafNodes of the given character list.
    private List<LeafNode> createLeafNodes(List<Character> chars) {
        ArrayList<LeafNode> leafNodes = new ArrayList<>();
        for(Character aChar : chars) {
            boolean foundChar = false;
            for(LeafNode leafNode : leafNodes) {
                if(leafNode.getCharacter() == aChar) {
                    leafNode.incrementFrequency();
                    foundChar = true;
                    break;
                }
            }
            if(!foundChar) {
                leafNodes.add(new LeafNode(aChar));
            }
        }
        return leafNodes;
    }

    //builds a huffman tree of the given character list and returns the root of the tree.
    private TreeNode buildHuffmanTree(List<Character> chars) {
        List<LeafNode> leafNodes = createLeafNodes(chars);
        Heap heap = new Heap(TreeNode.class, leafNodes.size());
        for(LeafNode leafNode : leafNodes) {
            heap.push(leafNode);
        }
        while(heap.getSize() > 1) {
            TreeNode n1 = (TreeNode)heap.pop();
            TreeNode n2 = (TreeNode)heap.pop();
            heap.push(new BranchNode(n1.getFrequency() + n2.getFrequency(), n1, n2));
        }
        return (TreeNode) heap.pop();
    }

    //creates a PNG graph of the huffman tree and saves it the given path.
    public File createTreeGraph(String fileName) {
        return GraphCreator.Create(root, fileName);
    }

    //Writes the byte array to the given fileName.
    private boolean write(String fileName, byte[] data, int size) {
        try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(fileName))) {
             os.write(data, 0, size);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Given a path to a text file and a path to store the new file. Encodes the file based on the huffman map.
    //Returns -1 if the operation is unsuccessful otherwise returns the size of the decoded file.
    public int encodeFile(String fileName, String newFileName) {
        List<Character> chars = readFile(fileName);
        byte[] encodedFile = new byte[chars.size()*10];
        int size = 0;
        byte padding = 0;
        StringBuilder encodedBinary = new StringBuilder();
        for(int i = 0 ; i < chars.size() ; i++) {
            //adds characters into the encoded buffer as their binary code.
            encodedBinary.append(codeWords.get(chars.get(i)));
            //if the binary code buffer reaches a byte add it to the encoded file byte array.
            while(encodedBinary.length() >= 8) {
                encodedFile[size++] = (byte) Short.parseShort( encodedBinary.substring(0, 8), 2);
                encodedBinary.delete(0, 8);
            }
        }
        //if the last binary code does not fit into a byte. adds padding and the size of the padding.
        if(encodedBinary.length() > 0) {
            while (encodedBinary.length() < 8) {
                encodedBinary.insert(0,'0');
                padding++;
            }
            encodedFile[size++] = (byte) Short.parseShort( encodedBinary.substring(0, 8), 2);
        }
        encodedFile[size++] = padding;
        //writes the encoded file to the given path.
        if(write(newFileName, encodedFile, size))
            return size;
        else
            return -1;
    }

    //Writes the character list to the given fileName.
    private boolean writeFile(ArrayList<Character> chars, String fileName) {
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), charset))) {
            for(int i = 0 ; i < chars.size() ; i++) {
                bw.write(chars.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Given a path to an encoded file and a path to store the new file. Decodes the file based on the huffman tree.
    //Returns -1 if the operation is unsuccessful otherwise returns the compression rate.
    public double decodeFile(String fileName, String newFileName) {
        byte[] encodedData = read(fileName);
        ArrayList<Character> decodedData = new ArrayList<>();
        TreeNode n = root;
        for(int i = 0 ; i < encodedData.length-1 ; i++) {
            //translates a byte into an 8-bit binary and saves it to a string.
            String s = String.format("%8s", Integer.toBinaryString(encodedData[i] & 0xFF)).replace(' ', '0');
            //removes padding if exists.
            if(i == encodedData.length - 2 && encodedData[i + 1] != 0) {
                s = s.substring(encodedData[i + 1]);
            }
            //traverses the tree to get the char.
            for(int j = 0 ; j < s.length() ; j++) {
                if(s.charAt(j) == '0') {
                    n = ((BranchNode)n).getLeftChild();
                } else if(s.charAt(j) == '1') {
                    n = ((BranchNode)n).getRightChild();
                }
                if(n instanceof LeafNode) {
                    decodedData.add(((LeafNode) n).getCharacter());
                    n = root;
                }
            }

        }
        //writes decoded data to file.
        if(writeFile(decodedData, newFileName)) {
            double decodedFileSize;
            try {
                 decodedFileSize = Files.size(FileSystems.getDefault().getPath(newFileName));
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

            return encodedData.length/decodedFileSize;
        }
        return -1;
    }

    //builds the codeWord map using a recursive helper method.
    private void buildMap() {
        codeWords = new LinkedHashMap<>();
        buildMap(root, "");
    }
    //recursive method that traverses that explores all paths in the tree and saves each binary path to a character.
    private void buildMap(TreeNode n, String s) {
        if(n instanceof LeafNode) {
            codeWords.put(((LeafNode) n).getCharacter(), s);
        } else {
            buildMap(((BranchNode) n).getLeftChild(), s + "0");
            buildMap(((BranchNode) n).getRightChild(), s + "1");
        }
    }

}

