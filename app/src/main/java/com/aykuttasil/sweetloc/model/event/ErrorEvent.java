package com.aykuttasil.sweetloc.model.event;

/**
 * Created by aykutasil on 4.07.2016.
 */
public class ErrorEvent {

    private String ErrorContent;
    private int ErrorCode;
    private String ErrorTitle;

    public String getErrorContent() {
        return ErrorContent;
    }

    public void setErrorContent(String errorContent) {
        ErrorContent = errorContent;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorTitle() {
        return ErrorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        ErrorTitle = errorTitle;
    }
}
