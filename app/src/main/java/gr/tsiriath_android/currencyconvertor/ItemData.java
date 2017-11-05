package gr.tsiriath_android.currencyconvertor;

/* Created by tsiriath on 30/10/2017. */

 class ItemData {

    private final String txt;       //Final data cannot be overridden by subclasses
    private final Integer imgId;    //Final data cannot be overridden by subclasses

    ItemData(String text, Integer imageId){
        this.txt=text;
        this.imgId=imageId;
    }

    String getText(){
        return txt;
    }

    Integer getImageId(){ return imgId; }
}