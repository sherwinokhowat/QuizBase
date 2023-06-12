package utility;

/**
 * An exception thrown when an error occurs in the SQLStatementBuilder class.
 *
 * @author Ricky Qin
 */
public class SQLStatementBuilderException extends RuntimeException {

    /**
     * Creates a SQLStatementBuilderExceptioin class
     * @param message the message to be passed on.
     */
    public SQLStatementBuilderException(String message) {
        super(message);
    }
}
