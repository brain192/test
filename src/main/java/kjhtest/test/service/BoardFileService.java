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

    @Value("${file.upload-dir}")
    private String uploadDir;

    public long uploadFile(MultipartFile file, long boardId) throws IOException {
        if (file == null || file.isEmpty()) return 0L;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) throw new IOException("업로드 디렉토리 생성 실패");
        }

        String originalName = file.getOriginalFilename();
        String savedName = UUID.randomUUID() + "_" + originalName;
        File dest = new File(dir, savedName);
        file.transferTo(dest);

        BoardFile bf = new BoardFile();
        bf.setBoardId(boardId);
        bf.setOriginalName(originalName);
        bf.setSavedName(savedName);
        bf.setFilePath(dest.getAbsolutePath());
        bf.setSize(file.getSize());

        return fileRepository.save(bf);
    }

    public BoardFile getFile(long id) {
        return fileRepository.findById(id);
    }

    public void deleteFile(long id) {
        BoardFile bf = fileRepository.findById(id);
        if (bf == null) return;
        File f = new File(bf.getFilePath());
        if (f.exists()) f.delete();
        fileRepository.delete(id);
    }

    public java.util.List<BoardFile> getFilesByBoardId(long boardId) {
        return fileRepository.findByBoardId(boardId);
    }
}