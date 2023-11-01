package com.project.cravehub.service;

import com.lowagie.text.DocumentException;
import com.project.cravehub.model.user.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class InvoiceService {

    @Autowired
    private TemplateEngine templateEngine;

    public byte[] generateInvoice(OrderItem orderItem)throws IOException, DocumentException {
        System.out.println("called-----invoiceService");
        Context context = new Context();
        context.setVariable("orderItem",orderItem);

        String htmlContent = templateEngine.process("invoice", context);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(os);
        os.close();

        return os.toByteArray();
    }
}
