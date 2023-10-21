package ru.shapov.lab2mlt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CVSReader {
    private CVSReader(){}

    public static List<Element> open(String path){
        List<Element> data = new ArrayList<Element>();
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader(path));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] rows = row.split(",");
                double[] values = new double[4];
                for (int i = 0; i < rows.length - 1; i++)
                    values[i] = Double.parseDouble(rows[i]);
                data.add(new Element(values, Integer.parseInt(rows[4])));
            }
            csvReader.close();
            return data;
        } catch (Exception e){
            return null;
        }
    }
}