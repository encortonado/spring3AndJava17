package br.com.bortolo.products.services;


import br.com.bortolo.products.exceptions.ProductException;
import br.com.bortolo.products.models.DTO.ProductDTO;
import br.com.bortolo.products.models.ProductModel;
import br.com.bortolo.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public ProductModel saveProduct(ProductDTO product) {

        UUID id = UUID.randomUUID();
        ProductModel productModel = ProductModel.builder()
                .id(id)
                .name(product.getName())
                .value(product.getValue())
                .build();

        return productRepository.save(productModel);

    }

    public ProductModel findProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Produto não encontrado"));
    }

    public ProductModel updateProduct(UUID oldId, ProductDTO product) {

        ProductModel productFound = findProductById(oldId);

        if (product.getName().equals(productFound.getName())
                && product.getValue().equals(productFound.getValue())) {
            throw new ProductException("Não há necessidade de atualizar dados identicos");
        }

        ProductModel productToUpdate = ProductModel.builder()
                .id(productFound.getId())
                .name(product.getName())
                .value(product.getValue())
                .build();

        return productRepository.save(productToUpdate);


    }

    public boolean deleteById(UUID id) {

        findProductById(id);
        productRepository.deleteById(id);

        return true;
    }


}
