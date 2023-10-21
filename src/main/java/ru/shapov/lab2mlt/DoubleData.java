package ru.shapov.lab2mlt;

public class DoubleData {
    private String data1;
    private String data2;
    private String data3;
    private String data4;

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }

    public String getData3() {
        return data3;
    }

    public String getData4() {
        return data4;
    }

    public void setData(int column, String value) {
        if (column == 1) {
            data1 = value;
        } else if (column == 2) {
            data2 = value;
        } else if (column == 3) {
            data3 = value;
        } else if(column == 4){
            data4 = value;
        }
    }
}
