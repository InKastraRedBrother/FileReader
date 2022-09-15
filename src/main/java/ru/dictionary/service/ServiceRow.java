package ru.dictionary.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dictionary.config.DictionaryParameters;
import ru.dictionary.dao.InterfaceDAOWord;
import ru.dictionary.model.Language;
import ru.dictionary.model.Row;
import ru.dictionary.model.Word;
import ru.dictionary.model.dto.BuiltRow;
import ru.dictionary.model.dto.RequestAddPairWordsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Establishes a set of available operations and coordinates the application's response in each operation.
 */
@Component
@AllArgsConstructor
public class ServiceRow {

    InterfaceDAOWord dao;
    ServiceWord serviceWord;
    ServiceLanguage serviceLanguage;


    public List<BuiltRow> findAllRows() {

        List<Row> listWithRawRows = dao.findAll();

        List<BuiltRow> builtRowList = new ArrayList<>();

        for (Row listWithRawRow : listWithRawRows) {

            BuiltRow builtRow = new BuiltRow();

            Word wordKey = serviceWord.getWordById(listWithRawRow.getIdWordKey());
            Word wordValue = serviceWord.getWordById(listWithRawRow.getIdWordValue());
            builtRow.setKey(wordKey.getWordValue());
            builtRow.setValue(wordValue.getWordValue());
            Language languageKeyWord = serviceLanguage.getLanguageById(wordKey.getLanguageId());
            Language languageValueWord = serviceLanguage.getLanguageById(wordValue.getLanguageId());
            builtRow.setNameLanguageOfKey(languageKeyWord.getLanguageName());
            builtRow.setNameLanguageOfValue(languageValueWord.getLanguageName());

            builtRowList.add(builtRow);
        }
        return builtRowList;
    }

    public List<BuiltRow> findAllBySelectedLanguageId(String languageSourceId, String languageTargetId) {


        List<Word> listWords = serviceWord.getListByLanguageUUID(languageSourceId);
        List<Row> listRows = dao.findAll();
        List<BuiltRow> listBuiltRow = new ArrayList<>();
        for (Word word : listWords) {
            BuiltRow builtRow = new BuiltRow();
            String keyWord = word.getWordId();
            for (Row row : listRows) {
                if (row.getIdWordKey().equals(keyWord)) {
                    builtRow.setNameLanguageOfKey(serviceLanguage.getLanguageById(languageSourceId).getLanguageName());
                    builtRow.setKey(word.getWordValue());
                    Word wordTemp = serviceWord.getWordById(row.getIdWordValue());
                    builtRow.setValue(wordTemp.getWordValue());
                    builtRow.setNameLanguageOfValue(serviceWord.getLanguageByWordId(wordTemp.getLanguageId()).getLanguageName());
                    listBuiltRow.add(builtRow);
                }
            }
        }
        return listBuiltRow;
    }

    public void addPair(RequestAddPairWordsDTO requestAddPairWordsDTO) {

        String uuidWordKey = UUID.randomUUID().toString();
        String uuidWordValue = UUID.randomUUID().toString();

        serviceWord.addWord(uuidWordKey, requestAddPairWordsDTO.getWordKey(), requestAddPairWordsDTO.getLanguageSourceId());
        serviceWord.addWord(uuidWordValue, requestAddPairWordsDTO.getWordValue(), requestAddPairWordsDTO.getLanguageTargetId());

        Row row = new Row();
        row.setIdRow(UUID.randomUUID().toString());
        row.setIdWordKey(uuidWordKey);
        row.setIdWordValue(uuidWordValue);

        dao.save(row);
    }

    public boolean deleteRowByKey(String key, DictionaryParameters dictionaryParameters) {
        return dao.deleteByKey(key, dictionaryParameters.getFileName());
    }

    public Optional<Row> findRowByKey(String key, DictionaryParameters dictionaryParameters) {
        return dao.findByKey(key, dictionaryParameters.getFileName());
    }
}