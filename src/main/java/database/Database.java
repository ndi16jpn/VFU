package database;

public interface Database {

    /**
     * Gets a selector for database content
     */
    DatabaseSelector getSelector();
    /**
     * Gets an inserter for database content
     */
    DatabaseInserter getInserter();
    /**
     * Gets a deleter for database content
     */
    DatabaseDeleter getDeleter();

}
