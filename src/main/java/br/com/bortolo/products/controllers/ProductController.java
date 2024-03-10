package br.com.bortolo.products.controllers;


import br.com.bortolo.products.models.DTO.ProductRecordDTO;
import br.com.bortolo.products.models.ProductModel;
import br.com.bortolo.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

    final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(productRecordDTO));

    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductModel>> listAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.listAllProducts());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductModel> findOneProduct(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductById(id));
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductModel> updateProduct(
            @PathVariable(value = "id") UUID id,
            @RequestBody @Valid ProductRecordDTO productRecordDTO) {

        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(id, productRecordDTO));

    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(value = "id") UUID id) {

        boolean hasDeleted = productService.deleteById(id);

        if (!hasDeleted) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Não foi possivel deletar, tente novamente.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com Sucesso");
    }

}
