package com.phoenix.blocker.controller;

import com.google.zxing.WriterException;
import com.phoenix.blocker.service.QRGenatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class GenController {

	@Autowired
	QRGenatorService qrGenatorService;



	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public String forUser() {
		System.out.println("sfdsvfdbcv");

			qrGenatorService.generateQRCodeImage("This is my first QR Code", 350, 350);

		return "welcome user";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String forAdmin() {

		return "welcome admin";
	}
	
	

}
