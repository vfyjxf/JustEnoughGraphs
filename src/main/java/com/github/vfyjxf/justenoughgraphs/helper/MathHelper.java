package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;

public class MathHelper {

    public static long getAmount(IContent<?> content) {
        long amount = content.getAmount();
        if (amount < 0) {
            return 0;
        }
        double chance = content.getChance();
        return (long) (content.getRole() == IContent.RecipeRole.INPUT ? (amount * chance) : (amount / chance));
    }

}
