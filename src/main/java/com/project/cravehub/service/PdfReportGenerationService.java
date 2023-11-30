package com.project.cravehub.service;

import com.lowagie.text.DocumentException;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.service.salesReportService.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfReportGenerationService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SalesReportService salesReportService;

    public byte[] generatePdfOrderReport(List<OrderItem> orderItemList, String start, String end, double totalRevenue, int totalSales) throws IOException, DocumentException {

        Context context = new Context();
        context.setVariable("orderItemList",orderItemList);
        context.setVariable("start",start);
        context.setVariable("end",end);
        context.setVariable("totalRevenue",totalRevenue);
        context.setVariable("totalSales",totalSales);


        String htmlContent = templateEngine.process("pdf", context);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(os);
        os.close();

        return os.toByteArray();
    }
}
