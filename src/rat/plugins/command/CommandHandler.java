package rat.plugins.command;

public class CommandHandler
{
    public static void callCommand(String command, CommandCallback callback)
    {
        new Thread(new CommandTask(command, callback)).start();
    }
}
