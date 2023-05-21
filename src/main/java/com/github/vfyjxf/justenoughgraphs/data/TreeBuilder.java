package com.github.vfyjxf.justenoughgraphs.data;

import java.util.Stack;
import java.util.function.Consumer;

/**
 * From LDLib.
 *
 * @param <K>
 * @param <V>
 * @author kilabash
 */
public class TreeBuilder<K, V> {
    protected final Stack<TreeNode<K, V>> stack = new Stack<>();

    public TreeBuilder(K key) {
        stack.push(new TreeNode<>(0, key));
    }

    public static <K, V> TreeBuilder<K, V> start(K key) {
        return new TreeBuilder<>(key);
    }

    public TreeBuilder<K, V> branch(K key, Consumer<TreeBuilder<K, V>> builderConsumer) {
        var children = stack.peek().getChildren();
        if (children != null && !children.isEmpty()) {
            for (var child : children) {
                if (!child.isLeaf() && child.key.equals(key)) {
                    stack.push(child);
                    builderConsumer.accept(this);
                    endBranch();
                    return this;
                }
            }
        }

        stack.push(stack.peek().getOrCreateChild(key));
        builderConsumer.accept(this);
        endBranch();
        return this;
    }

    public TreeBuilder<K, V> startBranch(K key) {
        stack.push(stack.peek().getOrCreateChild(key));
        return this;
    }

    public TreeBuilder<K, V> endBranch() {
        stack.pop();
        return this;
    }

    public TreeBuilder<K, V> leaf(K key, V content) {
        stack.peek().addContent(key, content);
        return this;
    }

    public TreeBuilder<K, V> remove(K key) {
        stack.peek().removeChild(key);
        return this;
    }

    public TreeNode<K, V> build() {
        while (stack.size() > 1) {
            stack.pop();
        }
        return stack.peek();
    }

}
