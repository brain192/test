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
    /**
     * BoardService
     * ----------------------------------------
     * - Controller와 Repository 사이에서 비즈니스 처리 담당
     * - 순수 비즈니스 로직을 책임지는 계층
     * - 페이징 계산은 Controller에서 하고,
     *   실제 데이터 조회는 Service에서 관리
     */

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

    /**
     * 페이지 번호에 맞는 게시글 목록을 가져오는 기능
     * @param page 페이지 번호(1부터 시작)
     * @param pageSize 한 페이지에 보여줄 글 개수
     */
    public List<BoardDTO> getPageList(int page, int pageSize) {
        int offset = (page - 1) * pageSize;  // OFFSET 계산
        return repository.findPage(offset, pageSize);
    }

    /**
     * 전체 글 개수 조회 → 총 페이지 수 계산 시 사용
     */
    public int getTotalCount() {
        return repository.countAll();
    }
}