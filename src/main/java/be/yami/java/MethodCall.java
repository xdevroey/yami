package be.yami.java;

import com.google.common.base.Preconditions;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a method call in a Java program.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class MethodCall {

    private final String methodClass;

    private final String methodName;

    private final List<String> parameterTypes;

    public MethodCall(String methodClass, String methodName, String... types) {
        this(methodClass, methodName, Arrays.asList(types));
    }

    public MethodCall(String methodClass, String methodName, List<String> types) {
        this.methodClass = methodClass;
        this.methodName = methodName;
        this.parameterTypes = new VirtualFlow.ArrayLinkedList<>();
        this.parameterTypes.addAll(types);
    }

    public String getMethodClass() {
        return methodClass;
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

}
