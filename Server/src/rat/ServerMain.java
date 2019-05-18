package rat;

import java.io.IOException;

public class ServerMain
{
    public static void main(String[] args)
    {
        try
        {
            new Thread(new Server(5403)).start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
