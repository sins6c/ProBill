import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class pdfGenerator {

	public static void makePdf(Object[] data, double total, int inv, double discountRate) {
		String invoice = "_invoice_.pdf";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); // 2016/11/16 12:08:43
		Document document = new Document();

		PdfWriter writer;

		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(invoice));

			document.open();
			Font f1 = new Font(Font.FontFamily.UNDEFINED, 18, Font.BOLD);
			Paragraph p1 = new Paragraph("THANGARAJA MALLIGAI", f1);

			Paragraph p2 = new Paragraph("Main Market Gulberg II Lahore");
			Paragraph p3 = new Paragraph("042-35712296\n\n\n");

			// Calculate totals
			double discountAmount = total * (discountRate / 100.0);
			double grandTotal = total - discountAmount;

			Paragraph pSub = new Paragraph("\n\nSubtotal: " + String.format("%.2f", total), f1);
			Paragraph pDisc = null;
			if (discountRate > 0) {
				pDisc = new Paragraph("Discount (" + discountRate + "%): -" + String.format("%.2f", discountAmount),
						f1);
			}
			Paragraph pGrand = new Paragraph("Grand Total: " + String.format("%.2f", grandTotal), f1);

			Paragraph p5 = new Paragraph("\nThank you for visiting us!!\nReturn/Exchange not possible with-out bill.");

			p1.setAlignment(Element.ALIGN_CENTER);
			p3.setAlignment(Element.ALIGN_CENTER);
			p2.setAlignment(Element.ALIGN_CENTER);
			document.add(p1);
			document.add(p2);
			document.add(p3);
			Font f2 = new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL);
			Phrase phrase = new Phrase("Time/Date: " + dateFormat.format(date), f2);
			PdfContentByte canvas = writer.getDirectContent();
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 40, 740, 0);
			Phrase invNo = new Phrase("Invoice No. " + inv, f2);
			PdfContentByte canv = writer.getDirectContent();
			ColumnText.showTextAligned(canv, Element.ALIGN_LEFT, invNo, 510, 785, 0);
			Image image;
			try {
				// Try to find logo locally or relative
				String logoPath = "logo.png";
				// Fallback to absolute if needed, but relative is better for portability if in
				// root
				File f = new File(logoPath);
				if (!f.exists()) {
					// code originally had "E:\\XAMPP\\htdocs\\logo.png", we might want to keep it
					// or try project root
					// Assuming user has logo.png in project root based on file list
					logoPath = "c:\\Users\\Asus\\OneDrive\\Documents\\projectSet\\Billing-System with invoice\\logo.png";
				}
				image = Image.getInstance(logoPath);
				image.setAbsolutePosition(10f, 730f);
				document.add(image);

			} catch (Exception e) {
				// Quietly fail for logo if not found to avoid crashing
				// e.printStackTrace();
			}

			PdfPTable table = new PdfPTable(5);
			float[] columnWidths = new float[] { 15f, 30f, 10f, 10f, 15f };
			table.setWidths(columnWidths);
			table.addCell("Product ID");

			table.addCell("Item Detail");
			table.addCell("Unit Price");
			table.addCell("Quantity");
			table.addCell("Total Price");
			for (int aw = 0; aw < data.length; aw++) {
				table.addCell(data[aw] + "");
			}
			document.add(table);

			// Add totals
			document.add(pSub);
			if (pDisc != null) {
				document.add(pDisc);
			}
			document.add(pGrand);

			document.add(p5);
			// writer.close();
			document.close();
			sendIn2printer(invoice);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void makePdf2(Object[] data, double total) {
		String sale = "_sale_.pdf";
		Document document = new Document();

		PdfWriter writer;

		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(sale));

			document.open();
			Font f1 = new Font(Font.FontFamily.UNDEFINED, 18, Font.BOLD);
			Paragraph p1 = new Paragraph("THANGARAJA MALLIGAI", f1);

			Paragraph p2 = new Paragraph("Main Market Gulberg II Lahore");
			Paragraph p3 = new Paragraph("042-35712296\n\n\n");
			Paragraph p4 = new Paragraph("\n\nGrand Total: " + total, f1);

			p1.setAlignment(Element.ALIGN_CENTER);
			p3.setAlignment(Element.ALIGN_CENTER);
			p2.setAlignment(Element.ALIGN_CENTER);
			document.add(p1);
			document.add(p2);
			document.add(p3);

			PdfPTable table = new PdfPTable(4);
			table.addCell("Date");
			table.addCell("Product ID");
			table.addCell("Company");
			table.addCell("Sale");
			for (int aw = 0; aw < data.length; aw++) {
				table.addCell(data[aw] + "");
			}
			document.add(table);
			document.add(p4);

			// writer.close();
			document.close();
			sendIn2printer(sale);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendIn2printer(String file) { // The desktop api can help calling other applications in our
														// machine
														// and also many other features...
		Desktop desktop = Desktop.getDesktop();
		try {
			// desktop.print(new File("DocXfile.docx"));
			desktop.print(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
