package trees;

import java.util.Stack;

/**
 * Binaire boom met geoptimalizeerde optimize methode
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
public class Obst2 extends AbstractObst {

    public Obst2() {
    }

    @Override
    public void optimize() {
        if(size == 0) {
            return;
        }
        
        int[][] w = new int[size][size];
        int[][] c = new int[size][size];
        int[][] r = new int[size][size];

        NodeWithDepth[] tree = sortTree();

        // vul w hoofddiagonaal
        for (int i = 0; i < size; ++i) {
            // verbreek meteen ook alle links
            tree[i].node.setLeftChild(null);
            tree[i].node.setRightChild(null);
            tree[i].node.setParent(null);
            w[i][i] = tree[i].node.getWeight();
            
            r[i][i] = i;
        }
        // bereken w
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                w[i][j] = w[i][j - 1] + w[j][j];
            }
        }

        // vul c hoofddiagonaal
        for (int i = 0; i < size; ++i) {
            c[i][i] = w[i][i];
        }
        // bereken c
        int i = 0;
        int j = 1;
        int iterations = -1;
        int minimum;
        int chosenK;
        while (i != 0 || j != size - 1) {
            ++iterations;
            i = -1;
            j = 0 + iterations;
            while (j < size - 1) {
                i++;
                j++;

                minimum = Integer.MAX_VALUE;
                chosenK = i;
                for (int k = r[i][j-1]; k < r[i+1][j] + 1; k++) {
                    int val = getC(c, i, k - 1) + getC(c, k + 1, j);
                    if (val < minimum) {
                        minimum = val;
                        chosenK = k;
                    }
                }
                c[i][j] = w[i][j] + minimum;
                r[i][j] = chosenK;
            }
        }

        // bouw de optimale BST
        Stack<StackElement> s = new Stack<StackElement>();
        s.add(new StackElement(0, size - 1));
        Node currentNode = tree[r[0][size - 1]].node; // wortel van de nieuwe boom
        root = currentNode;

        // zoek de kinderbomen van de wortel
        while (!s.empty()) {
            StackElement top = s.peek();
            if (top.start <= top.end) {
                int topRootIndex = r[top.start][top.end];
                currentNode = tree[topRootIndex].node;
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
                    Node n = tree[r[oldTop.start][oldTop.end]].node;
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
                Node n = tree[r[oldTop.start][oldTop.end]].node;
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
}
