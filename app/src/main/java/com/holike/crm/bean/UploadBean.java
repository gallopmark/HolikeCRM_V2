package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2017/12/1.
 * 上传头像参数bean
 */

public class UploadBean implements Serializable {

    /**
     * accessId : STS.DF4LFTGZdd2VV3D5gFFfTRQi3
     * accessSecret : 8ExFiZYrdqQb9DQWN67ZbqU9PPkYTqvaksmda4UXtQmr
     * bucketName : chenguisheng
     * endpoint : http://oss-cn-shenzhen.aliyuncs.com
     * host : https://chenguisheng.oss-cn-shenzhen.aliyuncs.com/
     * securityToken : CAIS%2FwF1q6Ft5B2yfSjIpYWBB%2FzgqoVF0%2FC9VBX10WcTSul4vZTC0Tz2IHlNenloBOoZs%2FsynGhU6vsTlqB6T55OSAmcNZIoLUOIZuvlMeT7oMWQweEuuv%2FMQBquaXPS2MvVfJ%2BOLrf0ceusbFbpjzJ6xaCAGxypQ12iN%2B%2Fm6%2FNgdc9FHHP7D1x8CcxROxFppeIDKHLVLozNCBPxhXfKB0ca3WgZgGhku6Ok2Z%2FeuFiMzn%2BCk7NK%2Bdqvc8L4MZYzYcoiA%2B3YhrImKvDztwdL8AVP%2BatMi6hJxCzKpNn1ASMKukzYY7KJqIIyc1IhPvRmRv9ew%2BTxjuEg4rXDeS4BbKPFmosagAEwylVX4I4gTJiKXZoWkeE6%2Bm2cQZDKiFngzt%2BsVPaHHBI21j%2B0lBHrNdZ2cQwXaJx4ptuGzeA2MI7GAYnSgrFj33Dd0I9fgN2%2FctLwUisMwTEZ33Cguo4eG8vgAN%2BD4K%2BuCwNbun3gGpRm7%2FSVNgmNM74qZdl8dR7IJSjzx%2Fl5mw%3D%3D
     */

    private String accessId;
    private String accessSecret;
    private String bucketName;
    private String endpoint;
    private String host;
    private String securityToken;

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
