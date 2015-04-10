package trees;


/**
 * Semi-Splay
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
public class Sbst1 extends AbstractBST {
    
    public Sbst1() {
        
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

        // voer semi-splay uit met als actuele top currentNode
        splay(currentNode);

        if (!found) {
            // de sleutel zat niet in de boom
            return -navigations;
        }
        // de sleutel zat wel in de boom
        return navigations;
    }

    private void splay(Node actualNode) {
        
        while (true) { // de lus stopt wanneer we kind van de wortel of wortel zijn
            // ga twee toppen terug op het pad
            Node parent = actualNode.getParent();
            Node grandParent = null;

            if (parent == null) {
                // actualNode is de wortel
                return;
            } else {
                grandParent = parent.getParent();
                if (grandParent == null) {
                    // actualNode is een kind van de wortel
                    return;
                }
            }

            // zoek de buitenbomen
            Node[] outerTrees = new Node[4];
            if (parent.getKey() > grandParent.getKey()) {
                // parent is rechterkind van grandParent, buitenboom b1 zit links van grandparent
                outerTrees[0] = grandParent.getLeftChild();
                if (actualNode.getKey() > parent.getKey()) {
                    // actualNode is rechterkind van parent, buitenboom b2 zit links van parent
                    outerTrees[1] = parent.getLeftChild();
                    // dus zijn b3 en b4 kinderen van actualNode
                    outerTrees[2] = actualNode.getLeftChild();
                    outerTrees[3] = actualNode.getRightChild();
                } else {
                    // actualNode is linkerkind van parent, buitenboom b2 en b3 zijn kinderen van actualNode
                    outerTrees[1] = actualNode.getLeftChild();
                    outerTrees[2] = actualNode.getRightChild();
                    // b4 is dan rechterkind van parent
                    outerTrees[3] = parent.getRightChild();
                }
            } else {
                // parent is linkerkind van grandParent, buitenboom b4 zit rechts van grandparent
                outerTrees[3] = grandParent.getRightChild();
                if (actualNode.getKey() > parent.getKey()) {
                    // actualNode is rechterkind van parent: b1 zit links van parent
                    outerTrees[0] = parent.getLeftChild();
                    // b2 en b3 zijn kinderen van actualNode
                    outerTrees[1] = actualNode.getLeftChild();
                    outerTrees[2] = actualNode.getRightChild();
                } else {
                    // actualNode is linkerkind van parent: b3 zit rechts van parent
                    outerTrees[2] = parent.getRightChild();
                    // b1 en b2 zijn kinderen van actualNode
                    outerTrees[0] = actualNode.getLeftChild();
                    outerTrees[1] = actualNode.getRightChild();
                }
            }

            // vervang de subboom door een nieuwe complete binaire boom
            Node leftMost = getSmallestNode(getSmallestNode(actualNode, parent), grandParent);
            Node rightMost = getBiggestNode(getBiggestNode(actualNode, parent), grandParent);
            Node middle = getBiggestNode(getSmallestNode(actualNode, parent), grandParent);
            if(middle == leftMost || middle == rightMost) {
                middle = getSmallestNode(getBiggestNode(actualNode, parent), grandParent);
            }

            Node greatGrandParent = grandParent.getParent();
            if (greatGrandParent != null) {
                if (greatGrandParent.getKey() > middle.getKey()) {
                    greatGrandParent.setLeftChild(middle);
                } else {
                    greatGrandParent.setRightChild(middle);
                }
            } else {
                // middle wordt de nieuwe root
                middle.setParent(null);
                root = middle;
            }
            middle.setLeftChild(leftMost);
            middle.setRightChild(rightMost);

            // voeg de buitenbomen in volgorde terug toe
            leftMost.setLeftChild(outerTrees[0]);
            leftMost.setRightChild(outerTrees[1]);

            rightMost.setLeftChild(outerTrees[2]);
            rightMost.setRightChild(outerTrees[3]);

            // herhaal met nieuwe actuele top
            actualNode = middle;
        }
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
            splay(currentNode);
            return -cost;
        }

        // zat nog niet in de boom
        splay(newNode);
        ++size;
        return cost;
    }

    @Override
    public void optimize() {
        throw new UnsupportedOperationException("Niet geïmplementeerd voor semi-splay bomen.");
    }
}