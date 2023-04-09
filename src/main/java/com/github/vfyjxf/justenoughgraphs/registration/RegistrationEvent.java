package com.github.vfyjxf.justenoughgraphs.registration;

import com.github.vfyjxf.justenoughgraphs.api.IRegistration;
import net.minecraftforge.eventbus.api.Event;

public class RegistrationEvent extends Event {

    private final RegistryManagerBuilder registration;

    public RegistrationEvent() {
        this.registration = new RegistryManagerBuilder();
        registration.build();
    }

    public IRegistration getRegistration() {
        return registration;
    }

}
