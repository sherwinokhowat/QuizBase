package utility;

/**
 * An exception thrown when an error occurs in the SQLStatementBuilder class.
 *
 * @author Ricky Qin
 */
public class SQLStatementBuilderException extends RuntimeException {
    public SQLStatementBuilderException(String message) {
        super(message);
    }
}
