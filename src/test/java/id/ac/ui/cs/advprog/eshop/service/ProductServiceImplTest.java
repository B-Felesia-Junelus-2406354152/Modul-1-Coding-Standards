package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("test-id");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.create(product)).thenReturn(product);
        Product result = productService.create(product);

        assertEquals(product, result);
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testDeleteProduct() {
        productService.delete("test-id");
        verify(productRepository, times(1)).delete("test-id");
    }

    @Test
    void testFindAll() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Iterator<Product> productIterator = productList.iterator();

        when(productRepository.findAll()).thenReturn(productIterator);
        List<Product> result = productService.findAll();

        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.findById("test-id")).thenReturn(product);
        Product result = productService.findById("test-id");

        assertEquals(product, result);
        verify(productRepository, times(1)).findById("test-id");
    }

    @Test
    void testEditProduct() {
        productService.edit(product);
        verify(productRepository, times(1)).edit(product);
    }
}
