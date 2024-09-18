package cn.zhang.sun.gameassistant.domain.dto;

import cn.zhang.sun.gameassistant.common.enums.SystemErrors;

import java.io.Serial;

public class ErrorDTO extends BaseDTO{
    @Serial
    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String errorMsg;
    private String errorDetail;

    public  ErrorDTO(SystemErrors systemErrors) {
        this.errorCode = systemErrors.getCode();
        this.errorMsg = systemErrors.getMessage();
        this.errorDetail ="";
    }

    public ErrorDTO(String errorCode, String errorMsg, String errorDetail) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.errorDetail = errorDetail;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

}
