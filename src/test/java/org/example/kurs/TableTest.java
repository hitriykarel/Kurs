package org.example.kurs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    private Table table;

    @BeforeEach
    void setUp() {
        // Инициализация объекта Table с ID 1
        table = new Table(1);
    }

    @Test
    void testGetId() {
        // Проверка корректности ID
        assertEquals(1, table.getId(), "ID стола должен быть равен 1.");
    }

    @Test
    void testIsOccupied_InitiallyFalse() {
        // Убедимся, что стол изначально не занят
        assertFalse(table.isOccupied(), "Стол должен быть свободен при создании.");
    }

    @Test
    void testOccupyTable() {
        // Занимаем стол
        table.occupyTable();
        assertTrue(table.isOccupied(), "Стол должен быть занят после вызова occupyTable.");
        assertNotNull(table.getStartTime(), "Время начала занятия должно быть установлено.");
    }

    @Test
    void testOccupyTable_AlreadyOccupied() {
        // Занимаем стол и пытаемся снова
        table.occupyTable();
        LocalDateTime startTimeBefore = table.getStartTime();
        table.occupyTable(); // Вызов не должен менять состояние
        assertEquals(startTimeBefore, table.getStartTime(), "Повторный вызов occupyTable не должен изменить время начала.");
    }

    @Test
    void testFreeTable_Success() {
        // Занимаем стол
        table.occupyTable();
        table.freeTable(2.5); // Освобождаем с ценой 2.5 за минуту
        assertFalse(table.isOccupied(), "Стол должен быть свободен после вызова freeTable.");
        assertEquals(1, table.getTotalOccupancyCount(), "Количество использований стола должно увеличиться.");
    }

    @Test
    void testFreeTable_CalculatesEarningsAndTime() throws InterruptedException {
        // Занимаем стол
        table.occupyTable();
        Thread.sleep(60001); // Ждём 1 минуту для симуляции занятости
        table.freeTable(2.5); // Освобождаем стол
        assertTrue(table.getTotalMinutesOccupied() >= 1, "Должно быть учтено не менее 1 минуты занятости.");
        assertTrue(table.getTotalEarnings() > 0, "Заработок должен быть больше 0.");
    }

    @Test
    void testFreeTable_NotOccupied() {
        // Попытка освободить не занятый стол
        table.freeTable(2.5);
        assertEquals(0, table.getTotalMinutesOccupied(), "Время занятости не должно изменяться для свободного стола.");
        assertEquals(0.0, table.getTotalEarnings(), "Заработок не должен увеличиваться для свободного стола.");
    }

    @Test
    void testGetTotalMinutesOccupied_InitiallyZero() {
        // Проверка начального значения времени занятости
        assertEquals(0, table.getTotalMinutesOccupied(), "Общее время занятости должно быть равно 0 при создании.");
    }

    @Test
    void testGetTotalEarnings_InitiallyZero() {
        // Проверка начального значения заработка
        assertEquals(0.0, table.getTotalEarnings(), "Общий заработок должен быть равен 0 при создании.");
    }

    @Test
    void testGetStartTime_InitiallyNull() {
        // Проверка, что время начала изначально null
        assertNull(table.getStartTime(), "Время начала должно быть null для нового стола.");
    }

    @Test
    void testGetTotalOccupancyCount_InitiallyZero() {
        // Проверка начального значения счётчика использования
        assertEquals(0, table.getTotalOccupancyCount(), "Счётчик использования должен быть равен 0 при создании.");
    }
}