package wu.loushanyun.com.libraryfive.m;

import java.util.List;

public class GridNumBean {

    /**
     * status : 200
     * msg : success
     * count : 1
     * data : [{"gridcode":"4201000532113","center":{"CX":1.2722890872E7,"CY":3578964.8246,"longitude":114.29167329,"latitude":30.58496024},"elevation":0}]
     */

    private int status;
    private String msg;
    private int count;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * gridcode : 4201000532113
         * center : {"CX":1.2722890872E7,"CY":3578964.8246,"longitude":114.29167329,"latitude":30.58496024}
         * elevation : 0.0
         */

        private String gridcode;
        private CenterBean center;
        private double elevation;

        public String getGridcode() {
            return gridcode;
        }

        public void setGridcode(String gridcode) {
            this.gridcode = gridcode;
        }

        public CenterBean getCenter() {
            return center;
        }

        public void setCenter(CenterBean center) {
            this.center = center;
        }

        public double getElevation() {
            return elevation;
        }

        public void setElevation(double elevation) {
            this.elevation = elevation;
        }

        public static class CenterBean {
            /**
             * CX : 1.2722890872E7
             * CY : 3578964.8246
             * longitude : 114.29167329
             * latitude : 30.58496024
             */

            private double CX;
            private double CY;
            private double longitude;
            private double latitude;

            public double getCX() {
                return CX;
            }

            public void setCX(double CX) {
                this.CX = CX;
            }

            public double getCY() {
                return CY;
            }

            public void setCY(double CY) {
                this.CY = CY;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }
        }
    }
}
