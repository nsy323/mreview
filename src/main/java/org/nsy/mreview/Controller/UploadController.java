package org.nsy.mreview.Controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.nsy.mreview.dto.UploadResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

     @Value("${org.nsy.upload.path}")  //application.properties의 변수
    private String uploadPath;

    /**
     * 파일 업로드 처리
     * @param uploadFiles
     */
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        log.info("uploadAjax...........");

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles){

            //이미지 파일만 업로드 가능
            if(uploadFile.getContentType().startsWith("image") == false){
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("originalName : " + originalName);
            log.info("fileName : " + fileName);

            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);    //저장경로

            try {
                //원본파일 저장
                uploadFile.transferTo(savePath);

                //섬네일 파일이름  s_
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;

                File thumbnailFile = new File(thumbnailSaveName);

                //섬네일 생성
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDTOList.add( new UploadResultDTO(fileName, uuid, folderPath) );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } //end of for
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    /**
     * 날짜폴더 생성
     * @return
     */
    private String makeFolder(){
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")); //현재날짜

        //make folder...
        String folderPath = str.replace("/", File.separator);   // '/'를 '\'로 변경

        File uploadPathFolder = new File(uploadPath, folderPath);

        log.info("uploadPathFolder : " + uploadPathFolder);

        //폵더가 존재하지 않으면 생성하기
        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    /**
     * 업로드 이미지 출력하기
     * @param fileName
     * @return
     */
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size){

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");  //인코딩한 한글파일명 디코딩

            File file = new File(uploadPath + File.separator + srcFileName);

            //원본파일 display하기 위한 처리(s_잘라내기)
            if(size != null && size.equals("1")){
                file = new File(file.getParent(), file.getName().substring(2));
            }

            log.info("file : " + file);

            HttpHeaders headers = new HttpHeaders();

            //MIME타입 처리
            headers.add("Content-Type", Files.probeContentType(file.toPath())); //Files.probeContentType() : 확장자를 이욜하여 파일의 마임타입 판단, 확장자가 없을 경우 null

            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), HttpStatus.OK);  //FileCopyUtils.copyToByteArray() : 한글은 http헤더에 사용할 수 없기 때문에
                                                                                                // 파일명은 영문명으로 인코딩하여 헤더에 적용용
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * 원본파일+섬네일 삭제
     * @param fileName
     * @return
     */
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){

        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");

            File file = new File(uploadPath + File.separator + srcFileName);

            boolean result = file.delete();

            File thumbnail = new File(file.getParent(),"s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
