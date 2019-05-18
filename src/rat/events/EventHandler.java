package rat.events;

import java.util.HashMap;
import java.util.Map;

public class EventHandler
{
    public Map<EventType, Event> events = new HashMap<>();

    public EventHandler()
    {

    }

    public void callEvent(EventType eventType)
    {
        events.get(eventType).call();
    }


}
