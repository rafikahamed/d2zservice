package com.d2z.d2zservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.model.fdm.FDMManifestRequest;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONObject;
import org.json.XML;

@Service
public class FTPUploader {

	FTPClient ftp = null;
	
	@Value("${ftp.source.server}")
	private String server;
	
	@Value("${ftp.source.user}")
	private String user;
	
	@Value("${ftp.source.pass}")
	private String pass;

	public void FTPUploaderVal(String host, String user, String pwd) throws Exception {
		ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		int reply;
		ftp.connect(host);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new Exception("Exception in connecting to FTP Server");
		}
		ftp.login(user, pwd);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
	}

	public void uploadFile(String localFileFullName, String fileName, String hostDir) throws Exception {
		try (InputStream input = new FileInputStream(new File(localFileFullName))) {
			this.ftp.storeFile(hostDir + fileName, input);
		}
	}

//	public void downloadFile(String remoteFilePath, String localFilePath) {
//        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
//            this.ftp.retrieveFile(remoteFilePath, fos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

	public void fdmFileCreation(FDMManifestRequest request) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(request);
		JSONObject json = new JSONObject(jsonStr);
		String requestXml = XML.toString(json);
		File file = new File("src/main/resources/FDM-Request/D2Z_" + getdate() + ".xml");
		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] contentInBytes = requestXml.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getdate() {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy_HH_mm_ss");
		Date date = new Date();
		return formatter.format(date);
	}

	public void ftpUpload() {
		List<String> fileList = new ArrayList<String>();
		File file = new File("src/main/resources/FDM-Request");
		File[] files = file.listFiles();
		for (File f : files) {
			fileList.add(f.getName());
			String hostDir = "FDM/";
			if (f.getName() != null) {
				try {
					FTPUploaderVal(server, user, pass);
					uploadFile(f.getPath(), f.getName(), hostDir);
					disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("No Files Found under Country folder");
			}
		}
	}

	public void deleteFiles(File folder) {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				boolean del = file.delete();
				System.out.println(fileName + " : got deleted ? " + del);
			} else if (file.isDirectory()) {
				deleteFiles(file);
			}
		}
	}

	public void disconnect() {
		if (this.ftp.isConnected()) {
			try {
				this.ftp.logout();
				this.ftp.disconnect();
			} catch (IOException f) {
			}
		}
	}

}
