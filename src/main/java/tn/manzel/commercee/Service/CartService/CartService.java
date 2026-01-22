package tn.manzel.commercee.Service.CartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import tn.manzel.commercee.DAO.Entities.Mysql.User;
import tn.manzel.commercee.DAO.Repositories.Mysql.CartItemRepository;
import tn.manzel.commercee.Service.ProductService.ProductService;
import tn.manzel.commercee.Service.UserService.UserManagementService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartRepository;
    private final UserManagementService userService;
    private final ProductService productService;

    public List<CartItem> getCart(String email) {
        return cartRepository.findByUserEmail(email);
    }

 //   @Transactional
    public CartItem addToCart(Long productId, int quantity, String email) {
        User user = userService.findByEmail(email);
        Product product = productService.findById(productId);

        if (product == null || user==null) {
            return null;
        }


        // Vérifier si le produit est déjà dans le panier
        Optional<CartItem> existingItem = cartRepository.findByUserEmailAndProductId(email, productId);

        if (existingItem.isEmpty()) {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            return cartRepository.save(newItem);

        }



        CartItem item = existingItem.get();
        item.setQuantity(item.getQuantity() + quantity);
        return cartRepository.save(item);

    }

    public void removeItem(Long itemId) {
        cartRepository.deleteById(itemId);
    }

    public void clearCartByUser(User user) {
   //     cartRepository.deleteByUser(user);
    }

    public void PayCart(){
        //to do
    }

    public void removeFromCart(String email, Set<Long> productsIds) {
        for (Long productId : productsIds) {
            Optional<CartItem> existingItem = cartRepository.findByUserEmailAndProductId(email, productId);
            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                cartRepository.delete(item);

            }


        }
    }

    public long calculateTotalPrice(String email) {
        List<CartItem> cartItems = cartRepository.findByUserEmail(email);
        long totalPrice = 0;

        for (CartItem item : cartItems) {
            totalPrice +=(long) item.getProduct().getPrice() * item.getQuantity();
        }

        return totalPrice;
    }


    public CartItem findById(Long id){
        return cartRepository.findById(id).orElse(null);
    }

    public boolean flushCart(String email) {
        User user = userService.findByEmail(email);

        if (user == null) {
            return false;
        }

        cartRepository.deleteByUser(user);
        return true;
    }
}
