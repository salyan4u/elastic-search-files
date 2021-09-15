package ru.solyanin.elasticsearchfiles.util;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.JodConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileConvertUtil {
    public static void downloadAndConvert(String downloadUrl, File downloaded, File pdf) throws IOException, OfficeException {
        URL url = new URL(downloadUrl);
        FileUtils.copyURLToFile(url, downloaded);
        JodConverter.convert(downloaded).to(pdf).execute();
    }

    public static String getStringFromPdf(File pdf, File downloaded) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(pdf, "r");
        PDFParser parser = new PDFParser(randomAccessFile);
        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        String data;
        data = pdfStripper.getText(pdDoc);
        pdDoc.close();
        cosDoc.close();
        randomAccessFile.close();
        FileUtils.delete(downloaded);
        FileUtils.delete(pdf);
        return data;
    }
}
