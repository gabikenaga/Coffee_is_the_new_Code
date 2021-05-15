package com.luizacode.Coffee_is_the_new_Code.model;

import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ApiIgnore
@Entity
@Table(name="product")
public class Product extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "title", unique = true)
    @NotEmpty
	private String title;
	
	@Column(name = "avaliable_quantity")
    private Integer avaliableQuantity;
	
	@Column(name = "price")
    private BigDecimal price;

    @ManyToMany
    @JoinTable(name="product_wishlist",
            joinColumns={@JoinColumn(name="produto_id")},
            inverseJoinColumns={@JoinColumn(name="wishilist_id")})
    private Set<WishList> wishLists;

    public Product (){
        wishLists = new HashSet<WishList>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAvaliableQuantity() {
        return avaliableQuantity;
    }

    public void setAvaliableQuantity(Integer avaliableQuantity) {
        this.avaliableQuantity = avaliableQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<WishList> getWishLists() {
        return wishLists;
    }

    public void setWishLists(Set<WishList> wishLists) {
        this.wishLists = wishLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Objects.equals(avaliableQuantity, product.avaliableQuantity) && Objects.equals(title, product.title) && Objects.equals(price, product.price)&& Objects.equals(wishLists, product.wishLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, avaliableQuantity, price, wishLists);
    }
}
