package mypack.testwithnetty.servertest.actors.msgs;

import mypack.testwithnetty.servertest.network.MajorPackage;

import java.io.Serializable;

public class OnePackageCome implements Serializable {
    private MajorPackage data;

    public OnePackageCome(MajorPackage data) {
        this.data = data;
    }

    public MajorPackage getData() {
        return data;
    }
}
