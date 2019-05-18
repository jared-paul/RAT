package rat.plugins;

public interface Plugin extends Addon, Cloneable
{
    byte getByte();

    void onTick();

    void stop();

    void start();

    boolean isRunning();

    Plugin clone();
}
