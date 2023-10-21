package backend.VocaProject.csv;

import backend.VocaProject.response.BaseResponse;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class CsvDownController {
    private final CsvService csvService;

    @GetMapping("/api/csv/down")
    public void csvDown(HttpServletResponse response, @RequestParam String categoryName) throws IOException {
        response.setContentType("text/csv; charset=UTF-8"); // Set the character encoding
        String fileName = URLEncoder.encode(categoryName, "UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"");

        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(),
                StandardCharsets.UTF_8);
        writer.write("\uFEFF");
        CSVWriter csvWriter = new CSVWriter(writer);

        csvWriter.writeAll(csvService.csvDown(categoryName));

        csvWriter.close();
        writer.close();
    }

    @PostMapping("/api/csv/insert")
    public BaseResponse csvInsert(@RequestParam("file") MultipartFile file) throws IOException {
        String resourceSrc = System.getProperty("user.dir") + "\\src\\main\\resources\\static";
        File dest = new File(resourceSrc + file.getOriginalFilename());
        file.transferTo(dest);

        // 파일을 읽어 디비 저장 함수
        csvService.csvInsert(dest);

        return new BaseResponse("csv 파일 올리기에 성공했습니다.");
    }
}
