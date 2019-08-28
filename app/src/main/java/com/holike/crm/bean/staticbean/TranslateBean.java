package com.holike.crm.bean.staticbean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslateBean {

    /**
     * tSpeakUrl : http://openapi.youdao.com/ttsapi?q=%E5%86%85%E5%AE%B9&langType=zh-CHS&sign=945869C6441D46C8FDD2CA17A7A4BECD&salt=1539164398531&voice=4&format=mp3&appKey=7b08cdb45d2131a8
     * web : [{"value":["内容","含量","整个页面","目录"],"key":"content"},{"value":["留言内容"],"key":"content "},{"value":["内容农场","形式农场","文字工厂","内容工场"],"key":"Content Farm"}]
     * query : content
     * translation : ["内容"]
     * errorCode : 0
     * dict : {"url":"yddict://m.youdao.com/dict?le=eng&q=content"}
     * webdict : {"url":"http://m.youdao.com/dict?le=eng&q=content"}
     * basic : {"exam_type":["高中","IELTS","GRE","商务英语"],"us-phonetic":"'kɑnt?nt","phonetic":"k?n'tent","uk-phonetic":"k?n'tent","uk-speech":"http://openapi.youdao.com/ttsapi?q=content&langType=en&sign=0999CFA1A79336CE42D06021BD91C484&salt=1539164398531&voice=5&format=mp3&appKey=7b08cdb45d2131a8","explains":["n. 内容，目录；满足；容量","adj. 满意的","vt. 使满足","n. (Content)人名；(法)孔唐"],"us-speech":"http://openapi.youdao.com/ttsapi?q=content&langType=en&sign=0999CFA1A79336CE42D06021BD91C484&salt=1539164398531&voice=6&format=mp3&appKey=7b08cdb45d2131a8"}
     * l : EN2zh-CHS
     * speakUrl : http://openapi.youdao.com/ttsapi?q=content&langType=en&sign=0999CFA1A79336CE42D06021BD91C484&salt=1539164398531&voice=4&format=mp3&appKey=7b08cdb45d2131a8
     */

    private String tSpeakUrl;
    private String query;
    private String errorCode;
    private DictBean dict;
    private WebdictBean webdict;
    private BasicBean basic;
    private String l;
    private String speakUrl;
    private List<WebBean> web;
    private List<String> translation;

    public String getTSpeakUrl() {
        return tSpeakUrl;
    }

    public void setTSpeakUrl(String tSpeakUrl) {
        this.tSpeakUrl = tSpeakUrl;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public DictBean getDict() {
        return dict;
    }

    public void setDict(DictBean dict) {
        this.dict = dict;
    }

    public WebdictBean getWebdict() {
        return webdict;
    }

    public void setWebdict(WebdictBean webdict) {
        this.webdict = webdict;
    }

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getSpeakUrl() {
        return speakUrl;
    }

    public void setSpeakUrl(String speakUrl) {
        this.speakUrl = speakUrl;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public static class DictBean {
        /**
         * url : yddict://m.youdao.com/dict?le=eng&q=content
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class WebdictBean {
        /**
         * url : http://m.youdao.com/dict?le=eng&q=content
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class BasicBean {
        /**
         * exam_type : ["高中","IELTS","GRE","商务英语"]
         * us-phonetic : 'kɑnt?nt
         * phonetic : k?n'tent
         * uk-phonetic : k?n'tent
         * uk-speech : http://openapi.youdao.com/ttsapi?q=content&langType=en&sign=0999CFA1A79336CE42D06021BD91C484&salt=1539164398531&voice=5&format=mp3&appKey=7b08cdb45d2131a8
         * explains : ["n. 内容，目录；满足；容量","adj. 满意的","vt. 使满足","n. (Content)人名；(法)孔唐"]
         * us-speech : http://openapi.youdao.com/ttsapi?q=content&langType=en&sign=0999CFA1A79336CE42D06021BD91C484&salt=1539164398531&voice=6&format=mp3&appKey=7b08cdb45d2131a8
         */

        @SerializedName("us-phonetic")
        private String usphonetic;
        private String phonetic;
        @SerializedName("uk-phonetic")
        private String ukphonetic;
        @SerializedName("uk-speech")
        private String ukspeech;
        @SerializedName("us-speech")
        private String usspeech;
        private List<String> exam_type;
        private List<String> explains;

        public String getUsphonetic() {
            return usphonetic;
        }

        public void setUsphonetic(String usphonetic) {
            this.usphonetic = usphonetic;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUkphonetic() {
            return ukphonetic;
        }

        public void setUkphonetic(String ukphonetic) {
            this.ukphonetic = ukphonetic;
        }

        public String getUkspeech() {
            return ukspeech;
        }

        public void setUkspeech(String ukspeech) {
            this.ukspeech = ukspeech;
        }

        public String getUsspeech() {
            return usspeech;
        }

        public void setUsspeech(String usspeech) {
            this.usspeech = usspeech;
        }

        public List<String> getExam_type() {
            return exam_type;
        }

        public void setExam_type(List<String> exam_type) {
            this.exam_type = exam_type;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }
    }

    public static class WebBean {
        /**
         * value : ["内容","含量","整个页面","目录"]
         * key : content
         */

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }
}
