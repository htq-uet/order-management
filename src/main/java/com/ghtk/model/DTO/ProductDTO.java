package com.ghtk.model.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ghtk.model.Order;
import com.ghtk.model.Shop;
import com.ghtk.model.Staff;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductDTO {

    int getID();
    String getName();
    int getPrice();
    LocalDateTime getCreated_at();
    LocalDateTime getUpdated_at();


}
