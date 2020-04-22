package testing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTextGenerator {
    Random random;

    public RandomTextGenerator() {
        random = new Random();
    }

    private char getChar() {
        int probability = random.nextInt(100);
        if(probability < 70) {
            return (char)(random.nextInt(26) + 97);
        } else if( probability < 99) {
            return ' ';
        } else {
            return '\n';
        }
    }

    //returns a list of english paragraphs from a random wikipedia article.
    private List<String> getEnglishParagraphs() {
        Document doc;
        try {
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/Special:Random").get();
        } catch (IOException e) {
            System.out.println("here");
            return null;
        }
        Element content = doc.getElementById("mw-content-text");
        List<Element> paragraphs = content.getElementsByTag("p");
        ArrayList<String> toReturn = new ArrayList<>();
        for(Element paragraph : paragraphs) {
            toReturn.add(paragraph.text());
        }
        return toReturn;

    }

    public String generateEnglishText(int size) {
        StringBuilder englishText = new StringBuilder();
        while(englishText.length() < size) {
            List<String> englishParagraphs = getEnglishParagraphs();
            for(String paragraph : englishParagraphs) {
                englishText.append(paragraph);
                englishText.append('\n');
            }
        }
        return englishText.substring(0, size);
    }

    public String generateRandomText(int size){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ; i < size ; i ++) {
            stringBuilder.append((char)(random.nextInt(26) + 'a'));
        }
        return stringBuilder.toString();
    }



}
