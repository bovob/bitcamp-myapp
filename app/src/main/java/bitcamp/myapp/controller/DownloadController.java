package bitcamp.myapp.controller;

import bitcamp.myapp.service.BoardService;
import bitcamp.myapp.service.StorageService;
import bitcamp.myapp.vo.AttachedFile;
import bitcamp.myapp.vo.User;
import java.io.OutputStream;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class DownloadController {

    private final BoardService boardService;
    private final StorageService storageService;

    // private Map<String, String> downloadPathMap = new HashMap<>();

    // public DownloadController(BoardService boardService, StorageService storageService) {
    //     this.boardService = boardService;
    //     this.storageService = storageService;
    //     // this.downloadPathMap.put("board", ctx.getRealPath("board/"));
    //     // this.downloadPathMap.put("user", ctx.getRealPath("/upload/user"));
    //     // this.downloadPathMap.put("project", ctx.getRealPath("/upload/project"));
    // }

    @GetMapping("/download")
    public HttpHeaders download(
        String path,
        int fileNo,
        HttpSession session,
        OutputStream out) throws Exception {

        HttpHeaders headers = new HttpHeaders();

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new Exception("로그인 하지 않았습니다.");
        }

        // String downloadDir = downloadPathMap.get(path);

        AttachedFile attachedFile = boardService.getAttachedFile(fileNo);

        headers.add("Content-Disposition",
            String.format("attachment; filename=\"%s\"", attachedFile.getOriginFilename())
        );

        storageService.download(path + "/" + attachedFile.getFilename(), out);

        return headers;
    }
}
