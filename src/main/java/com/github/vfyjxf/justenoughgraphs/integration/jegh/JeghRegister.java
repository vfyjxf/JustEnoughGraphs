package com.github.vfyjxf.justenoughgraphs.integration.jegh;

import com.github.vfyjxf.justenoughgraphs.api.IRegistration;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import com.github.vfyjxf.justenoughgraphs.content.FluidStackContent;
import com.github.vfyjxf.justenoughgraphs.content.ItemStackContent;
import com.github.vfyjxf.justenoughgraphs.content.helper.FluidStackContentHelper;
import com.github.vfyjxf.justenoughgraphs.content.helper.ItemStackContentHelper;
import com.github.vfyjxf.justenoughgraphs.content.renderer.FluidStackContentRenderer;
import com.github.vfyjxf.justenoughgraphs.content.renderer.ItemStackContentRenderer;
import com.github.vfyjxf.justenoughgraphs.content.tag.FluidTagContent;
import com.github.vfyjxf.justenoughgraphs.content.tag.ItemTagContent;
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
        registration.registerContent(
                ContentTypes.ITEM_STACK,
                ItemStackContent::new,
                new ItemStackContentHelper(),
                new ItemStackContentRenderer()
        );
        registration.registerContent(
                ContentTypes.FLUID_STACK,
                FluidStackContent::new,
                new FluidStackContentHelper(),
                new FluidStackContentRenderer()
        );
        registration.registerTagContent(
                ContentTypes.ITEM_STACK,
                ContentTypes.ITEM_TAG,
                ItemTagContent::create,
                new ItemStackContentRenderer()
        );
        registration.registerTagContent(
                ContentTypes.FLUID_STACK,
                ContentTypes.FLUID_TAG,
                FluidTagContent::create,
                new FluidStackContentRenderer()
        );
    }


}
