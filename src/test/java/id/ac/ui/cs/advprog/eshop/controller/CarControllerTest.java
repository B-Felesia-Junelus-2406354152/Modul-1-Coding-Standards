package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockitoBean
   private CarService carService;

   @Test
   void testCreateCarPage() throws Exception {
       mockMvc.perform(get("/car/createCar"))
               .andExpect(status().isOk())
               .andExpect(view().name("CreateCar"))
               .andExpect(model().attributeExists("car"));
   }

   @Test
   void testCreateCarPost() throws Exception {
       mockMvc.perform(post("/car/createCar")
                       .param("carName", "Toyota Avanza")
                       .param("carColor", "Silver")
                       .param("carQuantity", "10"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("listCar"));

       verify(carService, times(1)).create(any(Car.class));
   }

   @Test
   void testCarListPage() throws Exception {
       List<Car> carList = new ArrayList<>();
       Car car = new Car();
       car.setCarId("car-id-1");
       car.setCarName("Toyota Avanza");
       car.setCarColor("Silver");
       car.setCarQuantity(10);
       carList.add(car);

       when(carService.findAll()).thenReturn(carList);

       mockMvc.perform(get("/car/listCar"))
               .andExpect(status().isOk())
               .andExpect(view().name("CarList"))
               .andExpect(model().attributeExists("cars"));

       verify(carService, times(1)).findAll();
   }

   @Test
   void testEditCarPage() throws Exception {
       Car car = new Car();
       car.setCarId("car-id-1");
       car.setCarName("Toyota Avanza");
       car.setCarColor("Silver");
       car.setCarQuantity(10);

       when(carService.findById("car-id-1")).thenReturn(car);

       mockMvc.perform(get("/car/editCar/car-id-1"))
               .andExpect(status().isOk())
               .andExpect(view().name("EditCar"))
               .andExpect(model().attributeExists("car"));

       verify(carService, times(1)).findById("car-id-1");
   }

   @Test
   void testEditCarPost() throws Exception {
       mockMvc.perform(post("/car/editCar")
                       .param("carId", "car-id-1")
                       .param("carName", "Toyota Innova")
                       .param("carColor", "Black")
                       .param("carQuantity", "20"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("listCar"));

       verify(carService, times(1)).update(eq("car-id-1"), any(Car.class));
   }

   @Test
   void testDeleteCar() throws Exception {
       mockMvc.perform(post("/car/deleteCar")
                       .param("carId", "car-id-1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("listCar"));

       verify(carService, times(1)).deleteCarById("car-id-1");
   }
}
