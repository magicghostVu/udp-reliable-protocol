package mypack.testwithnetty.servertest.network;

import io.netty.buffer.ByteBuf;
import java.io.Serializable;
import java.util.List;


// đây là class để contruct một package thực sự
// có thể gửi đi qua network
public class RawDataPackage implements Serializable, WritableToByteBuf {


    private MajorPackage majorPackage;
    private List<ShortPackageInclude> listPackageResend;

    public RawDataPackage(MajorPackage majorPackage, List<ShortPackageInclude> listPackageResend) {
        this.majorPackage = majorPackage;
        this.listPackageResend = listPackageResend;
    }

    public MajorPackage getMajorPackage() {
        return majorPackage;
    }

    public List<ShortPackageInclude> getListPackageResend() {
        return listPackageResend;
    }

    @Override
    public void writeDataToByteBuf(ByteBuf target) {
        majorPackage.writeDataToByteBuf(target);
        target.writeShort(listPackageResend.size());
        for (var e : listPackageResend) {
            e.writeDataToByteBuf(target);
        }
    }
}
