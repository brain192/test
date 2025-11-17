package kjhtest.test.controller;

import jakarta.servlet.http.HttpSession;
import kjhtest.test.domain.BoardDTO;
import kjhtest.test.domain.MemberDTO;
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

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService service;

    private static final int PAGE_SIZE = 10;

    public BoardController(BoardService service) {
        this.service = service;
    }

    // 게시글 목록 (페이징 포함)
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "1") int page, // 기본 페이지: 1
            Model model) {

        int totalCount = service.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        List<BoardDTO> boards = service.getPageList(page, PAGE_SIZE);

        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "list";
    }

    @GetMapping("/write")
    public String writeForm(HttpSession session) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/logins";
        }
        return "write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute BoardDTO board,
                        @RequestParam("file") MultipartFile file,
                        HttpSession session) throws Exception {
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        board.setWriter(member.getUsername());
        service.write(board, file);
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
    public String update(@PathVariable("id") Long id, @ModelAttribute BoardDTO board) {
        board.setId(id);
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