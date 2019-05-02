package mypack.testwithnetty.servertest.cmd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum CmdDefine {
    HAND_SHAKE((short) 0),
    LOGIN((short) 1);
    private final short code;

    CmdDefine(short code) {
        this.code = code;
    }

    public short getCode() {
        return code;
    }

    private static Map<Short, CmdDefine> mapCached;

    private static CmdDefine fromCode(short codeTake) {
        if (mapCached != null) return mapCached.get(codeTake);
        else {
            synchronized (CmdDefine.class) {
                if (mapCached != null) return mapCached.get(codeTake);
                var tmpMap = new HashMap<Short, CmdDefine>();
                for (CmdDefine c : CmdDefine.values()) {
                    tmpMap.put(c.getCode(), c);
                }
                mapCached = Collections.unmodifiableMap(tmpMap);
                return mapCached.get(codeTake);
            }
        }
    }

}
