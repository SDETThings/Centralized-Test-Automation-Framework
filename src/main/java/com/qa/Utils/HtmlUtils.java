package com.qa.Utils;

import java.io.IOException;
import java.util.List;
import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class HtmlUtils {
    public synchronized boolean htmlComparator(String actualHtml, String expectedHtml,String resultsFodler,String resultFile) throws IOException {
        List<String> actualHtmlFile = Files.readAllLines(Paths.get(actualHtml), StandardCharsets.UTF_8);
        List<String> expectedHtmlFile = Files.readAllLines(Paths.get(expectedHtml), StandardCharsets.UTF_8);
        Patch<String> patch = DiffUtils.diff(actualHtmlFile, expectedHtmlFile);
        String highLightedContent = highlightDifferences(expectedHtmlFile, patch);
        Files.write(Paths.get(resultsFodler+"/result_"+resultFile), highLightedContent.getBytes());
        if(patch.getDeltas().size()>0)
        {
            return false;
        }else
        {
            return true;
        }
    }
    public synchronized String highlightDifferences(List<String> original, Patch<String> patch) {
        List<Delta<String>> deltas = patch.getDeltas();
        StringBuilder result = new StringBuilder();
        int cursor = 0;
        for (Delta<String> delta : deltas) {
            Chunk<String> originalChunk = delta.getOriginal();
            Chunk<String> revisedChunk = delta.getRevised();
            result.append("<br><strong>Actual:</strong><br>");
            if (!originalChunk.getLines().isEmpty()) {
                result.append("<mark>").append(String.join("",
                        originalChunk.getLines())).append("</mark>");
            }
            result.append("<br><strong>Expected:</strong><br>");
            if (!revisedChunk.getLines().isEmpty()) {
                result.append("<mark>").append(String.join("",
                        revisedChunk.getLines())).append("</mark>");
            }
            cursor = originalChunk.getPosition() + original.size();
        }
        return result.toString();
    }
}
