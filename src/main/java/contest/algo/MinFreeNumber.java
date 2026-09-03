package contest.algo;

import java.util.NavigableSet;
import java.util.TreeSet;

public class MinFreeNumber {

    // TreeSet, а не куча: куча не заметила бы, что номер освободили дважды
    private final NavigableSet<Integer> released = new TreeSet<>();
    private final int firstNumber;
    private int nextFresh;

    public MinFreeNumber(int firstNumber) {
        this.firstNumber = firstNumber;
        this.nextFresh = firstNumber;
    }

    public int acquire() {
        Integer reused = released.pollFirst();
        return reused != null ? reused : nextFresh++;
    }

    public void release(int number) {
        if (number < firstNumber || number >= nextFresh) {
            throw new IllegalArgumentException("номер " + number + " не выдавался");
        }
        if (!released.add(number)) {
            throw new IllegalStateException("номер " + number + " уже свободен");
        }
    }
}
