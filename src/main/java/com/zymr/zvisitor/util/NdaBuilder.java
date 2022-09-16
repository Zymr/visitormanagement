/******************************************************* * Copyright (C) 2017 ZVisitor
 *
 * This file is part of ZVisitor.
 *
 * ZVisitor can not be copied and/or distributed without the express
 * permission of ZYMR Inc.
 *
 *  * 
 *******************************************************/
package com.zymr.zvisitor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.zymr.zvisitor.service.ImageService;

/**
 * A class to create NDF file with pdf format and some content manipulation with in pdf.
 */
@Service
@Slf4j
public class NdaBuilder {

	public static File defaultNDAFile;
	
	public static URL ndaUrl;
	
	@Autowired
	protected  ImageService imageService;
	
	@PostConstruct
	public void init() throws MalformedURLException {
		ndaUrl = imageService.buildUrl("//files//NDA.pdf");
		log.info("NDA Generator {} ", toString());
	}

	/**
	 * Update NDA file with visitor name and visitor signature.
	 * 
	 * @param destFile 
	 * @param signatureImage signature file
	 * @param visitorName
	 * @return File
	 */
	public static File build(Path destFile, File signatureImage, String visitorName) {
		try {
			PdfReader pdfReader = new PdfReader(ndaUrl);
			PdfStamper pdfStamper = new PdfStamper(pdfReader,
					new FileOutputStream(destFile.toString()));
			Image image = createNDAImage(signatureImage, 0, 0);
			PdfContentByte pdfContentByte = pdfStamper.getOverContent(2);
			PdfContentByte signature = pdfStamper.getOverContent(3);
			pdfContentByte.addImage(image);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(BaseFont.createFont
					(BaseFont.HELVETICA, 
							BaseFont.CP1257, 
							BaseFont.EMBEDDED
							)
					, 10); 
			signature.setFontAndSize(BaseFont.createFont
					(BaseFont.HELVETICA, 
							BaseFont.CP1257, 
							BaseFont.EMBEDDED
							)
					, 10); 
			pdfContentByte.setTextMatrix(85, 165);
			String visitorTruncatedName = visitorName.length() > 25 ?  visitorName.substring(0, 25).concat("...") : visitorName; 
			pdfContentByte.showText(visitorTruncatedName);
			pdfContentByte.endText();
			signature.beginText();
			signature.setTextMatrix(89, 612);
			signature.showText(new SimpleDateFormat("E, dd MMM yyyy").format(new Date()));
			signature.setTextMatrix(340, 612);
			signature.showText(new SimpleDateFormat("E, dd MMM yyyy").format(new Date()));
			signature.endText();
			pdfStamper.close();
			return destFile.toFile();
		} catch (IOException | DocumentException | NumberFormatException e) {
			log.error("Exception while generating NDA file. ",e);
			return null;
		}
	}


	/**
	 * Create itextPdf image object with visitor signature.
	 * 
	 * @param file 
	 * @param imgHeight
	 * @param imgWidth
	 * @return com.itextpdf.text.Image
	 */
	public static Image createNDAImage(File file, int imgHeight, int imgWidth) {
		try {
			Image image = Image.getInstance(file.getAbsolutePath());
			if (imgHeight!=0 && imgWidth!=0) {
				image.scaleAbsolute(imgWidth, imgHeight);
			} else {
				image.scaleAbsolute(100, 16);
			} image.setAbsolutePosition(100, 183);
			return image;
		} catch (BadElementException | IOException e) {
			log.error("Exception while creating image for NDA file. ",e);
			return null;
		}
	}
	public static File getDefaultNDAFile() {
		return defaultNDAFile;
	}

	public static void setDefaultNDAFile(File defaultNDAFile) {
		NdaBuilder.defaultNDAFile = defaultNDAFile;
	}
}
