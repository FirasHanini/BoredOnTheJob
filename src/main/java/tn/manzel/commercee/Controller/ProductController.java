package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService service;

    @Auditable(action = AuditAction.CREATE, entity = "Product")
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product, Authentication authentication) {
        return ResponseEntity.ok().body(service.create(product, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok().body(service.findAll());
    }
    @GetMapping(ApiEndpoints.productEndpoints.GET_BY_SELLER)
    public ResponseEntity<List<Product>> getProudctBySeller(@RequestParam String email){
        return ResponseEntity.ok().body(service.findBySellerId(email));
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
