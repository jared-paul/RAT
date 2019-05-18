package rat.plugins.brightness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BrightnessUtil
{
    public static void setBrightness(double brightness) throws IOException
    {
        String commandFormat = String.format("$brightness = %d;", brightness)
                + "$delay = " + "0" + ";"
                + "$myMonitor = Get-WmiObject -Namespace root\\wmi -Class WmiMonitorBrightnessMethods;"
                + "$myMonitor.wmisetbrightness($delay, $brightness)";
        String command = "powershell.exe  " + commandFormat;

        Process powerShellProcess = Runtime.getRuntime().exec(command);

        powerShellProcess.getOutputStream().close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));

        String line = bufferedReader.readLine();

        if (line != null)
        {
            System.err.println("Error: ");
            do
            {
                System.err.println(line);
            } while ((line = bufferedReader.readLine()) != null);
        }
    }
}
