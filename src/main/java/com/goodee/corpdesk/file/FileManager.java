package com.goodee.corpdesk.file;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManager<T extends FileEntity> {
	/**
	 * 지정된 경로에 파일을 저장하고 파일 정보를 반환
	 *
	 * @param filePath
	 * @param fileData
	 * @return fileVO
	 * @throws Exception
	 */
	public T saveFile(String filePath, MultipartFile fileData) throws Exception {
		
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
		
		String fileName = savedName + extension;
		
		// 3. HDD에 저장
		file = new File(filePath, fileName);
		FileCopyUtils.copy(fileData.getBytes(), file);
		
		// 4. 반환할 정보를 FileVO에 바인딩
		FileVO fileVO = new FileVO();
		fileVO.setExtension(extension);
		fileVO.setOriName(this.getOriName(oriFileName));
		fileVO.setSavedName(savedName);
		
		return fileVO;
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
	 * 
	 */
	public boolean deleteFile(String filePath, T fileVO) throws Exception {
		File file = new File(filePath, fileVO.getSavedName() + "." + fileVO.getExtension());
		return file.delete();
	}
}
