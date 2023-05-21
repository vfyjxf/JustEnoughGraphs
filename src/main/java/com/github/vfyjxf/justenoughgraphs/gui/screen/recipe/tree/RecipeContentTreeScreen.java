package com.github.vfyjxf.justenoughgraphs.gui.screen.recipe.tree;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.gui.IGuiWidget;
import com.github.vfyjxf.justenoughgraphs.api.gui.ILayoutableGroup;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.recipe.IngredientType;
import com.github.vfyjxf.justenoughgraphs.api.recipe.RecipeType;
import com.github.vfyjxf.justenoughgraphs.gui.screen.ModularScreen;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.ColoredContentNode;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.WidgetGroup;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.layoutable.LayoutableWidgetGroup;
import com.github.vfyjxf.justenoughgraphs.gui.widgets.tree.AbstractRecipeTreeNode;
import com.github.vfyjxf.justenoughgraphs.helper.*;
import com.github.vfyjxf.justenoughgraphs.utils.BoundChecker;
import com.github.vfyjxf.justenoughgraphs.utils.ColorUtils;
import com.github.vfyjxf.justenoughgraphs.utils.Rect;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.elk.alg.layered.options.*;
import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.math.ElkPadding;
import org.eclipse.elk.core.options.*;
import org.eclipse.elk.graph.*;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Rule: input -> catalyst -> output
 */
public class RecipeContentTreeScreen extends ModularScreen {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final Color LINE_COLOR = new Color(255, 255, 255, 255);
    private static final ElkPadding padding = new ElkPadding(2D);

    private final IRegistryManager registryManager;
    private final IGraphLayoutEngine layoutEngine;
    private final Object sourceRecipe;
    /**
     * Represents the graph itself
     */
    private ILayoutableGroup internalGraph;
    private ILayoutableGroup outputGroup;
    private boolean inited = false;
    private Rect lastBounds = Rect.ofAbsolute(0, 0, 0, 0);
    @Nullable
    private Component errorMessage = null;

    public static <T> Screen openTest(T sourceRecipe) {
        return new RecipeContentTreeScreen(sourceRecipe, new RecursiveGraphLayoutEngine());
    }


    public <T> RecipeContentTreeScreen(T sourceRecipe, IGraphLayoutEngine layoutEngine) {
        this.registryManager = IRegistryManager.getInstance();
        this.layoutEngine = layoutEngine;
        this.sourceRecipe = sourceRecipe;
    }

    @Override
    public void init() {
        Rect currentBounds = Rect.ofAbsolute(0, 0, width, height);
        this.inited = inited && BoundChecker.matches(lastBounds, Rect.ofAbsolute(0, 0, width, height));
        if (!inited) {
            this.screen = new WidgetGroup(0, 0, width, height);
            components.clear();
            this.internalGraph = new LayoutableWidgetGroup();
            applyProperty(internalGraph);
            buildTree(sourceRecipe);
            this.screen.addWidget(internalGraph);
            inited = true;
        }
        internalGraph.layout(layoutEngine);

        ComponentHelper.moveToTopCenter(internalGraph, 0, 15);
        lastBounds = currentBounds;
    }

    @Override
    public boolean keyPressed(int keycode, int scancode, int modifiers) {
        if (keycode == InputConstants.KEY_R) {
            this.init();
        }
        return super.keyPressed(keycode, scancode, modifiers);
    }

    private void applyProperty(ILayoutableGroup group) {
        ElkNode node = group.getInternal();
        node.setProperty(LayeredOptions.COMPACTION_CONNECTED_COMPONENTS, true);
        node.setProperty(LayeredOptions.COMPACTION_POST_COMPACTION_STRATEGY, GraphCompactionStrategy.RIGHT);
        node.setProperty(LayeredOptions.COMPACTION_POST_COMPACTION_CONSTRAINTS, ConstraintCalculationStrategy.QUADRATIC);
        node.setProperty(LayeredOptions.EDGE_ROUTING, EdgeRouting.ORTHOGONAL);
        node.setProperty(CoreOptions.DIRECTION, Direction.UP);
        node.setProperty(CoreOptions.ALGORITHM, LayeredOptions.ALGORITHM_ID);
        node.setProperty(CoreOptions.SPACING_NODE_NODE, 8D);
        node.setProperty(CoreOptions.SPACING_EDGE_NODE, 8D);
        node.setProperty(CoreOptions.SPACING_EDGE_EDGE, 8D);
        node.setProperty(LayeredOptions.SPACING_NODE_NODE_BETWEEN_LAYERS, 5D);
        node.setProperty(LayeredOptions.SPACING_EDGE_EDGE_BETWEEN_LAYERS, 5D);
        node.setProperty(CoreOptions.CONTENT_ALIGNMENT, ContentAlignment.topCenter());
        node.setProperty(CoreOptions.HIERARCHY_HANDLING, HierarchyHandling.INCLUDE_CHILDREN);
        node.setProperty(CoreOptions.EXPAND_NODES, true);
        node.setProperty(CoreOptions.ALIGNMENT, Alignment.AUTOMATIC);
        node.setProperty(LayeredOptions.MERGE_EDGES, true);
        node.setProperty(LayeredOptions.FEEDBACK_EDGES, false);
        node.setProperty(LayeredOptions.LAYERING_STRATEGY, LayeringStrategy.MIN_WIDTH);
        node.setProperty(LayeredOptions.WRAPPING_STRATEGY, WrappingStrategy.MULTI_EDGE);
        node.setProperty(LayeredOptions.CYCLE_BREAKING_STRATEGY, CycleBreakingStrategy.DEPTH_FIRST);
        node.setProperty(LayeredOptions.ASPECT_RATIO, ((double) (width / height)));
        node.setProperty(CoreOptions.INTERACTIVE_LAYOUT, true);
        node.setProperty(LayeredOptions.HIGH_DEGREE_NODES_TREATMENT, true);
        node.setProperty(LayeredOptions.SPACING_EDGE_NODE_BETWEEN_LAYERS, 5D);
        node.setProperty(LayeredOptions.SEPARATE_CONNECTED_COMPONENTS, false);
        node.setProperty(LayeredOptions.PORT_CONSTRAINTS, PortConstraints.FREE);
        node.setProperty(LayeredOptions.ALLOW_NON_FLOW_PORTS_TO_SWITCH_SIDES, true);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(0);
        if (!inited) {
            RenderHelper.drawText(poseStack, TranslationHelper.translatable("gui.recipe.tree.error.not_found").getString(), width / 2f, height / 2f, 1, LINE_COLOR.getRGB());
            return;
        }
        poseStack.pushPose();
        {
            screen.render(poseStack, mouseX, mouseY, partialTick);

        }
        poseStack.popPose();

        int color = LINE_COLOR.getRGB();
        Set<ElkEdge> edges = new HashSet<>();
        collectAllEdge(internalGraph, edges);
        poseStack.pushPose();
        {
            poseStack.translate(internalGraph.getX(), internalGraph.getY(), 0);
            for (ElkEdge edge : edges) {
                renderSection(poseStack, color, edge.getSections());
            }
        }
        poseStack.popPose();
    }

    private void renderSection(PoseStack poseStack, int color, Collection<ElkEdgeSection> sections) {
        for (ElkEdgeSection section : sections) {
            final Collection<ElkBendPoint> points = section.getBendPoints();

            int x1 = (int) section.getStartX();
            int y1 = (int) section.getStartY();

            for (ElkBendPoint pt : points) {
                int x = (int) pt.getX();
                int y = (int) pt.getY();
                GuiComponent.fill(poseStack, x - 1, y - 1, x + 1, y + 1, color);
                RenderHelper.drawLine(poseStack, x1, y1, x, y, color, 0.5F);
                x1 = (int) pt.getX();
                y1 = (int) pt.getY();
            }

            RenderHelper.drawLine(poseStack, x1, y1, (int) section.getEndX(), (int) section.getEndY(), color, 0.5F);
        }

    }

    private void collectAllEdge(ILayoutableGroup component, Set<ElkEdge> edges) {
        ElkNode node = component.getInternal();
        edges.addAll(node.getContainedEdges());
        edges.addAll(node.getOutgoingEdges());
        edges.addAll(node.getIncomingEdges());
        for (IGuiWidget child : component.getChildren()) {
            if (child instanceof ILayoutableGroup layoutable)
                collectAllEdge(layoutable, edges);
        }
    }

    private void collectAllPorts(ILayoutableGroup component, Set<ElkPort> ports) {
        ElkNode node = component.getInternal();
        ports.addAll(node.getPorts());
        for (IGuiWidget child : component.getChildren()) {
            if (child instanceof ILayoutableGroup layoutable)
                collectAllPorts(layoutable, ports);
        }
    }

    private <T> void buildTree(T recipe) {
        RecipeType<T> recipeType = registryManager.getRecipeType(recipe);
        Map<IngredientType, List<IContent<?>>> ingredients = ContentMerger.merge(RecipeHelper.parseRecipe(recipe));
        if (!ingredients.containsKey(IngredientType.INPUT)) {
            errorMessage = TranslationHelper.translatable("recipe.tree.error.no_input");
            return;
        }
        if (!ingredients.containsKey(IngredientType.OUTPUT)) {
            errorMessage = TranslationHelper.translatable("recipe.tree.error.no_output");
            return;
        }
        LayoutableWidgetGroup outputs = new LayoutableWidgetGroup() {
            @Override
            public void renderBackground(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
                super.renderBackground(poseStack, mouseX, mouseY, partialTick);
                RenderHelper.drawBorder(poseStack, 1, 0, 0, getWidth(), getHeight(), Color.orange.getRGB());
            }
        };
        outputs.setProperty(CoreOptions.ALGORITHM, LayeredOptions.ALGORITHM_ID);
        outputs.setProperty(CoreOptions.EXPAND_NODES, true);
        this.internalGraph.addWidget(outputs);
        ElkPort inOfOutput = ElkHelper.createPoint(internalGraph.getNode(outputs));
        ElkConnectableShape inputTarget = inOfOutput;
        for (IContent<?> content : ingredients.get(IngredientType.OUTPUT)) {
            outputs.addWidget(ColoredContentNode.createWithBorder(content, ColorUtils.WHITE));
        }
        IContentNode<?> catalyst = createCatalystNode(ingredients);
        if (catalyst != null) {
            internalGraph.addWidget(catalyst);
            ElkNode catalystInternal = internalGraph.getNode(catalyst);
            ElkPort outOfCatalyst = ElkHelper.createPoint(catalystInternal);
            ElkPort inOfCatalyst = ElkHelper.createPoint(catalystInternal);
            ElkGraphUtil.createSimpleEdge(outOfCatalyst, inOfOutput);
            inputTarget = inOfCatalyst;
        }
        TreeContext context = new TreeContext(recipeType);
        context.getUsedRecipes().add(recipe);
        for (IContent<?> input : ingredients.get(IngredientType.INPUT)) {
            Map<RecipeType<?>, List<?>> recipeMap = RecipeHelper.lookupRecipe(input);
            AbstractRecipeTreeNode<?> node = AbstractRecipeTreeNode.create(input, context, recipeMap);
            internalGraph.addWidget(node);
            ElkNode inputNodeInternal = internalGraph.getNode(node);
            ElkPort outOfInput = ElkHelper.createPoint(inputNodeInternal);
            ElkGraphUtil.createSimpleEdge(outOfInput, inputTarget);
            node.generateBranch();
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

    public static class TreeContext {
        private final Set<Object> usedRecipes = new HashSet<>();
        private final RecipeType<?> prefer;

        public TreeContext(RecipeType<?> prefer) {
            this.prefer = prefer;
        }

        public Set<Object> getUsedRecipes() {
            return usedRecipes;
        }

        public void addUsedRecipe(Object recipe) {
            usedRecipes.add(recipe);
        }

        public boolean isUsed(Object recipe) {
            return usedRecipes.contains(recipe);
        }

        public RecipeType<?> getPrefer() {
            return prefer;
        }
    }

}
