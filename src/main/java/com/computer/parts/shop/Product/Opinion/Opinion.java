package com.computer.parts.shop.Product.Opinion;

import com.computer.parts.shop.Order.Order;
import com.computer.parts.shop.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "opinion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opinion_seq")
    @SequenceGenerator(name = "opinion_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "stars", nullable = false)
    private Short stars;

    @Column(name = "text", length = 1024)
    private String text;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

}