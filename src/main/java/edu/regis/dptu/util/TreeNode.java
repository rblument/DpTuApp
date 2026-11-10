/*
 * DPTu: Dynamic Programming Tutor
 *
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 *
 *  Unauthorized use, duplication or distribution without the authors'
 *  permission is strictly prohibited.
 *
 *  Unless required by applicable law or agreed to in writing, this
 *  software is distributed on an "AS IS" basis without warranties
 *  or conditions of any kind, either expressed or implied.
 */
package edu.regis.dptu.util;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.err.IllegalArgException;

/**
 * Data structure that keeps track of a parent node, child nodes, and its own value. Has accessors
 * and mutators, and functionality to add a new node to its children. If its child list is already
 * full on an add attempt, it will compare the new node to its children recursively. To be used in
 * TreeProblems.
 *
 * @author Kade Stephan
 */
public class TreeNode {
    private static final Logger log = LoggerFactory.getLogger(TreeNode.class);

    // intermediate error notice that is used multiple times.
    private static final String IMERROR =
            "This shouldn't happen, but a problem occurred when adding a child to a TreeNode.";

    // value of the current node. "Object" allows it to be a String, Character, or Integer.
    private final Object value;

    private TreeNode parent;

    private ArrayList<TreeNode> children;

    private final int maxChildren;

    // Current tree depth at given node
    private int depth;

    /**
     * Creates a bald leaf with a given value and 2 children at max.
     *
     * @param val Integer, String, or Character value to be associated with the node
     * @throws IllegalArgException if the provided object is not an Integer, String, or Character.
     */
    public TreeNode(Object val) throws IllegalArgException {
        if (val instanceof Integer || val instanceof Character || val instanceof String) {
            value = val;
        } else {
            throw new IllegalArgException(
                    "Bad object provided in TreeNode constructor."
                            + "Expected String, Character, or Integer, but got "
                            + val.getClass().toString());
        }
        maxChildren = 2;
        children = null;
        parent = null;
        depth = 0;
    }

    @Override
    public String toString() {
        return "node with value "
                + value
                + " with "
                + children.size()
                + " children at depth "
                + depth;
    }

    /**
     * Commenting this constructor out until problems with >2 children are added. Creates a bald
     * leaf with a given value and a given maximum amount of child nodes.
     *
     * <p>param val value of the current node. "Object" allows it to be a String, Character, or
     * Integer. param maxChild number of children to allow for this node
     *
     * <p>public TreeNode(Object val, int maxChild) { value = val; maxChildren = maxChild; children
     * = null; parent = null; depth = 0; }
     */

    /**
     * @return value associated with this node.
     */
    public Object getValue() {
        return value;
    }

    public TreeNode getParent() {
        return parent;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    // Should always be 2 until functionality for >2 children is added.
    public int getMaxChildren() {
        return maxChildren;
    }

    public int getDepth() {
        return depth;
    }

    // Set the current node to the given depth. Used for tree balancing and rebalancing.
    public void setDepth(int newDepth) {
        depth = newDepth;
    }

    /**
     * Returns the child associated with the given value.
     *
     * @param val expected value for one child to have
     * @return child associated with given val.
     * @throws IllegalArgException if there is no child with that value
     */
    public TreeNode getChildWithValueOf(Object val) throws IllegalArgException {
        for (TreeNode child : children) {
            if (child.getValue() == val) {
                return child;
            }
        }
        throw new IllegalArgException(
                "No child for " + this.toString() + " found with value " + val);
    }

    /**
     * Used for tree re-balancing. Unbalanced trees will have >1 difference between their lowest
     * depth and their highest depth.
     *
     * @return the depth of this node's deepest child node, or this node's depth if it is a leaf.
     */
    public int deepestChild() {
        if (children.isEmpty()) {
            return depth;
        }
        int mostDepth = depth;
        for (TreeNode child : children) {
            // compare to currentDepth.
            if (child.getChildren().isEmpty()) {
                int nextDepth = child.deepestChild();
                if (nextDepth > mostDepth) {
                    mostDepth = nextDepth;
                }
                // if child has no children but another branch is deeper on the node this was
                // called on, then don't change currentDepth.
            } else if (child.getDepth() > mostDepth) {
                mostDepth = child.getDepth();
            }
        }
        return mostDepth;
    }

    public void setParent(TreeNode newParent) {
        parent = newParent;
    }

    /**
     * Attempts to add a child to this node. Will recursively check its children if this node is
     * full of children already.
     *
     * @param child child node to add.
     * @throws IllegalArgException if the type for this node and child node are different.
     */
    public void addChild(TreeNode child) throws IllegalArgException {
        // if types mismatch, stop processing immediately and inform.
        if (child.getValue().getClass() != value.getClass()) {
            throw new IllegalArgException(
                    "Tree node value type mismatch. Expected "
                            + value.getClass().toString()
                            + " but got "
                            + child.getValue().getClass().toString()
                            + " instead.");
        }

        // Integer
        if (child.getValue() instanceof Integer) {
            addIntegerChild(child);
        }

        // Character
        if (child.getValue() instanceof Character) {
            addCharacterChild(child);
        }

        // String
        if (child.getValue() instanceof String) {
            addStringChild(child);
        }
    }

    /**
     * Specific handling for adding Integer value nodes as children to this node. Nodes less than
     * this go on the left, nodes greater than go on the right.
     *
     * @param child child node to add.
     */
    private void addIntegerChild(TreeNode child) {
        Integer cVal = (Integer) child.getValue();
        Integer val = (Integer) value;

        /* There are several possible cases here. TODO: More cases should be added if
        * maxChildren > 2 ever becomes relevant.
        * cVal > val and no children -> add this as a child of 'child' and make 'child' parent.
        * cVal > val and 1 child -> add 'child' to children. This case is unique, as we dont need to check the other child.
        * cVal > val and cVal > largest child val -> add 'child' to largest child in children
        * cVal > val and cVal <= largest child val -> add 'child' to largest child in children
        * cVal <= val and no children -> add 'child' to children
        * cVal <= val and 1 child ->
               * cVal > value of 1 child -> need to rebalance the 1 child to parent node of both nodes
               * cVal <= value of 1 child -> need to rebalance the 1 child to parent node of both nodes
        * cVal <= val and cVal > smallest child val -> add 'child' to smallest child in children
        * cVal <= val and cVal <= smallest child val -> add 'child' to smallest child in children

        */
        if (cVal > val) {
            if (children.isEmpty()) {
                try {
                    child.addChild(this);
                    setParent(child);
                } catch (IllegalArgException ex) {
                    log.error(IMERROR);
                }
            } else if (children.size() < maxChildren && !children.isEmpty()) {
                // for now, only for cases with 1 child
                children.add(child);
                child.setParent(this);
                // children == 2
            } else {
                Integer biggest = 0;
                for (TreeNode kid : children) {
                    if ((Integer) kid.getValue() > biggest) {
                        biggest = (Integer) kid.getValue();
                    }
                }
                // both cases have 'child' node added to the largest node as a new child.
                // TODO: change this for active balancing? see how tree balance coding goes
                try {
                    TreeNode destination = this.getChildWithValueOf(biggest);
                    destination.addChild(child);
                    child.setParent(destination);
                } catch (IllegalArgException ex) {
                    log.error(IMERROR);
                }
            }
            // cVal <= val
        } else {
            if (children.isEmpty()) {
                // this outcome forms the basis for other assumptions about child assignment.
                children.add(child);
                child.setParent(this);
            } else if (children.size() < maxChildren && !children.isEmpty()) {
                // this means both nodes below this one in the tree are smaller than this one, and
                // we need to rebalance these three with a rotation.

                // we will make the one child node the parent, but first capture it.
                TreeNode newParent = children.getFirst();

                // remove children from this node as it will become a leaf.
                children.clear();

                // change newParent's parent node.
                newParent.setParent(this.getParent());

                // add this and 'child' as children of newParent.
                try {
                    newParent.addChild(this);
                    this.setParent(newParent);
                    newParent.addChild(child);
                    child.setParent(newParent);
                } catch (IllegalArgException ex) {
                    log.error(IMERROR);
                }
            }
        }
    }

    /**
     * TODO: Implement Specific handling for adding Character value nodes as children to this node.
     * Nodes alphabetically before this go on the left, and alphabetically after go on the right.
     *
     * @param child child node to add.
     */
    private void addCharacterChild(TreeNode child) {
        Character cVal = (Character) child.getValue();
        Character val = (Character) value;
    }

    /**
     * TODO: Implement Specific handling for adding String value nodes as children to this node.
     * Nodes alphabetically before this go on the left, and alphabetically after go on the right.
     *
     * @param child child node to add.
     */
    private void addStringChild(TreeNode child) {
        String cVal = (String) child.getValue();
        String val = (String) value;
    }
}
