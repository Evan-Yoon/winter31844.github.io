package study03;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String pythonCmd = "python";
        String scriptName = "src/study03/emotionModelRunner.py";

        Scanner scanner = new Scanner(System.in, "EUC-KR");

        System.out.print("문장입력 : ");
        String inputText = scanner.nextLine();

        ProcessBuilder pb = new ProcessBuilder(pythonCmd, scriptName);

        pb.redirectErrorStream(true);

        Process process = pb.start();

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), "UTF-8"));
        bw.write(inputText);
        bw.newLine();
        bw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("DEBUG:")) {
                System.out.println(line);
                continue;
            }

            // 결과 판단
            if("1".equals(line.trim())) {
                System.out.println("결과: 긍정");
                break;
            } else if("0".equals(line.trim())) {
                System.out.println("결과: 부정");
                break;
            } else {
                System.out.println("파이썬 출력: " + line);
            }
        }

        int exitCode = process.waitFor();
        if(exitCode != 0) {
            System.out.println("파싱 실패: " + exitCode);
        }

        scanner.close();
    }
}