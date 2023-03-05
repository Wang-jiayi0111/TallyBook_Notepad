package com.example.mymemory2.Bean;

//封装记录记事本信息实体类
public class NotebookBean {
    private String id;
    private String notebookContent;     //记录内容
    private String notebookTime;        //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotebookContent() {
        return notebookContent;
    }

    public void setNotebookContent(String notebookContent) {
        this.notebookContent = notebookContent;
    }

    public String getNotebookTime() {
        return notebookTime;
    }

    public void setNotebookTime(String notebookTime) {
        this.notebookTime = notebookTime;
    }
}
