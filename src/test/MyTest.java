import junit.framework.TestCase;

import java.nio.ByteBuffer;

public class MyTest extends TestCase {


    public void testBuffer() {
        var g = new byte[5];

        for (var i = 0; i < g.length; i++) {
            g[i] = (byte) i;
        }

        ByteBuffer bf = ByteBuffer.wrap(g);

        //bf.flip();

        byte fB = bf.get();

        assertEquals(0, fB);

    }
}
