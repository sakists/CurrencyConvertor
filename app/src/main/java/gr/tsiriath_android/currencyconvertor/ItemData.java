package gr.tsiriath_android.currencyconvertor;

/* Created by tsiriath on 30/10/2017. */

 class ItemData {

    private final String txt;       //Final data cannot be overridden by subclasses
    private final String descr;    //Final data cannot be overridden by subclasse
    private final Integer imgId;    //Final data cannot be overridden by subclasses

     ItemData(String text, String text2, Integer imageId){
        this.txt=text;
        this.descr=text2;
        this.imgId=imageId;
    }

    String getText(){
        return txt;
    }

    String getDescr(){
         return descr;
     }

    Integer getImageId(){ return imgId; }
}