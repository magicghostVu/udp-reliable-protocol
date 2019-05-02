import junit.framework.TestCase;
import mypack.testwithnetty.servertest.network.HeaderPackage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class MyTest extends TestCase {


    public void testBuffer() {
        var g = new byte[5];

        for (var i = 0; i < g.length; i++) {
            g[i] = (byte) (i+1);
        }

        ByteBuffer bf = ByteBuffer.wrap(g);

        //bf.flip();

        // không cần flip sau khi sử dụng wrap
        byte fB = bf.get();

        assertEquals(1, fB);

    }

    public void testAcksBit() {
        var headerPackage = new HeaderPackage(0, 1);
        headerPackage.setValueForBit(20);

        headerPackage.setValueForBit(12);

        boolean v = headerPackage.isBitSet(20);
        boolean c = headerPackage.isBitSet(0);

        boolean[] allVal = new boolean[32];
        for (int i = 0; i < 32; i++) {
            allVal[i] = headerPackage.isBitSet(i);
        }
        System.out.println();
    }

    public void testIo() {

        try {

            var s = new ByteArrayOutputStream();

            var stream = new ObjectOutputStream(s);

            var p = new HeaderPackage(1, 2);

            stream.writeObject(p);

            stream.flush();


            byte[] g = s.toByteArray();
            var t = new ByteArrayInputStream(g);

            var stream1 = new ObjectInputStream(t);


            HeaderPackage h = (HeaderPackage) stream1.readObject();

            System.out.println();

        } catch (Exception e) {

        }

    }


}
