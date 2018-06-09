package com.diligroup.utils.download;

/**
 * Created by Administrator on 2016/6/17.
 */
public class DownloadInfo {
    private int threadId;
    private int startPos;
    private int endPos;
    private int compeleteSize;
    private String url;

    /**
     * 构造函数
     *
     * @param threadId
     *            下载器id/线程id
     * @param l
     *            开始下载的节点
     * @param m
     *            结束下载的节点
     * @param compeleteSize
     *            完成的进度
     * @param url
     *            下载器网络标识/下载地址
     */
    public DownloadInfo(int threadId, int startPos, int endPos,
                        int compeleteSize, String url) {
        this.threadId = threadId;
        this.startPos = startPos;
        this.endPos = endPos;
        this.compeleteSize = compeleteSize;
        this.url = url;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public int getCompeleteSize() {
        return compeleteSize;
    }

    public void setCompeleteSize(int compeleteSize) {
        this.compeleteSize = compeleteSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
