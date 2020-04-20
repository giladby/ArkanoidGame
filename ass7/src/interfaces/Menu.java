package interfaces;

/**
 * The Menu interface.
 *
 * @param <T> the parameter type.
 */
public interface Menu<T> extends Animation {
    /**
     * Add selection, according several given parameters.
     *
     * @param key       the key the user has to press in order to choose this option.
     * @param message   the message to present.
     * @param returnVal the operation to make.
     */
    void addSelection(String key, String message, T returnVal);

    /**
     * @return the menu's status.
     */
    T getStatus();

    /**
     * Restart the menu's status, already-pressed-a-bottom and stop mode.
     */
    void restart();

    /**
     * Add sub menu, according several parameters.
     *
     * @param key     the key the user has to press in order to choose this option.
     * @param message the message to present.
     * @param subMenu the sub menu.
     */
    void addSubMenu(String key, String message, Menu<T> subMenu);

    /**
     * Set the menu's background image by the given image path.
     *
     * @param path the given path.
     */
    void setBackground(String path);
}