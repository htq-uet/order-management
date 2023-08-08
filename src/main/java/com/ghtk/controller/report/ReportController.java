package com.ghtk.controller.report;

import com.ghtk.model.DTO.TotalOrderDTO;
import com.ghtk.request.report.DateRequest;
import com.ghtk.response.ReportResponse;
import com.ghtk.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/report")
    public ResponseEntity<ReportResponse> getReport(

            @RequestBody DateRequest dateRequest,
            HttpServletRequest request) {
        return ResponseEntity.ok(reportService.getReport(dateRequest, request));
    }
}
