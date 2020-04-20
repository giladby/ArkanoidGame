package interfaces;

/**
 * The Task interface.
 *
 * @param <T> the parameter type.
 */
public interface Task<T> {
    /**
     * Run the operation and return it's value.
     *
     * @return the value.
     */
    T run();
}
