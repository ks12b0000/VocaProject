package backend.VocaProject.csv;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CsvService {

    List<String[]> csvDown(String categoryName);

    void csvInsert(File dest) throws IOException;
}
