/////working
////package com.farmer_retailer.util;
////
////import com.farmer_retailer.model.Order;
////import com.lowagie.text.*;
////import com.lowagie.text.pdf.PdfWriter;
////
////import java.io.ByteArrayOutputStream;
////
////public class OrderPdfGenerator {
////
////    public static byte[] generateOrderPdf(Order order) {
////
////        try {
////            ByteArrayOutputStream baos = new ByteArrayOutputStream();
////            Document document = new Document(PageSize.A4);
////            PdfWriter.getInstance(document, baos);
////
////            document.open();
////
////            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
////            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
////
////            Paragraph title = new Paragraph("Order Details", titleFont);
////            title.setAlignment(Element.ALIGN_CENTER);
////            document.add(title);
////
////            document.add(new Paragraph(" "));
////
////            document.add(new Paragraph("Order ID: " + order.getId(), normalFont));
////            document.add(new Paragraph("Order Date: " + order.getOrderDate(), normalFont));
////            document.add(new Paragraph("Order Status: " + order.getStatus(), normalFont));
////
////            document.add(new Paragraph(" "));
////            document.add(new Paragraph("Buyer Name: " + order.getBuyer().getName(), normalFont));
////            document.add(new Paragraph("Buyer Email: " + order.getBuyer().getEmail(), normalFont));
////
////            document.add(new Paragraph(" "));
////            document.add(new Paragraph("Farmer Name: " + order.getSeller().getName(), normalFont));
////            document.add(new Paragraph("Farmer Email: " + order.getSeller().getEmail(), normalFont));
////
////            document.add(new Paragraph(" "));
////            document.add(new Paragraph("Product Name: " + order.getProduct().getName(), normalFont));
////            document.add(new Paragraph("Quantity: " + order.getQuantity(), normalFont));
////            document.add(new Paragraph("Price per unit: ₹" + order.getProduct().getPrice(), normalFont));
////            document.add(new Paragraph("Total Amount: ₹" + order.getAmount(), normalFont));
////
////            document.close();
////            return baos.toByteArray();
////
////        } catch (Exception e) {
////            e.printStackTrace();
////            return null;
////        }
////    }
////}
//
//
//package com.farmer_retailer.util;
//
//import com.farmer_retailer.model.Order;
//import com.lowagie.text.*;
//import com.lowagie.text.pdf.PdfWriter;
//
//import java.io.ByteArrayOutputStream;
//
//public class OrderPdfGenerator {
//
//    public static byte[] generateOrderPdf(Order order) {
//
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            Document document = new Document();
//
//            PdfWriter.getInstance(document, out);
//            document.open();
//
//            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
//            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
//
//            document.add(new Paragraph("Order Invoice", titleFont));
//            document.add(new Paragraph(" "));
//            document.add(new Paragraph("Order ID: " + order.getId(), textFont));
//            document.add(new Paragraph("Buyer: " + order.getBuyer().getName(), textFont));
//            document.add(new Paragraph("Seller: " + order.getSeller().getName(), textFont));
//            document.add(new Paragraph("Product: " + order.getProduct().getName(), textFont));
//            document.add(new Paragraph("Quantity: " + order.getQuantity(), textFont));
//            document.add(new Paragraph("Amount: ₹" + order.getAmount(), textFont));
//            document.add(new Paragraph("Status: " + order.getStatus(), textFont));
//            document.add(new Paragraph("Order Date: " + order.getOrderDate(), textFont));
//
//            document.close();
//            return out.toByteArray();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
package com.farmer_retailer.util;

import com.farmer_retailer.model.Order;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

public class OrderPdfGenerator {

    // Custom colors
    private static final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private static final Color LIGHT_BLUE = new Color(240, 248, 255);
    private static final Color GOLD = new Color(255, 215, 0);
    private static final Color DARK_GRAY = new Color(64, 64, 64);
    private static final Color LIGHT_GRAY = new Color(220, 220, 220);

    public static byte[] generateOrderPdf(Order order) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();

            // Add header with logo
            addHeader(document, writer);

            // Add invoice title
            addInvoiceTitle(document, order);

            // Add spacing
            document.add(new Paragraph(" "));

            // Add party details (From/To)
            addPartyDetails(document, order);

            // Add spacing
            document.add(new Paragraph(" "));

            // Add product table
            addProductTable(document, order);

            // Add spacing
            document.add(new Paragraph(" "));

            // Add payment summary
            addPaymentSummary(document, order);

            // Add spacing
            document.add(new Paragraph(" "));

            // Add footer
            addFooter(document);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            System.err.println("❌ Error generating PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static void addHeader(Document document, PdfWriter writer) throws Exception {
        // Create header table
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 3});

        // Logo cell with default image
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setPadding(5);

        // Create default logo
        Image logo = createDefaultLogo(writer);
        logo.scaleToFit(80, 80);
        logoCell.addElement(logo);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // Company info cell
        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setPadding(5);

        Font companyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, PRIMARY_GREEN);
        Font taglineFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
        Font contactFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);

        Paragraph companyName = new Paragraph("Farmer Retailer System", companyFont);
        Paragraph tagline = new Paragraph("Connecting Farmers & Retailers", taglineFont);
        Paragraph contact = new Paragraph("Email: support@farmerretailer.com | Phone: +91-1234567890", contactFont);

        infoCell.addElement(companyName);
        infoCell.addElement(tagline);
        infoCell.addElement(contact);

        headerTable.addCell(logoCell);
        headerTable.addCell(infoCell);

        document.add(headerTable);

        // Add horizontal line
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        line.setSpacingBefore(5);
        line.setSpacingAfter(10);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(Rectangle.NO_BORDER);
        lineCell.setBorderWidthBottom(2);
        lineCell.setBorderColorBottom(PRIMARY_GREEN);
        line.addCell(lineCell);
        document.add(line);
    }

    private static void addInvoiceTitle(Document document, Order order) throws Exception {
        PdfPTable titleTable = new PdfPTable(2);
        titleTable.setWidthPercentage(100);
        titleTable.setWidths(new float[]{1, 1});

        // Left side - Invoice title
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(Rectangle.NO_BORDER);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, PRIMARY_GREEN);
        Font invoiceNumFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.GRAY);

        Paragraph title = new Paragraph("ORDER INVOICE", titleFont);
        Paragraph invoiceNum = new Paragraph("Invoice #INV-" + order.getId(), invoiceNumFont);

        leftCell.addElement(title);
        leftCell.addElement(invoiceNum);

        // Right side - Date and Status
        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
        Font statusFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, new Color(0, 128, 0));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        String formattedDate = order.getOrderDate().format(formatter);

        Paragraph datePara = new Paragraph("Date: " + formattedDate, dateFont);
        datePara.setAlignment(Element.ALIGN_RIGHT);

        Paragraph statusPara = new Paragraph("Status: " + order.getStatus(), statusFont);
        statusPara.setAlignment(Element.ALIGN_RIGHT);

        rightCell.addElement(datePara);
        rightCell.addElement(statusPara);

        titleTable.addCell(leftCell);
        titleTable.addCell(rightCell);

        document.add(titleTable);
    }

    private static void addPartyDetails(Document document, Order order) throws Exception {
        PdfPTable detailsTable = new PdfPTable(2);
        detailsTable.setWidthPercentage(100);
        detailsTable.setWidths(new float[]{1, 1});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, PRIMARY_GREEN);
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        // Seller (FROM) details
        PdfPCell sellerCell = new PdfPCell();
        sellerCell.setBackgroundColor(LIGHT_BLUE);
        sellerCell.setPadding(15);

        Paragraph fromHeader = new Paragraph("FROM (Farmer)", headerFont);
        fromHeader.setSpacingAfter(5);

        sellerCell.addElement(fromHeader);
        sellerCell.addElement(new Paragraph(order.getSeller().getName(), nameFont));
        sellerCell.addElement(new Paragraph("Email: " + order.getSeller().getEmail(), infoFont));
//        sellerCell.addElement(new Paragraph("Phone: " + order.getSeller().getPhone(), infoFont));

        // Buyer (TO) details
        PdfPCell buyerCell = new PdfPCell();
        buyerCell.setBackgroundColor(LIGHT_BLUE);
        buyerCell.setPadding(15);

        Paragraph toHeader = new Paragraph("TO (Retailer)", headerFont);
        toHeader.setSpacingAfter(5);

        buyerCell.addElement(toHeader);
        buyerCell.addElement(new Paragraph(order.getBuyer().getName(), nameFont));
        buyerCell.addElement(new Paragraph("Email: " + order.getBuyer().getEmail(), infoFont));
//        buyerCell.addElement(new Paragraph("Phone: " + order.getBuyer().getPhone(), infoFont));

        detailsTable.addCell(sellerCell);
        detailsTable.addCell(buyerCell);

        document.add(detailsTable);
    }

    private static void addProductTable(Document document, Order order) throws Exception {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, PRIMARY_GREEN);
        Paragraph sectionTitle = new Paragraph("Product Details", sectionFont);
        sectionTitle.setSpacingAfter(10);
        document.add(sectionTitle);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 1, 1, 1.5f});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        // Header row
        String[] headers = {"Product Name", "Unit", "Quantity", "Price/Unit", "Total Amount"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(PRIMARY_GREEN);
            cell.setPadding(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Data row
        PdfPCell nameCell = new PdfPCell(new Phrase(order.getProduct().getName(), cellFont));
        nameCell.setPadding(10);
        nameCell.setBackgroundColor(Color.WHITE);
        table.addCell(nameCell);

//       PdfPCell unitCell = new PdfPCell(new Phrase(order.getProduct().getUnit(), cellFont));
//        unitCell.setPadding(10);
//        unitCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        unitCell.setBackgroundColor(Color.WHITE);
//        table.addCell(unitCell);

        PdfPCell qtyCell = new PdfPCell(new Phrase(String.valueOf(order.getQuantity()), cellFont));
        qtyCell.setPadding(10);
        qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        qtyCell.setBackgroundColor(Color.WHITE);
        table.addCell(qtyCell);

        PdfPCell priceCell = new PdfPCell(new Phrase("₹" + String.format("%.2f", order.getProduct().getPrice()), cellFont));
        priceCell.setPadding(10);
        priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        priceCell.setBackgroundColor(Color.WHITE);
        table.addCell(priceCell);

        PdfPCell totalCell = new PdfPCell(new Phrase("₹" + String.format("%.2f", order.getAmount()), totalFont));
        totalCell.setPadding(10);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setBackgroundColor(Color.WHITE);
        table.addCell(totalCell);

        document.add(table);
    }

    private static void addPaymentSummary(Document document, Order order) throws Exception {
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(60);
        summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.setWidths(new float[]{3, 1});

        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font totalLabelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font totalValueFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.WHITE);

        // Subtotal
        addSummaryRow(summaryTable, "Subtotal:", "₹" + String.format("%.2f", order.getAmount()), labelFont, false);

        // Tax
        double tax = 0.0; // Modify if needed
        addSummaryRow(summaryTable, "Tax:", "₹" + String.format("%.2f", tax), labelFont, false);

        // Total row
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL AMOUNT:", totalLabelFont));
        totalLabelCell.setBackgroundColor(PRIMARY_GREEN);
        totalLabelCell.setPadding(10);
        totalLabelCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell totalValueCell = new PdfPCell(new Phrase("₹" + String.format("%.2f", order.getAmount()), totalValueFont));
        totalValueCell.setBackgroundColor(PRIMARY_GREEN);
        totalValueCell.setPadding(10);
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValueCell.setBorder(Rectangle.NO_BORDER);

        summaryTable.addCell(totalLabelCell);
        summaryTable.addCell(totalValueCell);

        document.add(summaryTable);
    }

    private static void addSummaryRow(PdfPTable table, String label, String value, Font font, boolean isBold) {
        Font usedFont = isBold ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10) : font;

        PdfPCell labelCell = new PdfPCell(new Phrase(label, usedFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, usedFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private static void addFooter(Document document) throws Exception {
        // Add horizontal line
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        line.setSpacingBefore(5);
        line.setSpacingAfter(10);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(Rectangle.NO_BORDER);
        lineCell.setBorderWidthTop(1);
        lineCell.setBorderColorTop(LIGHT_GRAY);
        line.addCell(lineCell);
        document.add(line);

        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);
        Paragraph footer = new Paragraph();
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.add(new Phrase("Thank you for your business!\n", footerFont));
        footer.add(new Phrase("This is a system-generated invoice and does not require a signature.\n", footerFont));
        footer.add(new Phrase("For queries, contact: support@farmerretailer.com", footerFont));

        document.add(footer);
    }

    /**
     * Creates a default logo image using PdfTemplate
     */
    private static Image createDefaultLogo(PdfWriter writer) throws Exception {
        float width = 80;
        float height = 80;

        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate template = cb.createTemplate(width, height);

        // Draw background circle
        template.setColorFill(PRIMARY_GREEN);
        template.circle(width/2, height/2, width/2);
        template.fill();

        // Draw wheat/plant icon in gold
        template.setColorFill(GOLD);

        // Stem
        template.rectangle(width/2 - 2.5f, height * 0.3f, 5, height * 0.4f);
        template.fill();

        // Wheat grains (simplified shapes)
        float centerX = width/2;
        float topY = height * 0.7f;

        // Top grain
        template.circle(centerX, topY, 6);
        template.fill();

        // Left grain
        template.circle(centerX - 8, topY - 8, 5);
        template.fill();

        // Right grain
        template.circle(centerX + 8, topY - 8, 5);
        template.fill();

        // Middle grains
        template.circle(centerX - 6, topY - 16, 4);
        template.fill();
        template.circle(centerX + 6, topY - 16, 4);
        template.fill();

        // Leaves
        template.setColorFill(new Color(50, 180, 50));
        template.ellipse(centerX - 15, height * 0.4f, centerX - 5, height * 0.55f);
        template.fill();
        template.ellipse(centerX + 5, height * 0.4f, centerX + 15, height * 0.55f);
        template.fill();

        return Image.getInstance(template);
    }
}