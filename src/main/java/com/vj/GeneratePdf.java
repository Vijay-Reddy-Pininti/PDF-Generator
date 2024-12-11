package com.vj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class GeneratePdf 
{
	public static byte[] generatePdf(String data) throws IOException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
		Document document = new Document(pdfDoc, PageSize.A4);
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		document.add(new Paragraph("ACCOUNT STATEMENT").setFont(font).setTextAlignment(TextAlignment.CENTER));
		final JSONObject jsonObject = new JSONObject(data);
		
		Table accountTable = new Table(2);
		accountTable.setWidth(UnitValue.createPercentValue(100));
		
		Table billTable = new Table(2);
		billTable.setWidth(UnitValue.createPercentValue(100));
		
		Table accountChargesTable = new Table(6);
		accountChargesTable.setWidth(UnitValue.createPercentValue(100));
		
		Table serviceChargesTable = new Table(7);
		serviceChargesTable.setWidth(UnitValue.createPercentValue(100));
		
		Table usageChargesTable = new Table(11);
		usageChargesTable.setWidth(UnitValue.createPercentValue(100));
		
		final JSONArray accountCharacteristics = jsonObject.getJSONArray("accountCharacteristics");
		final int n = accountCharacteristics.length();
		for (int i = 0; i < n; i++) 
		{
			final JSONObject account = accountCharacteristics.getJSONObject(i);
			if (i == 0)
			{
				Cell cell = new Cell(1, 2)
						.add(new Paragraph("Account Details"))
						.setFont(font).setFontColor(ColorConstants.WHITE)
						.setBackgroundColor(ColorConstants.BLACK)
						.setTextAlignment(TextAlignment.CENTER);
				accountTable.addCell(cell);
			}
			accountTable.addCell(new Cell().add(new Paragraph((String) account.get("name"))));
			accountTable.addCell(new Cell().add(new Paragraph((String) account.get("value"))));
		}
		
		final JSONArray billCharacteristics = jsonObject.getJSONArray("billCharacteristics");
		final int billLength = billCharacteristics.length();
		for (int i = 0; i < billLength; i++)
		{
			final JSONObject bill = billCharacteristics.getJSONObject(i);
			if (i == 0)
			{
				Cell billCell = new Cell(1, 2)
						.add(new Paragraph("Bill Summary"))
						.setFont(font)
						.setFontColor(ColorConstants.WHITE)
						.setBackgroundColor(ColorConstants.BLACK)
						.setTextAlignment(TextAlignment.CENTER);
				billTable.addCell(billCell);
			}
			billTable.addCell(new Cell().add(new Paragraph((String) bill.get("name"))));
			billTable.addCell(new Cell().add(new Paragraph((String) bill.get("value"))));
		}
		
		final JSONArray accountCharges = jsonObject.getJSONArray("accountCharges");
		final int accountChargesLength = accountCharges.length();
		for (int i = 0; i < accountChargesLength; i++)
		{
			final JSONObject accountCharge = accountCharges.getJSONObject(i);
			if ( i == 0)
			{
				Cell accountChargeCell = new Cell(1, 6)
						.add(new Paragraph("Account Related Charges"))
						.setFont(font)
						.setFontColor(ColorConstants.WHITE)
						.setBackgroundColor(ColorConstants.BLACK)
						.setTextAlignment(TextAlignment.CENTER);
				accountChargesTable.addCell(accountChargeCell);
				accountChargesTable.addCell(new Cell().add(new Paragraph("Period")));
				accountChargesTable.addCell(new Cell().add(new Paragraph("Description")));
				accountChargesTable.addCell(new Cell().add(new Paragraph("Quantity")));
				accountChargesTable.addCell(new Cell().add(new Paragraph("Before Tax")));
				accountChargesTable.addCell(new Cell().add(new Paragraph("Tax Amount")));
				accountChargesTable.addCell(new Cell().add(new Paragraph("After Tax")));
			}
			accountChargesTable.addCell(new Cell().add(new Paragraph((String) accountCharge.get("period"))));
			accountChargesTable.addCell(new Cell().add(new Paragraph((String) accountCharge.get("description"))));
			accountChargesTable.addCell(new Cell().add(new Paragraph((String) accountCharge.get("quantity"))));
			accountChargesTable.addCell(new Cell().add(new Paragraph((String) accountCharge.get("beforeTax"))));
			accountChargesTable.addCell(new Cell().add(new Paragraph((String) accountCharge.get("taxAmount"))));
			accountChargesTable.addCell(new Cell().add(new Paragraph((String) accountCharge.get("afterTax"))));
		}
		
		final JSONObject serviceCharges = jsonObject.getJSONObject("serviceCharges");
		Iterator<String> keys = serviceCharges.keys();
		int j = 0;
		while (keys.hasNext())
		{
			String key = keys.next();
			final JSONArray serviceChargesArray = serviceCharges.getJSONArray(key);
			final int serviceChargesLength = serviceChargesArray.length();
			for (int i = 0; i < serviceChargesLength; i++)
			{
				final JSONObject serviceCharge = serviceChargesArray.getJSONObject(i);
				if (i == 0 && j == 0)
				{
					Cell serviceChargeCell = new Cell(1, 7)
							.add(new Paragraph("Service Related Charges"))
							.setFont(font)
							.setFontColor(ColorConstants.WHITE)
							.setBackgroundColor(ColorConstants.BLACK)
							.setTextAlignment(TextAlignment.CENTER);
					serviceChargesTable.addCell(serviceChargeCell);
					serviceChargesTable.addCell(new Cell().add(new Paragraph("Period")));
					serviceChargesTable.addCell(new Cell(1, 2).add(new Paragraph("Description")));
					serviceChargesTable.addCell(new Cell().add(new Paragraph("Quantity")));
					serviceChargesTable.addCell(new Cell().add(new Paragraph("Before Tax")));
					serviceChargesTable.addCell(new Cell().add(new Paragraph("Tax Amount")));
					serviceChargesTable.addCell(new Cell().add(new Paragraph("After Tax")));
					serviceChargesTable.addCell(new Cell(1, 7).add(new Paragraph((String) serviceCharge.get("accessNo"))).setFontColor(ColorConstants.BLACK).setBackgroundColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("period"))));
					serviceChargesTable.addCell(new Cell(1, 2).add(new Paragraph((String) serviceCharge.get("description"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("quantity"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("beforeTax"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("taxAmount"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("afterTax"))));
					j++;
				}
				else if (i == 0 && j != 0)
				{
					serviceChargesTable.addCell(new Cell(1, 7).add(new Paragraph((String) serviceCharge.get("accessNo"))).setFontColor(ColorConstants.BLACK).setBackgroundColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("period"))));
					serviceChargesTable.addCell(new Cell(1, 2).add(new Paragraph((String) serviceCharge.get("description"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("quantity"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("beforeTax"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("taxAmount"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("afterTax"))));
					j++;
				}
				else if ( i == serviceChargesLength - 1)
				{
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("period"))).setFontColor(ColorConstants.BLACK).setBackgroundColor(ColorConstants.LIGHT_GRAY));
					serviceChargesTable.addCell(new Cell(1, 2).add(new Paragraph((String) serviceCharge.get("description"))).setFontColor(ColorConstants.BLACK).setBackgroundColor(ColorConstants.LIGHT_GRAY));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("quantity"))).setFontColor(ColorConstants.BLACK).setBackgroundColor(ColorConstants.LIGHT_GRAY));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("beforeTax"))).setFontColor(ColorConstants.BLACK).setBackgroundColor(ColorConstants.LIGHT_GRAY));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("taxAmount"))).setFontColor(ColorConstants.BLACK).setBackgroundColor(ColorConstants.LIGHT_GRAY));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("afterTax"))).setFontColor(ColorConstants.BLACK).setBackgroundColor(ColorConstants.LIGHT_GRAY));
				}
				else
				{
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("period"))));
					serviceChargesTable.addCell(new Cell(1, 2).add(new Paragraph((String) serviceCharge.get("description"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("quantity"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("beforeTax"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("taxAmount"))));
					serviceChargesTable.addCell(new Cell().add(new Paragraph((String) serviceCharge.get("afterTax"))));
				}
			}
		}
		
		final JSONArray usageCharges = jsonObject.getJSONArray("usageCharges");
		final int usgaeChargesLength = usageCharges.length();
		for (int i = 0; i < usgaeChargesLength; i++)
		{
			final JSONObject usageCharge = usageCharges.getJSONObject(i);
			if (i == 0)
			{
				Cell usageChargeCell = new Cell(1, 11)
						.add(new Paragraph("Usage Allowance Ballance"))
						.setFont(font)
						.setBackgroundColor(ColorConstants.BLACK)
						.setTextAlignment(TextAlignment.CENTER)
						.setFontColor(ColorConstants.WHITE);
				usageChargesTable.addCell(usageChargeCell).setFontSize(10f);
				usageChargesTable.addCell(new Cell().add(new Paragraph("Allowance Name")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Supplied To")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Start Date")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("End Date")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Units")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Total Amount")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Amount Used")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Remaining")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Expiring")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Shared")));
				usageChargesTable.addCell(new Cell().add(new Paragraph("Date/Time")));
			}
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("allowanceName"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("suppliedTo"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("startDate"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("endDate"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("units"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("totalAmount"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("amountUsed"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("remaining"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("expiring"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("shared"))));
			usageChargesTable.addCell(new Cell().add(new Paragraph((String) usageCharge.get("dateTime"))));
		}
		
		document.add(accountTable);
		document.add(new Cell().add(new Paragraph("")));
		document.add(new Cell().add(new Paragraph("")));
		document.add(billTable);
		document.add(new Cell().add(new Paragraph("")));
		document.add(new Cell().add(new Paragraph("")));
		document.add(accountChargesTable);
		document.add(new Cell().add(new Paragraph("")));
		document.add(new Cell().add(new Paragraph("")));
		document.add(serviceChargesTable);
		document.add(new Cell().add(new Paragraph("")));
		document.add(new Cell().add(new Paragraph("")));
		document.add(usageChargesTable);
		document.close();
		
		return baos.toByteArray();
	}
}
