package tn.manzel.commercee.Service.CartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import tn.manzel.commercee.DAO.Entities.Mysql.User;
import tn.manzel.commercee.DAO.Repositories.Mysql.CartItemRepository;
import tn.manzel.commercee.DAO.Repositories.Mysql.ProductRepository;
import tn.manzel.commercee.DAO.Repositories.Mysql.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<CartItem> getCart(String email) {
        return cartRepository.findByUserEmail(email);
    }

    public CartItem addToCart(Long productId, int quantity, String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        // Vérifier si le produit est déjà dans le panier
        Optional<CartItem> existingItem = cartRepository.findByUserEmailAndProductId(email, productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            return cartRepository.save(newItem);
        }
    }

    public void removeItem(Long itemId) {
        cartRepository.deleteById(itemId);
    }
}
