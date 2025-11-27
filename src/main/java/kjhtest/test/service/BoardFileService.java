package kjhtest.test.service;

import kjhtest.test.domain.BoardFile;
import kjhtest.test.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardFileService {

    private final BoardFileRepository fileRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;

    // 파일 저장
    public void uploadFile(MultipartFile file, int boardId) throws Exception {

        if (file.isEmpty()) {
            return;
        }

        // 실제 저장될 파일명: UUID_원본명
        String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String savePath = uploadDir + "/" + savedName;

        // 파일 저장
        file.transferTo(new File(savePath));

        // DB 저장 정보 생성
        BoardFile bf = new BoardFile();
        bf.setBoardId(boardId);
        bf.setOriginalName(file.getOriginalFilename());
        bf.setSavedName(savedName);
        bf.setFilePath(savePath);

        // DB 저장
        fileRepository.save(bf);
    }

    public BoardFile getFile(int id) {
        return fileRepository.findById(id);
    }
}