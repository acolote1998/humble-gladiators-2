package com.github.acolote1998.humble_gladiators_2.characters.service;

import com.github.acolote1998.humble_gladiators_2.characters.model.Inventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    @Test
    void createBlankInventory_ShouldReturnInventoryWithEmptyLists() {
        // Act
        Inventory inventory = InventoryService.createBlankInventory();

        // Assert
        assertNotNull(inventory);
        assertNotNull(inventory.getArmors());
        assertNotNull(inventory.getBoots());
        assertNotNull(inventory.getConsumables());
        assertNotNull(inventory.getHelmets());
        assertNotNull(inventory.getShields());
        assertNotNull(inventory.getSpells());
        assertNotNull(inventory.getWeapons());
        assertTrue(inventory.getArmors().isEmpty());
        assertTrue(inventory.getBoots().isEmpty());
        assertTrue(inventory.getConsumables().isEmpty());
        assertTrue(inventory.getHelmets().isEmpty());
        assertTrue(inventory.getShields().isEmpty());
        assertTrue(inventory.getSpells().isEmpty());
        assertTrue(inventory.getWeapons().isEmpty());
        assertEquals(0, inventory.getGold());
    }

    @Test
    void createBlankInventory_ShouldReturnNewInstanceEachTime() {
        // Act
        Inventory inventory1 = InventoryService.createBlankInventory();
        Inventory inventory2 = InventoryService.createBlankInventory();

        // Assert
        assertNotSame(inventory1, inventory2);
        assertNotEquals(inventory1, inventory2);
    }
}





