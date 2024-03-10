package br.com.bortolo.products.services;


import br.com.bortolo.products.exceptions.ProductException;
import br.com.bortolo.products.models.DTO.ProductDTO;
import br.com.bortolo.products.models.DTO.ProductRecordDTO;
import br.com.bortolo.products.models.ProductModel;
import br.com.bortolo.products.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public ProductModel saveProduct(ProductRecordDTO productRecordDTO) {

        UUID id = UUID.randomUUID();
        ProductModel productModel = new ProductModel();

        BeanUtils.copyProperties(productRecordDTO, productModel);
        productModel.setId(id);

        return productRepository.save(productModel);

    }

    public ProductModel findProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Produto não encontrado"));
    }

    public List<ProductModel> listAllProducts() {
        return productRepository.findAll();
    }

    public ProductModel updateProduct(UUID oldId, ProductRecordDTO product) {

        ProductModel productFound = findProductById(oldId);

        ProductDTO productDTO = new ProductDTO(product.name(), product.value());

        if (productDTO.getName().equals(productFound.getName())
                && productDTO.getValue().equals(productFound.getValue())) {
            throw new ProductException("Não há necessidade de atualizar dados identicos");
        }

        ProductModel productToUpdate = ProductModel.builder()
                .id(productFound.getId())
                .name(productDTO.getName())
                .value(productDTO.getValue())
                .build();

        return productRepository.save(productToUpdate);


    }

    public boolean deleteById(UUID id) {

        findProductById(id);
        productRepository.deleteById(id);

        return true;
    }


}
