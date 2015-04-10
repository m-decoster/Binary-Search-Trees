package trees;


import java.util.Stack;
import obst.BST;

/**
 * Abstracte klasse die een wortel bijhoudt
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
abstract public class AbstractBST implements BST {

    protected Node root;
    protected int size;
    
    public AbstractBST() {
        
    }
    
    public int size() {
        return size;
    }

    /**
     * Bepaalt de kleinste sleutel
     * @param n1 Top 1
     * @param n2 Top 2
     * @return De top met de kleinste sleutel
     */
    protected Node getSmallestNode(Node n1, Node n2) {
        return n1.getKey() < n2.getKey() ? n1 : n2;
    }

    /**
     * Bepaalt de grootste sleutel
     * @param n1 Top 1
     * @param n2 Top 2
     * @return De top met de grootste sleutel
     */
    protected Node getBiggestNode(Node n1, Node n2) {
        return n1.getKey() < n2.getKey() ? n2 : n1;
    }

    /**
     * Print deze boom
     */
    public void print() {
        root.print();
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

    // Hulpklasse
    protected class NodeWithDepth {

        protected Node node;
        protected int depth;

        protected NodeWithDepth(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    // Hulpklasse
    protected class StackElement {

        protected int start, end;
        protected boolean leftDone, rightDone;

        protected StackElement(int start, int end) {
            this.start = start;
            this.end = end;
            leftDone = false;
            rightDone = false;
        }
    }
    
    @Override
    public void balance() {
        Node[] tree = new Node[size];
        // doorloop de boom in inorder om tree te vullen
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
                tree[currentTreeIndex++] = currentNode;
                ++depth;
                currentNode = currentNode.getRightChild();
            }
        }

        root = balanceSubTree(0, size - 1, tree);
    }

    /**
     * Een perfect gebalanceerde boom is een boom waarbij er evenveel kinderen
     * zijn links als rechts. Daarom neem ik steeds het middel van de gesorteerde lijst als wortel,
     * en bouw zo recursief een gebalanceerde boom op.
    **/
    private Node balanceSubTree(int start, int end, Node[] tree) {
        if (start > end) {
            return null;
        }
        Node n = tree[(start + end) / 2]; // middelste sleutel uit de boom
        n.setLeftChild(balanceSubTree(start, (start + end) / 2 - 1, tree));
        n.setRightChild(balanceSubTree((start + end) / 2 + 1, end, tree));
        return n;
    }
}
