package com.d2z.d2zservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.model.fdm.FDMManifestRequest;
import com.google.gson.Gson;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	@Value("${ftp.source.port}")
	private int port;
	
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

	public void fdmFileCreation(FDMManifestRequest request) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(request);
		JSONObject json = new JSONObject(jsonStr);
		String requestXml = XML.toString(json);
		System.out.println("FDM file Creation ------");
		File file = new File("src/main/resources/FDM-Request/D2Z_" + getdate() + ".xml");
		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] contentInBytes = requestXml.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			System.out.println("File Place successfully Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getdate() {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy_HH_mm_ss");
		Date date = new Date();
		return formatter.format(date);
	}

//	public void ftpUpload() {
//		List<String> fileList = new ArrayList<String>();
//		File file = new File("src/main/resources/FDM-Request");
//		File[] files = file.listFiles();
//		for (File f : files) {
//			fileList.add(f.getName());
//			String hostDir = "FDM/";
//			if (f.getName() != null) {
//				try {
//					FTPUploaderVal(server, user, pass);
//					uploadFile(f.getPath(), f.getName(), hostDir);
//					disconnect();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} else {
//				System.out.println("No Files Found under Country folder");
//			}
//		}
//	}
	
	
	public void ftpUpload(InputStream in) {
		   String SFTPWORKINGDIR = "/incoming";
		   Session session = null;
	       Channel channel = null;
	       ChannelSftp channelSftp = null;
		   try
		        {
		        JSch jsch = new JSch();
		        session = jsch.getSession(user, server, port);
		        session.setPassword(pass);
		        java.util.Properties config = new java.util.Properties();
		        config.put("StrictHostKeyChecking", "no");
		        session.setConfig(config);
		        System.out.println("Establishing Connection...");
		        session.connect();
		        System.out.println("Connection established.");
		        System.out.println("Creating SFTP Channel.");
		        channel = session.openChannel("sftp");
	            channel.connect();
		        System.out.println("FDM SFTP Connected----->");
		        System.out.println("SFTP Channel created.");
		        channelSftp = (ChannelSftp) channel;
	            channelSftp.cd(SFTPWORKINGDIR);
	            String fileName = "D2Z_" + getdate() + ".xml";
	            System.out.println("File Transfer Initiated");
	            channelSftp.put(in, fileName);
	            System.out.println("File Transfer Successfull");
		        }
		    	catch(Exception e){
		    	System.err.print(e);
		    }
	}


	public void deleteFiles(File folder) {
		File[] files = folder.listFiles();
		System.out.println("FDM - Inside the file delete method");
		for (File file : files) {
			if (file.isFile()) {
				System.out.println("File has content");
				String fileName = file.getName();
				boolean del = file.delete();
				System.out.println(fileName + " :FDM got deleted ? " + del);
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
