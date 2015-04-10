package tests;

import java.util.Random;
import trees.AbstractBST;
import trees.Obst1;

/**
 * Test de complexiteit van Optimize van Obst2 (geoptimaliseerde methode)
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
public class TestObst1Optimize implements Test {

    private static final int N = 1000;
    private static final int ITERATIONS = 5;
    private static final int SEED = 1234;
    Random rg;
    AbstractBST tree;

    @Override
    public void prepare() {
        tree = new Obst1();
        rg = new Random(SEED);
    }

    @Override
    public void execute() {
        // 1000, 2000, ..., 5000
        for (int n = 0; n <= 5 * N; n += N) {

            long avg = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                tree = new Obst1(); // reset boom
                rg = new Random(SEED); // reset random generator

                System.gc();

                for (int j = 0; j < n; j++) { // voeg n elementen toe
                    tree.add(rg.nextInt());
                }
                
                long start = System.currentTimeMillis();
                
                tree.optimize();

                long end = System.currentTimeMillis();

                avg += (end - start);
            }
            avg /= ITERATIONS;

            System.out.println("" + n + "," + avg);
        }

    }
}
