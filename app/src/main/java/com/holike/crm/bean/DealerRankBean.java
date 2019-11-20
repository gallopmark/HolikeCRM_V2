package com.holike.crm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/5/28.
 * 经销商排行
 */

public class DealerRankBean implements Serializable {
    /**
     * dealerData : {"achievementAfter":"测试内容q849","city":"晋中","cityCode":"B2","firstThree":[{"rank":"测试内容f478","achievementAfter":"493","achievementBefore":"371","area":"江苏","city":"盐城","name":"吴海燕"},{"rank":"测试内容f478","achievementAfter":"389","achievementBefore":"230","area":"晋蒙","city":"运城","name":"裴彩云"},{"rank":"测试内容f478","achievementAfter":"382","achievementBefore":"299","area":"豫北","city":"安阳","name":"马继飞"}],"preCity":"开封","rang":[{"isIn":"0","name":"卓越区"},{"isIn":"0","name":"优秀区"},{"isIn":"1","name":"潜力区"},{"isIn":"0","name":"努力区"},{"isIn":"0","name":"危险区"}],"rank":"26/53"}
     * isDealer : 测试内容41ih
     * rankList : [{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"493","achievementBefore":"371","area":"江苏","city":"盐城","name":"吴海燕"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"389","achievementBefore":"230","area":"晋蒙","city":"运城","name":"裴彩云"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"382","achievementBefore":"299","area":"豫北","city":"安阳","name":"马继飞"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"375","achievementBefore":"262","area":"江苏","city":"泰州","name":"唐丽滨"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"357","achievementBefore":"279","area":"豫南","city":"平顶山","name":"董运锋"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"344","achievementBefore":"282","area":"安徽","city":"蚌埠","name":"张义"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"277","achievementBefore":"227","area":"甘青新藏","city":"西宁","name":"齐卫国"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"275","achievementBefore":"173","area":"福建","city":"漳州","name":"林昌实"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"263","achievementBefore":"175","area":"贵州","city":"遵义","name":"李迎春"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"260","achievementBefore":"156","area":"河北","city":"邯郸","name":"蔡振军"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"255","achievementBefore":"192","area":"广东海南","city":"惠州","name":"贾瑛"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"244","achievementBefore":"159","area":"鲁东","city":"日照","name":"宋雄文"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"242","achievementBefore":"142","area":"晋蒙","city":"鄂尔多斯","name":"王伟"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"211","achievementBefore":"169","area":"广西","city":"桂林","name":"罗程伟"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"210","achievementBefore":"130","area":"广东海南","city":"清远","name":"鲍国伟"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"208","achievementBefore":"152","area":"晋蒙","city":"长治","name":"郭涛"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"203","achievementBefore":"129","area":"广东海南","city":"韶关","name":"董世新"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"202","achievementBefore":"155","area":"鲁东","city":"威海","name":"徐从军"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"199","achievementBefore":"127","area":"广东海南","city":"潮州","name":"何小迁"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"185","achievementBefore":"114","area":"广东海南","city":"肇庆","name":"欧友华"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"184","achievementBefore":"111","area":"江苏","city":"连云港","name":"李德龙"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"183","achievementBefore":"117","area":"河北","city":"张家口","name":"王峰 "},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"167","achievementBefore":"105","area":"东三省","city":"锦州","name":"李闯"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"152","achievementBefore":"91","area":"江西","city":"赣州","name":"戴昌宁"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"149","achievementBefore":"106","area":"豫北","city":"开封","name":"黄俊杰"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"146","achievementBefore":"82","area":"晋蒙","city":"晋中","name":"姜文亮"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"142","achievementBefore":"111","area":"豫南","city":"南阳","name":"周月"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"135","achievementBefore":"96","area":"陕宁","city":"宝鸡","name":"常党科"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"133","achievementBefore":"100","area":"湖南","city":"衡阳","name":"蒋晓梅"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"133","achievementBefore":"105","area":"河北","city":"秦皇岛","name":"张学尚"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"128","achievementBefore":"80","area":"湖北","city":"荆州","name":"计均文"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"123","achievementBefore":"91","area":"豫北","city":"新乡","name":"段锐"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"123","achievementBefore":"71","area":"湖北","city":"黄石","name":"石教康"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"117","achievementBefore":"77","area":"陕宁","city":"银川","name":"高峰"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"114","achievementBefore":"65","area":"晋蒙","city":"临汾","name":"李明海"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"114","achievementBefore":"78","area":"广东海南","city":"阳江","name":"鲁连军"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"110","achievementBefore":"66","area":"豫南","city":"商丘","name":"林红芳  "},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"102","achievementBefore":"76","area":"江苏","city":"淮安","name":"杜腾飞"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"100","achievementBefore":"77","area":"湖南","city":"株洲","name":"王康"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"98","achievementBefore":"68","area":"福建","city":"龙岩","name":"刘士钟"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"92","achievementBefore":"61","area":"鲁北","city":"德州","name":"牛同灵"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"90","achievementBefore":"55","area":"鲁北","city":"菏泽","name":"李美霞"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"89","achievementBefore":"63","area":"湖北","city":"十堰","name":"陈虎"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"89","achievementBefore":"49","area":"广东海南","city":"中山","name":"王辉"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"79","achievementBefore":"48","area":"湖南","city":"湘潭","name":"王一帆"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"76","achievementBefore":"46","area":"安徽","city":"淮南","name":"张健"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"66","achievementBefore":"44","area":"湖南","city":"郴州","name":"吴婵娟"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"41","achievementBefore":"35","area":"福建","city":"莆田","name":"黄汉强"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"39","achievementBefore":"24","area":"四川","city":"自贡","name":"李勇强"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"39","achievementBefore":"38","area":"东三省","city":"齐齐哈尔","name":"邢雨"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"24","achievementBefore":"16","area":"东三省","city":"四平","name":"李圆月"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"21","achievementBefore":"16","area":"东三省","city":"绥化","name":"张明宇"},{"orderMoneyTarget":"测试内容2sdh","rank":"测试内容58np","achievementAfter":"-","achievementBefore":"1","area":"浙江","city":"湖州","name":"李建挺"}]
     * select : {"cityCode":"B2"}
     * selectData : [{"cityCode":"B2"}]
     * timeData : 2018年01月01日-2018年05月25日
     */

    private DealerDataBean dealerData;
    private String isDealer;
    private SelectBean select;
    private String timeData;
    private List<RankListBean> rankList;
    private List<SelectDataBean> selectData;

    public DealerDataBean getDealerData() {
        return dealerData;
    }

    public void setDealerData(DealerDataBean dealerData) {
        this.dealerData = dealerData;
    }

    public String getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(String isDealer) {
        this.isDealer = isDealer;
    }

    public SelectBean getSelect() {
        return select;
    }

    public void setSelect(SelectBean select) {
        this.select = select;
    }

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public List<RankListBean> getRankList() {
        return rankList;
    }

    public void setRankList(List<RankListBean> rankList) {
        this.rankList = rankList;
    }

    public List<SelectDataBean> getSelectData() {
        return selectData;
    }

    public void setSelectData(List<SelectDataBean> selectData) {
        this.selectData = selectData;
    }

    public static class DealerDataBean implements Serializable {
        /**
         * achievementAfter : 测试内容poek
         * city : 晋中
         * cityCode : B2
         * firstThree : [{"rank":"测试内容5h5u","achievementAfter":"493","achievementBefore":"371","area":"江苏","city":"盐城","name":"吴海燕"},{"rank":"测试内容5h5u","achievementAfter":"389","achievementBefore":"230","area":"晋蒙","city":"运城","name":"裴彩云"},{"rank":"测试内容5h5u","achievementAfter":"382","achievementBefore":"299","area":"豫北","city":"安阳","name":"马继飞"}]
         * preCity : 开封
         * rang : [{"isIn":"0","name":"卓越区","position":"10%"},{"isIn":"0","name":"优秀区","position":"10%"},{"isIn":"1","name":"潜力区","position":"10%"},{"isIn":"0","name":"努力区","position":"10%"},{"isIn":"0","name":"危险区","position":"10%"}]
         * rank : 26/53
         * rankAft : 测试内容rh48
         * rankPre : 测试内容s2rb
         */

        private String achievementAfter;
        private String city;
        private String cityCode;
        private String preCity;
        private String rank;
        private String rankAft;
        private String rankPre;
        private List<FirstThreeBean> firstThree;
        private List<RangBean> rang;

        public String getAchievementAfter() {
            return achievementAfter;
        }

        public void setAchievementAfter(String achievementAfter) {
            this.achievementAfter = achievementAfter;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getPreCity() {
            return preCity;
        }

        public void setPreCity(String preCity) {
            this.preCity = preCity;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getRankAft() {
            return rankAft;
        }

        public void setRankAft(String rankAft) {
            this.rankAft = rankAft;
        }

        public String getRankPre() {
            return rankPre;
        }

        public void setRankPre(String rankPre) {
            this.rankPre = rankPre;
        }

        public List<FirstThreeBean> getFirstThree() {
            return firstThree;
        }

        public void setFirstThree(List<FirstThreeBean> firstThree) {
            this.firstThree = firstThree;
        }

        public List<RangBean> getRang() {
            return rang == null ? new ArrayList<>() : rang;
        }

        public void setRang(List<RangBean> rang) {
            this.rang = rang;
        }

        public static class FirstThreeBean implements Serializable {
            /**
             * rank : 测试内容5h5u
             * achievementAfter : 493
             * achievementBefore : 371
             * area : 江苏
             * city : 盐城
             * name : 吴海燕
             */

            private String rank;
            private String achievementAfter;
            private String achievementBefore;
            private String area;
            private String city;
            private String name;

            public String getRank() {
                return rank;
            }

            public void setRank(String rank) {
                this.rank = rank;
            }

            public String getAchievementAfter() {
                return achievementAfter;
            }

            public void setAchievementAfter(String achievementAfter) {
                this.achievementAfter = achievementAfter;
            }

            public String getAchievementBefore() {
                return achievementBefore;
            }

            public void setAchievementBefore(String achievementBefore) {
                this.achievementBefore = achievementBefore;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class RangBean implements Serializable {
            /**
             * isIn : 0
             * name : 卓越区
             * position : 10%
             */

            private String isIn;
            private String name;
            private String position;

            public String getIsIn() {
                return isIn;
            }

            public void setIsIn(String isIn) {
                this.isIn = isIn;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPosition() {
                return position == null ? "" : position;
            }

            public void setPosition(String position) {
                this.position = position;
            }
        }
    }

    public static class SelectBean implements Serializable {
        /**
         * cityCode : B2
         */

        private String cityCode;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
    }

    public static class RankListBean implements Serializable {
        /**
         * orderMoneyTarget : 测试内容2sdh
         * rank : 测试内容58np
         * achievementAfter : 493
         * achievementBefore : 371
         * area : 江苏
         * city : 盐城
         * name : 吴海燕
         */

        private String orderMoneyTarget;
        private String rank;
        private String achievementAfter;
        private String achievementBefore;
        private String area;
        private String city;
        private String name;

        public String getOrderMoneyTarget() {
            return orderMoneyTarget;
        }

        public void setOrderMoneyTarget(String orderMoneyTarget) {
            this.orderMoneyTarget = orderMoneyTarget;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getAchievementAfter() {
            return achievementAfter;
        }

        public void setAchievementAfter(String achievementAfter) {
            this.achievementAfter = achievementAfter;
        }

        public String getAchievementBefore() {
            return achievementBefore;
        }

        public void setAchievementBefore(String achievementBefore) {
            this.achievementBefore = achievementBefore;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SelectDataBean implements Serializable {
        /**
         * cityCode : B2
         */

        private String cityCode;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
    }
}
