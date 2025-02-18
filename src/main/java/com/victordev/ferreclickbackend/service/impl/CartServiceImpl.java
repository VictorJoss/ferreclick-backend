package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.cart.AddToCartRequest;
import com.victordev.ferreclickbackend.DTOs.api.cart.AddedToCartResponse;
import com.victordev.ferreclickbackend.DTOs.api.cart.CartResponse;
import com.victordev.ferreclickbackend.persistence.entity.Cart;
import com.victordev.ferreclickbackend.persistence.entity.CartItem;
import com.victordev.ferreclickbackend.persistence.entity.Product;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.CartItemRepository;
import com.victordev.ferreclickbackend.persistence.repository.CartRepository;
import com.victordev.ferreclickbackend.persistence.repository.ProductRepository;
import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import com.victordev.ferreclickbackend.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de carrito que proporciona métodos para añadir productos al carrito, obtener el carrito
 * de un usuario, eliminar productos del carrito y vaciar el carrito.
 */
@Service
public class CartServiceImpl implements ICartService {

    /**
     * Repositorio de usuarios.
     */
    @Autowired
    private UserRepository userRepository;
    /**
     * Repositorio de productos.
     */
    @Autowired
    private ProductRepository productRepository;
    /**
     * Repositorio de carritos.
     */
    @Autowired
    private CartRepository cartRepository;
    /**
     * Repositorio de elementos de carrito.
     */
    @Autowired
    private CartItemRepository cartItemRepository;


    /**
     * Añade un producto al carrito de un usuario.
     * @param addToCartRequest Objeto que contiene la información necesaria para añadir un producto al carrito.
     * @return Objeto que contiene el producto añadido y la cantidad añadida.
     */
    @Transactional
    public AddedToCartResponse addProductToCart(AddToCartRequest addToCartRequest) {
        Long userId = addToCartRequest.getUserId();
        Long productId = addToCartRequest.getProductId();
        int quantity = addToCartRequest.getQuantity();

        Optional<User> user = userRepository.findById(userId);
        Optional<Product> product = productRepository.findById(productId);
        AddedToCartResponse addedtocarresponse = new AddedToCartResponse();

        if (!user.isPresent() && !product.isPresent()) {
            throw new RuntimeException("user or product doesn't exist");
        }

        Long cartId = getActiveCartId(user.get());
        Optional<Cart> cart = cartRepository.findById(cartId);

        if (!cart.isPresent()) {
            throw new RuntimeException("cart doesn't exist");
        }

        List<CartItem> cartItems = cart.get().getItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getId().equals(productId)) {
                cartItem.setQuantity(quantity);
                cart.get().setItems(cartItems);
                cartRepository.save(cart.get());

                addedtocarresponse.setProduct(product.get());
                addedtocarresponse.setQuantity(cartItem.getQuantity());
                return addedtocarresponse;
            }
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product.get());
        newCartItem.setCart(cart.get());
        newCartItem.setQuantity(quantity);
        cartItemRepository.save(newCartItem);

        cartItems.add(newCartItem);
        cart.get().setItems(cartItems);
        cartRepository.save(cart.get());

        addedtocarresponse.setProduct(product.get());
        addedtocarresponse.setQuantity(quantity);
        return addedtocarresponse;
    }

    /**
     * Obtiene el carrito de un usuario.
     * @param userId Identificador del usuario.
     * @return Objeto que contiene los productos del carrito.
     */
    public CartResponse getCartByUserId(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()){
            throw new RuntimeException("User doesn't exist");
        }
        Long cartId = getActiveCartId(user.get());
        return new CartResponse(cartItemRepository.findByCart_Id(cartId));
    }

    /**
     * Elimina un producto del carrito de un usuario.
     * @param userId Identificador del usuario.
     * @param productId Identificador del producto.
     */
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User doesn't exist");
        }

        Long cartId = getActiveCartId(user.get());
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart doesn't exist");
        }

        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()){
            throw new RuntimeException("Product doesn't exist");
        }

        cartItemRepository.deleteByProduct_Id(productId);
    }

    /**
     * Vacia el carrito de un usuario.
     * @param userId Identificador del usuario.
     */
    @Transactional
    public void clearCart(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User doesn't exist");
        }

        Long cartId = getActiveCartId(user.get());
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart doesn't exist");
        }
        cartItemRepository.deleteAllByCart_Id(cartId);
    }

    /**
     * Crea un carrito para un usuario.
     * @param user Usuario al que se le creará el carrito.
     * @return Carrito creado.
     */
    @Transactional
    public Cart createCart(User user){

        Optional<User> existingUser = userRepository.findById(user.getId());
        if (!existingUser.isPresent()) {
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
        if (!user.isPresent()) {
            throw new RuntimeException("User doesn't exist");
        }

        Long cartId = getActiveCartId(user.get());
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart doesn't exist");
        }

        try {
            cart.get().setCompleted(true);
            cartRepository.save(cart.get());

            Cart newCart = createCart(user.get());
            List<Cart> updateCarts = user.get().getCarts();
            updateCarts.add(newCart);
            user.get().setCarts(updateCarts);
            userRepository.save(user.get());
        }catch (Exception e){
            throw new RuntimeException("Error trying to process payment", e);
        }
    }

    /**
     * Obtiene el identificador del carrito activo de un usuario.
     * @param user Usuario del que se obtendrá el carrito.
     * @return Identificador del carrito activo.
     */
    private Long getActiveCartId(User user){
        return user.getCarts()
                .stream()
                .filter(cart -> !cart.isCompleted())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active cart found")).getId();
    }
}
