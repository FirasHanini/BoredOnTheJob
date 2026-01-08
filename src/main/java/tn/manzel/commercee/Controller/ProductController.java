package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditAction;
import tn.manzel.commercee.Service.AuditService.Auditable;
import tn.manzel.commercee.Service.ProductService.ProductService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiEndpoints.PRODUCT_BASE)
public class ProductController {

    private final ProductService service;

    @Auditable(action = AuditAction.CREATE, entity = "Product")
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok().body(service.create(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        Product product = service.findById(id);
        if (product == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(product);
    }

    @Auditable(action = AuditAction.DELETE, entity = "Product")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().body("Product deleted");
    }

}
