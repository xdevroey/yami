package be.yami.java;

import be.yami.ngram.ObjectKeyGenerator;
import java.util.Iterator;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class ClassMethodParametersKeyGenerator implements ObjectKeyGenerator<MethodCall> {

    private static ClassMethodParametersKeyGenerator instance = null;

    private ClassMethodParametersKeyGenerator() {
    }

    @Override
    public String generateKey(MethodCall call) {
        StringBuilder buff = new StringBuilder(String.format("%s.%s(", call.getMethodClass(), call.getMethodName()));
        Iterator<String> it = call.parameters();
        while (it.hasNext()) {
            buff.append(it.next());
            if (it.hasNext()) {
                buff.append(',');
            }
        }
        buff.append(')');
        return buff.toString();
    }

    public static ClassMethodParametersKeyGenerator getInstance() {
        return instance == null ? instance = new ClassMethodParametersKeyGenerator() : instance;
    }

}
