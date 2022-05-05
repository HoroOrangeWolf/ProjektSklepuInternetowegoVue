package com.computer.parts.shop.Attachment;

import com.computer.parts.shop.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Blob;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Attachment {

  private String blob;

  public Attachment(String data) {
    this.blob = data;
  }
}
