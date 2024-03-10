package br.com.bortolo.products.services;

import br.com.bortolo.products.models.DTO.ProductDTO;
import br.com.bortolo.products.models.DTO.ProductRecordDTO;
import br.com.bortolo.products.models.ProductModel;
import br.com.bortolo.products.repositories.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    AutoCloseable mock;

    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }


    @Test
    public void shouldSaveProductTest() {

        var product = new ProductRecordDTO("Mock name", new BigDecimal(10));

        when(productRepository.save(any(ProductModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var productSaved = productService.saveProduct(product);

        Assertions.assertThat(productSaved).isInstanceOf(ProductModel.class).isNotNull();
        Assertions.assertThat(productSaved.getName()).isEqualTo(product.name());
        Assertions.assertThat(productSaved.getValue()).isEqualTo(product.value());
        Assertions.assertThat(productSaved.getId()).isNotNull();

        verify(productRepository, times(1)).save(any(ProductModel.class));


    }

    @Test
    public void shouldUpdateProductTest() {

        var id = UUID.randomUUID();
        var product = productMaker();
        product.setId(id);

        var newProduct = new ProductRecordDTO("new Mock name", new BigDecimal(20));

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        when(productRepository.save(any(ProductModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var productUpdated = productService.updateProduct(id, newProduct);

        Assertions.assertThat(productUpdated).isNotEqualTo(product);
        Assertions.assertThat(productUpdated.getName()).isEqualTo(newProduct.name());
        Assertions.assertThat(productUpdated.getValue()).isEqualTo(newProduct.value());

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(any(ProductModel.class));
    }

    @Test
    public void shouldFindOneProductByIdTest() {

        var id = UUID.randomUUID();
        var product = productMaker();
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        var productFound = productService.findProductById(id);

        Assertions.assertThat(productFound)
                .isNotNull()
                .isInstanceOf(ProductModel.class)
                .isEqualTo(product);


        verify(productRepository, times(1)).findById(any(UUID.class));

    }

    @Test
    public void shouldListAllProducts() {

        List<ProductModel> productModels = new ArrayList<>();

        productModels.add(productMaker());
        productModels.add(productMaker());

        when(productRepository.findAll()).thenReturn(productModels);

        var listProductsFound = productService.listAllProducts();

        Assertions.assertThat(listProductsFound).isNotNull().isEqualTo(productModels);

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void deleteProductTest() {

        var id = UUID.randomUUID();
        var product = productMaker();
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(id);

        var isRemoved = productService.deleteById(id);

        Assertions.assertThat(isRemoved).isTrue();

        verify(productRepository, times(1)).findById(any(UUID.class));
        verify(productRepository, times(1)).deleteById(any(UUID.class));

    }




    private ProductModel productMaker() {


        return ProductModel.builder()
                .id(UUID.randomUUID())
                .name("This is a test product name")
                .value(new BigDecimal(10))
                .build();
    }

}
