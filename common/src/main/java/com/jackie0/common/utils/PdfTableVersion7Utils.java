package com.jackie0.common.utils;

import com.itextpdf.layout.element.Table;
import com.jackie0.common.constant.Constant;
import com.jackie0.common.exception.BusinessException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

public class PdfTableVersion7Utils<T extends Serializable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfTableVersion7Utils.class);

    public Table generatePdfTableByDefaultTemplet(T data) {
        if (data == null) {
            LOGGER.info("传入的数据为空！");
            return null;
        }
        URL pdfTableTempletUrl = UrlUtils.getResourceURL("templet/pdfTables/default.html");
        if (pdfTableTempletUrl != null) {
            File file = new File(pdfTableTempletUrl.getPath());
            try {
                Document pdfTableTemplet = Jsoup.parse(file, Constant.DEF_ENC);
                Elements elements = pdfTableTemplet.getElementsByTag("table");
                if (elements != null && !elements.isEmpty()) {
                    Element element = elements.get(0);
                    return null;
                } else {
                    throw new BusinessException("COMMON_ERROR_12");
                }
            } catch (IOException e) {
                LOGGER.error("获取HTML异常", e);
                throw new BusinessException("COMMON_ERROR_11");
            }
        } else {
            throw new BusinessException("COMMON_ERROR_10");
        }
    }

    public static void main(String[] args) throws IOException {
        URL pdfTableTempletUrl = UrlUtils.getResourceURL("templet/pdfTables/default.html");
        if (pdfTableTempletUrl != null) {
            File file = new File(pdfTableTempletUrl.getPath());
            Document doc = Jsoup.parse(file, Constant.DEF_ENC);
            System.out.println(doc.getElementById("defaultPdfTable").outerHtml());
        }
    }
}
