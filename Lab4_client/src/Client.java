import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket sc = null; // Need to initialize as we are closing in finally block
        BufferedReader bf = null;
        DataOutputStream dout = null;
        try{
            //Binding socket to 50001 port number on localhost
            sc = new Socket("localhost",3001);

            dout = new DataOutputStream(sc.getOutputStream());

            //Sends data to the other end.
            dout.writeBytes("Hello Server" + "\n");

            bf = new BufferedReader(new InputStreamReader(sc.getInputStream()));

            //Print data received from other end on local console
            System.out.println("Message from Server: " + bf.readLine());

            //Quit server
            dout.writeBytes("quit");

        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                bf.close();
                sc.close();
                dout.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
