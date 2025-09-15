package com.goodee.corpdesk.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.goodee.corpdesk.file.entity.ApprovalFile;
import com.goodee.corpdesk.file.repository.ApprovalFileRepository;
import com.goodee.corpdesk.file.repository.BoardFileRepository;
import com.goodee.corpdesk.file.repository.MessageFileRepository;

import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "/file/*")
public class FileController {
	@Value("${app.upload}")
	private String path;
	
	@Autowired
	private ApprovalFileRepository approvalFileRepository;
	@Autowired
	private BoardFileRepository boardFileRepository;
	@Autowired
	private MessageFileRepository messageFileRepository;
	
	@GetMapping("approval/{fileId}")
	public void downloadApprovalFile(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws IOException {
		
		// 1. 파일id로 파일의 메타데이터를 받아옴
		Optional<ApprovalFile> result = approvalFileRepository.findById(fileId);
		ApprovalFile approvalFile;
		
		// 조회결과가 null이면 예외를 발생시키고, 조회결과가 null이 아니라면 객체에 값 할당
		result.orElseThrow();
		approvalFile = result.get();
		
		// 2. 메타데이터로 실제 파일을 받아옴
		String filePath = path + "approval";
		File file = new File(filePath, approvalFile.getSaveName() + "." + approvalFile.getExtension());
		String fileName = URLEncoder.encode(approvalFile.getOriName(), "UTF-8");
		
		// 3. 실제 파일을 클라이언트에게 전송
		// File을 읽어서 전송
		FileInputStream fi = new FileInputStream(file);
		// client로 연결
		OutputStream os = response.getOutputStream();
		// 전송
		FileCopyUtils.copy(fi, os);
		// 자원 해제
		os.close();
		fi.close();
		
	}
	
	@GetMapping("board/{fileId}")
	public String downloadBoardFile(Long fileId) {
		fileId = 1L;
		
		return new String();
	}
	
	@GetMapping("message/{fileId}")
	public String downloadMessageFile(Long fileId) {
		fileId = 1L;
		
		return new String();
	}
	
}
