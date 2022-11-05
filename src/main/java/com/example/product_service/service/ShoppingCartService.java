package com.example.product_service.service;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.product_service.model.CustomerDetails;
import com.example.product_service.model.Product;
import com.example.product_service.model.ProductOrder;
import com.example.product_service.model.ShoppingCart;
import com.example.product_service.model.enumeration.OrderStatus;
import com.example.product_service.model.enumeration.PaymentMethod;
import com.example.product_service.repository.CustomerDetailsRepository;
import com.example.product_service.repository.ShoppingCartRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartService {
    
    private final Logger log = LoggerFactory.getLogger(ShoppingCartService.class);

    private final ShoppingCartRepository shoppingCartRepository;

    private final ProductOrderService productOrderService;
    
    private final CustomerDetailsRepository customerDetailsRepository;

    private final ProductService productService;

    /**
     * Get a shopping cart by id 
     * @param id of the shopping cart 
     * @return the entity 
     */
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findOne(Long id) { 
        log.debug("Request to get a product by id: {}", id);

        return shoppingCartRepository.findById(id);
    }

    /**
     * Delete the shopping cart by id 
     * @param id of the shopping cart 
     */
    public void deleteOne(Long id) { 
        log.debug("Request to delete a shopping cart by id: {}", id);

        shoppingCartRepository.deleteById(id);
    }

    /**
     * Get all the shopping carts 
     * 
     * @return the list of shopping carts 
     */
    @Transactional(readOnly = true)
    public List<ShoppingCart> getAll() { 
        log.debug("Request to get all product order");

        return shoppingCartRepository.findAll();
    }

    /**
     * Saves a shopping cart 
     * @param shoppingCart
     * @return the persisted entity
     */
    public ShoppingCart save(ShoppingCart shoppingCart) { 
        log.debug("Request to get all product order");

        return shoppingCartRepository.save(shoppingCart);
    }

    /**
     * Partially update a shopping cart  
     * @param shoppingCart the entity to update partially 
     * @return the persisted entity 
     */
    public Optional<ShoppingCart> partialUpdate(ShoppingCart shoppingCart) {
        log.debug("Request to partially update a shopping cart: {} ", shoppingCart);

        return shoppingCartRepository.findById(shoppingCart.getId())
            .map(existingCart -> {
                
                if (shoppingCart.getOrderStatus() != null) existingCart.setOrderStatus(shoppingCart.getOrderStatus());
                if (shoppingCart.getTotalPrice() != null) existingCart.setTotalPrice(shoppingCart.getTotalPrice());
                if (shoppingCart.getPlacedDate() != null) existingCart.setPlacedDate(shoppingCart.getPlacedDate());
                if (shoppingCart.getPaymentMethod() != null) existingCart.setPaymentMethod(shoppingCart.getPaymentMethod());
                if (shoppingCart.getPaymentReference() != null) existingCart.setPaymentReference(shoppingCart.getPaymentReference());
                if (shoppingCart.getPaymentModificationReference() != null) existingCart.setPaymentModificationReference(shoppingCart.getPaymentModificationReference());

                return existingCart;
            }).map(shoppingCartRepository::save);
    }

    @Transactional(readOnly = true)
    public List<ShoppingCart> findCartsByUser(final String user) { 
        return shoppingCartRepository.findAllByCustomerDetailsUserInfoAndStatusNot(user, OrderStatus.OPEN);
    }

    public ShoppingCart findActiveCartByUser(String user) { 
        Optional<ShoppingCart> userCart = shoppingCartRepository.findFirstByCustomerDetailsUserInfoAndStatusOrderByIdAsc(user, OrderStatus.OPEN);
        // Get the user cart or create a new one with default values 
        ShoppingCart activeCart = userCart.orElseGet(() -> { 
            Optional<CustomerDetails> customer = customerDetailsRepository.findOneByUserInfo(user);
            ShoppingCart cart = new ShoppingCart();
                cart.setOrderStatus(OrderStatus.OPEN);
                cart.setPaymentMethod(PaymentMethod.CREDIT_CARD);
                cart.setPlacedDate(Instant.now());
                cart.setTotalPrice(BigDecimal.ZERO);
                cart.setCustomerDetails(customer.get());
            return shoppingCartRepository.save(cart);
        });
        return activeCart;
    }
    /**
     * 
     * @param id
     * @param user
     * @return
     * @throws EntityNotFoundException
     */
    public ShoppingCart addProductForUser(Long id, String user) throws EntityNotFoundException { 
        // get the active cart
        ShoppingCart activeCart = findActiveCartByUser(user);
        // Search product in database
        Product product = productService.getProduct(id).orElseThrow(() -> new EntityNotFoundException("Product Not Foundd!"));
        ProductOrder order = new ProductOrder();
        List<ProductOrder> orders = activeCart
            .getOrders()
            .stream()
            .filter(productorder -> productorder.getId().equals(id))
            .collect(Collectors.toList()); 
        
        /**
         * If orders are empty, 
         *  - increase the quantity
         *  - recalculate total price 
         *  - update the shopping cart 
         */
        if (orders.isEmpty()) { 
            order.setQuantity(1);
            order.setTotalPrice(product.getPricePerUnit());
            order.setProduct(product);
            order.setCart(activeCart);
            activeCart.addCart(order);
        } else { 
            order = orders.get(0);
            order.setQuantity( order.getQuantity() + 1);
            order.setTotalPrice(product.getPricePerUnit().multiply(new BigDecimal(order.getQuantity())));
        }
        activeCart.calculateTotalPrice();
        productOrderService.save(order);
        return save(activeCart);
    }

    /**
     * 
     * @param productId
     * @param user
     * @return
     */
    public ShoppingCart removeProductFromUser(final Long productId, final String user) { 
        ShoppingCart activeCart = findActiveCartByUser(user);
        // Get the list of products, find the item to be removed from the list and then update the list
        List<ProductOrder> orders = activeCart
            .getOrders()
            .stream()
            .filter(productOrder -> productOrder.getId().equals(productId))
            .collect(Collectors.toList());

        if (orders.isEmpty()) throw new EntityNotFoundException("Product is not found in the user's cart!");
       
        else {
            ProductOrder order = orders.get(0);
            activeCart.removeOrder(order);
            productOrderService.deleteOne(order.getId());
        }
        return save(activeCart);
    }
    /**
     * 
     * @param user
     * @param paymentMethod
     * @param paymentRef
     * @param status
     * @return
     */
    public ShoppingCart updateCartWithPaymentByUserName(
        final String user, 
        final String paymentMethod, 
        final String paymentRef,
        final OrderStatus status
    ) { 
        ShoppingCart activeCart = findActiveCartByUser(user);
        return updateCart(activeCart, paymentMethod, paymentRef, status);
    }

    /**
     * 
     * @param id
     * @param paymentMethod
     * @param paymentRef
     * @param status
     * @return
     */
    public ShoppingCart updateCartWithPaymentByCartId(
        final Long id,
        final String paymentMethod, 
        final String paymentRef,
        final OrderStatus status
    ) {
        ShoppingCart activeCart = findOne(id).orElseThrow(() -> new EntityNotFoundException("Unable to find active carts using Cart Id"));
        return updateCart(activeCart, paymentMethod, paymentRef, status);
    }

    /**
     * Generic function to update cart
     * 
     * @param activeCart cart of the user
     * @param paymentMethod type of paymentMethod 
     * @param paymentRef referenceId as String
     * @param status OrderStatus
     * @return persisted entity
     */
    public ShoppingCart updateCart(
        ShoppingCart activeCart,
        final String paymentMethod, 
        final String paymentRef,
        final OrderStatus status
    ) {
        activeCart.setPaymentReference(paymentRef);
        activeCart.setOrderStatus(status);
        activeCart.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        return activeCart;
    }

    /**
     * Find the shopping cart by the payment modifcation reference 
     * @param paymentRef
     * @return
     */
    public Optional<ShoppingCart> findByPaymentModificationRef(final String paymentRef) { 
        return shoppingCartRepository.findOneByPaymentModificationReference(paymentRef);
    }



}
