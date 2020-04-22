import gui.huffmanGUI;
import logic.HuffmanCoding;
import testing.RandomTextGenerator;
import testing.Tester;

public class Main {

    public static void main(String[] args) {
       /* HuffmanCoding hc = new HuffmanCoding("filesTest.txt");

        hc.encodeFile("filesTest.txt", "output/files.dat");
        double cr = hc.decodeFile("output/files.dat", "output/filesTest.txt");
        System.out.println(cr);*/
        /*try {
            Tester.test(1000, 1000);
        } catch (Exception e) {e.printStackTrace();}
*/

        huffmanGUI huffmanGUI = new huffmanGUI();
        huffmanGUI.run();
    }
}
