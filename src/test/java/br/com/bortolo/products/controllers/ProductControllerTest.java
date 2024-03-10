package br.com.bortolo.products.controllers;

import br.com.bortolo.products.handler.ProductExceptionHandler;
import br.com.bortolo.products.models.DTO.ProductDTO;
import br.com.bortolo.products.models.DTO.ProductRecordDTO;
import br.com.bortolo.products.models.ProductModel;
import br.com.bortolo.products.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService service;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {

        mock = MockitoAnnotations.openMocks(this);
        ProductController productController = new ProductController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ProductExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception{
        mock.close();
    }

    @Nested
    class ProductControllerTester {

        @Test
        void shouldCreateProductTest() throws Exception {

            var product = new ProductRecordDTO("test name", new BigDecimal(10));

            when(service.saveProduct(any(ProductRecordDTO.class))).thenReturn(productMaker());

            mockMvc.perform(
                    post("/product")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(product))
            ).andExpect(status().isCreated());

            verify(service, times(1)).saveProduct(any());

        }

        @Test
        void shouldListAllProductsTest() throws Exception {

            List<ProductModel> productModels = new ArrayList<>();

            productModels.add(productMaker());
            productModels.add(productMaker());

            when(service.listAllProducts()).thenReturn(productModels);

            mockMvc.perform(
                get("/product")

            )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(productModels.get(0).getId().toString()))
                .andExpect(jsonPath("$.[0].name").value(productModels.get(0).getName()))
                .andExpect(jsonPath("$.[0].value").value(productModels.get(0).getValue().toString()));

            verify(service, times(1)).listAllProducts();

        }

        @Test
        void shouldGetOneProductTest() throws Exception {

            var product = productMaker();

            when(service.findProductById(any(UUID.class))).thenReturn(product);

            mockMvc.perform(
                    get("/product/{id}", product.getId())
            )
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value(product.getId().toString()))
                    .andExpect(status().isOk());

            verify(service, times(1)).findProductById(any(UUID.class));

        }

        @Test
        void shouldUpdateOneProductTest() throws Exception {

            var product = new ProductRecordDTO("test name", new BigDecimal(10));

            var productModel = productMaker();

            when(service.updateProduct(any(UUID.class), any(ProductRecordDTO.class))).thenReturn(productModel);

            mockMvc.perform(
                    put("/product/{id}", productModel.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(product))
            )
                    .andDo(print())
                    .andExpect(status().isOk());

            verify(service, times(1)).updateProduct(any(), any());

        }

        @Test
        void shouldDeleteOneProduct() throws Exception {

            var id = UUID.randomUUID();

            when(service.deleteById(any(UUID.class))).thenReturn(true);

            mockMvc.perform(
                    delete("/product/{id}", id)
            )
                    .andDo(print())
                    .andExpect(status().isOk());

        }


    }




    private ProductModel productMaker() {


        return ProductModel.builder()
                .id(UUID.randomUUID())
                .name("This is a test product name")
                .value(new BigDecimal(10))
                .build();
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();

        om.registerModule(new JavaTimeModule());

        return om.writeValueAsString(obj);
    }

}