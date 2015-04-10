package trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Binaire boom
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
public class Obst4 extends AbstractBST {

    private int[][] w;
    private int[][][] c;
    private List<NodeWithDepth> tree;

    public Obst4() {
        tree = new ArrayList<NodeWithDepth>(size);
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

    @Override
    public void balance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int cost() {
        // Bereken de kost op een recursieve manier
        return cost(root, 0);
    }

    private int cost(Node n, int depth) {
        if (n == null) {
            return 0;
        }

        return (depth + 1) * n.getWeight()
                + cost(n.getLeftChild(), depth + 1)
                + cost(n.getRightChild(), depth + 1);
    }

    @Override
    public void optimize() {
        w = new int[size][size];
        c = new int[size][size][2];
        
        tree.clear();

        // doorloop de boom in inorder om tree te vullen
        {
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
                    tree.add(new NodeWithDepth(currentNode, depth));
                    ++depth;
                    currentNode = currentNode.getRightChild();
                }
            }
        }

        // bouw de optimale BST
        Stack<StackElement> s = new Stack<StackElement>();
        s.add(new StackElement(0, size - 1));
        Node currentNode = tree.get(getC(0, size - 1)[1]).node; // wortel van de nieuwe boom
        root = currentNode;

        // zoek de kinderbomen van de wortel
        while (!s.empty()) {
            StackElement top = s.peek();
            if (top.start <= top.end) {
                int topRootIndex = getC(top.start, top.end)[1];
                currentNode = tree.get(topRootIndex).node;
                if (!top.leftDone) {
                    // bekijk links
                    s.push(new StackElement(top.start, topRootIndex - 1));
                    // gaat terug naar begin van de while-lus
                } else if (!top.rightDone) {
                    // bekijk rechts
                    s.push(new StackElement(topRootIndex + 1, top.end));
                    // gaat terug naar begin van de while-lus
                } else { // allebei de kinderen zijn ingevuld
                    // verwijder top uit de stapel
                    s.pop();
                    if (s.empty()) {
                        // klaar!
                        return;
                    }
                    StackElement oldTop = s.peek();
                    Node n = tree.get(getC(oldTop.start, oldTop.end)[1]).node;
                    if (!oldTop.leftDone) {
                        n.setLeftChild(currentNode);
                        oldTop.leftDone = true;
                    } else {
                        n.setRightChild(currentNode);
                        oldTop.rightDone = true;
                    }
                    // gaat terug naar begin van de while-lus
                }
            } else { // top.start > top.end
                s.pop(); // verwijder deze van de stapel
                if (s.empty()) {
                    // klaar!
                    return;
                }
                StackElement oldTop = s.peek();
                Node n = tree.get(getC(oldTop.start, oldTop.end)[1]).node;
                if (!oldTop.leftDone) {
                    n.setLeftChild(null);
                    oldTop.leftDone = true;
                } else {
                    n.setRightChild(null);
                    oldTop.rightDone = true;
                }
            }

        }
    }

    private int[] getC(int i, int j) {
        if (i < 0 || j < 0 || i >= size || j >= size) {
            return new int[]{0, 0};
        }
        
        if(c[i][j][0] != 0) {
            // al ingevuld
            return c[i][j];
        }

        int minimum = Integer.MAX_VALUE;
        int chosenK = i;
        for (int k = i; k <= j; k++) {
            int val = getC(i, k - 1)[0] + getC(k + 1, j)[0];
            if (val < minimum) {
                minimum = val;
                chosenK = k;
            }
        }
        c[i][j][0] = getW(i,j) + minimum;
        c[i][j][1] = chosenK;

        return c[i][j];
    }

    private int getW(int i, int j) {
        if (i < 0 || j < 0 || i >= size || j >= size) {
            return 0;
        }
        
        if(w[i][j] != 0) { // al ingevuld
            return w[i][j];
        }
        
        if(i == j) {
            w[i][i] = tree.get(i).node.getWeight() * (tree.get(i).depth + 1);
            return w[i][i];
        }
        
        w[i][j] = getW(i,j - 1) + getW(j,j);

        return w[i][j];
    }

    // Hulpklasse
    private class NodeWithDepth {

        private Node node;
        private int depth;

        private NodeWithDepth(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    // Hulpklasse
    private class StackElement {

        private int start, end;
        private boolean leftDone, rightDone;

        private StackElement(int start, int end) {
            this.start = start;
            this.end = end;
            leftDone = false;
            rightDone = false;
        }
    }
}
