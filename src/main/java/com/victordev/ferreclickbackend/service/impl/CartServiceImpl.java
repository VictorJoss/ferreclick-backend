package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.dto.api.AddToCartRequest;
import com.victordev.ferreclickbackend.dto.api.AddedToCartResponse;
import com.victordev.ferreclickbackend.dto.api.CartResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

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

        Long cartId = user.get().getCart().getId();
        Optional<Cart> cart = cartRepository.findById(cartId);

        if (!cart.isPresent()) {
            throw new RuntimeException("cart doesn't exist");
        }

        List<CartItem> cartItems = cart.get().getItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getId().equals(productId)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
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

    public CartResponse getCartByUserId(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()){
            throw new RuntimeException("User doesn't exist");
        }
        Long cartId = user.get().getCart().getId();
        return new CartResponse(cartItemRepository.findByCart_Id(cartId));
    }

    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User doesn't exist");
        }

        Long cartId = user.get().getCart().getId();
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

    public Cart createCart(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }
}
