package javaStudy;

import java.rmi.Remote;

public class SmartTelevision implements Remote, Searchable, RemoteControl {

    @Override
    public void searchable(String url) {
        System.out.println("Searching: " + url);
    }

    @Override
    public void turnOn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'turnOn'");
    }

    @Override
    public void turnOff() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'turnOff'");
    }

    @Override
    public void setVolume(int setVolume) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVolume'");
    }
}
