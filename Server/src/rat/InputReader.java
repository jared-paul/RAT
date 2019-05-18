package rat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class InputReader implements Runnable
{
    private BufferedReader reader;
    private Socket clientSocket;

    public InputReader(ServerClient serverClient) throws IOException
    {
        this.clientSocket = serverClient.getClientSocket();
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println(clientSocket.toString());
    }

    @Override
    public void run()
    {
        while (!clientSocket.isClosed())
        {
            try
            {
                String string;

                while ((string = reader.readLine()) != null )
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
    }
}
