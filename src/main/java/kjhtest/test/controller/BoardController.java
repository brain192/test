package kjhtest.test.controller;

import jakarta.servlet.http.HttpSession;
import kjhtest.test.domain.BoardDTO;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * jakarta.servlet.http.HttpSession : 세션에 로그인 사용자 저장/조회용.
 * MultipartFile : 업로드된 파일을 받는 Spring 타입.
 * FileSystemResource, ResponseEntity : 파일을 스트림으로 내려줄 때 사용.
 * @Controller, @RequestMapping("/board") : 이 클래스의 모든 핸들러 기본 경로가 /board.
 */
@Controller
@RequestMapping("/board")
public class BoardController {
    /**
     * BoardController
     * ----------------------------------------
     * - 클라이언트 요청을 처리하는 계층
     * - URL 매핑 → Service 호출 → 결과를 View(HTML)에 전달
     * - 페이징 시 page 파라미터 처리
     *
     */


    public BoardController(BoardService service, BoardFileService boardFileService) {
        this.service = service;
        this.boardFileService = boardFileService;
    }

    private final BoardService service;
    private final BoardFileService boardFileService;



    // 한 페이지당 보여줄 글 개수
    private static final int PAGE_SIZE = 10;
    /*
    의미: BoardService를 생성자 주입으로 받음. 테스트/불변성에 유리.
    권장: @RequiredArgsConstructor 사용하면 더 간결합니다 (Lombok).
     */

    /**
     * 게시글 목록 + 페이징 처리
     * @param page 요청한 페이지 번호 (기본값 1)
     */
    @GetMapping
    public String list(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

        // 전체 게시글 개수 조회
        int totalCount = service.getTotalCount();

        // 총 페이지 개수 계산
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        // 현재 페이지의 게시글 목록 조회
        List<BoardDTO> boards = service.getPageList(page, PAGE_SIZE);

        // HTML로 전달할 데이터 등록
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "list"; // templates/list.html
    }

    @GetMapping("/write")
    public String writeForm(HttpSession session) {
        /*
        로그인 체크 후 비로그인 시 /logins로 리다이렉트(※ 보통 /login이 관례).
        권장: 리다이렉트 주소 일관성, 메시지 전달(예: ?error=needLogin) 고려.
         */
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/logins";
        }
        return "write";
    }

    /*
    Current request is not a multipart request
    → HTML 폼에 enctype="multipart/form-data"가 반드시 있어야 함.
    @RequestParam("file")의 name과 input의 name 일치 필요.
    세션에서 member가 null일 때 예외가 발생할 수 있으니 방어 코드 필요:
     */
    /** 글 저장 + 파일 업로드 */
    /*
    @PostMapping("/write")
    public String write(@ModelAttribute BoardDTO board,
                        @RequestParam("file") MultipartFile file,
                        HttpSession session) throws Exception {

        // 세션에서 로그인 사용자 정보 가져오기
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        board.setWriter(member.getUsername());

        // 1) 게시글 저장
        int boardId = service.write(board, file);

        // 2) 파일 저장 (DB + 실제 파일 저장)
        if (!file.isEmpty()) {
            boardFileService.uploadFile(file, boardId);
        }

        return "redirect:/board";
    }

     */
    @PostMapping("/write")
    public String write(@ModelAttribute BoardDTO board,
                        @RequestParam("file") MultipartFile file,
                        HttpSession session) throws Exception {
        // 1. 로그인 사용자 가져오기
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        if (member == null) {
            return "redirect:/login"; // 로그인 안 되어있으면 로그인 페이지로 이동
        }

        // 작성자 설정
        board.setWriter(member.getUsername());

        // 2. 게시글 저장 → 생성된 게시글 ID 반환
        long boardId = service.write(board,file);

        // 3. 파일 업로드 처리 (파일이 있을 경우에만)
        if (file != null && !file.isEmpty()) {
            boardFileService.uploadFile(file, boardId);
        }

        return "redirect:/board";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("board", service.getDetail(id));
        return "detail";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/board";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") int id, @ModelAttribute BoardDTO board) {
        board.setId((long) id);
        service.update(board);
        return "redirect:/board/" + id;
    }

    // 다운로드 기능
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws UnsupportedEncodingException {
        BoardDTO board = service.getDetail(id);
        String uploadDir = "uploads"; // service @Value 값과 동일해야 함
        FileSystemResource resource = new FileSystemResource(uploadDir + "/" + board.getFilename());

        String encodedName = URLEncoder.encode(board.getOriginalFilename(), "UTF-8").replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedName + "\"")
                .body(resource);
    }
}