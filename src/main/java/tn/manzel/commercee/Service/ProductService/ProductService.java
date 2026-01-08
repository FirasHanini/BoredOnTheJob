package tn.manzel.commercee.Service.ProductService;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import tn.manzel.commercee.DAO.Entities.Mysql.User;
import tn.manzel.commercee.DAO.Repositories.Mysql.ProductRepository;

import java.time.LocalDateTime;
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

    @PreAuthorize("""
    @productSecurityService.isOwner(#id, authentication)
    
""")//&& this.isOwner(#id, authentication)
    public void delete(Long id) {

        Product product = findById(id);

        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());

        repository.save(product);
    }



}
