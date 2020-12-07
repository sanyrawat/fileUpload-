package com.example.demo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.annotation.Resource;

//import javax.swing.text.Document;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
//import org.w3c.dom.Document;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.parser.clipper.Paths;

@SpringBootApplication
@RestController
public class FileUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadApplication.class, args);
	}
	String home = System.getProperty("user.home");
	

	@RequestMapping(value="/upload", method=RequestMethod.POST,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> upoadFile(@RequestParam ("file") MultipartFile file) throws IOException, DocumentException {
		//File convertFile = new File("C:\\Users\\The Arav\\Downloads\\"+file.getOriginalFilename());
		File convertFile = new File(home+"\\Documents\\"+file.getOriginalFilename());
		System.out.println(home+"\\Documents\\"+file.getOriginalFilename());
		//convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
 		fout.close();
 		//textTopdf(convertFile);
 		return new ResponseEntity<>("File Name :"+file.getOriginalFilename() +", Size: "+convertFile.length()+" Bytes, "+
 		"Number of Lines :"+getLineNumber(convertFile)+", Total No of Words are :"+getWordsCount(convertFile)+
 		", Total Number of Charecters: "+getcharCount(convertFile)+" Uploaded Successfully at "+home+"\\Documents\\", HttpStatus.OK);
	}
	@RequestMapping(value="/download")
	public ResponseEntity<Object> downloadFile(@RequestParam String para) throws DocumentException, IOException{
		String fileName=para+".txt";
		File convertFile = new File(home+"\\Documents\\"+fileName);
		textTopdf(convertFile);
		System.out.println("in");
		try (BufferedInputStream in = new BufferedInputStream(new URL(home+"\\Downloads\\"+para+".pdf").openStream());
				  FileOutputStream fileOutputStream = new FileOutputStream(para)) {
				    byte dataBuffer[] = new byte[1024];
				    int bytesRead;
				    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				        fileOutputStream.write(dataBuffer, 0, bytesRead);
				    }
				} catch (IOException e) {
				    // handle exception
				}
		return new ResponseEntity<>("File : "+para+".pdf successfully downloaded in "+home+"\\Downloads\\" , HttpStatus.OK);
	}
	
	public int getLineNumber(File file) throws IOException {
		
		   //FileReader       input = new FileReader(file);
		   //LineNumberReader count = new LineNumberReader(input);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		 return  lines;//count.getLineNumber() ;                                     
		}
	
	public int getWordsCount(File file) throws IOException {
		
		FileInputStream fis = new FileInputStream(file);
	      byte[] bytesArray = new byte[(int)file.length()];
	      fis.read(bytesArray);
	      String s = new String(bytesArray);
	      String [] data = s.split(" ");
	      int count=0;
		for (int i=0; i<data.length; i++) {
	         count++;
	      }
		 return  count ;                                     
		}
	public int getcharCount(File file) throws IOException {
		
		FileInputStream fis = new FileInputStream(file);
	      byte[] bytesArray = new byte[(int)file.length()];
	      fis.read(bytesArray);
	      String s = new String(bytesArray);
	      String [] data = s.split("");
	      int count=0;
		for (int i=0; i<data.length; i++) {
	         count++;
	      }
		 return  count ;                                     
		}
	public void textTopdf(File files) throws DocumentException, IOException {
		System.out.println("inside texToPdf");
		System.out.println("file name :" + files.getName());
		BufferedReader br = new BufferedReader(new FileReader(files));
		String fileName=files.getName();
		System.out.println("file name :" + fileName);
		int s=fileName.length();
		fileName=fileName.substring(0, s-4);
	Document pdfDoc = new Document(PageSize.A4);
	PdfWriter.getInstance(pdfDoc, new FileOutputStream("C:\\Users\\The Arav\\Downloads\\"+fileName+".pdf"))
	  .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	pdfDoc.open();
	Font myfont = new Font();
	myfont.setStyle(Font.NORMAL);
	myfont.setSize(11);
	pdfDoc.add(new Paragraph("\n"));
	//BufferedReader br = new BufferedReader(new FileReader(filename));
	String strLine;
	while ((strLine = br.readLine()) != null) {
	    Paragraph para = new Paragraph(strLine + "\n", myfont);
	    para.setAlignment(Element.ALIGN_JUSTIFIED);
	    pdfDoc.add(para);
	}	
	pdfDoc.close();
	br.close();
}
}
