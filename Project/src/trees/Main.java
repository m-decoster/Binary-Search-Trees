package trees;

import tests.Test;
import tests.TestObst1Optimize;

/**
 *
 * @author Mathieu De Coster (mth.decoster@gmail.com)
 */
public class Main {

    public static void main(String[] args) {
        Test t = new TestObst1Optimize();
        t.prepare();
        t.execute();
    }
}
