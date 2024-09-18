package cn.zhang.sun.gameassistant.domain.dto;

import cn.zhang.sun.gameassistant.common.constants.Constants;

public class Query {
    // Fields
    private int currentPageNum = 1;
    private int pageSize = Constants.PAGE_SIZE;
    private int totalRecordNum = 0;
    private String queryCondition = "";
    private String queryResultJson = "";

    // Getters and Setters
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPageNum() {
        return this.totalRecordNum / this.pageSize + (this.totalRecordNum % this.pageSize == 0 ? 0 : 1);
    }

    public int getTotalRecordNum() {
        return totalRecordNum;
    }

    public void setTotalRecordNum(int totalRecordNum) {
        this.totalRecordNum = totalRecordNum;
    }

    public String getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public String getQueryResultJson() {
        return queryResultJson;
    }

    public void setQueryResultJson(String queryResultJson) {
        this.queryResultJson = queryResultJson;
    }

    // Constructors
    public Query() {
    }

    public Query(int currentPageNum, int pageSize, int totalRecordNum, String queryCondition, String queryResultJson) {
        this.currentPageNum = currentPageNum;
        this.pageSize = pageSize;
        this.totalRecordNum = totalRecordNum;
        this.queryCondition = queryCondition;
        this.queryResultJson = queryResultJson;
    }

}
