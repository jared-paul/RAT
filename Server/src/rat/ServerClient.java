package rat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerClient implements Runnable
{
    private Server host;
    private Socket clientSocket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private PrintWriter printWriter;
    private BufferedReader reader;
    private int clientNumber;

    private Scanner scanner;

    public ServerClient(int clientNumber, Server server, Socket clientSocket) throws IOException
    {
        this.clientNumber = clientNumber;
        this.host = server;

        this.clientSocket = clientSocket;
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        this.inputStream = new DataInputStream(clientSocket.getInputStream());
        this.printWriter = new PrintWriter(outputStream, true);
        this.reader = new BufferedReader(new InputStreamReader(inputStream));

        this.scanner = new Scanner(System.in);
    }

    public int getClientNumber()
    {
        return clientNumber;
    }

    public PrintWriter getPrintWriter()
    {
        return printWriter;
    }

    @Override
    public void run()
    {
        System.out.println("Client connected!");

        System.out.println(clientSocket.toString());

        while (!clientSocket.isClosed() )
        {
            try
            {
                String string;

                while ((string = reader.readLine()) != null)
                {
                    System.out.println(string);
                }

                System.out.println("nice");
            }
            catch (IOException e)
            {
                e.printStackTrace();
                try
                {
                    clientSocket.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }

        System.out.println("Client " + clientNumber + " Disconnected!");
    }

    public Socket getClientSocket()
    {
        return clientSocket;
    }
}
