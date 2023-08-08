package com.ghtk.model.DTO;

import java.util.Date;

public interface OrderDTO {
    int getId();
    String getCreator();
    Date getCreated_at();
    Date getUpdated_at();
    Long getTotal_cost();
}
