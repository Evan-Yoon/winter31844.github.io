package addressBook;

import java.io.IOException;

public class Model {

    private int idx;         //순번
    private String name;     //이름
    private String phoneNum; //전화번호
    private boolean gender;  //성별

    public Model() throws IOException {
        FileManager fm = new FileManager("src\\addressBook\\Data.txt");
        String data = fm.getAllData();
        if(data.length() == 0)
            idx = 1;
        else {
            idx = data.split("\\n").length + 1;
        }
    }

    @Override
    public String toString() {
        return this.idx + "\t" +
               this.name + "\t" +
               this.phoneNum + "\t" +
               (this.gender ? "남" : "여");
    }

    // 우클릭 source actions > Generate Getters and Setters
    public void setIdx(int idx) {
        this.idx = idx;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    public int getIdx() {
        return idx;
    }
    public String getName() {
        return name;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public boolean isGender() {
        return gender;
    }
}
