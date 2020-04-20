package general;

/**
 * The Counter class.
 */
public class Counter {
    private int value;

    /**
     * Counter Constructor.
     * Initializes the counter object with a given start value.
     *
     * @param value the start value.
     */
    public Counter(int value) {
        this.value = value;
    }

    /**
     * Increases the counter's value by a given number.
     *
     * @param number the given number.
     */
    public void increase(int number) {
        this.value += number;
    }

    /**
     * Decreases the counter's value by a given number.
     *
     * @param number the given number.
     */
    public void decrease(int number) {
        this.value -= number;
    }

    /**
     * @return the counter's value.
     */
    public int getValue() {
        return this.value;
    }
}
