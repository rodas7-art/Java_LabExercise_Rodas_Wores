import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        try {

            Socket socket =
                    new Socket("localhost", 5000);

            System.out.println("Connected to server!");

            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter
                    (socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message =
                            reader.readLine()) != null) {
                        System.out.println(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();
            while (true) {
                String message = scanner.nextLine();
                writer.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}