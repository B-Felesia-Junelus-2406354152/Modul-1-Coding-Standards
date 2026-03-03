package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepositoryInterface carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-id-1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
    }

    @Test
    void testCreateCar() {
        when(carRepository.create(car)).thenReturn(car);
        Car result = carService.create(car);

        assertEquals(car, result);
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testFindAll() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        Iterator<Car> carIterator = carList.iterator();

        when(carRepository.findAll()).thenReturn(carIterator);
        List<Car> result = carService.findAll();

        assertEquals(1, result.size());
        assertEquals(car, result.get(0));
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(carRepository.findById("car-id-1")).thenReturn(car);
        Car result = carService.findById("car-id-1");

        assertEquals(car, result);
        verify(carRepository, times(1)).findById("car-id-1");
    }

    @Test
    void testUpdate() {
        carService.update("car-id-1", car);
        verify(carRepository, times(1)).update("car-id-1", car);
    }

    @Test
    void testDeleteCarById() {
        carService.deleteCarById("car-id-1");
        verify(carRepository, times(1)).delete("car-id-1");
    }
}
