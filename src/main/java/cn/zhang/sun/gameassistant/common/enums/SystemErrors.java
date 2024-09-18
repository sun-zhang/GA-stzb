package cn.zhang.sun.gameassistant.common.enums;

public enum SystemErrors {
    // Fields
    SUCCESS("0000", "Success"),
    SYSTEM_ERROR("9999", "System Error"),
    PARAMETER_ERROR("1001", "Parameter Error"),
    DATA_NOT_FOUND("1002", "Data Not Found"),
    DATA_ALREADY_EXISTS("1003", "Data Already Exists"),
    DATA_NOT_MATCH("1004", "Data Not Match"),
    DATA_NOT_UNIQUE("1005", "Data Not Unique"),
    DATA_NOT_VALID("1006", "Data Not Valid");

    // Fields
    private String code;
    private String message;

    // Constructor
    SystemErrors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // Methods
    public static SystemErrors getSystemError(String code) {
        for (SystemErrors systemError : SystemErrors.values()) {
            if (systemError.getCode().equals(code)) {
                return systemError;
            }
        }
        return null;
    }

}
