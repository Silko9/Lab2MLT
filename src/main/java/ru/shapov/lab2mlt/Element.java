package ru.shapov.lab2mlt;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Element {
    private double[] attributes;
    private int category;

    public Element(double[] attributes){
        this(attributes, 0);
    }
    public Element(double[] attributes, int category){
        this.attributes = attributes;
        this.category = category;
    }

    public double getAttribute(int index) {
        return attributes[index];
    }
    public int size(){return attributes.length;}

    public DoubleProperty getPropertyMar(){return new SimpleDoubleProperty(attributes[0]); }
    public DoubleProperty getPropertyKrm(){return new SimpleDoubleProperty(attributes[1]); }
    public DoubleProperty getPropertySer(){return new SimpleDoubleProperty(attributes[2]); }
    public DoubleProperty getPropertySpd(){return new SimpleDoubleProperty(attributes[3]); }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {this.category = category;}

    public IntegerProperty getPropertyCategory() {return new SimpleIntegerProperty(category);}
}