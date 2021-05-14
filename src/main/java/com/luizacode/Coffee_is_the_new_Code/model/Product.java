package com.luizacode.Coffee_is_the_new_Code.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class Product extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "title", unique = true, nullable = false)
	private String title;
	
	@Column(name = "avaliable_quantity", nullable = false)
    private Integer avaliableQuantity;
	
	@Column(name = "price", nullable = false)
    private BigDecimal price;
    
    @ManyToMany(mappedBy="products")
    private Set<WishList> wishLists;

    public Product (){}

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
        return avaliableQuantity == product.avaliableQuantity && title.equals(product.title) && price.equals(product.price) && wishLists.equals(product.wishLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, avaliableQuantity, price, wishLists);
    }
}