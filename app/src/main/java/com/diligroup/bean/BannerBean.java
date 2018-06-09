package com.diligroup.bean;

import java.util.List;

/**
 * Created by hjf on 2016/7/21.
 */
public class BannerBean extends CommonBean {

    private int totalCount;
    private List<ImageUrlBean> imageUrl;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ImageUrlBean> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<ImageUrlBean> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static class ImageUrlBean {
        private String imageUrl;
        private String linkUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }
    }
}
