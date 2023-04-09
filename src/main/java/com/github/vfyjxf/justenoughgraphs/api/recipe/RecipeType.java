package com.github.vfyjxf.justenoughgraphs.api.recipe;

import net.minecraft.resources.ResourceLocation;

/**
 * Equivalent to {@link mezz.jei.api.recipe.RecipeType}
 * In order for jei to work in a compatible way, jegh and jei should share the same uid
 */
public class RecipeType<R> {

    public static <T> RecipeType<T> create(String nameSpace, String path, Class<? extends T> recipeClass) {
        ResourceLocation uid = new ResourceLocation(nameSpace, path);
        return new RecipeType<>(uid, recipeClass);
    }

    private final ResourceLocation id;
    private final Class<? extends R> recipeClass;

    public RecipeType(ResourceLocation id, Class<? extends R> recipeClass) {
        this.id = id;
        this.recipeClass = recipeClass;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Class<? extends R> getRecipeClass() {
        return recipeClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeType<?> that = (RecipeType<?>) o;

        if (!id.equals(that.id)) return false;
        return recipeClass.equals(that.recipeClass);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + recipeClass.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RecipeType{" +
                "id=" + id +
                ", recipeClass=" + recipeClass +
                '}';
    }
}
