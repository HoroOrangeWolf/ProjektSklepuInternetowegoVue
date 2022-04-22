package com.computer.parts.shop.Product;

import com.computer.parts.shop.Attachment.Attachment;
import com.computer.parts.shop.Category.Category;
import com.computer.parts.shop.Product.Opinion.Opinion;
import com.computer.parts.shop.Specification.Specification;
import com.computer.parts.shop.Views.ProductView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator_sequence")
    @SequenceGenerator(name = "product_generator_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "count", nullable = false)
    private Integer count = 0;

    @Column(name = "description",length = 4096)
    private String description;

    @Column(name = "file_name")
    private String fileName = null;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "product", orphanRemoval = true)
    @BatchSize(size = 10)
    private List<Specification> specifications = new ArrayList<>();

    @Column(name = "producer", nullable = false)
    private String producer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name, String producer, BigDecimal price, Integer count, String description, Category category) {
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.count = count;
        this.description = description;
        this.category = category;
    }

    public Product(Long id, String name, BigDecimal price, Integer count, String description, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.description = description;
        this.category = category;
    }
}