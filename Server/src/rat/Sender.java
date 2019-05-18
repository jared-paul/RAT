package rat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Sender implements Runnable
{
    private Server server;
    private Scanner scanner;
    private int clientNumber;

    public Sender(Server server) throws IOException
    {
        this.server = server;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run()
    {
        while (!server.getServerSocket().isClosed())
        {
            System.out.println("test1");

            if (scanner.hasNext())
            {
                try
                {
                    String string = scanner.nextLine();

                    char clientNumber = string.charAt(0);

                    if (Character.isDigit(clientNumber) && string.startsWith(clientNumber + ": "))
                    {
                        System.out.println("printing");
                        ServerClient client = server.getClient(Character.getNumericValue(clientNumber));

                        System.out.println(string.substring(3));
                        client.getPrintWriter().println(string.substring(3));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
