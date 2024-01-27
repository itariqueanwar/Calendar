import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Calendar extends Application {
    private static Map<LocalDate, String> eventsMap = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java Calendar");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        DatePicker datePicker = new DatePicker();
        TextArea eventTextArea = new TextArea();
        Button addButton = new Button("Add Event");
        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);

        // Add event button action
        addButton.setOnAction(e -> {
            LocalDate date = datePicker.getValue();
            String eventDescription = eventTextArea.getText();

            if (date != null && !eventDescription.isEmpty()) {
                eventsMap.put(date, eventDescription);
                eventTextArea.clear();
                updateDisplayArea(displayArea);
            }
        });

        grid.add(new Label("Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Event:"), 0, 1);
        grid.add(eventTextArea, 1, 1);
        grid.add(addButton, 1, 2);
        grid.add(new Label("Events:"), 0, 3);
        grid.add(displayArea, 1, 3);

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private static void updateDisplayArea(TextArea displayArea) {
        displayArea.clear();

        for (Map.Entry<LocalDate, String> entry : eventsMap.entrySet()) {
            displayArea.appendText(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }
}
