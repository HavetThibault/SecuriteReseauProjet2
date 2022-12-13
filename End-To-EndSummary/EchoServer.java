import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public
class EchoServer {
    public
            static
    void
            main(String[] arstring) {
        try {
            System.setProperty("javax.net.ssl.keyStore","mySrvKeystore");
            System.setProperty("javax.net.ssl.keyStorePassword","123456");
            
            SSLServerSocketFactory sslserversocketfactory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket =
                    (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);
            while(true)
            {
            	SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();


            	InputStream inputstream = sslsocket.getInputStream();
            	InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            	BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            	new myThread(bufferedreader).start();
            }

            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}


 
    