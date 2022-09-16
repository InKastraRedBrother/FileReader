package ru.dictionary.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dictionary.dao.LanguageDAO;
import ru.dictionary.model.Language;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ServiceLanguage {

    private final LanguageDAO languageDAO;

    public List<Language> findAllLanguages() {
        return languageDAO.getAllLanguages();
    }

    public boolean addLanguage(Language language) {
        language.setLanguageId(UUID.randomUUID());
        return languageDAO.saveLanguage(language);
    }

    public Language getLanguageById(UUID uuidLanguage) {
        return languageDAO.searchById(uuidLanguage);
    }
}
