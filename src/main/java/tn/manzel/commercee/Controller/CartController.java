package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.Service.CartService.CartService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiEndpoints.CART_BASE)
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getMyCart(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(auth.getName()));
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartItem> addProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication auth) {
        return ResponseEntity.ok(cartService.addToCart(productId, quantity, auth.getName()));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> remove(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
