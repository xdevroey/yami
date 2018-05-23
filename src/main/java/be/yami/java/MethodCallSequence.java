package be.yami.java;

import be.yami.Sequence;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a sequence of Java methods calls.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class MethodCallSequence implements Sequence<MethodCall>{

    private final String className;
    private final List<MethodCall> lst;

    public MethodCallSequence(String className) {
        this.className = className;
        this.lst = new ArrayList<>();
    }

    public MethodCall get(int i) {
        Preconditions.checkPositionIndex(i, callsCount(), "Index " + i + " out of bonds, must be between 0 and " + callsCount());
        return lst.get(i);
    }

    public int callsCount() {
        return lst.size();
    }

    public String getClassName() {
        return className;
    }
    
    public void add(MethodCall call){
        lst.add(call);
    }
    
    @Override
    public Iterator<MethodCall> iterator() {
        return lst.iterator();
    }

    @Override
    public String toString() {
        return "MethodCallSequence{" + "className=" + className + ", lst=" + lst + '}';
    }
    
}
