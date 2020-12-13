import java.io.IOException;
import java.net.*;


public class Client {
    public static void main(String[] args){
        DatagramSocket sc = null;

        try{
            sc = new DatagramSocket();

            //Server port number
            int portNum = 1234;

            //Create InetAddress object on localhost
            InetAddress ad = InetAddress.getByName("localhost");

            //Prepare byte array of message
            byte[] data = "Hello".getBytes();

            /*Create packet which contains our data and labeled to reach
            to the port and IP address mentioned
            */
            DatagramPacket dp = new DatagramPacket(data, data.length,ad,portNum);

            //Send the packet using datagram socket
            sc.send(dp);

            //Receive response from sent by process at the other end
            data = new byte[100];
            sc.receive(new DatagramPacket(data, data.length));

            System.out.println("Message Server: " + new String(dp.getData()));
            data = "quit".getBytes();
            sc.send(new DatagramPacket(data,data.length,ad,portNum));
        }
        catch (SocketException e){
            System.out.println(e.getMessage());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
