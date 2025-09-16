package com.goodee.corpdesk.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.goodee.corpdesk.file.dto.FileDTO;

@Component
public class FileManager {
	/**
	 * 지정된 경로에 파일을 저장하고 파일 정보를 반환
	 *
	 * @param filePath
	 * @param fileData
	 * @return fileDTO
	 * @throws Exception
	 */
	public FileDTO saveFile(String filePath, MultipartFile fileData) throws Exception {
		
		// 1. 디렉토리 생성
		File file = new File(filePath);
		
		// 만약 디렉토리가 없다면 생성
		if (!file.exists()) {
			file.mkdirs();
		}
		
		// 2. 저장파일용 파일명 생성
		String savedName = UUID.randomUUID().toString().replaceAll("-", "");
		
		String oriFileName = fileData.getOriginalFilename();
		String extension = this.getExtension(oriFileName);
		
		String fileName = savedName + "." + extension;
		
		// 3. HDD에 저장
		file = new File(filePath, fileName);
		FileCopyUtils.copy(fileData.getBytes(), file);
		
		// 4. 반환할 정보를 FileDTO에 바인딩
		FileDTO fileDTO = new FileDTO();
		fileDTO.setExtension(extension);
		fileDTO.setOriName(this.getOriName(oriFileName));
		fileDTO.setSaveName(savedName);
		
		return fileDTO;
	}
	
	/**
	 * 파일 이름에서 확장자를 추출하고 반환
	 * @param oriFileName
	 * @return extension
	 * @throws Exception
	 */
	public String getExtension(String oriFileName) throws Exception {
		
		return oriFileName.substring(oriFileName.lastIndexOf(".") + 1);
	}
	
	/**
	 * 파일 이름에서 확장자를 추출하고 반환
	 * @param oriFileName
	 * @return extension
	 * @throws Exception
	 */
	public String getOriName(String oriFileName) throws Exception {
		
		return oriFileName.substring(0, oriFileName.lastIndexOf("."));
	}
	
	/**
	 * 지정된 경로에 있는 파일을 삭제하고 파일 삭제 여부를 반환
	 * @param filePath
	 * @param fileVO
	 * @return file.delete()
	 * @throws Exception
	 */
	public boolean deleteFile(String filePath, FileDTO fileDTO) throws Exception {
		File file = new File(filePath, fileDTO.getSaveName() + "." + fileDTO.getExtension());
		return file.delete();
	}

}
