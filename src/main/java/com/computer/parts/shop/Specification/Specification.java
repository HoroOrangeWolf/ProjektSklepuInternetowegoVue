package com.computer.parts.shop.Specification;

import com.computer.parts.shop.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "specification")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specification_seq")
    @SequenceGenerator(name = "specification_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "keyName", nullable = false)
    private String keyName;

    @Column(name = "keyValue", nullable = false)
    private String keyValue;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public Specification(String name, String value, Product product) {
        this.keyName = name;
        this.keyValue = value;
        this.product = product;
    }
}