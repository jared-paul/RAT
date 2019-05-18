package rat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable
{
    private int clients = 0;
    private ServerSocket serverSocket;
    private List<ServerClient> clientList = new ArrayList<>();

    public Server(int port) throws IOException
    {
        this.serverSocket = new ServerSocket(port);
        new Thread(new Sender(this)).start();
    }

    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    @Override
    public void run()
    {
        try
        {
            while (!serverSocket.isClosed())
            {
                ServerClient serverClient = new ServerClient(++clients, this, serverSocket.accept());
                clientList.add(serverClient);
                new Thread(serverClient).start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (this.serverSocket != null && !this.serverSocket.isClosed())
            {
                try
                {
                    this.serverSocket.close();
                    System.out.println("Closed server socket");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public ServerClient getClient(int number)
    {
        for (ServerClient serverClient : clientList)
        {
            if (serverClient.getClientNumber() == number)
            {
                return serverClient;
            }
        }

        return null;
    }
}
