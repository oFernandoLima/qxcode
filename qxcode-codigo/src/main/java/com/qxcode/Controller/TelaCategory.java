package com.qxcode.Controller;

import com.qxcode.DAO.CategoryDAO;
import com.qxcode.JDBC.JDBC;
import com.qxcode.Main;
import com.qxcode.Model.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class TelaCategory {
    @FXML
    public AnchorPane telaCategory;
    @FXML
    private GridPane gridPane;
    @FXML
    private ArrayList<Pane> categoryCards;
    @FXML
    NewCategory newCategory = new NewCategory();
    @FXML
    NewQuestion newQuestion = new NewQuestion();


    @FXML
    public void initialize() throws IOException {
        this.initGridCategories();
    }


    private void initGridCategories() {
        List<Category> categories = getAllCategories();

        for (Category categoria : categories) {
            if (categoria != null) {
                this.adicionarCategoryEmGrid(categoria);
            } else {
                // handle the null case, e.g. log a warning
                System.out.println("Warning: null Category object encountered");
            }
        }

    }


    private void adicionarCategoryEmGrid(Category categoria) {
        try {
            FXMLLoader childLoader = obterFXMLCategoryLoader();
            AnchorPane childNode = childLoader.load();
            CategoryComponent childController = childLoader.getController();
            childController.setCategory(categoria);

            // Adiciona o categoryCard ao gridPane
            gridPane.getChildren().add(childNode);

            // Calcula as posições da coluna e da linha
            int columnIndex = gridPane.getChildren().indexOf(childNode) % gridPane.getColumnCount();
            int rowIndex = gridPane.getChildren().indexOf(childNode) / gridPane.getColumnCount();

            // Define as posições na grade
            GridPane.setColumnIndex(childNode, columnIndex);
            GridPane.setRowIndex(childNode, rowIndex);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private FXMLLoader obterFXMLCategoryLoader() {
        URL resource = Main.class.getResource("View/components/categoryComponent.fxml");
        if (resource == null) {
            System.out.println("FXML file not found");
        } else {
            System.out.println("FXML file found at: " + resource);
        }
        return new FXMLLoader(resource);
    }


    private List<Category> getAllCategories() {
        ControllerCategory controller = new ControllerCategory();
        List<Category> categories = controller.getAllCategories();
        if (categories != null) {
            return categories;
        } else {
            // handle the null case, e.g. return an empty list
            return new ArrayList<>();
        }
    }

    public void entrarNovaCategoria(MouseEvent mouseEvent) throws IOException {
        Main.setRoot(newCategory.getTela());
    }

    public void entrarNovaQuestao(MouseEvent mouseEvent) throws IOException {
        Main.setRoot(newQuestion.getTela());
    }

    public String getTela() {
        return "com/qxcode/View/telaCategory.fxml";
    }
}
