import java.io.IOException;
import java.net.*;

public class Server {
    public static void main(String[] args){
        DatagramSocket sc = null;
        try{
            sc = new DatagramSocket(1234);

            while (true){
                byte[] data = new byte[100];
                DatagramPacket dp = new DatagramPacket(data, data.length);

                sc.receive(dp);
                String clientdata =  new String(dp.getData()).trim();

                System.out.println("Message Client: " + clientdata);


                data = "Hey".getBytes();
                sc.send(new DatagramPacket(data,data.length,dp.getAddress(),dp.getPort()));
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            sc.close();
        }
    }
}