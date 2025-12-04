package kjhtest.test.service;

import kjhtest.test.domain.BoardFile;
import kjhtest.test.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardFileService {

    private final BoardFileRepository fileRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;


    /**
     * 게시글 파일 업로드
     */
    public void uploadFile(MultipartFile file, int boardId) throws IOException {

        // 1) 파일이 없으면 종료
        if (file == null || file.isEmpty()) {
            log.info("업로드할 파일이 없습니다.");
            return;
        }

        // 2) 업로드 폴더 없으면 생성
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                log.error("업로드 폴더 생성 실패: {}", uploadDir);
                throw new IOException("업로드 폴더 생성 실패");
            }
        }

        // 3) 저장 파일명 생성
        String originalName = file.getOriginalFilename();
        String savedName = UUID.randomUUID() + "_" + originalName;
        String savePath = uploadDir + File.separator + savedName;

        // 4) 실제 파일 저장
        File dest = new File(savePath);
        file.transferTo(dest);
        log.info("파일 저장 완료: {}", savePath);

        // 5) DB 저장
        BoardFile bf = new BoardFile();
        bf.setBoardId(boardId);
        bf.setOriginalName(originalName);
        bf.setSavedName(savedName);
        bf.setFilePath(savePath);

        fileRepository.save(bf);
    }


    /**
     * 파일 정보 가져오기 (다운로드 시 사용)
     */
    public BoardFile getFile(int id) {
        return fileRepository.findById(id);
    }


    /**
     * 파일 삭제
     */
    public void deleteFile(int fileId) {
        BoardFile file = fileRepository.findById(fileId);
        if (file == null) return;

        File target = new File(file.getFilePath());
        if (target.exists()) {
            boolean deleted = target.delete();
            log.info("파일 삭제: {}, 성공={}", file.getFilePath(), deleted);
        }

        fileRepository.delete(file);
    }
}