package com.farmer_retailer.model;

import com.farmer_retailer.model.Order;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

public class OrderPdfGenerator {

    public static byte[] generateOrderPdf(Order order) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            document.add(new Paragraph("Order Details", titleFont));
            document.add(new Paragraph(" "));

            // Order details
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Buyer Name: " + order.getBuyer().getName()));
            document.add(new Paragraph("Buyer Email: " + order.getBuyer().getEmail()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Farmer Name: " + order.getSeller().getName()));
            document.add(new Paragraph("Farmer Email: " + order.getSeller().getEmail()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Product Name: " + order.getProduct().getName()));
            document.add(new Paragraph("Quantity: " + order.getQuantity()));
            document.add(new Paragraph("Price per unit: ₹" + order.getProduct().getPrice()));
            document.add(new Paragraph("Total Amount: ₹" + order.getAmount()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Order Status: " + order.getStatus()));

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
