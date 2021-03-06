package com.eliottgray.searchtrees;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class AVLTreeTest extends TreeTestSkeleton {

    @Override
    public AVLTree<Integer> buildEmptyTree(Comparator<Integer> comparator){
        return new AVLTree<>(comparator);
    }

    private AVLTree<Integer> testTree;

    @Before
    public void setUp(){
        testTree = buildEmptyTree(Integer::compareTo);
    }

    /**
     * Add multiple nodes to the far right branch, until the tree rotates 3 times.
     *
     *  { Simple Rotation }        { Simple Rotation }              { Complex Rotation }
     *
     *         Left        Insert          Left        Insert         Right        Left
     *    1            2           2               2             2          2             3
     *     \          / \         / \             / \           / \        / \           / \
     *      2        1   3       1   3           1   5         1   5      1   3         2   5
     *       \                        \             / \           / \          \       /   / \
     *        3                        5           3   6         3   6          5     1   4   6
     *                                  \                         \            / \
     *                                   6                         4          4   6
     */
    @Test
    public void insert_repeatedRotations_left() throws InvalidSearchTreeException{
        // Unbalance tree with insertions.
        testTree = testTree.insert(1);
        testTree = testTree.insert(2);
        testTree = testTree.insert(3);

        // Verify first rotation
        assertEquals(2, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().getRight().key.intValue());
        assertEquals(3, testTree.getRoot().size);
        assertEquals(2, testTree.getRoot().height);
        testTree.validate();

        // Unbalance tree with insertions.
        testTree = testTree.insert(5);
        testTree = testTree.insert(6);

        // Verify second rotation
        assertEquals(2, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(3, testTree.getRoot().getRight().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().getRight().key.intValue());
        assertEquals(5, testTree.getRoot().size);
        assertEquals(3, testTree.getRoot().height);
        testTree.validate();

        // Unbalance tree with insertions.
        testTree = testTree.insert(4);

        // Verify complex rotation.
        assertEquals(3, testTree.getRoot().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(4, testTree.getRoot().getRight().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().getRight().key.intValue());
        assertEquals(6, testTree.getRoot().size);
        assertEquals(3, testTree.getRoot().height);
        testTree.validate();
    }

    /**
     * Add multiple nodes to the far left branch, until the tree rotates 3 times.
     *
     *  { Simple Rotation }        { Simple Rotation }              { Complex Rotation }
     *
     *         Right       Insert          Right        Insert         Left        Right
     *       6         5            5               5             5          5             4
     *      /         / \          / \             / \           / \        / \           / \
     *     5         4   6        4   6           2   6         2   6      4   6         2   5
     *    /                      /               / \           / \        /             / \   \
     *   4                      2               1   4         1   4      2             1   3   6
     *                         /                                 /      / \
     *                        1                                 3      1   3
     */
    @Test
    public void insert_repeatedRotations_right() throws InvalidSearchTreeException{
        // Unbalance tree with insertions.
        testTree = testTree.insert(6);
        testTree = testTree.insert(5);
        testTree = testTree.insert(4);

        // Verify first rotation
        assertEquals(5, testTree.getRoot().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(4, testTree.getRoot().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().size);
        assertEquals(2, testTree.getRoot().height);
        testTree.validate();

        // Unbalance tree with insertions.
        testTree = testTree.insert(2);
        testTree = testTree.insert(1);

        // Verify second rotation
        assertEquals(5, testTree.getRoot().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(4, testTree.getRoot().getLeft().getRight().key.intValue());
        assertEquals(5, testTree.getRoot().size);
        assertEquals(3, testTree.getRoot().height);
        testTree.validate();

        // Unbalance tree with insertions.
        testTree = testTree.insert(3);

        // Verify complex rotation.
        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().getRight().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().getLeft().getRight().key.intValue());
        assertEquals(6, testTree.getRoot().size);
        assertEquals(3, testTree.getRoot().height);
        testTree.validate();
    }

    /**
     * Test deletion of multiple nodes, with varying number of children.
     *
     * Nodes to be deleted in brackets: []
     *
     *              Delete        Delete       Delete     Delete    Delete
     *        4              4            4           [4]      [3]
     *       / \            / \          / \          /
     *      2   5         [2]  5        3  [5]       3                   [empty]
     *       \   \          \
     *        3  [6]         3
     */
    @Test
    public void testDelete_zeroOrOneChild() throws InvalidSearchTreeException{
        // Fill initial tree.
        testTree = testTree.insert(4);
        testTree = testTree.insert(2);
        testTree = testTree.insert(5);
        testTree = testTree.insert(3);
        testTree = testTree.insert(6);
        testTree.validate();

        // Test deletion of a node with no children.
        testTree = testTree.delete(6);
        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertFalse(testTree.getRoot().getRight().hasRight());
        assertEquals(1, testTree.getRoot().getRight().size);
        assertEquals(1, testTree.getRoot().getRight().height);
        assertEquals(3, testTree.getRoot().height);
        assertEquals(4, testTree.getRoot().size);
        testTree.validate();

        // Test deletion of a node with one child.
        testTree = testTree.delete(2);
        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(3, testTree.getRoot().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(3, testTree.getRoot().size);
        assertEquals(1, testTree.getRoot().getLeft().height);
        testTree.validate();

        // Test deletion of a node with no children.
        testTree = testTree.delete(5);
        assertEquals(4, testTree.getRoot().key.intValue());
        assertNull(testTree.getRoot().right);
        assertEquals(2, testTree.getRoot().height);
        assertEquals(2, testTree.getRoot().size);
        testTree.validate();

        // Test deletion of Root with one child.
        testTree = testTree.delete(4);
        assertEquals(3, testTree.getRoot().key.intValue());
        assertFalse(testTree.getRoot().hasLeft() && testTree.getRoot().hasRight());
        assertEquals(1, testTree.getRoot().size);
        assertEquals(1, testTree.getRoot().height);
        testTree.validate();

        // Test deletion of Root with no child.
        testTree = testTree.delete(3);
        assertTrue(testTree.isEmpty());

        // Test deletion of value from empty tree.
        testTree = testTree.delete(3);
        assertTrue(testTree.isEmpty());
    }

    /**
     * Test deletion of nodes with two children
     *
     *              Delete        Delete        Delete
     *        [4]             [3]             5             5
     *       /   \           /   \           / \           / \
     *     1      6         1     6        [1]  6         2   6
     *    / \    /         / \    /        / \           /
     *   0  (3) 5         0   2 (5)       0  (2)        0
     *      /
     *     2
     */
    @Test
    public void testDelete_nodeWithTwoChildren() throws InvalidSearchTreeException{
        testTree = testTree.insert(4);
        testTree = testTree.insert(1);
        testTree = testTree.insert(6);
        testTree = testTree.insert(3);
        testTree = testTree.insert(0);
        testTree = testTree.insert(5);
        testTree = testTree.insert(2);

        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(4, testTree.getRoot().height);
        testTree.validate();

        // Test deletion of root when LEFT subtree is longer.
        testTree = testTree.delete(4);
        assertEquals(3, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().getLeft().key.intValue());
        assertEquals(0, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().getRight().key.intValue());
        assertEquals(3, testTree.getRoot().height);
        assertEquals(6, testTree.getRoot().size);
        testTree.validate();

        // Test deletion of root when Right subtree is longer OR subtrees are same size.
        testTree = testTree.delete(3);
        assertEquals(5, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(0, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().getRight().key.intValue());
        assertEquals(3, testTree.getRoot().height);
        assertEquals(5, testTree.getRoot().size);
        testTree.validate();

        // Test deletion of non-root with two children.
        testTree = testTree.delete(1);
        assertEquals(5, testTree.getRoot().key.intValue());
        assertEquals(2, testTree.getRoot().getLeft().key.intValue());
        assertEquals(6, testTree.getRoot().getRight().key.intValue());
        assertEquals(0, testTree.getRoot().getLeft().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().height);
        assertEquals(4, testTree.getRoot().size);
        testTree.validate();
    }

    /**
     * Test deletion which causes right rotation of tree.
     *
     *             Delete     Rotate
     *        3            3         2
     *       / \          /         / \
     *      2  [4]       2         1   3
     *     /            /
     *    1            1
     */
    @Test
    public void testDelete_rightRotation() throws InvalidSearchTreeException{
        testTree = testTree.insert(3);
        testTree = testTree.insert(4);
        testTree = testTree.insert(2);
        testTree = testTree.insert(1);

        assertEquals(3, testTree.getRoot().key.intValue());

        // Deletion should cause rotation.
        testTree = testTree.delete(4);
        assertEquals(2, testTree.getRoot().key.intValue());
        assertEquals(1, testTree.getRoot().getLeft().key.intValue());
        assertEquals(3, testTree.getRoot().getRight().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(3, testTree.getRoot().size);
        testTree.validate();

    }

    /**
     * Test deletion which causes left rotation of tree.
     *
     *           Delete      Rotate
     *        3          3            4
     *       / \          \          / \
     *     [2]  4          4        3   5
     *           \          \
     *            5          5
     */
    @Test
    public void testDelete_leftRotation() throws InvalidSearchTreeException{
        testTree = testTree.insert(3);
        testTree = testTree.insert(4);
        testTree = testTree.insert(2);
        testTree = testTree.insert(5);

        assertEquals(3, testTree.getRoot().key.intValue());

        // Deletion should cause rotation.
        testTree = testTree.delete(2);
        assertEquals(4, testTree.getRoot().key.intValue());
        assertEquals(3, testTree.getRoot().getLeft().key.intValue());
        assertEquals(5, testTree.getRoot().getRight().key.intValue());
        assertEquals(2, testTree.getRoot().height);
        assertEquals(3, testTree.getRoot().size);
        testTree.validate();

    }

    /**
     * Test that deletion of nodes properly results in replacement of all direct parent nodes.
     */
    @Test
    public void testDelete_maintainsImmutability(){
        testTree = testTree.insert(4);
        testTree = testTree.insert(1);
        testTree = testTree.insert(6);

        // Delete left child of root, leaving root and right child.
        AVLTree<Integer> postDelete = testTree.delete(1);

        // Ensure that root now has a new identity, but right child remains the same.
        assertEquals(testTree.getRoot().key, postDelete.getRoot().key);
        assertNotEquals(testTree.getRoot(), postDelete.getRoot());
        assertEquals(testTree.getRoot().right, postDelete.getRoot().right);
    }
}
