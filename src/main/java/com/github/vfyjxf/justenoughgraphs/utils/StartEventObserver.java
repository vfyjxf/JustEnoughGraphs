package com.github.vfyjxf.justenoughgraphs.utils;

import com.github.vfyjxf.justenoughgraphs.event.PermanentEventSubscribers;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * From {@link mezz.jei.forge.startup.StartEventObserver}
 */
public class StartEventObserver implements ResourceManagerReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<Class<? extends Event>> REQUIRED_EVENTS = Set.of(TagsUpdatedEvent.class, RecipesUpdatedEvent.class);

    private enum State {
        DISABLED, ENABLED, STARTED
    }

    private final Set<Class<? extends Event>> observedEvents = new HashSet<>();
    private final Runnable startRunnable;
    private final Runnable stopRunnable;
    private State state = State.DISABLED;

    public StartEventObserver(Runnable startRunnable, Runnable stopRunnable) {
        this.startRunnable = startRunnable;
        this.stopRunnable = stopRunnable;
    }

    public void register(PermanentEventSubscribers subscriptions) {
        REQUIRED_EVENTS
                .forEach(eventClass -> subscriptions.register(eventClass, this::onEvent));

        subscriptions.register(ClientPlayerNetworkEvent.LoggingIn.class, event -> {
            LOGGER.info("JEGh StartEventObserver received {}", event.getClass());
            if (this.state == State.DISABLED) {
                transitionState(State.ENABLED);
            }
        });

        subscriptions.register(ClientPlayerNetworkEvent.LoggingOut.class, event -> {
            if (event.getPlayer() != null) {
                LOGGER.info("JEGh StartEventObserver received {}", event.getClass());
                transitionState(State.DISABLED);
            }
        });
    }

    /**
     * Observe an event and start JEI if we have observed all the required events.
     */
    private <T extends Event> void onEvent(T event) {
        if (this.state == State.DISABLED) {
            return;
        }
        LOGGER.info("JEGh StartEventObserver received {}", event.getClass());
        Class<? extends Event> eventClass = event.getClass();
        if (REQUIRED_EVENTS.contains(eventClass) &&
                observedEvents.add(eventClass) &&
                observedEvents.containsAll(REQUIRED_EVENTS)
        ) {
            if (this.state == State.STARTED) {
                restart();
            } else {
                transitionState(State.STARTED);
            }
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        restart();
    }

    private void restart() {
        if (this.state != State.STARTED) {
            return;
        }
        transitionState(State.DISABLED);
        transitionState(State.ENABLED);
        transitionState(State.STARTED);
    }

    private void transitionState(State newState) {
        LOGGER.info("JEGh StartEventObserver transitioning state from " + this.state + " to " + newState);

        switch (newState) {
            case DISABLED -> {
                if (this.state == State.STARTED) {
                    this.stopRunnable.run();
                }
            }
            case ENABLED -> {
                if (this.state != State.DISABLED) {
                    throw new IllegalStateException("Attempted Illegal state transition from " + this.state + " to " + newState);
                }
            }
            case STARTED -> {
                if (this.state != State.ENABLED) {
                    throw new IllegalStateException("Attempted Illegal state transition from " + this.state + " to " + newState);
                }
                this.startRunnable.run();
            }
        }

        this.state = newState;
        this.observedEvents.clear();
    }
}
