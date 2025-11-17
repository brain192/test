package kjhtest.test.service;

import kjhtest.test.domain.BoardDTO;
import kjhtest.test.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    private final BoardRepository repository;

    public BoardService(BoardRepository repository) {
        this.repository = repository;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void write(BoardDTO board, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            // 업로드 디렉토리 생성
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // 저장 파일명 생성
            String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(dir, storedName);
            file.transferTo(dest);

            board.setFilename(storedName);
            board.setOriginalFilename(file.getOriginalFilename());
        }
        repository.save(board);
    }

    public List<BoardDTO> getList() {
        return repository.findAll();
    }

    public BoardDTO getDetail(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public void update(BoardDTO board) {
        repository.update(board);
    }

    // 페이지별 게시글 가져오기
    public List<BoardDTO> getPageList(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return repository.findPage(offset, pageSize);
    }

    // 전체 게시글 개수 반환
    public int getTotalCount() {
        return repository.countAll();
    }
}