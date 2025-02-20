package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.cart.AddToCartRequest;
import com.victordev.ferreclickbackend.DTOs.api.cart.AddedToCartResponse;
import com.victordev.ferreclickbackend.DTOs.api.cart.CartResponse;
import com.victordev.ferreclickbackend.persistence.entity.Cart;
import com.victordev.ferreclickbackend.persistence.entity.CartItem;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.CartRepository;
import com.victordev.ferreclickbackend.persistence.repository.ProductRepository;
import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import com.victordev.ferreclickbackend.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Implementación del servicio de carrito que proporciona métodos para añadir productos al carrito, obtener el carrito
 * de un usuario, eliminar productos del carrito y vaciar el carrito.
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    /**
     * Repositorio de usuarios.
     */
    private final UserRepository userRepository;
    /**
     * Repositorio de productos.
     */
    private final ProductRepository productRepository;
    /**
     * Repositorio de carritos.
     */
    private final CartRepository cartRepository;

    /**
     * Añade un producto al carrito de un usuario.
     * @param addToCartRequest Objeto que contiene la información necesaria para añadir un producto al carrito.
     * @return Objeto que contiene el producto añadido y la cantidad añadida.
     */
    @Transactional
    public AddedToCartResponse addProductToCart(AddToCartRequest addToCartRequest) {

        Optional<User> user = userRepository.findById(addToCartRequest.getUserId());
        Optional<Product> product = productRepository.findById(addToCartRequest.getProductId());

        if (user.isPresent() && product.isPresent()) {

            Cart cart = getActiveCart(user.get());

            Optional<CartItem> existingCartItem = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getId().equals(addToCartRequest.getProductId()))
                    .findFirst();

            if (existingCartItem.isPresent()) {
                existingCartItem.get().setQuantity(addToCartRequest.getQuantity());
                return AddedToCartResponse.builder()
                        .product(product.get())
                        .quantity(addToCartRequest.getQuantity())
                        .build();
            }

            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product.get());
            newCartItem.setCart(cart);
            newCartItem.setQuantity(addToCartRequest.getQuantity());

            cart.getItems().add(newCartItem);

            return AddedToCartResponse.builder()
                    .product(product.get())
                    .quantity(addToCartRequest.getQuantity())
                    .build();
        }
        throw new RuntimeException("user or product doesn't exist");
    }

    /**
     * Obtiene el carrito de un usuario.
     * @param userId Identificador del usuario.
     * @return Objeto que contiene los productos del carrito.
     */
    public CartResponse getCartByUserId(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new RuntimeException("User doesn't exist");
        }

        return CartResponse.builder()
                .cartItems(getActiveCart(user.get()).getItems())
                .build();
    }

    /**
     * Elimina un producto del carrito de un usuario.
     * @param userId Identificador del usuario.
     * @param productId Identificador del producto.
     */
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User doesn't exist");
        }

        Cart cart = getActiveCart(user.get());

        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()){
            throw new RuntimeException("Product doesn't exist");
        }

        cart.getItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
    }

    /**
     * Vacia el carrito de un usuario.
     * @param userId Identificador del usuario.
     */
    @Transactional
    public void clearCart(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User doesn't exist");
        }

        Cart cart = getActiveCart(user.get());

        cart.getItems().clear();
    }

    /**
     * Crea un carrito para un usuario.
     * @param user Usuario al que se le creará el carrito.
     * @return Carrito creado.
     */
    @Transactional
    public Cart createCart(User user){

        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User doesn't exist");
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.setCreatedDate(LocalDateTime.now());
        cart.setCompleted(false);

        return cartRepository.save(cart);
    }

    /**
     * Procesa el pago de un carrito.
     * @param userId Identificador del usuario.
     */
    @Transactional
    public void processPaymentCart(Long userId){

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new RuntimeException("User doesn't exist");
        }

        Cart cart = getActiveCart(user.get());

        try {
            cart.setCompleted(true);

            Cart newCart = createCart(user.get());
            user.get().getCarts().add(newCart);
        }catch (Exception e){
            throw new RuntimeException("Error trying to process payment", e);
        }
    }

    /**
     * Obtiene el identificador del carrito activo de un usuario.
     * @param user Usuario del que se obtendrá el carrito.
     * @return Identificador del carrito activo.
     */
    private Cart getActiveCart(User user){
        return user.getCarts()
                .stream()
                .filter(cart -> !cart.isCompleted())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active cart found"));
    }
}
