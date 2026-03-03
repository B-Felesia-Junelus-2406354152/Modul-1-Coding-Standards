package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    Car car;

    @BeforeEach
    void setUp() {
        this.car = new Car();
        this.car.setCarId("car-id-123");
        this.car.setCarName("Toyota Avanza");
        this.car.setCarColor("Silver");
        this.car.setCarQuantity(10);
    }

    @Test
    void testGetCarId() {
        assertEquals("car-id-123", this.car.getCarId());
    }

    @Test
    void testGetCarName() {
        assertEquals("Toyota Avanza", this.car.getCarName());
    }

    @Test
    void testGetCarColor() {
        assertEquals("Silver", this.car.getCarColor());
    }

    @Test
    void testGetCarQuantity() {
        assertEquals(10, this.car.getCarQuantity());
    }
}
