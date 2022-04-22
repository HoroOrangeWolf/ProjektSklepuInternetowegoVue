package com.computer.parts.shop.Attachment;

import com.computer.parts.shop.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;


@Getter
@Setter
@NoArgsConstructor
public class Attachment {

    private String blob;

    public Attachment(String data) {
        this.blob = data;
    }
}