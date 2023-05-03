package com.github.vfyjxf.justenoughgraphs.helper;

import com.google.common.collect.Iterators;
import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.ElkPort;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.jooq.lambda.Seq;

import java.util.Iterator;
import java.util.List;

public class ElkHelper {

    public static ElkPort createPoint(ElkNode node) {
        ElkPort port = ElkGraphUtil.createPort(node);
        port.setHeight(0);
        port.setWidth(0);
        port.setProperty(LayeredOptions.NODE_SIZE_FIXED_GRAPH_SIZE, true);
        return port;
    }

    public static Iterator<ElkNode> getAllElkNodes(final ElkNode parentNode) {
        return Iterators.filter(parentNode.eAllContents(), ElkNode.class);
    }

    public static <T> List<T> getAllContent(final ElkNode parentNode, final Class<T> clazz) {
        return Seq.seq(Iterators.filter(parentNode.eAllContents(), clazz)).toList();
    }

}
