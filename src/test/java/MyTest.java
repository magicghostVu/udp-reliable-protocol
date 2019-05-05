import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import junit.framework.TestCase;
import mypack.mutils.MyUtils;
import mypack.testwithnetty.servertest.network.HeaderPackage;
import mypack.testwithnetty.servertest.network.MajorPackage;
import mypack.testwithnetty.servertest.network.RawDataPackage;
import mypack.testwithnetty.servertest.network.ShortPackageInclude;
import org.apache.commons.collections4.BoundedMap;
import org.apache.commons.collections4.map.FixedSizeSortedMap;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.TreeMap;

public class MyTest extends TestCase {


    public void testBuffer() {
        var g = new byte[5];

        for (var i = 0; i < g.length; i++) {
            g[i] = (byte) (i + 1);
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


    //work as expected
    public void testIo() {
        try {

            var h = new HeaderPackage(0, 1024);

            var payload = new byte[100];
            var m = new MajorPackage(h, (short) 1001, payload);

            var listInclude = new ArrayList<ShortPackageInclude>();

            var i1 = new ShortPackageInclude((short) 1002, (short) 1004, payload);
            listInclude.add(i1);


            var r = new RawDataPackage(m, listInclude);


            var s = new ByteArrayOutputStream();
            var stream = new ObjectOutputStream(s);

            stream.writeObject(r);
            stream.flush();

            byte[] g = s.toByteArray();
            var t = new ByteArrayInputStream(g);
            var stream1 = new ObjectInputStream(t);
            Object oo = stream1.readObject();
            System.out.println();

            var allocator = ByteBufAllocator.DEFAULT;

            var b = allocator.buffer();

            r.writeDataToByteBuf(b);

            var nm = MyUtils.readMajorPackage(b);

            short sizeInClude = b.readShort();

            var lInclude = new ArrayList<ShortPackageInclude>();

            for (int i = 0; i < sizeInClude; i++) {
                var tmp = MyUtils.readShortPackage(b);
                lInclude.add(tmp);
            }

            System.out.println();


        } catch (Exception e) {

        }
    }

    public void testByteBuf() {
        var allocator = ByteBufAllocator.DEFAULT;
        var a = allocator.buffer();
        a.writeInt(1);
        a.writeInt(3);
        var b = allocator.buffer();
        b.writeBytes(a);
        a.release();
        int c = b.readInt();
        int d = b.readInt();
        b.release();
        assertEquals(1, c);
        assertEquals(3, d);
    }

    public void testUnsignedShort() {
        int e = 63000;

        var b = ByteBufAllocator.DEFAULT.buffer();
        b.writeShort(e);

        var a = b.readShort();

        System.out.println("a is" + a);
    }


    public void testByteBuffer() {
        var b = ByteBuffer.allocate(1000);
        b.putInt(1234);
        b.flip();
        var t = b.getInt();
        b.clear();
        b.put((byte) 9);
        b.flip();
        var yy = b.get();
        assertEquals(1234, t);
        assertEquals((byte) 9, yy);
    }


}
