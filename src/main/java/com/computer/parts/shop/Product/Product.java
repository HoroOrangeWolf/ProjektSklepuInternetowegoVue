package com.computer.parts.shop.Product;

import com.computer.parts.shop.Category.Category;
import com.computer.parts.shop.Specification.Specification;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "product_generator_sequence"
  )
  @SequenceGenerator(
    name = "product_generator_sequence",
    sequenceName = "product_sequence",
    allocationSize = 1
  )
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false, precision = 19, scale = 2)
  private BigDecimal price;

  @Column(name = "description", length = 4096)
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

  public Product(
    String name,
    String producer,
    BigDecimal price,
    String description,
    Category category
  ) {
    this.name = name;
    this.producer = producer;
    this.price = price;
    this.description = description;
    this.category = category;
  }

  public Product(
    Long id,
    String name,
    BigDecimal price,
    String description,
    Category category
  ) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.description = description;
    this.category = category;
  }
}
