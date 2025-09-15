package com.goodee.corpdesk.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
import com.goodee.corpdesk.file.entity.BoardFile;
import com.goodee.corpdesk.file.entity.MessageFile;
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
	public void downloadApprovalFile(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws Exception {
		
		// 1. 파일id로 파일의 메타데이터를 받아옴
		Optional<ApprovalFile> result = approvalFileRepository.findById(fileId);
		ApprovalFile approvalFile = result.orElseThrow(); // 조회결과가 null이면 예외를 발생시키고, 조회결과가 null이 아니라면 객체에 값 할당
		
		// 2. 메타데이터로 file 객체 생성
		String filePath = path + "approval";
		File file = new File(filePath, approvalFile.getSaveName() + "." + approvalFile.getExtension());
		String fileName = URLEncoder.encode(approvalFile.getOriName(), "UTF-8");
		
		// 응답시 필요한 값을 response에 바인딩
		response.setContentLengthLong(file.length()); // 총 파일의 크기 -> 파일 다운로드시 시간이 얼마나 남았는지 알려주려면 이게 필요함
		
		// header 설정
		// 파일을 보낼 때 이미지인지, 음악인지, 문서 등인지에 대한 정보를 담아 보내야 함
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 3. 실제 파일을 클라이언트에게 전송
		FileInputStream fi = new FileInputStream(file); // File을 읽어서 전송
		OutputStream os = response.getOutputStream(); // client로 연결
		FileCopyUtils.copy(fi, os); // 전송 & 자원 해제
		
	}
	
	@GetMapping("board/{fileId}")
	public void downloadBoardFile(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws Exception {
		
		// 1. 파일id로 파일의 메타데이터를 받아옴
		Optional<BoardFile> result = boardFileRepository.findById(fileId);
		BoardFile boardFile = result.orElseThrow(); // 조회결과가 null이면 예외를 발생시키고, 조회결과가 null이 아니라면 객체에 값 할당
		
		// 2. 메타데이터로 file 객체 생성
		String filePath = path + "board";
		File file = new File(filePath, boardFile.getSaveName() + "." + boardFile.getExtension());
		String fileName = URLEncoder.encode(boardFile.getOriName(), "UTF-8");
		
		// 응답시 필요한 값을 response에 바인딩
		response.setContentLengthLong(file.length()); // 총 파일의 크기 -> 파일 다운로드시 시간이 얼마나 남았는지 알려주려면 이게 필요함
		
		// header 설정
		// 파일을 보낼 때 이미지인지, 음악인지, 문서 등인지에 대한 정보를 담아 보내야 함
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 3. 실제 파일을 클라이언트에게 전송
		FileInputStream fi = new FileInputStream(file); // File을 읽어서 전송
		OutputStream os = response.getOutputStream(); // client로 연결
		FileCopyUtils.copy(fi, os); // 전송 & 자원 해제
		
	}
	
	@GetMapping("message/{fileId}")
	public void downloadMessageFile(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws Exception {
		
		// 1. 파일id로 파일의 메타데이터를 받아옴
		Optional<MessageFile> result = messageFileRepository.findById(fileId);
		MessageFile messageFile = result.orElseThrow(); // 조회결과가 null이면 예외를 발생시키고, 조회결과가 null이 아니라면 객체에 값 할당
		
		// 2. 메타데이터로 file 객체 생성
		String filePath = path + "message";
		File file = new File(filePath, messageFile.getSaveName() + "." + messageFile.getExtension());
		String fileName = URLEncoder.encode(messageFile.getOriName(), "UTF-8");
		
		// 응답시 필요한 값을 response에 바인딩
		response.setContentLengthLong(file.length()); // 총 파일의 크기 -> 파일 다운로드시 시간이 얼마나 남았는지 알려주려면 이게 필요함
		
		// header 설정
		// 파일을 보낼 때 이미지인지, 음악인지, 문서 등인지에 대한 정보를 담아 보내야 함
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 3. 실제 파일을 클라이언트에게 전송
		FileInputStream fi = new FileInputStream(file); // File을 읽어서 전송
		OutputStream os = response.getOutputStream(); // client로 연결
		FileCopyUtils.copy(fi, os); // 전송 & 자원 해제
		
	}
	
}
