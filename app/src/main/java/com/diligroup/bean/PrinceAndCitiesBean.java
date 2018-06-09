package com.diligroup.bean;

import java.util.List;

/**
 * Created by hjf on 2016/9/8.
 */
public class PrinceAndCitiesBean extends CommonBean {
    /**
     * list : [{"list":[{"name":"东城区","sortCode":"110100"},{"name":"西城区","sortCode":"110100"},{"name":"朝阳区","sortCode":"110100"},{"name":"丰台区","sortCode":"110100"},{"name":"石景山区","sortCode":"110100"},{"name":"海淀区","sortCode":"110100"},{"name":"门头沟区","sortCode":"110100"},{"name":"房山区","sortCode":"110100"},{"name":"通州区","sortCode":"110100"},{"name":"顺义区","sortCode":"110100"},{"name":"昌平区","sortCode":"110100"},{"name":"大兴区","sortCode":"110100"},{"name":"怀柔区","sortCode":"110100"},{"name":"平谷区","sortCode":"110100"},{"name":"密云县","sortCode":"110100"},{"name":"延庆县","sortCode":"110100"}],"name":"北京市","sortCode":"110100"}]
     * name : 北京
     * sortCode : 110000
     */

    private List<ProvinceListBean> districtList;

    public List<ProvinceListBean> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<ProvinceListBean> districtList) {
        this.districtList = districtList;
    }

    public static class ProvinceListBean {
        private String name;
        private String sortCode;
        /**
         * list : [{"name":"东城区","sortCode":"110100"},{"name":"西城区","sortCode":"110100"},{"name":"朝阳区","sortCode":"110100"},{"name":"丰台区","sortCode":"110100"},{"name":"石景山区","sortCode":"110100"},{"name":"海淀区","sortCode":"110100"},{"name":"门头沟区","sortCode":"110100"},{"name":"房山区","sortCode":"110100"},{"name":"通州区","sortCode":"110100"},{"name":"顺义区","sortCode":"110100"},{"name":"昌平区","sortCode":"110100"},{"name":"大兴区","sortCode":"110100"},{"name":"怀柔区","sortCode":"110100"},{"name":"平谷区","sortCode":"110100"},{"name":"密云县","sortCode":"110100"},{"name":"延庆县","sortCode":"110100"}]
         * name : 北京市
         * sortCode : 110100
         */

        private List<CityListBean> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSortCode() {
            return sortCode;
        }

        public void setSortCode(String sortCode) {
            this.sortCode = sortCode;
        }

        public List<CityListBean> getList() {
            return list;
        }

        public void setList(List<CityListBean> list) {
            this.list = list;
        }

        public static class CityListBean {
            private String name;
            private String sortCode;
            /**
             * name : 东城区
             * sortCode : 110100
             */

            private List<DistrictListBean> list;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSortCode() {
                return sortCode;
            }

            public void setSortCode(String sortCode) {
                this.sortCode = sortCode;
            }

            public List<DistrictListBean> getList() {
                return list;
            }

            public void setList(List<DistrictListBean> list) {
                this.list = list;
            }

            public static class DistrictListBean {
                private String name;
                private String sortCode;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSortCode() {
                    return sortCode;
                }

                public void setSortCode(String sortCode) {
                    this.sortCode = sortCode;
                }
            }
        }
    }
}
