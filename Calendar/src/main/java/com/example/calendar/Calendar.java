package com.example.calendar;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Calendar extends Application {

    private Button updateButton; // Button to update the calendar

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Month Calendar");

        // Create a button to open the calendar
        Button openCalendarButton = new Button("Open Calendar");
        openCalendarButton.setOnAction(event -> openCalendar());

        StackPane landingPane = new StackPane(openCalendarButton);
        landingPane.setAlignment(Pos.CENTER);
        Scene landingScene = new Scene(landingPane, 300, 200);

        primaryStage.setScene(landingScene);
        primaryStage.show();
    }

    private void openCalendar() {
        // Create ChoiceBoxes for month and year
        ChoiceBox<String> monthChoiceBox = new ChoiceBox<>();
        for (int i = 1; i <= 12; i++) {
            monthChoiceBox.getItems().add(LocalDate.of(2000, i, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        }
        monthChoiceBox.setValue(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));

        ChoiceBox<String> yearChoiceBox = new ChoiceBox<>();
        for (int i = 2000; i <= 2100; i++) {
            yearChoiceBox.getItems().add(String.valueOf(i));
        }
        yearChoiceBox.setValue(String.valueOf(LocalDate.now().getYear()));

        // Create an "Update" button
        updateButton = new Button("Update");

        // Create an HBox for ChoiceBoxes and the "Update" button
        HBox navigationBar = new HBox(10);
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.getChildren().addAll(monthChoiceBox, yearChoiceBox, updateButton);

        // Create a GridPane to hold the navigation bar and the calendar
        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);
        calendarPane.setVgap(10);

        // Add the navigation bar to the GridPane
        calendarPane.add(navigationBar, 0, 0);

        // Create the calendar table
        TableView<ObservableList<String>> calendarTable = createCalendarTable(LocalDate.now(), monthChoiceBox, yearChoiceBox);
        calendarPane.add(calendarTable, 0, 1);

        Scene calendarScene = new Scene(calendarPane, 600, 400);
        Stage calendarStage = new Stage();
        calendarStage.setTitle("Calendar");
        calendarStage.setScene(calendarScene);

        calendarStage.show();
    }

    private TableView<ObservableList<String>> createCalendarTable(LocalDate currentDate, ChoiceBox<String> monthChoiceBox, ChoiceBox<String> yearChoiceBox) {
        TableView<ObservableList<String>> calendarTable = new TableView<>();
        calendarTable.setEditable(false);

        // Add column headers (Weekdays)
        String[] weekdays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < weekdays.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(weekdays[i]);
            // Set cell value factory to extract values from the underlying data model
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(columnIndex)));
            calendarTable.getColumns().add(column);
        }

        // Add rows (Week numbers and days)
        int daysInMonth = currentDate.lengthOfMonth();
        int dayOfWeek = currentDate.withDayOfMonth(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int currentDay = 1;

        for (int i = 1; i <= 5; i++) {
            ObservableList<String> row = FXCollections.observableArrayList();

            for (int j = 1; j <= 7; j++) {
                if (currentDay <= daysInMonth || (i == 1 && j < dayOfWeek)) {
                    if (i == 1 && j < dayOfWeek) {
                        // Add empty cells for days before the first day of the month
                        row.add("");
                    } else {
                        // Add days of the month
                        row.add(String.valueOf(currentDay));
                        currentDay++;
                    }
                } else {
                    // Add empty cells for days after the last day of the month
                    row.add("");
                }
            }

            calendarTable.getItems().add(row);
        }

        // Update ChoiceBoxes when the "Update" button is clicked
        updateButton.setOnAction(event -> {
            int selectedMonth = monthChoiceBox.getSelectionModel().getSelectedIndex() + 1;
            int selectedYear = Integer.parseInt(yearChoiceBox.getValue());
            updateCalendarTable(LocalDate.of(selectedYear, selectedMonth, 1), calendarTable, monthChoiceBox, yearChoiceBox);
        });

        return calendarTable;
    }

    private void updateCalendarTable(LocalDate currentDate, TableView<ObservableList<String>> calendarTable, ChoiceBox<String> monthChoiceBox, ChoiceBox<String> yearChoiceBox) {
        // Clear the existing table
        calendarTable.getItems().clear();
        calendarTable.getColumns().clear();

        // Call createCalendarTable with the updated date
        createCalendarTable(currentDate, monthChoiceBox, yearChoiceBox, calendarTable);
    }

    private void createCalendarTable(LocalDate currentDate, ChoiceBox<String> monthChoiceBox, ChoiceBox<String> yearChoiceBox, TableView<ObservableList<String>> calendarTable) {
        // Add column headers (Weekdays)
        String[] weekdays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < weekdays.length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(weekdays[i]);
            // Set cell value factory to extract values from the underlying data model
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(columnIndex)));
            calendarTable.getColumns().add(column);
        }

        // Add rows (Week numbers and days)
        int daysInMonth = currentDate.lengthOfMonth();
        int dayOfWeek = currentDate.withDayOfMonth(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int currentDay = 1;

        for (int i = 1; i <= 5; i++) {
            ObservableList<String> row = FXCollections.observableArrayList();

            for (int j = 1; j <= 7; j++) {
                if (currentDay <= daysInMonth || (i == 1 && j < dayOfWeek)) {
                    if (i == 1 && j < dayOfWeek) {
                        // Add empty cells for days before the first day of the month
                        row.add("");
                    } else {
                        // Add days of the month
                        row.add(String.valueOf(currentDay));
                        currentDay++;
                    }
                } else {
                    // Add empty cells for days after the last day of the month
                    row.add("");
                }
            }

            calendarTable.getItems().add(row);
        }

        // Update ChoiceBoxes when the "Update" button is clicked
        updateButton.setOnAction(event -> {
            int selectedMonth = monthChoiceBox.getSelectionModel().getSelectedIndex() + 1;
            int selectedYear = Integer.parseInt(yearChoiceBox.getValue());
            updateCalendarTable(LocalDate.of(selectedYear, selectedMonth, 1), calendarTable, monthChoiceBox, yearChoiceBox);
        });
    }
}
