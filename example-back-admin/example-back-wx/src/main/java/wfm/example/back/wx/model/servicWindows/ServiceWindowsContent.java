package wfm.example.back.wx.model.servicWindows;

public class ServiceWindowsContent {

    /**
     * 服务窗名称
     */
    private String appName;

    /**
     * 服务窗头像，请使用照片上传接口上传图片获得image_url
     */
    private String logoUrl;

    /**
     * 欢迎语
     */
    private String publicGreeting;

    /**
     * 营业执照，请使用照片上传接口上传图片获得image_url
     */
    private String licenseUrl;

    /**
     * 第一张门店照片 请使用照片上传接口上传图片获得image_url
     */
    private String shopPic1;

    /**
     * 第二张门店照片 请使用照片上传接口上传图片获得image_url
     */
    private String shopPic2;

    /**
     * 第三张门店照片 请使用照片上传接口上传图片获得image_url
     */
    private String shopPic3;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPublicGreeting() {
        return publicGreeting;
    }

    public void setPublicGreeting(String publicGreeting) {
        this.publicGreeting = publicGreeting;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getShopPic1() {
        return shopPic1;
    }

    public void setShopPic1(String shopPic1) {
        this.shopPic1 = shopPic1;
    }

    public String getShopPic2() {
        return shopPic2;
    }

    public void setShopPic2(String shopPic2) {
        this.shopPic2 = shopPic2;
    }

    public String getShopPic3() {
        return shopPic3;
    }

    public void setShopPic3(String shopPic3) {
        this.shopPic3 = shopPic3;
    }

}
