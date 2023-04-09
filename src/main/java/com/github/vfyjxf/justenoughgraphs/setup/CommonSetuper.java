package com.github.vfyjxf.justenoughgraphs.setup;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonSetuper {

    protected final IEventBus eventBus;
    protected final IEventBus modEventBus;

    public CommonSetuper() {
        this.eventBus = MinecraftForge.EVENT_BUS;
        this.modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onLoadCompleted(FMLLoadCompleteEvent event) {

    }

}
