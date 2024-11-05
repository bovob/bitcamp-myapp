package bitcamp.myapp.controller;


import bitcamp.myapp.service.UserService;
import bitcamp.myapp.vo.User;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor // final 붙은거 의존성 생성자 만들어 줌
public class AuthController {

    // final이 붙으면 생성자에서 반드시 초기화 시켜야한다.
    private final UserService userService;

    @GetMapping("form")
    public void form(@CookieValue(required = false) String email, Model model) {
        model.addAttribute("email", email);
    }

    @PostMapping("login")
    public String login(
        boolean saveEmail,
        String email,
        String password,
        HttpServletResponse res,
        HttpSession session) throws Exception {

        User user = userService.exists(email, password);
        if (user == null) {
            res.setHeader("Refresh", "2; url=form");
            return "auth/fail";
        }

        if (saveEmail) {
            Cookie cookie = new Cookie("email", email);
            cookie.setMaxAge(60 * 60 * 24 * 7);
            res.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("email", "test@test.com");
            cookie.setMaxAge(0);
            res.addCookie(cookie);
        }

        session.setAttribute("loginUser", user);
        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
