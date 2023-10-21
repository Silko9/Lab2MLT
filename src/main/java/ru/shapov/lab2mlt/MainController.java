package ru.shapov.lab2mlt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

public class MainController implements Initializable {
    @FXML private TableColumn<Element, Double> mar;
    @FXML private TableColumn<Element, Double> krm;
    @FXML private TableColumn<Element, Double> ser;
    @FXML private TableColumn<Element, Double> spd;
    @FXML private TableColumn<Element, Integer> number;
    @FXML private TableView<Element> table;
    private List<Element> train;
    private static String trainPath;
    private FileChooser fileChooser = new FileChooser();

    private List<Element> X = new ArrayList<>();

    private double[][] W = new double[4][4];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File("src/main/resources/ru/shapov/lab2mlt"));

        mar.setCellValueFactory(cellData -> cellData.getValue().getPropertyMar().asObject());
        krm.setCellValueFactory(cellData -> cellData.getValue().getPropertyKrm().asObject());
        ser.setCellValueFactory(cellData -> cellData.getValue().getPropertySer().asObject());
        spd.setCellValueFactory(cellData -> cellData.getValue().getPropertySpd().asObject());
        number.setCellValueFactory(cellData -> cellData.getValue().getPropertyCategory().asObject());

    }

    public void onClickedOpenCVS() throws IOException {
        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;

        trainPath = file.getCanonicalPath();

        train = CVSReader.open(trainPath);

        if(train == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия CVS файла.");
            trainPath = null;
            alert.show();
        }

        table.getItems().addAll(train);

        X = Fisher.findX(train);
        for(Element e : X){
            System.out.println(e.getAttributes()[0] + " " +
                            e.getAttributes()[1] + " " +
                            e.getAttributes()[2] + " " +
                            e.getAttributes()[3] + " " + e.getCategory());
        }

        W = Fisher.findW(train, X);
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                System.out.println(W[i][j] + " ");
            System.out.println('\n');
        }

        RealMatrix matrix = MatrixUtils.inverse(MatrixUtils.createRealMatrix(W));
        RealMatrix a = roundMatrix(matrix, 9);

        System.out.println("Обратная матрица:");
        System.out.println(a.toString());

        double[][] B = Fisher.findB(a, X);
        System.out.println("Матрица B:");
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++)
                System.out.println(B[i][j] + " ");
            System.out.println('\n');
        }

    }
    public static RealMatrix roundMatrix(RealMatrix matrix, int decimalPlaces) {
        int numRows = matrix.getRowDimension();
        int numCols = matrix.getColumnDimension();

        double[][] roundedData = new double[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                double originalValue = matrix.getEntry(i, j);
                double roundedValue = Precision.round(originalValue, decimalPlaces);
                roundedData[i][j] = roundedValue;
            }
        }

        return new org.apache.commons.math3.linear.Array2DRowRealMatrix(roundedData);
    }
}