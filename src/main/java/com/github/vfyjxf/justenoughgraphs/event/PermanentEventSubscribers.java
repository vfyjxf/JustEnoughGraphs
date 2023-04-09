package com.github.vfyjxf.justenoughgraphs.event;

import com.google.common.base.Preconditions;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.function.Consumer;

/**
 * From {@link mezz.jei.forge.events.PermanentEventSubscriptions}
 */
public class PermanentEventSubscribers {

    public static PermanentEventSubscribers getInstance() {
        Preconditions.checkState(instance != null, "PermanentEventSubscribers is not initialized yet.");
        return instance;
    }

    public static void init(IEventBus eventBus, IEventBus modEventBus) {
        instance = new PermanentEventSubscribers(eventBus, modEventBus);
    }


    private static PermanentEventSubscribers instance;

    private final IEventBus eventBus;
    private final IEventBus modEventBus;

    public PermanentEventSubscribers(IEventBus eventBus, IEventBus modEventBus) {
        this.eventBus = eventBus;
        this.modEventBus = modEventBus;
        instance = this;
    }

    public <T extends Event> void register(Class<T> eventType, Consumer<T> listener) {
        register(eventType, EventPriority.NORMAL, listener);
    }

    public <T extends Event> void register(Class<T> eventType, EventPriority priority, Consumer<T> listener) {
        if (IModBusEvent.class.isAssignableFrom(eventType)) {
            modEventBus.addListener(priority, false, eventType, listener);
        } else {
            eventBus.addListener(priority, false, eventType, listener);
        }
    }

}
