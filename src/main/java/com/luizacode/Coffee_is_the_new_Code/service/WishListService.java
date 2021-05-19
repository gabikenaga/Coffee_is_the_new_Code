package com.luizacode.Coffee_is_the_new_Code.service;

import com.luizacode.Coffee_is_the_new_Code.dto.WishListInputDto;
import com.luizacode.Coffee_is_the_new_Code.dto.WishListOutputDto;
import com.luizacode.Coffee_is_the_new_Code.error.NegocioException;
import com.luizacode.Coffee_is_the_new_Code.error.ResourceNotFoundException;
import com.luizacode.Coffee_is_the_new_Code.model.Customer;
import com.luizacode.Coffee_is_the_new_Code.model.Product;
import com.luizacode.Coffee_is_the_new_Code.model.WishList;
import com.luizacode.Coffee_is_the_new_Code.repository.WishListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class WishListService {
    private static final Logger log = LoggerFactory.getLogger(WishListService.class);

    private static final Integer MAXIMUM_AMOUNT_OF_PRODUCTS = 20;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;
    
    public WishList findById(Long id) {
    	WishList wishList = wishListRepository.findById(id).orElse(null);

    	return wishList;
    }
    
    public WishList save(WishListInputDto wishListInputDto){
        Product product = productService.findById(wishListInputDto.getIdProduct());
        Customer customer = customerService.findById(wishListInputDto.getIdCustomer());
        WishList wishListModel = new WishList();

        if(Objects.isNull(customer.getWishList())){
            wishListModel.setProducts(new HashSet<Product>());
            customer.setWishList(wishListModel);
        }else {
        	wishListModel = customer.getWishList();
            validarQuantidadeProdutosWishlist(wishListModel);
        }
        log.info("Adding product to customer's wishlist");
        
        wishListModel.getProducts().add(product);
        //wishListModel.setCustomer(customer);
        
        wishListModel = wishListRepository.save(wishListModel);

//        if(Objects.isNull(customer.getWishList())) {
//        	customer.setWishList(wishListModel);
//        	customerService.registry(customer);
//        }
        log.info("Product was added to wishlist");
        return wishListModel;
    }

    public WishListOutputDto findAll(Long id){
        WishList wishList = findById(id);
        if(Objects.isNull(wishList)){
            throw new ResourceNotFoundException("Wishlist not found");
        }
        log.info("Searching all products on the wishlist id: "+id);
        WishListOutputDto wishListOutputDto = modelMapper.map(wishList,WishListOutputDto.class);
        return wishListOutputDto;
    }

    public void deleteProductOfWishlist(WishListInputDto wishListInputDto){
    	Customer customer = customerService.findById(wishListInputDto.getIdCustomer());

    	if(Objects.isNull(customer.getWishList())){
            throw new ResourceNotFoundException("The customer does not have a wishlist");
        }

    	WishList wishList = customer.getWishList();

        Product product = productService.findById(wishListInputDto.getIdProduct());
        log.info("Deleting product id: "+ product.getId()+" from wishlist");

        if(wishList.getProducts().contains(product)) {
            wishList.getProducts().remove(product);
            wishListRepository.save(wishList);
            log.info("Deleted product");
        }

        if(wishList.getProducts().size() == 0) {
            customer.setWishList(null);
            customerService.registry(customer);
            wishListRepository.delete(wishList);
        }
    }
    
    public boolean checkProductWishList(Long idCustomer, Long idProduct) {
    	Customer customer = customerService.findById(idCustomer);
    	boolean wishListHasProduct = false;
    	if(Objects.nonNull(customer.getWishList().getProducts())) {
    		Product product = productService.findById(idProduct);
    		log.info("Searching for product id: "+ product.getId() +" on wishlist id: "+customer.getWishList().getId());
    		wishListHasProduct = customer.getWishList().getProducts().contains(product);
    	}
    	
    	return wishListHasProduct;
    }
    public void validarQuantidadeProdutosWishlist(WishList wishList){
        if(wishList.getProducts().size() == MAXIMUM_AMOUNT_OF_PRODUCTS){
            log.warn("It is not possible to add more products to the customer's wishlist id: "+wishList.getCustomer().getId());
            throw new NegocioException("It is not possible to add more products to the customer's wishlist id: "+wishList.getCustomer().getId());
        }
    }
}

