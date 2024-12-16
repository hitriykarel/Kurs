package org.example.kurs;

import java.util.ArrayList;
import java.util.List;

/**
 * The CafeManager class manages tables in a cafe, tracks their occupancy, calculates earnings,
 * and provides statistical data about table usage and revenue.
 */
public class CafeManager {
    private final List<Table> tables;
    private final double pricePerMinute;
    private double totalEarnings;

    /**
     * Constructs a CafeManager with a specified number of tables and price per minute.
     *
     * @param tableCount     the number of tables in the cafe
     * @param pricePerMinute the price charged per minute of table occupancy
     */
    public CafeManager(int tableCount, double pricePerMinute) {
        this.tables = new ArrayList<>();
        for (int i = 1; i <= tableCount; i++) {
            tables.add(new Table(i));
        }
        this.pricePerMinute = pricePerMinute;
        this.totalEarnings = 0.0;
    }

    /**
     * Gets the list of tables in the cafe.
     *
     * @return a list of tables
     */
    public List<Table> getTables() {
        return tables;
    }

    /**
     * Marks a specified table as occupied.
     *
     * @param tableId the ID of the table to occupy
     * @return true if the table was successfully occupied, false otherwise
     */
    public boolean occupyTable(int tableId) {
        Table table = findTableById(tableId);
        if (table != null && !table.isOccupied()) {
            table.occupyTable();
            return true;
        }
        return false;
    }

    /**
     * Frees a specified table and calculates the earnings for its usage.
     *
     * @param tableId the ID of the table to free
     * @return true if the table was successfully freed, false otherwise
     */
    public boolean freeTable(int tableId) {
        Table table = findTableById(tableId);
        if (table != null && table.isOccupied()) {
            table.freeTable(pricePerMinute);
            totalEarnings += table.getTotalEarnings();
            return true;
        }
        return false;
    }

    /**
     * Gets the total earnings from all tables.
     *
     * @return the total earnings
     */
    public double getTotalEarnings() {
        return totalEarnings;
    }

    /**
     * Gets the price charged per minute for table occupancy.
     *
     * @return the price per minute
     */
    public double getPricePerMinute() {
        return pricePerMinute;
    }

    /**
     * Finds a table by its ID.
     *
     * @param id the ID of the table to find
     * @return the table with the specified ID, or null if no such table exists
     */
    private Table findTableById(int id) {
        return tables.stream().filter(table -> table.getId() == id).findFirst().orElse(null);
    }
}