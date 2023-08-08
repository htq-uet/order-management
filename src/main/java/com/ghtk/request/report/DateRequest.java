package com.ghtk.request.report;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DateRequest {
    private String year;
    private String month;
    private String day;
}
