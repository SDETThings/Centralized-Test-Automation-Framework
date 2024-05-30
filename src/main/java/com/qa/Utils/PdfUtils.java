package com.qa.Utils;
import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
//import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class PdfUtils {
    /* public synchronized List<String> comparePdfs(String actualpdfPath , String
    expectedpdfPath) throws IOException {
    List<String> differences = new ArrayList<>();
    String expectedTextFromPdf = extractStringFromPdf(expectedpdfPath);
    String actualTextFromPdf = extractStringFromPdf(actualpdfPath);
    String[] expectedLines = expectedTextFromPdf.split("\\r?\\n");
    String[] actualLines = actualTextFromPdf.split("\\r?\\n");
    for(int i=0;i<Math.min(expectedLines.length,actualLines.length);i++)
    {
    if(!expectedLines[i].equals(actualLines[i]))
    {
    differences.add("Mismatch at line "+(i+1)+": Expected '"+expectedLines[i]+"' ,
    Actual '"+ actualLines[i]+"'");
    }
    }
    for(int i=Math.min(expectedLines.length,actualLines.length);i<actualLines.length;i++)
    {
    differences.add("Additional line in actual pdf at line "+ (i+1)+": '"+actualLines[i]+"'");
    }
    return differences;
    }
    public synchronized String extractStringFromPdf(String pdfPath) throws IOException {
    try(PDDocument document = loadPdfDcoument(pdfPath)){
    PDFTextStripper pdfTextStripper = new PDFTextStripper();
    return pdfTextStripper.getText(document);
    }
    }
    public synchronized PDDocument loadPdfDcoument(String pdfPath) throws IOException {
    PDDocument document = Loader.loadPDF(new File(pdfPath));
    return document;
    }*/
    public synchronized boolean pdfComparatorFunc(String actualpdfPath , String expectedpdfPath,String resultPath) throws IOException {
        String actualpdf1 = "./src/test/resources/loanSummary1.pdf";
        String expectedPdf1 = "./src/test/resources/loanSummary2.pdf";
        PdfComparator pdfComparator = new PdfComparator(actualpdfPath,expectedpdfPath);
        pdfComparator.compare().writeTo(resultPath);
        CompareResult result = pdfComparator.getResult();
        if(!result.getDifferences().isEmpty())
        {
            return false;
        }else
        {
            return true;
        }
    }
    public boolean isPdfEmpty(Path path) {
        try {
            long fileSize = Files.size(path);
            return fileSize<=0;
        } catch (IOException e) {
            System.err.println("Error checking PDF file size "+e.getMessage());
            return false;
        }
    }
}
