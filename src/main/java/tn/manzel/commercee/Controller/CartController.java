package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.Service.CartService.CartService;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiEndpoints.CART_BASE)
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService service;

    @GetMapping
    public ResponseEntity<List<CartItem>> getMyCart(Authentication auth) {
        return ResponseEntity.ok(service.getCart(auth.getName()));
    }

    @PostMapping()
    public ResponseEntity<CartItem> addProduct(
            @RequestParam Long productId,
                @RequestParam(defaultValue = "1") int quantity,
            Authentication auth) {
        return ResponseEntity.ok(service.addToCart(productId, quantity, auth.getName()));
    }

    @DeleteMapping()
    public ResponseEntity<Void>removeFromCart(Authentication auth,@RequestParam Set<Long> productsIds){
        service.removeFromCart(auth.getName(), productsIds);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/{itemId}")
//    public ResponseEntity<Void> remove(@PathVariable Long itemId) {
//        service.removeItem(itemId);
//        return ResponseEntity.noContent().build();
//    }
}
