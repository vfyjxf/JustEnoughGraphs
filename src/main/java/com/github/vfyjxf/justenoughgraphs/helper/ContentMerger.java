package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.ITagContent;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IngredientType;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContentMerger {

    public static Map<IngredientType, List<IContent<?>>> merge(Map<IngredientType, List<IContent<?>>> contents) {
        return contents.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mergeSameType(entry.getValue())));
    }

    public static List<IContent<?>> mergeSameType(List<IContent<?>> contents) {
        return mergeHelper(contents);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> List<IContent<?>> mergeHelper(List<IContent<?>> contents) {
        Map<ContentType<?>, List<IContent<T>>> normalContents = new HashMap<>();
        Map<ContentType<T>, List<ITagContent<T, ?>>> tagContents = new HashMap<>();
        for (IContent<?> content : contents) {
            if (content instanceof ITagContent tag) {
                tagContents.computeIfAbsent(tag.getTagType(), key -> new ArrayList<>()).add(tag);
                continue;
            }
            normalContents.computeIfAbsent(content.getType(), k -> new ArrayList<>())
                    .add((IContent<T>) content);
        }
        List<IContent<?>> mergedContents = new ArrayList<>();
        normalContents.forEach((key, value) -> mergedContents.addAll(merge(value)));
        tagContents.forEach((key, contentList) -> mergedContents.addAll(mergeTags((contentList))));

        return mergedContents;
    }

    public static <T> List<IContent<T>> merge(List<IContent<T>> contents) {
        List<IContent<T>> mergedContents = new ArrayList<>();
        IRegistryManager registryManager = IRegistryManager.getInstance();
        IContentHelper<T> contentHelper = contents.stream().findFirst()
                .map(IContent::getType)
                .map(registryManager::getContentHelper)
                .orElseThrow(() -> new IllegalArgumentException("No content helper found"));
        for (IContent<T> current : contents) {
            boolean merged = false;
            for (IContent<T> mergedContent : mergedContents) {
                if (contentHelper.mergeContent(mergedContent, current)) {
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                mergedContents.add(current);
            }
        }
        return mergedContents;
    }

    /**
     * @param contents must be same type
     * @return the merged contents
     */
    public static <T> List<ITagContent<T, ?>> mergeTags(List<ITagContent<T, ?>> contents) {
        List<ITagContent<T, ?>> mergedContents = new ArrayList<>();

        for (ITagContent<T, ?> current : contents) {
            boolean found = false;
            for (ITagContent<T, ?> mergedContent : mergedContents) {
                if (mergeTag(mergedContent, current)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                mergedContents.add(current);
            }
        }
        return mergedContents;
    }

    public static <STACK> boolean mergeTag(@Nullable ITagContent<STACK, ?> first, @Nullable ITagContent<STACK, ?> second) {
        if (first == null || second == null) {
            return false;
        }
        boolean matches = first.getTagType().equals(second.getTagType()) &&
                first.getTag().equals(second.getTag()) &&
                first.getChance() == second.getChance();
        if (!matches) {
            return false;
        }
        first.setAmount(first.getAmount() + second.getAmount());
        return true;
    }


    private static class ContentPair<T> {
        private T content;
        private float chance;

        public static <T> ContentPair<T> of(T content, float chance) {
            return new ContentPair<>(content, chance);
        }

        public ContentPair(T content, float chance) {
            this.content = content;
            this.chance = chance;
        }

        public T getContent() {
            return content;
        }

        public void setContent(T content) {
            this.content = content;
        }

        public float getChance() {
            return chance;
        }

        public void setChance(float chance) {
            this.chance = chance;
        }
    }

    private static class EditableTagPair<T> {
        private TagKey<T> tag;
        private long amount;
        private float chance;

        public EditableTagPair(TagKey<T> tag, long amount, float chance) {
            this.tag = tag;
            this.amount = amount;
            this.chance = chance;
        }

        public TagKey<T> getTag() {
            return tag;
        }

        public void setTag(TagKey<T> tag) {
            this.tag = tag;
        }

        public float getChance() {
            return chance;
        }

        public void setChance(float chance) {
            this.chance = chance;
        }

        public long getAmount() {
            return amount;
        }

        public void addAmount(long amount) {
            this.amount += amount;
        }
    }

}
