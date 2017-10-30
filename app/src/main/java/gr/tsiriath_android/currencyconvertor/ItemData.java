package gr.tsiriath_android.currencyconvertor;

/**
 * Created by tsiriath on 30/10/2017.
 */

 class ItemData {

    private String txt;
    private Integer imgId;

    ItemData(String text, Integer imageId){
        this.txt=text;
        this.imgId=imageId;
    }

    String getText(){
        return txt;
    }

    Integer getImageId(){
        return imgId;
    }
}