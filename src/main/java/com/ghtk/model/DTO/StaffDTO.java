package com.ghtk.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public interface StaffDTO {
    int getId();
    String getName();
    String getPhone();
    String getUsername();
    String getStatus();

}
