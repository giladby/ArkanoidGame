package interfaces;

/**
 * The Hit notifier interface.
 */
public interface HitNotifier {
    /**
     * Adds a given listener to the list.
     *
     * @param hl the given listener.
     */
    void addHitListener(HitListener hl);

    /**
     * Removes a given listener to the list.
     *
     * @param hl the given listener.
     */
    void removeHitListener(HitListener hl);
}
