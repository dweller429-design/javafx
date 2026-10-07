package com.example.customermanager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // =========================
        // HEADER
        // =========================

        Label title = new Label("Customer Manager");
        title.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label subtitle = new Label("Manage your customer records");
        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #d1d5db;"
        );

        VBox header = new VBox(5, title, subtitle);
        header.setPadding(new Insets(20));
        header.setStyle(
                "-fx-background-color: #1f2937;" +
                        "-fx-background-radius: 10;"
        );

        // =========================
        // CUSTOMER NAME
        // =========================

        Label nameLabel = new Label("Customer Name");
        nameLabel.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        );

        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");
        nameField.setPrefHeight(40);

        nameLabel.setLabelFor(nameField);

        // =========================
        // PROVINCE
        // =========================

        Label provinceLabel = new Label("Province");
        provinceLabel.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        );

        ComboBox<String> provinceBox = new ComboBox<>();

        provinceBox.getItems().addAll(
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );

        provinceBox.setPromptText("Select province");
        provinceBox.setPrefHeight(40);
        provinceBox.setMaxWidth(Double.MAX_VALUE);

        provinceLabel.setLabelFor(provinceBox);

        // =========================
        // STATUS MESSAGE
        // =========================

        Label status = new Label();
        status.setStyle(
                "-fx-text-fill: #15803d;" +
                        "-fx-font-weight: bold;"
        );

        // =========================
        // BUTTONS
        // =========================

        Button saveButton = new Button("Save Customer");
        Button deleteButton = new Button("Delete Customer");

        saveButton.setPrefHeight(40);
        deleteButton.setPrefHeight(40);

        saveButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setMaxWidth(Double.MAX_VALUE);

        saveButton.setStyle(
                "-fx-background-color: #2563eb;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;"
        );

        deleteButton.setStyle(
                "-fx-background-color: #dc2626;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;"
        );

        // Enter can activate Save
        saveButton.setDefaultButton(true);

        HBox buttonBox = new HBox(10, saveButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(saveButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);

        // =========================
        // TABLE
        // =========================

        TableView<Customer> table = new TableView<>();

        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Customer Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> provinceColumn =
                new TableColumn<>("Province");

        provinceColumn.setCellValueFactory(
                new PropertyValueFactory<>("province"));

        nameColumn.setPrefWidth(240);
        provinceColumn.setPrefWidth(200);

        table.getColumns().addAll(nameColumn, provinceColumn);
        table.setItems(customers);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPrefHeight(230);

        // =========================
        // SAVE CUSTOMER
        // =========================

        saveButton.setOnAction(event -> {

            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            if (name.isEmpty()) {
                showError(
                        "Invalid Input",
                        "Please enter the customer's name."
                );

                nameField.requestFocus();
                return;
            }

            if (province == null) {
                showError(
                        "Invalid Input",
                        "Please select a province."
                );

                provinceBox.requestFocus();
                return;
            }

            Customer customer = new Customer(name, province);
            customers.add(customer);

            status.setText("Customer saved successfully.");

            nameField.clear();
            provinceBox.setValue(null);

            nameField.requestFocus();
        });

        // =========================
        // DELETE CUSTOMER
        // =========================

        deleteButton.setOnAction(event -> {

            Customer selectedCustomer =
                    table.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {

                showError(
                        "No Customer Selected",
                        "Please select a customer to delete."
                );

                return;
            }

            Alert confirmation =
                    new Alert(Alert.AlertType.CONFIRMATION);

            confirmation.setTitle("Delete Customer");
            confirmation.setHeaderText("Confirm Deletion");
            confirmation.setContentText(
                    "Are you sure you want to delete "
                            + selectedCustomer.getName() + "?"
            );

            Optional<ButtonType> result =
                    confirmation.showAndWait();

            if (result.isPresent()
                    && result.get() == ButtonType.OK) {

                customers.remove(selectedCustomer);

                status.setText(
                        "Customer deleted successfully."
                );
            }
        });

        // =========================
        // FORM
        // =========================

        VBox form = new VBox(
                8,
                nameLabel,
                nameField,
                provinceLabel,
                provinceBox,
                status,
                buttonBox
        );

        form.setPadding(new Insets(20));
        form.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #e5e7eb;" +
                        "-fx-border-radius: 10;"
        );

        // =========================
        // MAIN LAYOUT
        // =========================

        VBox root = new VBox(
                15,
                header,
                form,
                table
        );

        root.setPadding(new Insets(20));

        root.setStyle(
                "-fx-background-color: #f3f4f6;"
        );

        // =========================
        // SCENE
        // =========================

        Scene scene = new Scene(root, 620, 650);

        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.setMinWidth(550);
        stage.setMinHeight(600);

        // Start with name field focused
        nameField.requestFocus();

        stage.show();
    }

    // =========================
    // ERROR ALERT
    // =========================

    private void showError(String title, String message) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {
        launch(args);
    }
}