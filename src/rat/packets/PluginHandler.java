package rat.packets;

import rat.plugins.Addon;
import rat.plugins.Plugin;
import rat.plugins.brightness.BrightnessPlugin;
import rat.plugins.trackpad.TrackpadPlugin;
import rat.plugins.wifi.WiFiPlugin;

import java.util.*;

public class PluginHandler
{
    private Map<Byte, Plugin> plugins = new HashMap<>();

    private List<Plugin> activePlugins = new ArrayList<>();

    public PluginHandler()
    {
        initialize();
    }

    private void initialize()
    {
        plugins.put((byte) 1, new BrightnessPlugin(1, 10000));
        plugins.put((byte) 2, new WiFiPlugin(1000000));
        plugins.put((byte) 3, new TrackpadPlugin());
    }

    public void callAddon(Plugin plugin)
    {
    }

    public void callAddon(byte packet)
    {
        Plugin plugin = getPlugin(packet);

        activePlugins.add(plugin);
        new Thread(plugin).start();
    }

    public Plugin getPlugin(byte packet)
    {
        for (Plugin plugin : activePlugins)
        {
            if (plugin.getByte() == packet)
            {
                return plugin;
            }
        }

        return plugins.get(packet);
    }

    public void removeActivePlugin(byte packet)
    {
        Iterator<Plugin> pluginIterator = activePlugins.iterator();
        while (pluginIterator.hasNext())
        {
            Plugin plugin = pluginIterator.next();

            if (plugin.getByte() == packet)
            {
                pluginIterator.remove();
            }
        }
    }

    public void addActivePlugin(Plugin plugin)
    {
        activePlugins.add(plugin);
    }
}
