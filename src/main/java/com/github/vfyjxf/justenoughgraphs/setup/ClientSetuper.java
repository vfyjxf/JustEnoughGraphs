package com.github.vfyjxf.justenoughgraphs.setup;

import com.github.vfyjxf.justenoughgraphs.JustEnoughGraphs;
import com.github.vfyjxf.justenoughgraphs.event.PermanentEventSubscribers;
import com.github.vfyjxf.justenoughgraphs.gui.JeiGuiHooker;
import com.github.vfyjxf.justenoughgraphs.integration.jegh.JeghRegister;
import com.github.vfyjxf.justenoughgraphs.registration.RegistrationEvent;
import com.github.vfyjxf.justenoughgraphs.utils.StartEventObserver;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.alg.mrtree.options.MrTreeOptions;
import org.eclipse.elk.core.data.LayoutMetaDataService;

public class ClientSetuper extends CommonSetuper {
    public static final Logger LOGGER = LogManager.getLogger();

    public ClientSetuper() {
        super();
        PermanentEventSubscribers.init(eventBus, modEventBus);
        PermanentEventSubscribers.getInstance()
                .register(RegisterClientReloadListenersEvent.class, this::onRegisterClientReloadListenersEvent);
        eventBus.register(new JeghRegister());
        eventBus.register(new JeiGuiHooker());
        LayoutMetaDataService service = LayoutMetaDataService.getInstance(JustEnoughGraphs.class.getClassLoader());
        service.registerLayoutMetaDataProviders(new LayeredOptions());
        service.registerLayoutMetaDataProviders(new MrTreeOptions());
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new JeghRegister());
    }

    public void onRegisterClientReloadListenersEvent(RegisterClientReloadListenersEvent event) {
        StartEventObserver observer = new StartEventObserver(this::start, this::stop);
        observer.register(PermanentEventSubscribers.getInstance());
    }

    private void start() {
        LOGGER.info("JustEnoughGraphs starting!");
        LOGGER.info("JustEnoughGraphs registering stuff!");
        MinecraftForge.EVENT_BUS.post(new RegistrationEvent());
        LOGGER.info("JustEnoughGraphs registered stuff!");
    }

    private void stop() {
        LOGGER.info("JustEnoughGraphs loading finished!");
    }

}
