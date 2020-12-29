package com.wu.loushanyun.basemvp.m;

import java.util.List;

public class ProvinceCitysBean {


    private List<ProvinceBean> province;

    public List<ProvinceBean> getProvince() {
        return province;
    }

    public void setProvince(List<ProvinceBean> province) {
        this.province = province;
    }

    public static class ProvinceBean {
        /**
         * id : 110000
         * name : 北京市
         * cityList : [{"id":"110000","name":"北京市","cityList":[{"id":"110101","name":"东城区"},{"id":"110102","name":"西城区"},{"id":"110105","name":"朝阳区"},{"id":"110106","name":"丰台区"},{"id":"110107","name":"石景山区"},{"id":"110108","name":"海淀区"},{"id":"110109","name":"门头沟区"},{"id":"110111","name":"房山区"},{"id":"110112","name":"通州区"},{"id":"110113","name":"顺义区"},{"id":"110114","name":"昌平区"},{"id":"110115","name":"大兴区"},{"id":"110116","name":"怀柔区"},{"id":"110117","name":"平谷区"},{"id":"110118","name":"密云区"},{"id":"110119","name":"延庆区"}]}]
         * gisBd09Lat : 0
         * gisBd09Lng : 0
         * gisGcj02Lat : 0
         * gisGcj02Lng : 0
         * pinYin : HongKong
         */

        private String id;
        private String name;
        private int gisBd09Lat;
        private int gisBd09Lng;
        private int gisGcj02Lat;
        private int gisGcj02Lng;
        private String pinYin;
        private List<CityListBeanX> cityList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGisBd09Lat() {
            return gisBd09Lat;
        }

        public void setGisBd09Lat(int gisBd09Lat) {
            this.gisBd09Lat = gisBd09Lat;
        }

        public int getGisBd09Lng() {
            return gisBd09Lng;
        }

        public void setGisBd09Lng(int gisBd09Lng) {
            this.gisBd09Lng = gisBd09Lng;
        }

        public int getGisGcj02Lat() {
            return gisGcj02Lat;
        }

        public void setGisGcj02Lat(int gisGcj02Lat) {
            this.gisGcj02Lat = gisGcj02Lat;
        }

        public int getGisGcj02Lng() {
            return gisGcj02Lng;
        }

        public void setGisGcj02Lng(int gisGcj02Lng) {
            this.gisGcj02Lng = gisGcj02Lng;
        }

        public String getPinYin() {
            return pinYin;
        }

        public void setPinYin(String pinYin) {
            this.pinYin = pinYin;
        }

        public List<CityListBeanX> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBeanX> cityList) {
            this.cityList = cityList;
        }

        public static class CityListBeanX {
            /**
             * id : 110000
             * name : 北京市
             * cityList : [{"id":"110101","name":"东城区"},{"id":"110102","name":"西城区"},{"id":"110105","name":"朝阳区"},{"id":"110106","name":"丰台区"},{"id":"110107","name":"石景山区"},{"id":"110108","name":"海淀区"},{"id":"110109","name":"门头沟区"},{"id":"110111","name":"房山区"},{"id":"110112","name":"通州区"},{"id":"110113","name":"顺义区"},{"id":"110114","name":"昌平区"},{"id":"110115","name":"大兴区"},{"id":"110116","name":"怀柔区"},{"id":"110117","name":"平谷区"},{"id":"110118","name":"密云区"},{"id":"110119","name":"延庆区"}]
             */

            private String id;
            private String name;
            private List<CityListBean> cityList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<CityListBean> getCityList() {
                return cityList;
            }

            public void setCityList(List<CityListBean> cityList) {
                this.cityList = cityList;
            }

            public static class CityListBean {
                /**
                 * id : 110101
                 * name : 东城区
                 */

                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
