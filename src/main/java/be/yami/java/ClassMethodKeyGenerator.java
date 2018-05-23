package be.yami.java;

import be.yami.ngram.ObjectKeyGenerator;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class ClassMethodKeyGenerator implements ObjectKeyGenerator<MethodCall> {
    
    private static ClassMethodKeyGenerator instance = null;
    
    private ClassMethodKeyGenerator(){}
    
    @Override
    public String generateKey(MethodCall call) {
        return String.format("%s.%s", call.getMethodClass(), call.getMethodName());
    }
    
    public static ClassMethodKeyGenerator getInstance(){
        return instance == null ? instance = new ClassMethodKeyGenerator() : instance;
    }
    
}
