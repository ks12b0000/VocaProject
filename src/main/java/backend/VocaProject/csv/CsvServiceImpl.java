package backend.VocaProject.csv;

import backend.VocaProject.domain.VocaBook;
import backend.VocaProject.domain.VocaBookCategory;
import backend.VocaProject.response.BaseException;
import backend.VocaProject.vocaBook.VocaBookRepository;
import backend.VocaProject.vocaBookCategory.VocaBookCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static backend.VocaProject.response.BaseExceptionStatus.NON_EXISTENT_BOOK;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService{

    private final VocaBookRepository vocaBookRepository;

    private final VocaBookCategoryRepository vocaBookCategoryRepository;

    @Override
    public List<String[]> csvDown(String categoryName) {
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

    @Override
    @Transactional
    public void csvInsert(File dest) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(dest));
        String line;
        if((line = br.readLine()) != null) {
            while ((line = br.readLine()) != null) {
                String[] datalines = line.split(",");
                try {
                    String word = datalines[0];
                    String meaning = datalines[1];
                    Long categoryId = Long.valueOf(datalines[2]);
                    Optional<VocaBookCategory> category = vocaBookCategoryRepository.findById(categoryId);
                    // DB에 데이터 삽입
                    VocaBook vocaBook = new VocaBook(word, meaning, category.get());
                    vocaBookRepository.save(vocaBook);
                } catch (NumberFormatException e) {
                    continue;  // 첫번째 줄(제목 행) 제외하기 위함
                }
            }
            br.close();
        }
    }
}
