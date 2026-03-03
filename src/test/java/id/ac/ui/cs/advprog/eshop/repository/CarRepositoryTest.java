package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {

    @InjectMocks
    CarRepository carRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Car car = new Car();
        car.setCarId("car-id-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
        carRepository.create(car);

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car savedCar = carIterator.next();
        assertEquals(car.getCarId(), savedCar.getCarId());
        assertEquals(car.getCarName(), savedCar.getCarName());
        assertEquals(car.getCarColor(), savedCar.getCarColor());
        assertEquals(car.getCarQuantity(), savedCar.getCarQuantity());
    }

    @Test
    void testCreateCarWithNullId_generatesUUID() {
        Car car = new Car();
        car.setCarName("Honda Jazz");
        car.setCarColor("Red");
        car.setCarQuantity(5);
        carRepository.create(car);

        assertNotNull(car.getCarId());
        assertFalse(car.getCarId().isEmpty());
    }

    @Test
    void testCreateCarWithExistingId_keepsId() {
        Car car = new Car();
        car.setCarId("existing-id");
        car.setCarName("Honda Jazz");
        car.setCarColor("Red");
        car.setCarQuantity(5);
        carRepository.create(car);

        assertEquals("existing-id", car.getCarId());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Car> carIterator = carRepository.findAll();
        assertFalse(carIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneCar() {
        Car car1 = new Car();
        car1.setCarId("car-id-1");
        car1.setCarName("Toyota Avanza");
        car1.setCarColor("Silver");
        car1.setCarQuantity(10);
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("car-id-2");
        car2.setCarName("Honda Jazz");
        car2.setCarColor("Red");
        car2.setCarQuantity(5);
        carRepository.create(car2);

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car savedCar = carIterator.next();
        assertEquals(car1.getCarId(), savedCar.getCarId());
        savedCar = carIterator.next();
        assertEquals(car2.getCarId(), savedCar.getCarId());
        assertFalse(carIterator.hasNext());
    }

    @Test
    void testFindById_found() {
        Car car = new Car();
        car.setCarId("car-id-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car found = carRepository.findById("car-id-1");
        assertNotNull(found);
        assertEquals("car-id-1", found.getCarId());
        assertEquals("Toyota Avanza", found.getCarName());
    }

    @Test
    void testFindById_notFound() {
        Car car = new Car();
        car.setCarId("car-id-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car result = carRepository.findById("non-existent-id");
        assertNull(result);
    }

    @Test
    void testUpdate_found() {
        Car car = new Car();
        car.setCarId("car-id-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Toyota Innova");
        updatedCar.setCarColor("Black");
        updatedCar.setCarQuantity(20);

        Car result = carRepository.update("car-id-1", updatedCar);
        assertNotNull(result);
        assertEquals("Toyota Innova", result.getCarName());
        assertEquals("Black", result.getCarColor());
        assertEquals(20, result.getCarQuantity());
    }

    @Test
    void testUpdate_notFound() {
        Car car = new Car();
        car.setCarId("car-id-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda Jazz");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(5);

        Car result = carRepository.update("non-existent-id", updatedCar);
        assertNull(result);
    }

    @Test
    void testDelete_found() {
        Car car = new Car();
        car.setCarId("car-id-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
        carRepository.create(car);

        carRepository.delete("car-id-1");

        Iterator<Car> carIterator = carRepository.findAll();
        assertFalse(carIterator.hasNext());
    }

    @Test
    void testDelete_notFound() {
        Car car = new Car();
        car.setCarId("car-id-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
        carRepository.create(car);

        carRepository.delete("non-existent-id");

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
    }

    @Test
    void testDelete_fromMultiple() {
        Car car1 = new Car();
        car1.setCarId("car-id-1");
        car1.setCarName("Toyota Avanza");
        car1.setCarColor("Silver");
        car1.setCarQuantity(10);
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("car-id-2");
        car2.setCarName("Honda Jazz");
        car2.setCarColor("Red");
        car2.setCarQuantity(5);
        carRepository.create(car2);

        carRepository.delete("car-id-1");

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car remaining = carIterator.next();
        assertEquals("car-id-2", remaining.getCarId());
        assertFalse(carIterator.hasNext());
    }
}
