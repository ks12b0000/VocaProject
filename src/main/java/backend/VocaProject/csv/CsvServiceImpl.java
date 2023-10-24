package backend.VocaProject.csv;

import backend.VocaProject.domain.VocabularyBook;
import backend.VocaProject.domain.VocabularyBookCategory;
import backend.VocaProject.vocabularyBook.VocabularyBookRepository;
import backend.VocaProject.vocabularyBookCategory.VocabularyBookCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CsvServiceImpl implements CsvService{

    private final VocabularyBookRepository vocabularyBookRepository;

    private final VocabularyBookCategoryRepository vocabularyBookCategoryRepository;

    /**
     * Csv 내려받기
     * @param categoryName
     * @return
     */
    @Override
    public List<String[]> csvDown(String categoryName) {
        VocabularyBookCategory category = vocabularyBookCategoryRepository.findByName(categoryName);
        List<VocabularyBook> list = vocabularyBookRepository.findByVocabularyBookCategory(category);
        List<String[]> listStrings = new ArrayList<>();
        listStrings.add(new String[]{"단어", "의미", "단어장 카테고리"});
        for (VocabularyBook book: list) {
            String[] rowData = new String[3];
            rowData[0] = book.getWord();
            rowData[1] = book.getMeaning();
            rowData[2] = String.valueOf(book.getVocabularyBookCategory().getId());
            listStrings.add(rowData);
        }
        return listStrings;
    }

    /**
     * Csv 올리기
     * @param file
     * @throws IOException
     */
    @Override
    @Transactional
    public void csvInsert(MultipartFile file) throws IOException {
        String resourceSrc = System.getProperty("user.dir");
        File dest = new File(resourceSrc + file.getOriginalFilename());
        file.transferTo(dest);

        BufferedReader br = new BufferedReader(new FileReader(dest));
        String line;
        if((line = br.readLine()) != null) {
            while ((line = br.readLine()) != null) {
                String[] datalines = line.split(",");
                try {
                    String word = datalines[0];
                    String meaning = datalines[1];
                    Long categoryId = Long.valueOf(datalines[2]);
                    Optional<VocabularyBookCategory> category = vocabularyBookCategoryRepository.findById(categoryId);
                    // DB에 데이터 삽입
                    VocabularyBook vocabularyBook = new VocabularyBook(word, meaning, category.get());
                    vocabularyBookRepository.save(vocabularyBook);
                } catch (NumberFormatException e) {
                    continue;  // 첫번째 줄(제목 행) 제외하기 위함
                }
            }
            br.close();
        }
    }
}
