package tn.manzel.commercee.Service.ProductService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.PostgresSql.Product;
import tn.manzel.commercee.DAO.Repositories.PostgresSql.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public Product create(Product product) {
        return repository.save(product);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }
    public Product findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
