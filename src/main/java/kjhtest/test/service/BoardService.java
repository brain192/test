package kjhtest.test.service;

import kjhtest.test.domain.BoardDTO;
import kjhtest.test.domain.BoardFile;
import kjhtest.test.repository.BoardFileRepository;
import kjhtest.test.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;
    private final BoardFileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 게시글 저장 (파일은 여기서 저장하거나 Controller에서 분리 가능)
    // 이 메서드는 "게시글 저장만" 수행하고 id를 반환
    public long write(BoardDTO board) {
        return repository.save(board);
    }

    // 게시글 + 파일 동시에 저장(편의 메서드, 선택)
    public long write(BoardDTO board, MultipartFile file) throws IOException {
        // 1) 게시글 저장 (id 획득)
        long boardId = repository.save(board);

        // 2) 파일이 있으면 저장
        if (file != null && !file.isEmpty()) {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                if (!dir.mkdirs()) throw new IOException("업로드 디렉토리 생성 실패: " + uploadDir);
            }

            String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(dir, savedName);
            file.transferTo(dest);

            BoardFile bf = new BoardFile();
            bf.setBoardId(boardId);
            bf.setOriginalName(file.getOriginalFilename());
            bf.setSavedName(savedName);
            bf.setFilePath(dest.getAbsolutePath());
            bf.setFileSize(file.getSize());

            fileRepository.save(bf);
        }

        return boardId;
    }

    public List<BoardDTO> getPageList(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return repository.findPage(offset, pageSize);
    }

    public int getTotalCount() {
        return repository.countAll();
    }

    public BoardDTO getDetail(long id) {
        return repository.findById(id);
    }

    public void delete(long id) {
        // 게시글 삭제 시 board_file은 FK ON DELETE CASCADE 사용하므로 DB에서 자동 삭제됨
        repository.delete(id);
    }

    public void update(BoardDTO board) {
        repository.update(board);
    }
}