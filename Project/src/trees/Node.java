package trees;

/**
 *
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
public class Node {

    private Node leftChild, rightChild;
    private Node parent;
    private int key;
    private int weight;

    public Node(int key) {
        this.key = key;
        weight = 1;
    }

    /**
     * Print een top en zijn kinderen
     */
    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "+-- " : "|-- ") + key + " (" + weight + ")");
        if (leftChild != null) {
            leftChild.print(prefix + (isTail ? "    " : "|   "), rightChild == null);
        }
        if (rightChild != null) {
            rightChild.print(prefix + (isTail ? "    " : "|   "), true);
        }
    }
    
    /**
     * Plaatst een top als linkerkind
     * @param leftChild het nieuwe linkerkind
     */
    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
        if (leftChild != null) {
            leftChild.parent = this;
        }
    }
    
    /**
     * Plaatst een top als rechterkind
     * @param rightChild het nieuwe rechterkind
     */
    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
        if (rightChild != null) {
            rightChild.parent = this;
        }
    }
    
    /**
     * Wordt opgeroepen als een top bezocht wordt
     * Incrementeert het gewicht van deze top
     */
    public void visit() {
        ++weight;
    }
    
    /**
     * Is deze top een blad?
     */
    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }

    //// GETTERS & SETTERS ////
    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public int getKey() {
        return key;
    }

    public int getWeight() {
        return weight;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}