package br.com.bortolo.products.repositories;


import br.com.bortolo.products.models.ProductModel;
import org.junit.jupiter.api.AfterEach;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;


class ProductRepositoryTests {


    @Mock()
    private ProductRepository productRepository;

    AutoCloseable openMocks;

    @BeforeEach()
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }


    @Test()
    void shouldSaveProductTest() {

        ProductModel product = this.productMaker();

        when(productRepository.save(any(ProductModel.class))).thenReturn(product);

        var productSaved = productRepository.save(product);

        Assertions.assertThat(productSaved)
                .isNotNull()
                .isInstanceOf(ProductModel.class)
                .isEqualTo(product);

        verify(productRepository, times(1)).save(any(ProductModel.class));


    }

    @Test()
    void shouldGetProductTest() {

        ProductModel product = this.productMaker();

        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(product));

        var productFound = productRepository.findById(product.getId());

        Assertions.assertThat(productFound).isPresent().containsSame(product);

        productFound.ifPresent(prd -> {
            Assertions.assertThat(prd.getId()).isEqualTo(product.getId());
            Assertions.assertThat(prd.getName()).isEqualTo(product.getName());
            Assertions.assertThat(prd.getValue()).isEqualTo(product.getValue());
        });

        verify(productRepository, times(1)).findById(any(UUID.class));

    }

    @Test()
    void shouldDeleteProductTest() {

        var id = UUID.randomUUID();

        doNothing().when(productRepository).deleteById(any(UUID.class));

        productRepository.deleteById(id);

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
