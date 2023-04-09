package com.github.vfyjxf.justenoughgraphs.gui.screen;

import com.github.vfyjxf.justenoughgraphs.gui.factories.SimpleComponentFactory;
import com.github.vfyjxf.justenoughgraphs.helper.DrawHelper;
import com.github.vfyjxf.justenoughgraphs.helper.ElkHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.math.KVector;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.Direction;
import org.eclipse.elk.core.util.BasicProgressMonitor;
import org.eclipse.elk.core.util.ElkUtil;
import org.eclipse.elk.graph.*;
import org.eclipse.elk.graph.json.ElkGraphJson;
import org.eclipse.elk.graph.util.ElkGraphUtil;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TestScreen extends ModularScreen {

    private static final Color LINE_COLOR = new Color(255, 255, 255, 255);

    public static Screen openTest() {
        return new TestScreen();
    }

    private final ElkNode elkGraph;

    public TestScreen() {
        super(new SimpleComponentFactory());
        this.elkGraph = ElkGraphUtil.createGraph();
        elkGraph.setLocation(80, 50);
        ElkNode inputNodes = ElkGraphUtil.createNode(elkGraph);
        ElkPort outputPort = ElkGraphUtil.createPort(inputNodes);
        ElkNode nodeA = createNodeWithSize(inputNodes, 16, 16);
        ElkNode nodeB = createNodeWithSize(inputNodes, 16, 16);
        ElkNode nodeC = createNodeWithSize(inputNodes, 16, 16);
        ElkGraphUtil.createSimpleEdge(nodeA, outputPort);
        ElkGraphUtil.createSimpleEdge(nodeB, outputPort);
        ElkGraphUtil.createSimpleEdge(nodeC, outputPort);

        ElkNode nodeD = createNodeWithSize(elkGraph, 16, 16);
        ElkNode nodeE = createNodeWithSize(elkGraph, 16, 16);
        ElkNode nodeF = createNodeWithSize(elkGraph, 16, 16);
        ElkNode nodeG = createNodeWithSize(elkGraph, 16, 16);

        ElkGraphUtil.createSimpleEdge(nodeD, outputPort);
        ElkGraphUtil.createSimpleEdge(nodeE, outputPort);
        ElkGraphUtil.createSimpleEdge(nodeF, outputPort);
        ElkGraphUtil.createSimpleEdge(nodeG, outputPort);
        IGraphLayoutEngine layoutEngine = new RecursiveGraphLayoutEngine();
        elkGraph.setProperty(CoreOptions.DIRECTION, Direction.LEFT);
        elkGraph.setProperty(CoreOptions.ALGORITHM, LayeredOptions.ALGORITHM_ID);
        elkGraph.setProperty(LayeredOptions.MERGE_EDGES, true);
        ElkHelper.getAllElkNodes(elkGraph).forEachRemaining(node -> {
            node.setProperty(CoreOptions.DIRECTION, Direction.LEFT);
            node.setProperty(CoreOptions.ALGORITHM, LayeredOptions.ALGORITHM_ID);
            node.setProperty(LayeredOptions.MERGE_EDGES, true);
        });
        layoutEngine.layout(elkGraph, new BasicProgressMonitor().withLogging(true));
        System.out.println(ElkGraphJson.forGraph(elkGraph).toJson());
    }

    public static ElkNode createNodeWithSize(ElkNode elkGraph, int width, int height) {
        ElkNode node = ElkGraphUtil.createNode(elkGraph);
        node.setWidth(width);
        node.setHeight(height);
        return node;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(0);
        Spliterator<ElkNode> spliterator = Spliterators.spliteratorUnknownSize(
                ElkHelper.getAllElkNodes(elkGraph), Spliterator.NONNULL);
        Stream<ElkNode> stream = StreamSupport.stream(spliterator, false);
        //渲染node,填充为白色边框,使用vLine和hLine
//        List<ElkNode> nodes = stream.toList();
        renderNode(poseStack, elkGraph);
        Set<ElkEdge> edges = new HashSet<>();
        collectAllEdge(elkGraph, edges);
        for (ElkEdge edge : edges) {
            KVector abs = ElkUtil.absolutePosition(edge);
            poseStack.pushPose();
            poseStack.translate(abs.x, abs.y, 0);
            renderEdge(poseStack, edge);
            poseStack.popPose();
        }
        Set<ElkPort> ports = new HashSet<>();
        getPorts(elkGraph, ports);
        //以白色点的形式渲染port
        for (ElkPort port : ports) {

            KVector abs = ElkUtil.absolutePosition(port);
            poseStack.pushPose();
            {
                DrawHelper.drawSolidRect(poseStack, (int) abs.x, (int) abs.y - 2, 5, 5, 0xFF000000);
            }
            poseStack.popPose();
        }

//        int color = LINE_COLOR.getRGB();
//        for (ElkEdge elkEdge : StreamSupport.stream(Spliterators.spliteratorUnknownSize(
//                ElkHelper.getAllContent(elkGraph, ElkEdge.class), Spliterator.NONNULL), false).toList()) {
//            poseStack.pushPose();
//            {
//                for (ElkEdgeSection section : elkEdge.getSections()) {
//                    renderSection(poseStack, color, section);
//                }
//            }
//            poseStack.popPose();
//
//        }
    }

    private void renderNode(PoseStack poseStack, ElkNode node) {
        poseStack.pushPose();
        {
            boolean isRoot = node.getParent() == null;
            double px = isRoot ? 0 : node.getParent().getX();
            double py = isRoot ? 0 : node.getParent().getY();
            poseStack.translate(px, py, 0);
            int x = (int) node.getX();
            int y = (int) node.getY();
            int width = (int) node.getWidth();
            int height = (int) node.getHeight();
            this.vLine(poseStack, x, y, y + height, 0xFF000000);
            this.vLine(poseStack, x + width, y, y + height, 0xFF000000);
            this.hLine(poseStack, x, x + width, y, 0xFF000000);
            this.hLine(poseStack, x, x + width, y + height, 0xFF000000);
        }

        for (ElkNode child : node.getChildren()) {
            renderNode(poseStack, child);
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

    private void renderEdge(PoseStack poseStack, ElkEdge edge) {
        poseStack.pushPose();
        {
            renderSection(poseStack, LINE_COLOR.getRGB(), edge.getSections());
        }
        poseStack.popPose();
    }

    private void collectAllEdge(ElkNode node, Set<ElkEdge> edges) {
        edges.addAll(node.getContainedEdges());
        edges.addAll(node.getOutgoingEdges());
        edges.addAll(node.getIncomingEdges());
        for (ElkNode child : node.getChildren()) {
            collectAllEdge(child, edges);
        }
    }

    private void getPorts(ElkNode node, Set<ElkPort> ports) {
        ports.addAll(node.getPorts());
        for (ElkNode child : node.getChildren()) {
            getPorts(child, ports);
        }
    }

    private Set<ElkEdge> getAllEdgeFromNode(ElkNode node) {
        return Stream.of(node.getContainedEdges(), node.getOutgoingEdges(), node.getIncomingEdges())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void getAllChildren(ElkNode node, Set<ElkNode> children) {
        children.addAll(node.getChildren());
        for (ElkNode child : node.getChildren()) {
            getAllChildren(child, children);
        }
    }

    private void getAllEdges(ElkNode node, Set<ElkEdge> edges) {
        for (ElkNode child : node.getChildren()) {
            edges.addAll(child.getContainedEdges());
            edges.addAll(child.getOutgoingEdges());
            edges.addAll(child.getIncomingEdges());
            getAllEdges(child, edges);
        }
    }

}
