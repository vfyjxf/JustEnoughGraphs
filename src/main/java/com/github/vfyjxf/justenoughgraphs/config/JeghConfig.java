package com.github.vfyjxf.justenoughgraphs.config;

import com.github.vfyjxf.justenoughgraphs.tree.RecipeSelectionStrategy;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JeghConfig {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ClientConfig CLIENT_CONFIG;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT_CONFIG = specPair.getLeft();
    }

    public final static class ClientConfig {
        private final ForgeConfigSpec.EnumValue<RecipeSelectionStrategy> recipeSelectionStrategy;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("recipe-tree");
            {
                recipeSelectionStrategy = builder
                        .comment("The strategy used to select the recipe to display in the recipe tree.")
                        .defineEnum("recipeSelectionStrategy", RecipeSelectionStrategy.MINIMUM_CONSUME);
            }
            builder.pop();
        }
    }

    public static RecipeSelectionStrategy getRecipeSelectionStrategy() {
        return CLIENT_CONFIG.recipeSelectionStrategy.get();
    }
}
