package com.project.cravehub.controller.admin;

import com.itextpdf.html2pdf.HtmlConverter;
import com.lowagie.text.DocumentException;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.service.PdfReportGenerationService;
import com.project.cravehub.service.salesReportService.SalesReportService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;

@Controller
@RequestMapping("/admin")
public class SalesReportController {

    @Autowired
    private SalesReportService salesReportService;

    @Autowired
    private PdfReportGenerationService pdfReportGenerationService;

    @GetMapping("/salesReport")
    public String showSalesReportGet(Model model,@RequestParam(name = "start", required = false) String startDate,
                                     @RequestParam(name = "end", required = false) String endDate) {

        if(endDate != null && startDate !=null)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            List<OrderItem> orderItemList = salesReportService.generateSalesReport(start,end);
            model.addAttribute("orderItemList",orderItemList);
        }
        else {
            List<OrderItem> orderItemList = salesReportService.getSalesReport();
            model.addAttribute("orderItemList",orderItemList);
        }
        Map<Product, Integer> profitData = salesReportService.getSalesProfit();
        Map<String, String> salesData = salesReportService.getSalesDataForLastNPeriods("day=");
        model.addAttribute("salesData", salesData);
        model.addAttribute("profitData",profitData);
        return "salesReport";
    }

    @PostMapping("/salesReport")
    @ResponseBody
    public Map<String, String> showSalesReportPost(@RequestBody String period) {
        Map<String, String> salesData = salesReportService.getSalesDataForLastNPeriods(period);
        return salesData;
    }

    @GetMapping("/downloadExcel")
    public void downloadSalesReport(HttpServletResponse response,@RequestParam(name = "start", required = false) String start,
                                    @RequestParam(name = "end", required = false) String end) throws IOException {

        List<OrderItem> orderItemList = new ArrayList<>();
        if(end != null && start !=null) {
            LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE);
            orderItemList = salesReportService.generateSalesReport(startDate,endDate);
        }
        else {
             orderItemList = salesReportService.getSalesReport();
        }
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sales Report");

        // Creating heading of the table
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Order Id");
        headerRow.createCell(1).setCellValue("Products-Qty");
        headerRow.createCell(2).setCellValue("Date");
        headerRow.createCell(3).setCellValue("Customer");
        headerRow.createCell(4).setCellValue("Total Amount");


        int rowNum = 1;
        // adding data's to the row
        for (OrderItem orderItem : orderItemList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(orderItem.getOrder().getOrderId());
            row.createCell(1).setCellValue(orderItem.getProduct().getProductName() + "-" + orderItem.getItemCount());
            row.createCell(2).setCellValue(orderItem.getOrder().getOrderDate());
            row.createCell(3).setCellValue(orderItem.getOrder().getUser().getFirstName());
            row.createCell(4).setCellValue(orderItem.getProduct().getPrice() * orderItem.getItemCount());
        }

        // Set content type and headers for the response
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=SalesReport.xls");

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


    @GetMapping("/downloadPdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestParam(name = "start", required = false) String start,
                                              @RequestParam(name = "end", required = false) String end) throws IOException, DocumentException {


        List<OrderItem> orderItemList = new ArrayList<>();
        double totalRevenue =0.0;
        int totalSales =0;

        if(end != null && start !=null) {
            LocalDate dateFrom = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate dateTo = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE);
             orderItemList = salesReportService.generateSalesReport(dateFrom,dateTo);
          totalRevenue = salesReportService.totalRevenue(dateFrom , dateTo);
          totalSales = salesReportService.totalSales(dateFrom,dateTo);

        }
        else {
             orderItemList = salesReportService.getSalesReport();
             totalRevenue = salesReportService.getTotalRevenue();
             totalSales = salesReportService.getTotalSales();
        }

        byte[] pdfBytes = pdfReportGenerationService.generatePdfOrderReport(orderItemList,start,end,totalRevenue,totalSales);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "order_report.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @GetMapping("/pdf")
    public String pgf()
    {
        return "pdf";
    }

}
