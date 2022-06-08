package dictionary.service;

import dictionary.dao.Dao;

import java.util.Optional;

/**
 * Establishes a set of available operations and coordinates the application's response in each operation.
 */
public class Service {
    Dao dao;

    /**
     * Constructor for DI
     *
     * @param dao DI
     */
    public Service(Dao dao) {
        this.dao = dao;
    }

    public String showAllRows() {
        return dao.showAll();
    }

    public boolean addRow(String key, String value) {
        return dao.add(key, value);
    }

    public boolean deleteRow(String key) {
        return dao.delete(key);
    }

    public Optional<String> searchRow(String key) {
        return dao.search(key);
    }
}
