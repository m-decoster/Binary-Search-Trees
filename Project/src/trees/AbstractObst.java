package trees;

import java.util.Stack;

/**
 * Superklasse die methoden bevat die gedeeld worden door Obst[i]
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
abstract public class AbstractObst extends AbstractBST {

    public AbstractObst() {
    }

    protected int getC(int[][] c, int i, int j) {
        if (i < 0 || j < 0 || i >= size || j >= size) {
            return 0;
        }

        return c[i][j];
    }

    @Override
    public int contains(int key) {
        if (root == null) {
            return 0;
        }

        boolean found = false;
        int navigations = 0;
        Node currentNode = root;

        while (!found) {
            currentNode.visit();

            if (key == currentNode.getKey()) {
                found = true;
                break;
            } else if (key < currentNode.getKey()) {
                // zoek links
                Node leftChild = currentNode.getLeftChild();
                if (leftChild == null) {
                    break; // zit niet in de boom
                }
                currentNode = leftChild;
            } else { // key > currentNode.getKey()
                // zoek rechts
                Node rightChild = currentNode.getRightChild();
                if (rightChild == null) {
                    break; // zit niet in de boom
                }
                currentNode = rightChild;
            }

            ++navigations;
        }

        if (!found) {
            // de sleutel zat niet in de boom
            return -navigations;
        }
        // de sleutel zat wel in de boom
        return navigations;
    }

    @Override
    public int add(int key) {
        if (root == null) {
            root = new Node(key);
            ++size;
            return 1;
        }

        boolean found = false;
        int cost = 1;
        Node currentNode = root;
        Node newNode = null;

        while (!found) {
            currentNode.visit();

            if (key == currentNode.getKey()) {
                // zat al in de boom!
                found = true;
                break;
            } else if (key < currentNode.getKey()) {
                // zoek links verder
                Node leftChild = currentNode.getLeftChild();
                if (leftChild == null) {
                    // plaats hier
                    newNode = new Node(key);
                    currentNode.setLeftChild(newNode);
                    break;
                }
                // nog niet geplaatst...
                currentNode = leftChild;
            } else { // key > currentNode.getKey()
                // zoek rechts verder
                Node rightChild = currentNode.getRightChild();
                if (rightChild == null) {
                    // plaats hier
                    newNode = new Node(key);
                    currentNode.setRightChild(newNode);
                    break;
                }
                // nog niet geplaatst...
                currentNode = rightChild;
            }

            ++cost;
        }

        if (found) {
            // zat al in de boom
            return -cost;
        }

        // zat nog niet in de boom
        ++size;
        return cost;
    }

    protected NodeWithDepth[] sortTree() {
        NodeWithDepth[] tree = new NodeWithDepth[size];
        
        int currentTreeIndex = 0;
        Node currentNode = root;
        int depth = 0;
        Stack<NodeWithDepth> parentStack = new Stack<NodeWithDepth>();
        while (!parentStack.isEmpty() || currentNode != null) {
            if (currentNode != null) {
                parentStack.push(new NodeWithDepth(currentNode, depth));
                currentNode = currentNode.getLeftChild();
                ++depth;
            } else {
                NodeWithDepth nwd = parentStack.pop();
                currentNode = nwd.node;
                depth = nwd.depth;
                tree[currentTreeIndex++] = new NodeWithDepth(currentNode, depth);
                ++depth;
                currentNode = currentNode.getRightChild();
            }
        }
        
        return tree;
    }
}
