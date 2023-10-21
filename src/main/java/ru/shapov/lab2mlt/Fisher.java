package ru.shapov.lab2mlt;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;

public class Fisher {
    private Fisher(){}

    public static List<Element> findX(List<Element> train){
        List<Element> result = new ArrayList<>();
        List<Element>[] category = splitCategories(train);
        for(int i = 0; i < 3; i++)
            result.add(findAvr(category[i]));
        return result;
    }

    public static double[][] findW(List<Element> train, List<Element> X) {
        double[][] W = new double[4][4];
        List<Element>[] category = splitCategories(train);

        for (int k = 0; k < 3; k++)
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    for (Element element : category[k]) {
                        double diffI = element.getAttributes()[i] - X.get(k).getAttributes()[i];
                        double diffJ = element.getAttributes()[j] - X.get(k).getAttributes()[j];
                        W[i][j] += diffI * diffJ;
                    }

        return W;
    }

    public static double[][] findB(RealMatrix a, List<Element> X){
        double[][] B = new double[4][3];
        for(int i = 0; i < 4; i++)
            for(int k = 0; k < 3; k++) {
                B[i][k] = 0;
                for(int j = 0; j < 4; j++)
                    B[i][k] += a.getEntry(i, j) * X.get(k).getAttributes()[j];
                B[i][k] *= 12;
            }
        return B;
    }

    private static Element findAvr(List<Element> elements){
        double[] att = {0, 0, 0, 0};
        for(Element element : elements){
            for(int i = 0; i < 4; i++){
                att[i] += element.getAttributes()[i];
            }
        }
        for(int i = 0; i < 4; i++){
            att[i] = att[i] * 1.0 / elements.size();
        }
        return new Element(att, elements.get(0).getCategory());
    }

    private static List<Element>[] splitCategories(List<Element> train){
        List<Element>[] category = new ArrayList[3];
        category[0] = new ArrayList<>();
        category[1] = new ArrayList<>();
        category[2] = new ArrayList<>();
        for(Element element : train)
            category[element.getCategory() - 1].add(element);
        return  category;
    }
}
