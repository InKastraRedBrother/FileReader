package ru.dictionary.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dictionary.dao.WordDAO;
import ru.dictionary.model.Language;
import ru.dictionary.model.Word;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ServiceWord {
    WordDAO wordDAO;
    ServiceLanguage serviceLanguage;

    public void addWord(UUID wordUUID, String wordValue, UUID wordLanguageUUID) {
        Word word = new Word();
        word.setWordUUID(wordUUID);
        word.setWordValue(wordValue);
        word.setWordLanguageUUID(wordLanguageUUID);

        wordDAO.saveWord(word);
    }

    public List<Word> getListByLanguageUUID(UUID languageUUID){
        return wordDAO.searchAllByUUID(languageUUID);
    }

    public Word getWordByUUID(UUID wordUUID) {
        return wordDAO.searchByUUID(wordUUID);
    }

    public Language getLanguageByWordUUID(UUID wordUUID) {
        return serviceLanguage.getLanguageByUUID(wordUUID);

    }
}
