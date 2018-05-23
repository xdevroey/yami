package be.yami.java;

import be.yami.SequenceEntry;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a method call in a Java program.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class MethodCall implements SequenceEntry{

    private final String className;

    private final String methodName;

    private final List<String> parameterTypes;

    public MethodCall(String className, String methodName, String... types) {
        this(className, methodName, Arrays.asList(types));
    }

    public MethodCall(String methodClass, String methodName, List<String> types) {
        this.className = methodClass;
        this.methodName = methodName;
        this.parameterTypes = new ArrayList<>();
        this.parameterTypes.addAll(types);
    }

    public String getMethodClass() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getParameterType(int i) {
        Preconditions.checkPositionIndex(i, parametersCount(), "Index " + i + " out of bonds, must be between 0 and " + parametersCount());
        return parameterTypes.get(i);
    }

    public int parametersCount() {
        return parameterTypes.size();
    }

    public Iterator<String> parameters() {
        return parameterTypes.iterator();
    }

    @Override
    public String toString() {
        return "MethodCall{" + "className=" + className + ", methodName=" + methodName + ", parameterTypes=" + parameterTypes + '}';
    }

}
