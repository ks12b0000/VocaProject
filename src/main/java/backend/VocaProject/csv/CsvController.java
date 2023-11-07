package backend.VocaProject.csv;

import backend.VocaProject.response.BaseResponse;
import com.opencsv.CSVWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Csv", description = "Csv 관련 API")
@RequestMapping("/api/csv")
public class CsvController {
    private final CsvService csvService;

    @Operation(summary = "Csv 내려받기 API")
    @Tag(name = "Csv")
    @GetMapping
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

    @Operation(summary = "Csv 올리기 API", responses = {
            @ApiResponse(responseCode = "201", description = "csv 파일 올리기에 성공했습니다.")
    })
    @Tag(name = "Csv")
    @PostMapping
    public ResponseEntity csvInsert(@RequestParam("file") MultipartFile file) throws IOException {
        // 파일을 읽어 디비 저장 함수
        csvService.csvInsert(file);

        return ResponseEntity.created(URI.create("/api/csv")).body(new BaseResponse<>(201, "csv 파일 올리기에 성공했습니다."));
    }
}
