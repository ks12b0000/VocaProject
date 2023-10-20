package backend.VocaProject.csv;

import backend.VocaProject.domain.VocaBook;
import backend.VocaProject.domain.VocaBookCategory;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.vocaBook.VocaBookRepository;
import backend.VocaProject.vocaBookCategory.VocaBookCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static backend.VocaProject.response.BaseExceptionStatus.NON_EXISTENT_BOOK;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService{

    private final VocaBookRepository vocaBookRepository;

    private final VocaBookCategoryRepository vocaBookCategoryRepository;

    @Override
    public List<String[]> listVocaBookString(String categoryName) {
        VocaBookCategory category = vocaBookCategoryRepository.findByName(categoryName);
        List<VocaBook> list = vocaBookRepository.findByVocaBookCategory(category);
        List<String[]> listStrings = new ArrayList<>();
        listStrings.add(new String[]{"단어", "의미", "단어장 카테고리"});
        for (VocaBook book: list) {
            String[] rowData = new String[3];
            rowData[0] = book.getWord();
            rowData[1] = book.getMeaning();
            rowData[2] = String.valueOf(book.getVocaBookCategory().getId());
            listStrings.add(rowData);
        }
        return listStrings;
    }
}
