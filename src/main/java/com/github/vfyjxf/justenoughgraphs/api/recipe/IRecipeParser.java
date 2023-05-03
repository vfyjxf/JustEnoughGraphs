package com.github.vfyjxf.justenoughgraphs.api.recipe;

import com.github.vfyjxf.justenoughgraphs.api.content.ICompositeContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IListContent;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IRecipeParser<R> {

    /**
     * Different types of ingredients in a recipe.
     * Each element in the list represents a "slot".
     * For a parser that can provide a catalyst type,
     * it should return an {@link IListContent} or {@link ICompositeContent} as the catalyst.
     */
    Map<IngredientType, List<IContent<?>>> parse(R recipe);

    Collection<IContent<?>> parseByType(R recipe, IngredientType type);

}
