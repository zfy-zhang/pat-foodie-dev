package com.pat.pojo.vo;

/**
 * @Description: 用于展示搜索商品搜索列表结果VO
 * @Author 不才人
 * @Create Date 2020/5/11 7:04 下午
 * @Modify
 */
public class SearchItemVO {
    private String itemId;
    private String itemName;
    private int sellCount;
    private String imgUrl;
    private int price;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getSellCount() {
        return sellCount;
    }

    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
