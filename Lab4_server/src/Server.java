import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){
        ServerSocket sc = null;
        Socket scc = null;
        BufferedReader bfr = null;
        DataOutputStream dout = null;
        try {
            sc = new ServerSocket(3001);

            scc = sc.accept();

            //Go into infinite loop unless client ask to quit
            while(true){
                bfr = new BufferedReader(new InputStreamReader(scc.getInputStream()));
                String clientdata = bfr.readLine();

                if (clientdata.equals("quit")){
                    break;
                }

                System.out.println("Message from Client is: " + clientdata);
                dout = new DataOutputStream(scc.getOutputStream());
                dout.writeBytes("Hey Client" + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sc.close();
                scc.close();
                bfr.close();
                dout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}