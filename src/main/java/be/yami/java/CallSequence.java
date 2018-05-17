package be.yami.java;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a sequence of Java methods calls.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class CallSequence implements Iterable<MethodCall> {

    private final List<MethodCall> lst;

    public CallSequence() {
        this.lst = new ArrayList<>();
    }

    public MethodCall get(int i) {
        Preconditions.checkPositionIndex(i, callsCount(), "Index " + i + " out of bonds, must be between 0 and " + callsCount());
        return lst.get(i);
    }

    public int callsCount() {
        return lst.size();
    }

    @Override
    public Iterator<MethodCall> iterator() {
        return lst.iterator();
    }
}
