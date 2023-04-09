package com.github.vfyjxf.justenoughgraphs.integration.jegh;

import com.github.vfyjxf.justenoughgraphs.api.IRegistration;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import com.github.vfyjxf.justenoughgraphs.content.helper.FluidStackContentHelper;
import com.github.vfyjxf.justenoughgraphs.content.helper.ItemStackContentHelper;
import com.github.vfyjxf.justenoughgraphs.content.renderer.FluidStackContentRenderer;
import com.github.vfyjxf.justenoughgraphs.content.renderer.ItemStackContentRenderer;
import com.github.vfyjxf.justenoughgraphs.integration.jegh.recipe.looker.JeiRecipeLooker;
import com.github.vfyjxf.justenoughgraphs.integration.jegh.recipe.parser.JeiRecipeParser;
import com.github.vfyjxf.justenoughgraphs.registration.RegistrationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class JeghRegister {

    @SubscribeEvent
    public void register(RegistrationEvent registrationEvent) {
        IRegistration registration = registrationEvent.getRegistration();
        registerContents(registration);
    }

    /**
     * The default adapter should come after the other adapters.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerUniversalParser(RegistrationEvent registrationEvent) {
        registrationEvent.getRegistration().registerUniversalRecipeParser(
                new JeiRecipeParser()
        );
        registrationEvent.getRegistration().registerRecipeLooker(new JeiRecipeLooker());
    }

    private void registerContents(IRegistration registration) {
        registration.registerContent(ContentTypes.ITEM_STACK, new ItemStackContentHelper(), new ItemStackContentRenderer());
        registration.registerContent(ContentTypes.FLUID_STACK, new FluidStackContentHelper(), new FluidStackContentRenderer());
    }


}
