package cn.zhang.sun.gameassistant.common.enums;

public enum FactionType {
    ALL(0, "全部"),
    WEI(1, "魏"),
    SHU(2, "蜀"),
    WU(3, "吴"),
    HAN(4, "汉"),
    QUN(5, "群"),
    JIN(6, "晋");

    private int code;
    private String name;

    FactionType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static FactionType getFactionType(int code) {
        for (FactionType factionType : FactionType.values()) {
            if (factionType.getCode() == code) {
                return factionType;
            }
        }
        return null;
    }

}
