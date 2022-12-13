import java.io.BufferedReader;

public class myThread extends Thread {
			  public BufferedReader br;
			  
			  public myThread(BufferedReader buf)
			  {
			  	br = buf;
			  }
			  
        public void run() { 
                try {
                    String string = null;
			          while ((string = br.readLine()) != null) {
			              System.out.println(string);
			              System.out.flush();
			          }
                } catch (Exception e) {}
            }
        }