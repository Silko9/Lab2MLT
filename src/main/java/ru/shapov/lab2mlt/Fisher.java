package ru.shapov.lab2mlt;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fisher {
    private int countAtt;
    private int countCategory;
    private int countTrainData;
    private List<Element> X;
    private double[][] W;
    private RealMatrix A;
    private double[][] B;
    private double[] B0;
    public Fisher(List<Element> train){
        countTrainData = train.size();
        countAtt = train.get(0).size();
        X = findX(train);
        W = findW(train, X);
        A = findA(W);
        B = findB(A, X);
        B0 = findB0(B, X);
    }

    public Element defineCategory(Element e){
        int numberCat = 0;
        double maxH = 0;
        double[] H = findH(B, B0, e);
        for(int i = 0; i < H.length; i++)
            if(maxH < H[i]){
                maxH = H[i];
                numberCat = i +1;
            }
        e.setCategory(numberCat);
        return e;
    }

    public List<Element> defineCategories(List<Element> test){
        List<Element> elements = new ArrayList<>();
        for(Element element : test)
            elements.add(defineCategory(element));
        return elements;
    }

    private List<Element> findX(List<Element> train){
        List<Element> result = new ArrayList<>();
        List<Element>[] category = splitCategories(train);
        for(int i = 0; i < category.length; i++)
            result.add(findAvr(category[i]));
        return result;
    }

    private double[][] findW(List<Element> train, List<Element> X) {
        double[][] W = new double[countAtt][countAtt];
        List<Element>[] category = splitCategories(train);

        for (int k = 0; k < category.length; k++)
            for (int i = 0; i < countAtt; i++)
                for (int j = 0; j < countAtt; j++)
                    for (Element element : category[k]) {
                        double diffI = element.getAttribute(i) - X.get(k).getAttribute(i);
                        double diffJ = element.getAttribute(j) - X.get(k).getAttribute(j);
                        W[i][j] += diffI * diffJ;
                    }

        return W;
    }

    private double[][] findB(RealMatrix a, List<Element> X){
        double[][] B = new double[countAtt][countCategory];
        for(int i = 0; i < countAtt; i++)
            for(int k = 0; k < countCategory; k++) {
                B[i][k] = 0;
                for(int j = 0; j < countAtt; j++)
                    B[i][k] += a.getEntry(i, j) * X.get(k).getAttribute(j);
                B[i][k] *= countTrainData - countCategory;
            }
        return B;
    }

    private double[] findB0(double[][] B, List<Element> X){
        double[] result = new double[countCategory];
        Arrays.fill(result, 0);
        for(int k = 0; k < countCategory; k++){
            for(int j = 0; j < countAtt; j++)
                result[k] += B[j][k] * X.get(k).getAttribute(j);
            result[k] *= -(1.0/2.0);
        }
        return result;
    }

    private double[] findH(double[][] B, double[] B0, Element X){
        double[] result = new double[countCategory];
        Arrays.fill(result, 0);
        for(int k = 0; k < countCategory; k++){
            result[k] += B0[k];
            for(int i = 0; i < countAtt; i++)
                result[k] += B[i][k] * X.getAttribute(i);
        }
        return result;
    }

    private Element findAvr(List<Element> elements){
        double[] att = new double[countAtt];
        Arrays.fill(att, 0);
        for(Element element : elements)
            for(int i = 0; i < countAtt; i++)
                att[i] += element.getAttribute(i);
        for(int i = 0; i < countAtt; i++)
            att[i] = att[i] / elements.size();
        return new Element(att, elements.get(0).getCategory());
    }

    private List<Element>[] splitCategories(List<Element> train){
        int maxCategory = 0;
        for (Element element : train) {
            int category = element.getCategory();
            if (category > maxCategory)
                maxCategory = category;
        }
        countCategory = maxCategory;
        List<Element>[] category = new ArrayList[maxCategory];
        for(int i = 0; i < maxCategory; i++) category[i] = new ArrayList<>();
        for(Element element : train)
            category[element.getCategory() - 1].add(element);
        return  category;
    }

    private RealMatrix roundMatrix(RealMatrix matrix, int decimalPlaces) {
        int numRows = matrix.getRowDimension();
        int numCols = matrix.getColumnDimension();
        double[][] roundedData = new double[numRows][numCols];
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numCols; j++) {
                double originalValue = matrix.getEntry(i, j);
                double roundedValue = Precision.round(originalValue, decimalPlaces);
                roundedData[i][j] = roundedValue;
            }
        return new org.apache.commons.math3.linear.Array2DRowRealMatrix(roundedData);
    }

    private RealMatrix findA(double[][] W){
        RealMatrix matrix = MatrixUtils.createRealMatrix(W);
        return MatrixUtils.inverse(matrix);
    }

    public List<Element> getX() {
        return X;
    }

    public double[][] getW() {
        return W;
    }

    public RealMatrix getA() {
        return A;
    }

    public double[][] getB() {
        return B;
    }

    public double[] getB0() {
        return B0;
    }
}