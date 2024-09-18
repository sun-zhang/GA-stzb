package cn.zhang.sun.gameassistant.common.enums;

public enum PullCardType {
    FREE(1, "免费"),
    HALF(2, "半价"),
    FULL(3, "全价"),
    FIVE_TIMES(4, "五连"),
    GOLD(5, "金币"),
    SEARCH(6, "寻访");

    private int code;
    private String desc;

    PullCardType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PullCardType getPullCardType(int code) {
        for (PullCardType pullCardType : PullCardType.values()) {
            if (pullCardType.getCode() == code) {
                return pullCardType;
            }
        }
        return null;
    }
}
