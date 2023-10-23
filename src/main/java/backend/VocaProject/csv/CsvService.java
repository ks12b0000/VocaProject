package backend.VocaProject.csv;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CsvService {

    List<String[]> csvDown(String categoryName);

    void csvInsert(MultipartFile file) throws IOException;
}
