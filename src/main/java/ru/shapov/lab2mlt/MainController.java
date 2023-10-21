package ru.shapov.lab2mlt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    @FXML private Button btClan;
    @FXML private TableView tableB;
    @FXML private TableView<DoubleData> tableA;
    @FXML private Button btRunFicher;
    @FXML private TableView<Element> tableTrain;
    @FXML private TableColumn<Element, Double> marTrain;
    @FXML private TableColumn<Element, Double> krmTrain;
    @FXML private TableColumn<Element, Double> serTrain;
    @FXML private TableColumn<Element, Double> spdTrain;
    @FXML private TableColumn<Element, Integer> numberTrain;

    @FXML private TableView<Element> tableTest;
    @FXML private TableColumn<Element, Double> marTest;
    @FXML private TableColumn<Element, Double> krmTest;
    @FXML private TableColumn<Element, Double> serTest;
    @FXML private TableColumn<Element, Double> spdTest;
    @FXML private TableColumn<Element, Integer> numberTest;

    @FXML private TableView<Element> tableX;
    @FXML private TableColumn<Element, Double> marX;
    @FXML private TableColumn<Element, Double> krmX;
    @FXML private TableColumn<Element, Double> serX;
    @FXML private TableColumn<Element, Double> spdX;
    @FXML private TableColumn<Element, Integer> numberX;

    @FXML private TableView<Element> tableH;
    @FXML private TableColumn<Element, Double> marH;
    @FXML private TableColumn<Element, Double> krmH;
    @FXML private TableColumn<Element, Double> serH;
    @FXML private TableColumn<Element, Double> spdH;
    @FXML private TableColumn<Element, Integer> numberH;

    private List<Element> train;
    private static String trainPath;
    private List<Element> test;
    private static String testPath;
    private FileChooser fileChooser = new FileChooser();
    private Fisher fisher;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File("src/main/resources/ru/shapov/lab2mlt"));

        marTrain.setCellValueFactory(cellData -> cellData.getValue().getPropertyMar().asObject());
        krmTrain.setCellValueFactory(cellData -> cellData.getValue().getPropertyKrm().asObject());
        serTrain.setCellValueFactory(cellData -> cellData.getValue().getPropertySer().asObject());
        spdTrain.setCellValueFactory(cellData -> cellData.getValue().getPropertySpd().asObject());
        numberTrain.setCellValueFactory(cellData -> cellData.getValue().getPropertyCategory().asObject());

        marTest.setCellValueFactory(cellData -> cellData.getValue().getPropertyMar().asObject());
        krmTest.setCellValueFactory(cellData -> cellData.getValue().getPropertyKrm().asObject());
        serTest.setCellValueFactory(cellData -> cellData.getValue().getPropertySer().asObject());
        spdTest.setCellValueFactory(cellData -> cellData.getValue().getPropertySpd().asObject());
        numberTest.setCellValueFactory(cellData -> cellData.getValue().getPropertyCategory().asObject());

        marX.setCellValueFactory(cellData -> cellData.getValue().getPropertyMar().asObject());
        krmX.setCellValueFactory(cellData -> cellData.getValue().getPropertyKrm().asObject());
        serX.setCellValueFactory(cellData -> cellData.getValue().getPropertySer().asObject());
        spdX.setCellValueFactory(cellData -> cellData.getValue().getPropertySpd().asObject());
        numberX.setCellValueFactory(cellData -> cellData.getValue().getPropertyCategory().asObject());

        marH.setCellValueFactory(cellData -> cellData.getValue().getPropertyMar().asObject());
        krmH.setCellValueFactory(cellData -> cellData.getValue().getPropertyKrm().asObject());
        serH.setCellValueFactory(cellData -> cellData.getValue().getPropertySer().asObject());
        spdH.setCellValueFactory(cellData -> cellData.getValue().getPropertySpd().asObject());
        numberH.setCellValueFactory(cellData -> cellData.getValue().getPropertyCategory().asObject());
    }

    @FXML private void onClickedOpenTrain() throws IOException {
        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;

        trainPath = file.getCanonicalPath();

        train = CVSReader.open(trainPath);

        if(train == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия CVS файла.");
            trainPath = null;
            btRunFicher.setDisable(true);
            alert.show();
        }

        tableTrain.getItems().clear();
        tableTrain.getItems().addAll(train);

        btRunFicher.setDisable(false);
    }

    @FXML private void onClickedOpenTest() throws IOException {
        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;

        testPath = file.getCanonicalPath();

        test = CVSReader.open(testPath);

        if(test == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия CVS файла.");
            testPath = null;
            alert.show();
        }

        tableTest.getItems().clear();
        tableTest.getItems().addAll(test);
        btClan.setDisable(fisher != null);
    }

    @FXML private void onClickedRunFisher() {
        tableX.getItems().clear();
        tableB.getItems().clear();
        tableA.getItems().clear();
        tableA.getColumns().clear();

        Fisher fisher = new Fisher(train);
        tableX.getItems().addAll(fisher.getX());
        RealMatrix A = fisher.getA();
        double[] B0 = fisher.getB0();
        double[][] B = fisher.getB();

        for (int i = 0; i < A.getColumnDimension(); i++) {
            TableColumn<DoubleData, Double> column = new TableColumn<>("attr " + (i + 1));
            column.setCellValueFactory(new PropertyValueFactory<>("data" + (i + 1)));
            tableA.getColumns().add(column);
        }
        for (int row = 0; row < A.getColumnDimension(); row++) {
            DoubleData rowData = new DoubleData();
            for (int col = 0; col < A.getColumnDimension(); col++)
                rowData.setData(col + 1, String.format("%.15f", A.getEntry(row, col)));
            tableA.getItems().add(rowData);
        }

        for (int i = 0; i < B0.length; i++) {
            TableColumn<DoubleData, Double> column = new TableColumn<>("attr " + (i + 1));
            column.setCellValueFactory(new PropertyValueFactory<>("data" + (i + 1)));
            tableB.getColumns().add(column);
        }
        DoubleData rowDataB0 = new DoubleData();
        for (int col = 0; col < B0.length; col++)
            rowDataB0.setData(col + 1, String.format("%.15f", B0[col]));
        tableB.getItems().add(rowDataB0);
        for (int row = 0; row < B.length; row++) {
            DoubleData rowData = new DoubleData();
            for (int col = 0; col < B0.length; col++)
                rowData.setData(col + 1, String.format("%.15f", B[row][col]));
            tableB.getItems().add(rowData);
        }

        btClan.setDisable(test == null );
    }

    @FXML private void onClickedClaf() {
        Fisher fisher = new Fisher(train);
        tableH.getItems().clear();
        test = fisher.defineCategories(test);
        tableH.getItems().addAll(test);
    }
}