package com.github.vfyjxf.justenoughgraphs.gui.screen;

import com.github.vfyjxf.justenoughgraphs.api.IRegistryManager;
import com.github.vfyjxf.justenoughgraphs.api.content.IContent;
import com.github.vfyjxf.justenoughgraphs.api.gui.content.IContentNode;
import com.github.vfyjxf.justenoughgraphs.api.gui.elk.IElkGraphComponent;
import com.github.vfyjxf.justenoughgraphs.api.recipe.*;
import com.github.vfyjxf.justenoughgraphs.gui.IComponentFactory;
import com.github.vfyjxf.justenoughgraphs.gui.factories.SimpleComponentFactory;
import com.github.vfyjxf.justenoughgraphs.helper.ContentMerger;
import com.github.vfyjxf.justenoughgraphs.helper.DrawHelper;
import com.github.vfyjxf.justenoughgraphs.helper.ElkHelper;
import com.github.vfyjxf.justenoughgraphs.helper.TranslationHelper;
import com.github.vfyjxf.justenoughgraphs.utils.BoundChecker;
import com.github.vfyjxf.justenoughgraphs.utils.Rect;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.elk.alg.layered.options.FixedAlignment;
import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.math.ElkPadding;
import org.eclipse.elk.core.math.KVector;
import org.eclipse.elk.core.options.*;
import org.eclipse.elk.core.util.ElkUtil;
import org.eclipse.elk.core.util.IElkProgressMonitor;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.*;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Rule: input -> catalyst -> output
 */
public class RecipeTreeScreen extends ModularScreen {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final Color LINE_COLOR = new Color(255, 255, 255, 255);
    private static final ElkPadding padding = new ElkPadding(2D);
    private static final Consumer<IElkGraphComponent> defaultProperty = component -> {
        ElkNode node = component.getNode();
        node.setProperty(CoreOptions.DIRECTION, Direction.UP);
        node.setProperty(CoreOptions.ALGORITHM, LayeredOptions.ALGORITHM_ID);
        node.setProperty(CoreOptions.SPACING_NODE_NODE, 2D);
        node.setProperty(CoreOptions.PADDING, padding);
        node.setProperty(CoreOptions.CONTENT_ALIGNMENT, ContentAlignment.centerCenter());
        node.setProperty(CoreOptions.HIERARCHY_HANDLING, HierarchyHandling.INCLUDE_CHILDREN);
        node.setProperty(CoreOptions.EXPAND_NODES, true);
        node.setProperty(CoreOptions.ALIGNMENT, Alignment.CENTER);
        node.setProperty(LayeredOptions.MERGE_EDGES, true);
        node.setProperty(LayeredOptions.MERGE_HIERARCHY_EDGES, true);
        node.setProperty(LayeredOptions.FEEDBACK_EDGES, true);
        node.setProperty(CoreOptions.INTERACTIVE_LAYOUT, true);
        node.setProperty(LayeredOptions.HIGH_DEGREE_NODES_TREATMENT, true);
        node.setProperty(LayeredOptions.NODE_PLACEMENT_BK_FIXED_ALIGNMENT, FixedAlignment.BALANCED);
    };

    private final IRegistryManager registryManager;
    private final IComponentFactory componentFactory;
    private final IGraphLayoutEngine layoutEngine;
    private final Object sourceRecipe;
    /**
     * Represents the graph itself
     */
    private IElkGraphComponent internalGraph;
    private boolean inited = false;
    private Rect lastBounds = Rect.ofAbsolute(0, 0, 0, 0);

    public static <T> Screen openTest(T sourceRecipe) {
        return new RecipeTreeScreen(sourceRecipe, new SimpleComponentFactory(), new RecursiveGraphLayoutEngine());
    }


    public <T> RecipeTreeScreen(T sourceRecipe, IComponentFactory componentFactory, IGraphLayoutEngine layoutEngine) {
        super(componentFactory);
        this.registryManager = IRegistryManager.getInstance();
        this.componentFactory = componentFactory;
        this.layoutEngine = layoutEngine;
        this.sourceRecipe = sourceRecipe;
        this.componentFactory.setProperties(defaultProperty);
    }

    @Override
    public void init() {
        this.inited = inited && BoundChecker.matches(lastBounds, Rect.ofAbsolute(0, 0, width, height));
        if (!inited) {
            this.screen = componentFactory.createComponentGroup(null);
            this.screen.setBounds(0, 0, width, height);
            this.internalGraph = componentFactory.createComponentGroup(screen);
//            this.screen.getNode().setProperty(CoreOptions.PADDING, new ElkPadding(10, 0, 0, width / 2.85D));
            Map<IngredientType, List<IContent<?>>> ingredients = parseHelper(sourceRecipe);
            if (!ingredients.isEmpty()) {

                IElkGraphComponent outputNode = createRecipeComponent(internalGraph, ingredients, IngredientType.OUTPUT);
                ElkPort outputInputPort = ElkGraphUtil.createPort(outputNode.getNode());
                IElkGraphComponent catalystNode = createCatalystNode(internalGraph, ingredients.getOrDefault(IngredientType.CATALYST, Collections.emptyList()));
                IElkGraphComponent commonInputComponent = componentFactory.createComponentGroup(internalGraph);
                ElkPort inputOutputPort = ElkGraphUtil.createPort(commonInputComponent.getNode());
                List<PortedNode<?>> inputNodes = createInputNodes(commonInputComponent, inputOutputPort, ingredients.get(IngredientType.INPUT));
                if (ingredients.containsKey(IngredientType.OTHER)) {
                    createRecipeComponent(commonInputComponent, ingredients, IngredientType.OTHER);
                }
                if (catalystNode != null) {
                    ElkGraphUtil.createSimpleEdge(inputOutputPort, catalystNode.getNode());
                    ElkGraphUtil.createSimpleEdge(catalystNode.getNode(), outputInputPort);
                } else {
                    ElkGraphUtil.createSimpleEdge(inputOutputPort, outputInputPort);
                }
                searchAndCreate(inputNodes);
                inited = true;
            } else {
                inited = false;
            }
            this.screen.getNode().setProperty(CoreOptions.NODE_SIZE_FIXED_GRAPH_SIZE, true);
        }

        IElkProgressMonitor progressMonitor = new NullElkProgressMonitor();
        layoutEngine.layout(screen.getNode(), progressMonitor);
        ElkHelper.moveToTopCenter(internalGraph, 0, 15);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(0);
        if (!inited) {
            DrawHelper.drawText(poseStack, TranslationHelper.translatable("gui.recipe.tree.error.not_found").getString(), width / 2f, height / 2f, 1, LINE_COLOR.getRGB());
            return;
        }
        poseStack.pushPose();
        {
            screen.render(poseStack, mouseX, mouseY, partialTick);

            int color = LINE_COLOR.getRGB();
            Set<ElkEdge> edges = new HashSet<>();
            collectAllEdge(screen, edges);
            for (ElkEdge edge : edges) {
                KVector abs = ElkUtil.absolutePosition(edge);
                poseStack.pushPose();
                poseStack.translate(abs.x, abs.y, 0);
                renderSection(poseStack, color, edge.getSections());
                poseStack.popPose();
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
                DrawHelper.drawLine(poseStack, x1, y1, ((int) pt.getX()), ((int) pt.getY()), color, 0.4f);
                x1 = (int) pt.getX();
                y1 = (int) pt.getY();
            }

            DrawHelper.drawLine(poseStack, x1, y1, (int) section.getEndX(), (int) section.getEndY(), color, 0.4f);
        }

    }

    private void collectAllEdge(IElkGraphComponent component, Set<ElkEdge> edges) {
        ElkNode node = component.getNode();
        edges.addAll(node.getContainedEdges());
        edges.addAll(node.getOutgoingEdges());
        edges.addAll(node.getIncomingEdges());
        for (IElkGraphComponent child : component.getChildren()) {
            collectAllEdge(child, edges);
        }
    }

    private void collectAllPorts(IElkGraphComponent component, Set<ElkPort> ports) {
        ElkNode node = component.getNode();
        ports.addAll(node.getPorts());
        for (IElkGraphComponent child : component.getChildren()) {
            collectAllPorts(child, ports);
        }
    }

    private IElkGraphComponent createRecipeComponent(IElkGraphComponent parent, Map<IngredientType, List<IContent<?>>> ingredients, IngredientType type) {
        IElkGraphComponent typedComponents = componentFactory.createComponentGroup(parent);
        if (!ingredients.containsKey(type)) return typedComponents;
        List<IContent<?>> typedSource = ContentMerger.mergeSameType(ingredients.get(type));
        for (IContent<?> typedContent : typedSource) {
            componentFactory.createContentNode(typedComponents, typedContent);
        }
        return typedComponents;
    }

    @Nullable
    private IContentNode<?> createCatalystNode(IElkGraphComponent parent, List<IContent<?>> contents) {
        if (contents.isEmpty()) return null;
        else return componentFactory.createContentNode(parent, contents.get(0));
    }

    private List<IContentNode<?>> createContentNodeFromSource(IElkGraphComponent parent, Map<IngredientType, List<IContent<?>>> ingredients, IngredientType type) {
        if (!ingredients.containsKey(type)) return Collections.emptyList();
        return ingredients.get(type)
                .stream()
                .map(content -> componentFactory.createContentNode(parent, content))
                .collect(Collectors.toList());
    }

    private List<PortedNode<?>> createInputNodes(IElkGraphComponent parent, ElkPort outputPort, @Nullable List<IContent<?>> contents) {
        if (contents == null) return Collections.emptyList();
        return ContentMerger.mergeSameType(contents)
                .stream()
                .map(content -> {
                    IContentNode<?> node = componentFactory.createContentNode(parent, content);
                    ElkGraphUtil.createSimpleEdge(node.getNode(), outputPort);
                    return new PortedNode<>(node, Suppliers.memoize(() -> ElkGraphUtil.createPort(parent.getNode())));
                })
                .collect(Collectors.toList());
    }


    /**
     * Recursive creation of recipe components
     *
     * @param sourceNodes the input, not including "other" type.
     */
    private void searchAndCreate(List<PortedNode<?>> sourceNodes) {
        RecipeType<?> recipeType = registryManager.getRecipeType(sourceRecipe);
        Optional<? extends IRecipeLooker> maybeLooker = registryManager.getRecipeLookers()
                .stream()
                .filter(looker -> looker.isSupport(recipeType))
                .findFirst();
        var recipes = sourceNodes.stream()
                .map(node -> {
                    IContent<?> content = node.node().getContent();
                    return Pair.of(node, lookHelper(content, maybeLooker.map(looker -> looker.lookForRecipe(content))));
                })
                .filter(pair -> pair.getRight() != null)
                .toList();
        if (recipes.isEmpty()) return;

        var ingredientsByRecipe = recipes.stream()
                .map(pair -> Pair.of(pair.getLeft(), parseHelper(pair.getRight())))
                .toList();

        for (var ingredientTypeListMap : ingredientsByRecipe) {
            PortedNode<?> sourceNode = ingredientTypeListMap.getLeft();
            Map<IngredientType, List<IContent<?>>> ingredients = ingredientTypeListMap.getRight();
            IElkGraphComponent catalystComponent = createCatalystNode(internalGraph, ingredients.getOrDefault(IngredientType.CATALYST, Collections.emptyList()));
            IElkGraphComponent commonInputComponent = null;
            List<PortedNode<?>> inputNodes = null;
            if (ingredients.containsKey(IngredientType.INPUT)) {
                commonInputComponent = componentFactory.createComponentGroup(internalGraph);
                createRecipeComponent(internalGraph, ingredients, IngredientType.OUTPUT);
                ElkPort outputPort = ElkGraphUtil.createPort(commonInputComponent.getNode());
                inputNodes = createInputNodes(commonInputComponent, outputPort, ingredients.get(IngredientType.INPUT));
                if (catalystComponent != null) {
                    ElkGraphUtil.createSimpleEdge(outputPort, catalystComponent.getNode());
                    ElkGraphUtil.createSimpleEdge(catalystComponent.getNode(), sourceNode.portSupplier.get());
                } else {
                    ElkGraphUtil.createSimpleEdge(outputPort, sourceNode.portSupplier.get());
                }
            }

            if (ingredients.containsKey(IngredientType.OTHER)) {
                IElkGraphComponent parent = commonInputComponent == null ? internalGraph : commonInputComponent;
                createRecipeComponent(parent, ingredients, IngredientType.OTHER);
            }

            if (inputNodes != null)
                searchAndCreate(inputNodes);
        }
    }

    @Nullable
    private Object lookHelper(IContent<?> result, Optional<Object> maybeResult) {
        return maybeResult.orElseGet(() -> registryManager.getRecipeLookers()
                .stream()
                .map(looker -> looker.lookForRecipe(result))
                .flatMap(Optional::stream)
                .findFirst()
                .orElse(null));
    }

    private <T> Map<IngredientType, List<IContent<?>>> parseHelper(T recipe) {
        Optional<IRecipeParser<T>> maybeParser = registryManager.getRecipeParser(recipe);
        Map<IngredientType, List<IContent<?>>> ingredients = new HashMap<>();
        maybeParser.ifPresentOrElse(
                parser -> ingredients.putAll(parser.parse(recipe)),
                () -> parseByUniversal(registryManager.getRecipeType(recipe), ingredients)
        );
        return ingredients;
    }

    private <T> void parseByUniversal(@Nullable RecipeType<T> recipeType, Map<IngredientType, List<IContent<?>>> ingredients) {
        IUniversalRecipeParser recipeParser = registryManager.getUniversalRecipeParsers()
                .stream()
                .filter(parser -> parser.isSupport(recipeType))
                .findFirst()
                .orElse(null);
        if (recipeParser != null) {
            var all = recipeParser.parse(sourceRecipe);
            ingredients.putAll(all);
        } else {
            //TODO:Unsupported message
        }
    }

    private static void connect(IElkGraphComponent source, IElkGraphComponent target) {
        ElkGraphUtil.createSimpleEdge(source.getNode(), target.getNode());
    }

    private static void connect(List<? extends IElkGraphComponent> sources, IElkGraphComponent target) {
        for (IElkGraphComponent source : sources) {
            connect(source, target);
        }
    }

    /**
     * A record class to store the ports and node.
     */
    public record PortedNode<T>(IContentNode<T> node, Supplier<ElkPort> portSupplier) {

    }

}
