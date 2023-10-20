package backend.VocaProject.csv;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
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

        csvWriter.writeAll(csvService.listVocaBookString(categoryName));

        csvWriter.close();
        writer.close();
    }
}
