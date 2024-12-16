package org.example.kurs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The CafeManagerApp class represents a JavaFX application for managing an anti-cafe.
 * Users can interact with tables, view current and archive statistics.
 */
public class CafeManagerApp extends Application {
    private static final Logger logger = LogManager.getLogger(CafeManagerApp.class); // Логгер для класса

    private CafeManager cafeManager;
    private Map<Integer, Button> tableButtons;

    /**
     * Entry point for the JavaFX application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the JavaFX application stage.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        cafeManager = new CafeManager(10, 5.0);
        tableButtons = new HashMap<>();

        primaryStage.setTitle("Анти-кафе");

        // Сетка для кнопок столиков
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Кнопки для столиков
        for (int i = 1; i <= cafeManager.getTables().size(); i++) {
            Button tableButton = new Button("Столик " + i);
            tableButton.setPrefSize(100, 50);

            int tableId = i;
            tableButton.setOnAction(e -> handleTableButtonPress(tableId));

            tableButtons.put(tableId, tableButton);
            gridPane.add(tableButton, (i - 1) % 5, (i - 1) / 5); // Распределение кнопок в сетке
        }

        // Кнопки для работы со статистикой
        Button viewCurrentStatsButton = new Button("Текущая статистика");
        Button viewArchiveStatsButton = new Button("Архивная статистика");

        viewCurrentStatsButton.setOnAction(e -> openStatsWindow("Текущая статистика", getCurrentStats()));
        viewArchiveStatsButton.setOnAction(e -> openStatsWindow("Архивная статистика", getArchiveStats()));

        // Основной макет
        VBox root = new VBox(15);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
                new Label("Нажмите на столик, чтобы занять его или освободить."),
                gridPane,
                viewCurrentStatsButton,
                viewArchiveStatsButton
        );

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handles the press of a table button, toggling its occupied status.
     *
     * @param tableId the ID of the table to toggle
     */
    private void handleTableButtonPress(int tableId) {
        Table table = cafeManager.getTables().get(tableId - 1); // Получаем объект столика по ID

        if (table.isOccupied()) {
            // Освободить столик
            cafeManager.freeTable(tableId);
            tableButtons.get(tableId).setStyle("");
            tableButtons.get(tableId).setText("Столик " + tableId);
            logger.info("Столик {} освобожден.", tableId); // Логируем освобождение столика
        } else {
            // Занять столик
            cafeManager.occupyTable(tableId);
            tableButtons.get(tableId).setStyle("-fx-background-color: red; -fx-text-fill: white;");
            tableButtons.get(tableId).setText("Занят");
            logger.info("Столик {} занят.", tableId); // Логируем занятие столика
        }
    }

    /**
     * Opens a new window displaying the provided statistics.
     *
     * @param title the title of the window
     * @param stats the statistics content to display
     */
    private void openStatsWindow(String title, String stats) {
        logger.info("Открытие окна с статистикой: {}", title); // Логируем открытие окна статистики

        Stage statsStage = new Stage();
        statsStage.setTitle(title);

        TextArea statsArea = new TextArea(stats);
        statsArea.setEditable(false);

        VBox layout = new VBox(statsArea);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 400);
        statsStage.setScene(scene);
        statsStage.show();
    }

    /**
     * Generates the current statistics for all tables.
     *
     * @return a string representation of the current statistics
     */
    private String getCurrentStats() {
        logger.debug("Генерация текущей статистики..."); // Логируем процесс генерации статистики

        StringBuilder stats = new StringBuilder("Текущая статистика:\n");
        double totalCurrentCost = 0.0;

        for (Table table : cafeManager.getTables()) {
            if (table.isOccupied()) {
                long minutes = java.time.Duration.between(table.getStartTime(), java.time.LocalDateTime.now()).toMinutes();
                double currentCost = minutes * cafeManager.getPricePerMinute();
                stats.append("Столик ").append(table.getId())
                        .append(": Занят (").append(minutes).append(" минут), Сумма к оплате: ")
                        .append(currentCost).append(" руб.\n");

                totalCurrentCost += currentCost;
            } else {
                stats.append("Столик ").append(table.getId()).append(": Свободен\n");
            }
        }

        stats.append("\nОбщая сумма, которую нужно заплатить всем гостям (если они покинут кафе): ")
                .append(totalCurrentCost).append(" руб.\n");

        logger.debug("Генерация текущей статистики завершена.");
        return stats.toString();
    }

    /**
     * Generates the archive statistics for all tables.
     *
     * @return a string representation of the archive statistics
     */
    private String getArchiveStats() {
        logger.debug("Генерация архивной статистики..."); // Логируем процесс генерации архивной статистики

        StringBuilder stats = new StringBuilder("Архивная статистика:\n");
        Table mostPopularTable = null;
        Table highestEarningTable = null;

        for (Table table : cafeManager.getTables()) {
            stats.append("Столик ").append(table.getId())
                    .append(": Всего минут занято: ").append(table.getTotalMinutesOccupied())
                    .append(", Заработано: ").append(table.getTotalEarnings()).append(" руб.\n");

            if (mostPopularTable == null || table.getTotalOccupancyCount() > mostPopularTable.getTotalOccupancyCount()) {
                mostPopularTable = table;
            }

            if (highestEarningTable == null || table.getTotalEarnings() > highestEarningTable.getTotalEarnings()) {
                highestEarningTable = table;
            }
        }

        if (mostPopularTable != null) {
            stats.append("\nСамый популярный столик: ").append(mostPopularTable.getId()).append("\n");
        }

        if (highestEarningTable != null) {
            stats.append("Столик с наибольшей выручкой: ").append(highestEarningTable.getId()).append("\n");
        }

        stats.append("Общая выручка: ").append(cafeManager.getTotalEarnings()).append(" руб.\n");

        logger.debug("Генерация архивной статистики завершена.");
        return stats.toString();
    }
}
