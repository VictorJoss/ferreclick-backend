package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.cart.AddToCartRequest;
import com.victordev.ferreclickbackend.DTOs.api.cart.AddedToCartResponse;
import com.victordev.ferreclickbackend.DTOs.api.cart.CartResponse;
import com.victordev.ferreclickbackend.exceptions.cart.CartNotFoundException;
import com.victordev.ferreclickbackend.exceptions.cart.PaymentProcessingException;
import com.victordev.ferreclickbackend.exceptions.product.ProductNotFoundException;
import com.victordev.ferreclickbackend.exceptions.security.IllegalUserException;
import com.victordev.ferreclickbackend.persistence.entity.Cart;
import com.victordev.ferreclickbackend.persistence.entity.CartItem;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.CartRepository;
import com.victordev.ferreclickbackend.service.IAuthService;
import com.victordev.ferreclickbackend.service.ICartService;
import com.victordev.ferreclickbackend.service.IEntityFinderService;
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
     * Repositorio de carritos.
     */
    private final CartRepository cartRepository;
    /**
     * Servicio de búsqueda de entidades.
     */
    private final IEntityFinderService entityFinderService;
    /**
     * Servicio de autenticación.
     */
    private final IAuthService authService;

    /**
     * Añade un producto al carrito de un usuario.
     * @param addToCartRequest Objeto que contiene la información necesaria para añadir un producto al carrito.
     * @return Objeto que contiene el producto añadido y la cantidad añadida.
     */
    @Transactional
    public AddedToCartResponse addProductToCart(AddToCartRequest addToCartRequest) {

        User user = entityFinderService.getUserById(addToCartRequest.getUserId());

        if(authService.isLegalUser(user.getId())){
            throw new IllegalUserException("User is not authorized to perform this action.");
        }

        Product product = entityFinderService.getProductById(addToCartRequest.getProductId());
        Cart cart = getActiveCart(user);

        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(addToCartRequest.getProductId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            existingCartItem.get().setQuantity(addToCartRequest.getQuantity());
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(addToCartRequest.getQuantity());
            cart.getItems().add(newCartItem);
        }

        return AddedToCartResponse.builder()
                .product(product)
                .quantity(addToCartRequest.getQuantity())
                .build();
    }

    /**
     * Obtiene el carrito de un usuario.
     * @param userId Identificador del usuario.
     * @return Objeto que contiene los productos del carrito.
     */
    public CartResponse getCartByUserId(Long userId){
        User user = entityFinderService.getUserById(userId);

        if(authService.isLegalUser(user.getId())){
            throw new IllegalUserException("User is not authorized to remove products from this cart.");
        }

        return CartResponse.builder()
                .cartItems(getActiveCart(user).getItems())
                .build();
    }

    /**
     * Elimina un producto del carrito de un usuario.
     * @param userId Identificador del usuario.
     * @param productId Identificador del producto.
     */
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        User user = entityFinderService.getUserById(userId);

        if(authService.isLegalUser(user.getId())){
            throw new IllegalUserException("User is not authorized to remove products from this cart.");
        }

        entityFinderService.getProductById(productId);
        Cart cart = getActiveCart(user);

        boolean removed = cart.getItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));

        if (!removed) {
            throw new ProductNotFoundException("Product not found in the cart.");
        }
    }

    /**
     * Vacia el carrito de un usuario.
     * @param userId Identificador del usuario.
     */
    @Transactional
    public void clearCart(Long userId){
        User user = entityFinderService.getUserById(userId);

        if(authService.isLegalUser(user.getId())){
            throw new IllegalUserException("User is not authorized to remove products from this cart.");
        }

        Cart cart = getActiveCart(user);

        if (cart.getItems().isEmpty()) {
            throw new ProductNotFoundException("Cart is already empty.");
        }

        cart.getItems().clear();
    }

    /**
     * Crea un carrito para un usuario.
     * @param user Usuario al que se le creará el carrito.
     * @return Carrito creado.
     */
    @Transactional
    public Cart createCart(User user){
        User userFound = entityFinderService.getUserById(user.getId());

        Cart cart = new Cart();
        cart.setUser(userFound);
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
        User user = entityFinderService.getUserById(userId);

        if(authService.isLegalUser(user.getId())){
            throw new IllegalUserException("User is not authorized to remove products from this cart.");
        }

        Cart cart = getActiveCart(user);

        if (cart.getItems().isEmpty()) {
            throw new ProductNotFoundException("Cannot process payment. Cart is empty.");
        }

        try {
            cart.setCompleted(true);
            Cart newCart = createCart(user);
            user.getCarts().add(newCart);
        } catch (Exception e) {
            throw new PaymentProcessingException("Error occurred while processing the payment.", e);
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
                .orElseThrow(() -> new CartNotFoundException("No active cart found for the user."));
    }
}