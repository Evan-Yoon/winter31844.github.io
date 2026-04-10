package study02;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) throws IOException {
        FileManager originalFm = new FileManager("src\\study02\\data.txt", false);

        String str = originalFm.getAllData();

        String[] subStr = str.split("\\n");
        for(int i=0;i<subStr.length;i++){
            String[] subSubStr = subStr[i].split("]");
            try {
                System.out.println(i + " " + subSubStr[1].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
    }
}