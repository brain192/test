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

/**
 * BoardService 클래스
 * ----------------------------------------
 * - 게시판 기능과 관련된 주요 비즈니스 로직을 수행하는 서비스 계층 클래스
 * - Spring의 @Service로 등록되어 컨트롤러와 레포지토리 사이의 중간 역할을 함
 * - 컨트롤러에서 전달받은 요청을 처리하고, 필요한 경우 파일을 저장하거나
 *   레포지토리를 호출하여 DB와 상호작용함
 * - 페이징 연산의 일부는 컨트롤러에서 처리하지만, 실제 데이터 조회는 서비스에서 수행함
 */
@Service
public class BoardService {
    // BoardRepository(데이터 접근 레이어) 의존성 주입
    private final BoardRepository repository;

    // BoardService 생성자에서 의존성 주입(스프링이 BoardRepository를 주입)
    public BoardService(BoardRepository repository) {
        this.repository = repository;
    }

    // application.properties 파일에 정의된 업로드 경로를 주입받음
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 새 게시글 작성 처리 메서드
     * - BoardDTO와 첨부파일(MultipartFile)을 받아 DB 저장 및 파일 저장 처리
     * - 파일이 비어있지 않으면 파일을 지정된 경로(uploadDir)에 저장하고,
     *   고유 식별명(UUID)으로 파일명을 생성하여 BoardDTO에 설정함
     * - 파일 저장 후에는 BoardDTO 객체를 BoardRepository를 통해 DB에 저장함
     * @param board 저장할 게시글 정보 (제목, 내용, 작성자 등 포함)
     * @param file 업로드된 파일 (첨부파일)
     * @throws IOException 파일 저장 중 예외 발생 가능성
     */
    public void write(BoardDTO board, MultipartFile file) throws IOException {
        // 파일이 비어있지 않은 경우에만 파일 저장 로직을 수행
        if (!file.isEmpty()) {
            // 업로드 디렉토리가 존재하지 않으면 새로 생성
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // 디렉토리 생성
            }

            // 저장할 파일명 생성 (원본 파일명 앞에 UUID 붙여 유니크하게 만듦)
            String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(dir, storedName);

            // 실제 파일을 지정된 경로에 저장
            file.transferTo(dest);

            // 저장한 파일명과 원본 파일명을 BoardDTO에 설정
            board.setFilename(storedName);
            board.setOriginalFilename(file.getOriginalFilename());
        }

        // 파일 저장 작업이 끝나면 게시글(BoardDTO) 정보를 DB에 저장
        repository.save(board);
    }

    /**
     * 전체 게시글 목록 조회 메서드
     * - BoardRepository를 통해 DB에서 모든 게시글 데이터를 가져와 반환
     * @return 게시글 목록 리스트
     */
    public List<BoardDTO> getList() {
        return repository.findAll();
    }

    /**
     * 게시글 상세 조회 메서드
     * - 주어진 ID에 해당하는 게시글을 조회하여 반환
     * - Controller에서 게시글 상세보기 요청 시 사용
     * @param id 조회할 게시글의 ID
     * @return 조회된 게시글 데이터 (BoardDTO)
     */
    public BoardDTO getDetail(Long id) {
        return repository.findById(id);
    }

    /**
     * 게시글 삭제 메서드
     * - 주어진 ID에 해당하는 게시글을 DB에서 삭제
     * @param id 삭제할 게시글의 ID
     */
    public void delete(Long id) {
        repository.delete(id);
    }

    /**
     * 게시글 수정 메서드
     * - 주어진 BoardDTO의 ID에 해당하는 게시글 정보를 업데이트
     * @param board 수정할 게시글 정보 (ID, 제목, 내용 등 포함)
     */
    public void update(BoardDTO board) {
        repository.update(board);
    }

    /**
     * 페이지 번호에 맞는 게시글 목록 조회 (페이징 처리)
     * - 요청된 페이지(page)와 페이지 크기(pageSize)를 이용해 DB 조회 범위 지정
     * - SQL의 LIMIT과 OFFSET 기능으로 특정 범위의 데이터만 조회함
     * @param page 조회할 페이지 번호 (1부터 시작)
     * @param pageSize 한 페이지에 표시할 게시글 수
     * @return 해당 페이지에 표시할 게시글 목록 리스트
     */
    public List<BoardDTO> getPageList(int page, int pageSize) {
        int offset = (page - 1) * pageSize;  // LIMIT 조회 시작 위치 계산 (OFFSET)
        return repository.findPage(offset, pageSize);
    }

    /**
     * 전체 게시글 개수 조회 메서드
     * - 전체 게시글 수를 조회하여 페이지 네비게이션 계산 시 사용
     * @return 전체 게시글 수
     */
    public int getTotalCount() {
        return repository.countAll();
    }
}
