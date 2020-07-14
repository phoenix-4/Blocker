package com.phoenix.blocker.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class QRGenatorService {

    private final static Logger logger = LoggerFactory.getLogger(QRGenatorService.class);
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";


    public void generateQRCodeImage(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
                } catch (WriterException e) {
                    logger.error(">>Error in QR Generator", e.getMessage());
                }

                Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH);
                try {
                    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
                } catch (IOException e) {
                    logger.error(">>Error in QR Generator", e.getMessage());
                }

//        System.out.println(bitMatrix);
    }


    public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        System.out.println(pngData);
        return pngData;
    }


    public ByteArrayOutputStream getQRCodeImage1(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        System.out.println(pngData);
        return pngOutputStream;
    }
}
