package javaStudy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Study12 {
    public static void main(String[] args) {

        try {
            InputStream is = new FileInputStream("Test1.db");

            while (true) {
                int data = is.read();
                if(data == -1) break;
                System.out.println(data);
            }

            is.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
