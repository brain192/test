package kjhtest.test.controller;

import jakarta.servlet.http.HttpSession;
import kjhtest.test.domain.MemberDTO;
import kjhtest.test.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    // 홈 페이지
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        model.addAttribute("member", loginMember);
        return "home";
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(@ModelAttribute MemberDTO member) {
        service.register(member);
        return "redirect:/login";
    }

    // 로그인 페이지
    @GetMapping("/logins")
    public String showLoginForm() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/logins")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        return service.login(username, password).map(member -> {
            session.setAttribute("loginMember", member);
            return "redirect:/";
        }).orElseGet(() -> {
            model.addAttribute("error", "아이디 또는 비밀번호 오류");
            return "login";
        });
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
