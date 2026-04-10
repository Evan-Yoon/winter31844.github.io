package javaStudy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Study10 {
    public static void main(String[] args) {

        try {
            OutputStream os = new FileOutputStream("Test2.db");

            byte[] array = {10, 20, 30};

            os.write(array);

            os.flush();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
