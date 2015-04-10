package tests;

import java.util.Random;
import trees.AbstractBST;
import trees.Obst2;

/**
 * Test de complexiteit van add en contains van Obst2 (Gewone binaire zoekboom)
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
public class TestObst2 implements Test {

    private static final int N = 100000;
    private static final int ITERATIONS = 5;
    private static final int SEED = 1234;
    Random rg;
    AbstractBST tree;

    @Override
    public void prepare() {
        tree = new Obst2();
        rg = new Random(SEED);
    }

    @Override
    public void execute() {
        // 100.000, 200.000, ..., 5.000.000
        for (int n = 0; n <= 50 * N; n += N) {

            long avgAdd = 0;
            long avgContains = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                tree = new Obst2(); // reset boom
                rg = new Random(SEED); // reset random generator

                System.gc();

                long start = System.currentTimeMillis();

                for (int j = 0; j < n; j++) { // voeg n elementen toe
                    tree.add(rg.nextInt());
                }

                long end = System.currentTimeMillis();

                avgAdd += (end - start);

                long startCont = System.currentTimeMillis();

                for (int j = 0; j < n; j++) { // zoek n elementen op
                    tree.contains(rg.nextInt());
                }

                long endCont = System.currentTimeMillis();

                avgContains += (endCont - startCont);
            }
            avgAdd /= ITERATIONS;
            avgContains /= ITERATIONS;

            System.out.println("" + n + "," + avgAdd
                    + "," + avgContains);
        }

    }
}
