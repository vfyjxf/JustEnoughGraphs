package com.github.vfyjxf.justenoughgraphs.gui.widgets.tree;

import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.gui.ILayoutableGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.IWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IngredientType;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import com.github.vfyjxf.justenoughgraphs.gui.screen.recipe.tree.RecipeContentTreeScreen;
import com.github.vfyjxf.justenoughgraphs.gui.textures.ColorBorderTexture;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.ButtonWidget;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.ColoredContentNode;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.WidgetGroup;
import com.github.vfyjxf.justenoughgraphs.helper.ContentMerger;
import com.github.vfyjxf.justenoughgraphs.helper.ElkHelper;
import com.github.vfyjxf.justenoughgraphs.helper.RecipeHelper;
import com.github.vfyjxf.justenoughgraphs.utils.ColorUtils;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.eclipse.elk.graph.ElkConnectableShape;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.ElkPort;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A node of a recipe tree, which can be either the root or a branch,
 * the children are redefined as the child nodes attached to this Node.
 *
 * @author vfyjxf
 */
public abstract sealed class AbstractRecipeTreeNode<T> extends WidgetGroup permits ColoredTreeNode {

    public static <T> AbstractRecipeTreeNode<T> create(IContent<T> content, RecipeContentTreeScreen.TreeContext context, Map<RecipeType<?>, List<?>> recipeMap) {
        Object recipe = recipeMap.values().stream().flatMap(Collection::stream).findFirst().orElse(null);
        return new ColoredTreeNode<>(content, context, recipeMap, recipe).setBackgroundColor(0x000000);
    }


    private final RecipeContentTreeScreen.TreeContext context;
    @Nullable
    protected Object recipe;
    @Nullable
    protected IContentNode<?> catalystNode;

    /**
     * The ingredients of this node, which are the children of this node.
     */
    protected final List<AbstractRecipeTreeNode<?>> inputNodes;
    private final Map<RecipeType<?>, List<?>> recipeMap;
    private final List<RecipeType<?>> recipeTypes;
    private int recipeTypeIndex;
    private List<?> currentRecipes;

    private int recipeIndex;

    public AbstractRecipeTreeNode(

            IContent<T> content,
            RecipeContentTreeScreen.TreeContext context,
            Map<RecipeType<?>, List<?>> recipeMap,
            @Nullable Object recipe
    ) {
        this.context = context;
        this.recipeMap = ImmutableMap.copyOf(recipeMap);
        this.recipeTypes = List.copyOf(recipeMap.keySet());
        int preferIndex = recipeTypes.indexOf(context.getPrefer());
        this.recipeTypeIndex = Math.max(preferIndex, 0);
        this.recipeIndex = 0;
        this.currentRecipes = recipeTypes.isEmpty() ? null : recipeMap.get(recipeTypes.get(recipeTypeIndex));
        this.recipe = recipe;
        this.inputNodes = new ArrayList<>();
        ColoredContentNode<T> contentNode = ColoredContentNode.createWithBorder(content, ColorUtils.WHITE);
        if (recipeTypes.size() > 1) {
            this.addWidget(
                    new ButtonWidget()
                            .setBounds(0, 0, 5, 12)
                            .setBackground(new ColorBorderTexture(1, 0xFFFFFF, 0x000000, getWidth(), getHeight()))
                            .setClickHandler((screen, inputContext) -> {
                                if (Screen.hasShiftDown()) previousType();
                                else previousRecipe();
                                return true;
                            })
            );
            contentNode.setPos(7, 0);
            this.addWidget(
                    new ButtonWidget()
                            .setBounds(contentNode.getX() + contentNode.getWidth() + 3, 0, 5, 12)
                            .setBackground(new ColorBorderTexture(1, 0xFFFFFF, 0x000000, getWidth(), getHeight()))
                            .setClickHandler((screen, inputContext) -> {
                                if (Screen.hasShiftDown()) nextType();
                                else nextRecipe();
                                return true;
                            })
//                        .setButtonTexture()//TODO: add texture
            );
        } else {
            contentNode.setBorderColor(0x000000);
        }
        this.addWidget(contentNode);
    }

    @Override
    public void renderForeground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    public AbstractRecipeTreeNode<T> setParent(@Nullable IWidgetGroup parent) {
        super.setParent(parent);
        return this;
    }

    public void generateBranch() {
        int recipesCount = Seq.seq(recipeMap.values()).mapToInt(Collection::size).sum();

        if (recipesCount <= 0 || recipe == null || context.isUsed(recipe))
            return;

        if (inputNodes.isEmpty()) {
            generateNodes(this.recipe);
        }
        if (inputNodes.isEmpty()) return;

        for (AbstractRecipeTreeNode<?> node : this.inputNodes) {
            node.generateBranch();
        }
    }

    protected <R> void generateNodes(@Nullable R recipe) {
        if (recipe == null) return;
        this.context.getUsedRecipes().add(recipe);
        Map<IngredientType, List<IContent<?>>> ingredients = ContentMerger.merge(RecipeHelper.parseRecipe(recipe));
        this.catalystNode = createCatalystNode(ingredients);
        ILayoutableGroup parent = (ILayoutableGroup) this.parent;
        assert parent != null;
        ElkPort inOfOutput = ElkHelper.createPoint(parent.getNode(this));
        ElkConnectableShape inputTarget = inOfOutput;
        if (catalystNode != null) {
            this.parent.addWidget(catalystNode);
            ElkNode catalystInternal = parent.getNode(catalystNode);
            ElkPort outOfCatalyst = ElkHelper.createPoint(catalystInternal);
            ElkPort inOfCatalyst = ElkHelper.createPoint(catalystInternal);
            ElkGraphUtil.createSimpleEdge(outOfCatalyst, inOfOutput);
            inputTarget = inOfCatalyst;
        }
        if (ingredients.containsKey(IngredientType.INPUT)) {
            List<IContent<?>> inputs = ingredients.get(IngredientType.INPUT);
            for (IContent<?> input : inputs) {
                Map<RecipeType<?>, List<?>> recipeMap = RecipeHelper.lookupRecipe(input);
                AbstractRecipeTreeNode<?> inputNode = AbstractRecipeTreeNode.create(input, this.context, recipeMap);
                this.parent.addWidget(inputNode);
                ElkNode inputNodeInternal = parent.getNode(inputNode);
                ElkPort outOfInput = ElkHelper.createPoint(inputNodeInternal);
                ElkGraphUtil.createSimpleEdge(outOfInput, inputTarget);
                this.inputNodes.add(inputNode);
            }
        }

    }

    @Nullable
    private static IContentNode<?> createCatalystNode(Map<IngredientType, List<IContent<?>>> contentsByRole) {
        if (!contentsByRole.containsKey(IngredientType.CATALYST)) return null;
        List<IContent<?>> catalysts = contentsByRole.get(IngredientType.CATALYST);
        if (!catalysts.isEmpty()) {
            return ColoredContentNode.createWithBorder(catalysts.get(0), Color.cyan.getRGB());
        }
        return null;
    }

    private void nextType() {
        recipeTypeIndex++;
        if (recipeTypeIndex >= recipeTypes.size()) {
            recipeTypeIndex = 0;
        }
        this.currentRecipes = recipeMap.get(recipeTypes.get(recipeTypeIndex));
        this.recipeIndex = 0;
        this.recipe = currentRecipes.get(recipeIndex);
        this.rebuild();
    }

    private void nextRecipe() {
        recipeIndex++;
        if (recipeIndex >= currentRecipes.size()) {
            recipeIndex = 0;
        }
        this.recipe = currentRecipes.get(recipeIndex);
        this.rebuild();
    }

    private void previousType() {
        recipeTypeIndex--;
        if (recipeTypeIndex < 0) {
            recipeTypeIndex = recipeTypes.size() - 1;
        }
        this.currentRecipes = recipeMap.get(recipeTypes.get(recipeTypeIndex));
        this.recipeIndex = 0;
        this.recipe = currentRecipes.get(recipeIndex);
        this.rebuild();
    }

    private void previousRecipe() {
        recipeIndex--;
        if (recipeIndex < 0) {
            recipeIndex = currentRecipes.size() - 1;
        }
        this.recipe = currentRecipes.get(recipeIndex);
        this.rebuild();
    }

    private void rebuild() {
        assert parent != null;
        for (AbstractRecipeTreeNode<?> contentNode : inputNodes) {
//            this.parent.remove(contentNode.outPort);
//            this.parent.remove(contentNode.inPort);
            this.parent.remove(contentNode);
            contentNode.rebuild();
        }
        inputNodes.clear();
        generateBranch();
    }

    @Override
    public String toString() {
        return "AbstractRecipeTreeNode{" +
                "recipe=" + recipe +
                ", catalystNode=" + catalystNode +
//                ", inPort=" + inPort.self() +
//                ", outPort=" + outPort.self() +
                ", inputNodes=" + inputNodes +
                ", recipeMap=" + recipeMap +
                ", recipeTypes=" + recipeTypes +
                ", recipeTypeIndex=" + recipeTypeIndex +
                ", currentRecipes=" + currentRecipes +
                ", recipeIndex=" + recipeIndex +
                '}';
    }
}
