package javaStudy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Study13 {
    public static void main(String[] args) {

        try {
            InputStream is = new FileInputStream("Test2.db");

            byte[] data = new byte[100]; // 충분히 큰 배열 준비를 하는 이유는 파일의 크기를 모를 때를 대비해서임

            while (true) {
                int num = is.read(data);
                if(num == -1) break;
                for (int i=0;i<num;i++)
                    System.out.println(data[i]);
            }
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
