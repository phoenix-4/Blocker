package com.phoenix.blocker.service;

import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.phoenix.blocker.dao.Vehicle;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class PdfService {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    QRGenatorService qrGenatorService;

    @Value("${qr.image}")
    private String QRURL;


    public HttpEntity<byte[]> createPdf(Vehicle vehicle){

        /* initialize an engine */
        VelocityEngine ve = new VelocityEngine();

        /* Get the Template */
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.init();
        Template template = ve.getTemplate("templates/registrationCertificate.vm");

        /* create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("vehicle", vehicle);


        qrGenatorService.generateQRCodeImage(vehicle.getHash(), 350, 350);
        context.put("qrCode",QRURL);




        /* now render the template into a StringWriter */
        StringWriter writer1 = new StringWriter();
        template.merge(context, writer1);

        System.out.println(writer1.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos = generatePdf(writer1.toString());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        String fileName = vehicle.getRegistrationNo()+".pdf";
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName.replace(" ", "_"));
        header.setContentLength(baos.toByteArray().length);

        return new HttpEntity<byte[]>(baos.toByteArray(), header);
    }


    public ByteArrayOutputStream generatePdf(String html1) {

        String pdfFilePath = "";
        PdfWriter pdfWriter = null;

        // create a new document
        Document document = new Document();
        try {

            document = new Document();
            // document header attributes
            document.addAuthor("Yasiru Kavishka");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("Yasiru");
            document.addTitle("Vehicle Registration");
            document.setPageSize(PageSize.A4);
            document.setMargins(15,15,15,10);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            // open a document
            document.open();

            XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
            xmlWorkerHelper.getDefaultCssResolver(true);


            xmlWorkerHelper.parseXHtml(pdfWriter, document, new StringReader(
                    html1));

            document.setPageSize(PageSize.A4.rotate());

            // close the document
            document.close();
            System.out.println("PDF generated successfully");

            return baos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
