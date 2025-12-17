package kjhtest.test.controller;

import jakarta.servlet.http.HttpSession;
import kjhtest.test.domain.BoardDTO;
import kjhtest.test.domain.BoardFile;
import kjhtest.test.domain.MemberDTO;
import kjhtest.test.service.BoardFileService;
import kjhtest.test.service.BoardService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 게시판 Controller
 * - 화면 요청 처리
 * - Service 계층 호출
 * - 파일 업로드/다운로드 처리
 * - 세션 로그인 체크
 */
@Controller                 // MVC Controller
@RequestMapping("/board")   // 기본 URL: /board
public class BoardController {

    private final BoardService service;
    private final BoardFileService fileService;

    // 한 페이지당 게시글 수
    private static final int PAGE_SIZE = 10;

    // 생성자 주입
    public BoardController(BoardService service, BoardFileService fileService) {
        this.service = service;
        this.fileService = fileService;
    }

    /**
     * 게시글 목록
     * GET /board?page=1
     */
    @GetMapping
    public String list(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

        // 전체 게시글 수
        int totalCount = service.getTotalCount();

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        // 현재 페이지 게시글 목록
        List<BoardDTO> boards = service.getPageList(page, PAGE_SIZE);

        // View로 데이터 전달
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "list"; // list.html
    }

    /**
     * 글쓰기 폼
     * GET /board/write
     */
    @GetMapping("/write")
    public String writeForm(HttpSession session) {

        // 로그인 체크
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/login";
        }

        return "write";
    }

    /**
     * 글 작성 처리
     * POST /board/write
     */
    @PostMapping("/write")
    public String write(@ModelAttribute BoardDTO board,
                        @RequestParam(name = "file", required = false) MultipartFile file,
                        HttpSession session) throws Exception {

        // 로그인 사용자 정보 가져오기
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        board.setWriter(member.getUsername());

        // 게시글 저장
        long boardId = service.write(board);

        // 파일이 있으면 업로드
        if (file != null && !file.isEmpty()) {
            fileService.uploadFile(file, boardId);
        }

        return "redirect:/board";
    }

    /**
     * 게시글 상세보기
     * GET /board/{id}
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") long id, Model model) {

        // 게시글 조회
        BoardDTO board = service.getDetail(id);

        // 첨부파일 목록 조회
        List<BoardFile> files = fileService.getFilesByBoardId(id);

        model.addAttribute("board", board);
        model.addAttribute("files", files);

        return "detail";
    }

    /**
     * 파일 다운로드
     * GET /board/download/{fileId}
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") long fileId) throws Exception {

        // 파일 정보 조회
        BoardFile bf = fileService.getFile(fileId);

        // 실제 파일 리소스
        FileSystemResource resource =
                new FileSystemResource(bf.getFilePath());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 한글 파일명 인코딩
        String encoded =
                URLEncoder.encode(bf.getOriginalName(), StandardCharsets.UTF_8)
                        .replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encoded + "\"")
                .body(resource);
    }

    /**
     * 게시글 삭제
     * GET /board/{id}/delete
     */
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, HttpSession session) {

        // 실무에서는 작성자 권한 체크 필요
        service.delete(id);

        return "redirect:/board";
    }

    /**
     * 게시글 수정
     * POST /board/{id}/update
     */
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") long id,
                         @ModelAttribute BoardDTO board) {

        board.setId(id);
        service.update(board);

        return "redirect:/board/" + id;
    }
}