package bitcamp.myapp.controller;

import bitcamp.myapp.service.StorageService;
import bitcamp.myapp.service.UserService;
import bitcamp.myapp.vo.User;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final StorageService storageService;
    private final UserService userService;

    private String folderName = "user/";

    @GetMapping("form")
    public String form() {
        return "user/form";
    }

    @PostMapping
    public String add(User user, MultipartFile file) throws Exception {
        // 첨부 파일을 Object Storage에 올린다.
        // 클라이언트가 보낸 파일을 저장할 때 다른 파일 이름과 충돌나지 않도록 임의의 새 파일 이름을 생성한다.
        String fileName = UUID.randomUUID().toString();

        HashMap<String, Object> options = new HashMap<>();
        options.put(StorageService.CONTENT_TYPE, file.getContentType());

        storageService.upload(
            folderName + fileName, // 업로드 파일의 경로(폴더 경로 포함)
            file.getInputStream(), // 업로드 파일 데이터를 읽어 들일 입력스트림
            options);

        user.setPhoto(fileName); // DB에 저장할 사진 파일 이름 설정

        userService.add(user);
        return "redirect:../users";
    }

    @GetMapping
    public String list(Model model) throws Exception {
        List<User> list = userService.list();
        model.addAttribute("list", list);
        return "user/list";
    }

    @GetMapping("{no}")
    public String view(
        @PathVariable int no,
        Model model) throws Exception {
        User user = userService.get(no);
        model.addAttribute("user", user);
        return "user/view";
    }

    @GetMapping("myInfo")
    public String myInfo(
        HttpSession session,
        Model model) throws Exception {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new Exception("로그인이 필요합니다.");
        }

        User user = userService.get(loginUser.getNo());
        model.addAttribute("user", user);
        return "user/view";
    }

    @PostMapping("{no}")
    public String update(
        @PathVariable int no,
        User user,
        MultipartFile file) throws Exception {

        user.setNo(no);

        User old = userService.get(no);

        if (file != null && file.getSize() > 0) {
            storageService.delete(folderName + old.getPhoto());

            String fileName = UUID.randomUUID().toString();
            HashMap<String, Object> options = new HashMap<>();
            options.put(StorageService.CONTENT_TYPE, file.getContentType());

            storageService.upload(
                folderName + fileName, // 업로드 파일의 경로(폴더 경로 포함)
                file.getInputStream(), // 업로드 파일 데이터를 읽어 들일 입력스트림
                options);

            user.setPhoto(fileName);
        } else {
            user.setPhoto(old.getPhoto());
        }

        if (userService.update(user)) {
            return "redirect:../users";
        } else {
            throw new Exception("없는 회원입니다!");
        }
    }

    @Transactional
    @DeleteMapping("{no}")
    @ResponseBody
    public String delete(@PathVariable int no) throws Exception {
        //여기에 두면 파일먼저 삭제
        User old = userService.get(no);

        if (userService.delete(no)) {
            // 여기에 두면 데이터베이스 에서 부터 삭제
            storageService.delete(folderName + old.getPhoto());
            return "success";
        } else {
            return "failure";
            // throw new Exception("없는 회원입니다.");
        }
    }
}
