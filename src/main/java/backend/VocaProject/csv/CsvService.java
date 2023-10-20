package backend.VocaProject.csv;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CsvService {

    List<String[]> listVocaBookString(String categoryName);
}
