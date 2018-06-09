package com.diligroup.bean;

/**
 * Created by hjf on 2016/9/12.
 */
public class UpdateVersionBean extends CommonBean {

    /**
     * downloadPath : http://images.ypp2015.com/ypp/upload/apk/apk_debug_15.apk
     * versionCode : 2
     * versionName : V1.0
     */

    private MapBean map;

    public MapBean getMap() {
        return map;
    }

    public void setMap(MapBean map) {
        this.map = map;
    }

    public static class MapBean {
        private String downloadPath;
        private String versionCode;
        private String versionName;

        public boolean isForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        private boolean forceUpdate;
        private String describe;

        public String getDownloadPath() {
            return downloadPath;
        }

        public void setDownloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
    }
}
