package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testCreateProductWithEmptyId_generatesUUID() {
        Product product = new Product();
        product.setProductId("");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isEmpty());
    }

    @Test
    void testCreateProductWithNullId_generatesUUID() {
        Product product = new Product();
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isEmpty());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProduct_success() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product deletedProduct = productRepository.delete("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertNotNull(deletedProduct);
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", deletedProduct.getProductId());

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProduct_notFound() {
        Product deletedProduct = productRepository.delete("non-existent-id");
        assertNull(deletedProduct);
    }

    @Test
    void testDeleteProduct_fromMultiple() {
        Product product1 = new Product();
        product1.setProductId("id-1");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("id-2");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        productRepository.delete("id-1");

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product remaining = productIterator.next();
        assertEquals("id-2", remaining.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindById_found() {
        Product product = new Product();
        product.setProductId("test-id-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product found = productRepository.findById("test-id-1");
        assertNotNull(found);
        assertEquals("test-id-1", found.getProductId());
        assertEquals("Sampo Cap Bambang", found.getProductName());
        assertEquals(100, found.getProductQuantity());
    }

    @Test
    void testFindById_notFound() {
        Product product = new Product();
        product.setProductId("existing-id");
        product.setProductName("Sampo");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product result = productRepository.findById("non-existent-id");
        assertNull(result);
    }

    @Test
    void testEditProduct_found() {
        Product product = new Product();
        product.setProductId("edit-id-1");
        product.setProductName("Sampo Lama");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updated = new Product();
        updated.setProductId("edit-id-1");
        updated.setProductName("Sampo Baru");
        updated.setProductQuantity(50);

        Product result = productRepository.edit(updated);
        assertNotNull(result);
        assertEquals("Sampo Baru", result.getProductName());
        assertEquals(50, result.getProductQuantity());
    }

    @Test
    void testEditProduct_notFound() {
        Product product = new Product();
        product.setProductId("existing-id");
        product.setProductName("Sampo");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updated = new Product();
        updated.setProductId("non-existent-id");
        updated.setProductName("Sampo Baru");
        updated.setProductQuantity(50);

        Product result = productRepository.edit(updated);
        assertNull(result);
    }

    @Test
    void testDeleteProduct_notFoundWithExistingProducts() {
        Product product = new Product();
        product.setProductId("existing-id");
        product.setProductName("Sampo");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product result = productRepository.delete("non-existent-id");
        assertNull(result);

        Iterator<Product> iterator = productRepository.findAll();
        assertTrue(iterator.hasNext());
    }
}