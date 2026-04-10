package study01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) throws IOException, InterruptedException { //예외처리를 하는 이유는 ProcessBuilder가 외부 프로그램을 실행하기 때문에
        Scanner scan = new Scanner(System.in);
        System.out.println("몇 단?");
        String n = scan.next();

        ProcessBuilder pb = new ProcessBuilder("python", "src\\study01\\gugudan.py", n);
        Process process = pb.start();

        // InputStreamReader is = new InputStreamReader(process.getInputStream());
        // BufferedReader reader = new BufferedReader(is);

        //리팩토링 작업
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while((line = reader.readLine()) != null){ //null이 아닐 때까지 계속 읽음
            System.out.println(line);
        }

        int exitCode = process.waitFor(); //프로세스가 종료될 때까지 대기
        System.out.println("파이썬 종료 코드 = " + exitCode);
        scan.close();
    }
}
