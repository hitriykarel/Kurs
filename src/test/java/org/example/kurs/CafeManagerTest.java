package org.example.kurs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CafeManagerTest {

    private CafeManager cafeManager;

    @BeforeEach
    void setUp() {
        // Создаём новый экземпляр CafeManager с 3 столами и стоимостью 2.0 за минуту
        cafeManager = new CafeManager(3, 2.0);
    }

    @Test
    void testOccupyTable_Success() {
        // Проверяем, что стол успешно занимаетcя
        boolean result = cafeManager.occupyTable(1);
        assertTrue(result, "Стол должен быть успешно занят.");
    }

    @Test
    void testOccupyTable_AlreadyOccupied() {
        // Занимаем стол и проверяем, что повторная попытка не удалась
        cafeManager.occupyTable(1);
        boolean result = cafeManager.occupyTable(1);
        assertFalse(result, "Нельзя занять уже занятый стол.");
    }

    @Test
    void testFreeTable_Success() {
        // Занимаем и освобождаем стол
        cafeManager.occupyTable(1);
        boolean result = cafeManager.freeTable(1);
        assertTrue(result, "Стол должен быть успешно освобождён.");
    }

    @Test
    void testFreeTable_NotOccupied() {
        // Проверяем, что нельзя освободить незанятый стол
        boolean result = cafeManager.freeTable(1);
        assertFalse(result, "Нельзя освободить незанятый стол.");
    }

    @Test
    void testFindTableById_InvalidId() {
        // Проверяем, что при поиске стола с несуществующим ID возвращается null
        assertNull(cafeManager.getTables().stream().filter(table -> table.getId() == 99).findFirst().orElse(null),
                "Не должно быть стола с несуществующим ID.");
    }
}
