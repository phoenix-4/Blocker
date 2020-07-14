package com.phoenix.blocker.controller;

import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.phoenix.blocker.dao.Vehicle;
import com.phoenix.blocker.service.PdfService;
import com.phoenix.blocker.service.QRGenatorService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import payloads.LoginRequest;

import javax.validation.Valid;
import java.io.*;

@RestController
public class PdfController {

    @Autowired
    PdfService pdfService;

    @Autowired
    QRGenatorService qrGenatorService;

    @PostMapping("/genpdf")
    HttpEntity<byte[]> generatePdf(@RequestBody Vehicle vehicle) throws IOException, WriterException {




        return pdfService.createPdf(vehicle);


    }




}
