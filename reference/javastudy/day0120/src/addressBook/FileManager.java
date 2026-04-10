package addressBook;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class FileManager {

    String fileName;
    boolean append;

    public FileManager(String fileName, boolean append) {
        this.fileName = fileName;
        this.append = append;
    }

    public void dataCreate(String tupleStr) throws IOException {
        OutputStream os = new FileOutputStream(fileName, append);
        Writer writer = new OutputStreamWriter(os);
        writer.write(tupleStr);
        writer.flush();
        writer.close();
    }

    public String getAllData() throws IOException {
        String dataStr = "";
        Reader reader = new FileReader(fileName);
        while (true) {
            int data = reader.read();
            if (data == -1)
                break;
            dataStr += (char) data;
        }
        reader.close();
        return dataStr;
    }

    public void delData(int delNum) throws IOException {
        int idx = 1;
        String newData = "";
        String data = getAllData();
        String[] subData = data.split("\\n");
        for(int i=0;i<subData.length;i++){
            String[] subSubData = subData[i].split("\\t");
            if(!(delNum == Integer.parseInt(subSubData[0]))){
                newData += (idx++) + "\t" + subSubData[1]
                                   + "\t" + subSubData[2]
                                   + "\t" + subSubData[3] + "\n";
            }
        }
        OutputStream os = new FileOutputStream(fileName);
        Writer writer = new OutputStreamWriter(os);
        writer.write(newData);
        writer.flush();
        writer.close();
        System.out.println(delNum + "번 데이터가 삭제되었습니다.");
    }

    public void updateData(int updateNum, Model m) throws IOException {
        String newData = "";
        String data = getAllData();
        String[] subData = data.split("\\n");
        for(int i=0;i<subData.length;i++){
            String[] subSubData = subData[i].split("\\t");
            if(!(updateNum == Integer.parseInt(subSubData[0]))){
                newData += subSubData[0]
                                        + "\t" + subSubData[1]
                                        + "\t" + subSubData[2]
                                        + "\t" + subSubData[3] + "\n";
            } else {
                newData += m.getIdx()
                                        + "\t" + m.getName()
                                        + "\t" + m.getPhoneNum()
                                        + "\t" + m.getGender() + "\n";
            };
        }
        OutputStream os = new FileOutputStream(fileName);
        Writer writer = new OutputStreamWriter(os);
        writer.write(newData);
        writer.flush();
        writer.close();
        System.out.println(updateNum + "번 데이터가 수정되었습니다.");
    }

    public void updateData(int updateNum, String updatePhoneNum) throws IOException {
        String newData = "";
        String data = getAllData();
        String[] subData = data.split("\\n");
        for(int i=0;i<subData.length;i++){
            String[] subSubData = subData[i].split("\\t");
            if(!(updateNum == Integer.parseInt(subSubData[0]))){
                newData += subSubData[0]
                                        + "\t" + subSubData[1]
                                        + "\t" + subSubData[2]
                                        + "\t" + subSubData[3] + "\n";
            } else {
                newData += subSubData[0]
                                        + "\t" + subSubData[1]
                                        + "\t" + updatePhoneNum
                                        + "\t" + subSubData[3] + "\n";
            }
        }
        OutputStream os = new FileOutputStream(fileName);
        Writer writer = new OutputStreamWriter(os);
        writer.write(newData);
        writer.flush();
        writer.close();
        System.out.println(updateNum + "번 데이터가 수정되었습니다.");
    }
}