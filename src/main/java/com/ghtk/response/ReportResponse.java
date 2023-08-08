package com.ghtk.response;

import com.ghtk.model.DTO.TotalOrderDTO;
import com.ghtk.model.DTO.TotalProductDTO;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    TotalOrderDTO totalOrder;
    TotalProductDTO totalProduct;
}
