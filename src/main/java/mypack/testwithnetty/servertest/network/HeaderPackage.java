package mypack.testwithnetty.servertest.network;

import java.io.Serializable;

public class HeaderPackage implements Serializable {

    // giá trị này sẽ tăng lên mỗi khi có package được gửi đi, nó như là packageId
    private short sequenceId;

    // giá trị sequenceId nhận được gần nhất (sau cùng),
    // tính từ phía người gửi package này
    // giá trị này được set khi gửi package
    private short lastReceived;

    // một bit field để mã hoá acks
    // đây là một mảng 4 byte,
    // nếu bit thứ n là true thì gói tin thứ (lastReceived - n) đã nhận được
    // acks bit này dành cho sender đi
    // giá trị này cũng được set khi gửi package đi
    private byte[] ackBits;

    public HeaderPackage(int sequenceId, int lastReceived) {

        if (sequenceId > Short.MAX_VALUE || lastReceived > Short.MAX_VALUE) {
            throw new IllegalArgumentException("wrong number sequenceId or last received");
        }

        this.sequenceId = (short) sequenceId;
        this.lastReceived = (short) lastReceived;
        this.ackBits = new byte[4];
    }

    public short getSequenceId() {
        return sequenceId;
    }

    public short getLastReceived() {
        return lastReceived;
    }

    public byte[] getAckBits() {
        return ackBits;
    }

    public void setValueForBit(int position) {
        // quá chỉ số
        if (position < 0 || position > 31) {
            throw new IllegalArgumentException("wrong position");
        }
        var indexToTake = position / 8;
        var indexToSet = position % 8;
        var oldVal = ackBits[indexToTake];
        ackBits[indexToTake] = (byte) (oldVal | (1 << indexToSet));
    }

    public boolean isBitSet(int position) {
        if (position < 0 || position > 31) {
            throw new IllegalArgumentException("wrong position");
        }
        int indexToTake = position / 8;
        int indexToCheck = position % 8;
        var oldVal = ackBits[indexToTake];
        return (oldVal & (1 << indexToCheck)) != 0;
    }
}
