package mypack.testwithnetty.servertest.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RawDataPackage implements Serializable {


    private MajorPackage majorPackage;
    private List<ShortPackageInclude> listPackageResend;

    public RawDataPackage(MajorPackage majorPackage) {
        this.majorPackage = majorPackage;
        listPackageResend = new ArrayList<>();
    }

    public MajorPackage getMajorPackage() {
        return majorPackage;
    }

    public List<ShortPackageInclude> getListPackageResend() {
        return listPackageResend;
    }
}
