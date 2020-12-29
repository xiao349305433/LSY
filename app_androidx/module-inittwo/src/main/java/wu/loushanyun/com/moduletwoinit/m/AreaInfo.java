package wu.loushanyun.com.moduletwoinit.m;

public class AreaInfo {


    /**
     * msg : success
     * code : 0
     * data : {"areaName":"华旭车间2","latitude":28.216646,"imageUrl":"http://www.loushanyun.net/static/LouShanCloudAllImage/911129929384575/locationImages/2018year/07month/11day/d0d7682d-57c6-4f96-95ce-cc06ca7c4989.png,http://www.loushanyun.net/static/LouShanCloudAllImage/911129929384575/locationImages/2018year/07month/11day/f734b4c9-5cca-4803-8d15-77a170981296.png,http://www.loushanyun.net/static/LouShanCloudAllImage/911129929384575/locationImages/2018year/07month/11day/f402c15f-0ca6-43f2-bd10-b935dba90920.png,http://www.loushanyun.net/static/LouShanCloudAllImage/911129929384575/locationImages/2018year/07month/11day/03c52f8c-0957-4d76-8710-6eee9416530f.png","areaNumber":"106N842559A28N216646","nums":3,"longitude":106.842559}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * areaName : 华旭车间2
         * latitude : 28.216646
         * imageUrl : http://www.loushanyun.net/static/LouShanCloudAllImage/911129929384575/locationImages/2018year/07month/11day/d0d7682d-57c6-4f96-95ce-cc06ca7c4989.png,http://www.loushanyun.net/static/LouShanCloudAllImage/911129929384575/locationImages/2018year/07month/11day/f734b4c9-5cca-4803-8d15-77a170981296.png,http://www.loushanyun.net/static/LouShanCloudAllImage/911129929384575/locationImages/2018year/07month/11day/f402c15f-0ca6-43f2-bd10-b935dba90920.png,http://www.loushanyun.net/static/LouShanCloudAllImage/911129929384575/locationImages/2018year/07month/11day/03c52f8c-0957-4d76-8710-6eee9416530f.png
         * areaNumber : 106N842559A28N216646
         * nums : 3
         * longitude : 106.842559
         */

        private String areaName;
        private double latitude;
        private String imageUrl;
        private String areaNumber;
        private int nums;
        private double longitude;

        @Override
        public String toString() {
            return "DataBean{" +
                    "areaName='" + areaName + '\'' +
                    ", latitude=" + latitude +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", areaNumber='" + areaNumber + '\'' +
                    ", nums=" + nums +
                    ", longitude=" + longitude +
                    '}';
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getAreaNumber() {
            return areaNumber;
        }

        public void setAreaNumber(String areaNumber) {
            this.areaNumber = areaNumber;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
