package com.diligroup.utils.download;

/**
 * Created by Administrator on 2016/6/17.
 */
public class DownloaderInfo {
    private int fileSize;// 文件的大小
    private int complete;// 下载的完成度
    private String url;// 下载器标识

    /**
     *
     * @param fileSize
     *            下载文件的大小
     * @param complete
     *            下载完成进度
     * @param url
     *            下载器标识/下载地址
     */
    public DownloaderInfo(int fileSize, int complete, String url) {
        this.fileSize = fileSize;
        this.complete = complete;
        this.url = url;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DownloaderInfo [fileSize=" + fileSize + ", complete="
                + complete + ", url=" + url + "]";
    }
}
