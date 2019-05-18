package rat.plugins;

import rat.ClientMain;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractPlugin implements Plugin
{
    public boolean stop = false;
    public boolean running = false;
    private UUID uuid;

    public AbstractPlugin()
    {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public void run()
    {
        initialize();

        while (!stop)
        {
            onTick();
        }
    }

    public abstract void initialize();

    @Override
    public boolean isRunning()
    {
        return running;
    }

    @Override
    public void stop()
    {
        this.stop = true;
        this.running = false;
    }

    @Override
    public void start()
    {
        new Thread(this).start();
        this.running = true;
        ClientMain.getPluginHandler().addActivePlugin(this);
    }

    public UUID getUUID()
    {
        return uuid;
    }

    @Override
    public AbstractPlugin clone()
    {
        try
        {
            return (AbstractPlugin) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
