import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clients =
            new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            writer = new PrintWriter(
                    socket.getOutputStream(), true);

            clients.add(this);
            System.out.println("New Client Connected!!!");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run () {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                System.out.println("Message: " + message);
                broadecastMessage(message);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }



    public void broadecastMessage(String message){

        for (ClientHandler client : clients) {
            client.writer.println(message);
        }
    }

}
