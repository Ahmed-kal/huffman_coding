package testing;

import logic.HuffmanCoding;

import java.io.IOException;
import java.nio.file.*;

public class Tester {

    public static void test(int numberOfFiles, int sizeOfFiles) throws IOException {
        Files.createDirectories(Paths.get("Tester/TextFiles"));
        Files.createDirectories(Paths.get("Tester/EncodedTextFiles"));
        Files.createDirectories(Paths.get("Tester/DecodedTextFiles"));
        Files.createFile(Paths.get("Tester/result.txt"));
        RandomTextGenerator textGenerator = new RandomTextGenerator();

        for(int i = 0 , s = 100; i < numberOfFiles ; i++, s*=10) {
            String textPath = "Tester/TextFiles/text_" + i;
            String encodedPath = "Tester/EncodedTextFiles/encodedText_" + i;
            String decodedPath = "Tester/DecodedTextFiles/decodedText_" + i;
            Files.write(Paths.get(textPath), textGenerator.generateRandomText(s).getBytes());
            HuffmanCoding hc = new HuffmanCoding(textPath);
            hc.encodeFile(textPath, encodedPath);
            double compressionRate = hc.decodeFile(encodedPath, decodedPath);
            String result = Files.size(Paths.get(textPath)) + "     " + Files.size(Paths.get(decodedPath))
             + "    " + Files.size(Paths.get(encodedPath)) + "      " + compressionRate + "\n";
            Files.writeString(FileSystems.getDefault().getPath("Tester/result.txt"), result, StandardOpenOption.APPEND);
        }

    }
}
