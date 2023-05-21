package com.github.vfyjxf.justenoughgraphs.api.content;

public interface IContent<T> {

    ContentType<T> getType();

    default RecipeRole getRole(){
        return RecipeRole.INPUT;
    }

    /**
     * For TagContent, it should be the content that can represent this tag.
     */
    T getContent();

    /**
     * The actual consumption will be obtained by taking a rounded approach based on the chance.
     * @return
     */
    long getAmount();

    void setAmount(long amount);

    /**
     * We agreed that 1.0 is the maximum chance and 0 is the minimum chance.
     */
    float getChance();

    void setChance(float chance);

    /**
     * Determine how the chances will be used to calculate the true amount.
     */
    enum RecipeRole {
        INPUT,
        OUTPUT
    }

}
