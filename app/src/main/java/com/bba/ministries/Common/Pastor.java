package com.bba.ministries.Common;

/**
 * Created by v-62 on 12/9/2016.
 */

public class Pastor {
    public String id,name,pic,phone,facebook,twitter,email,designation;
    boolean isfbSelected;

    public boolean istwitterSelected() {
        return istwitterSelected;
    }

    public void setIstwitterSelected(boolean istwitterSelected) {
        this.istwitterSelected = istwitterSelected;
    }

    public boolean isfbSelected() {
        return isfbSelected;
    }

    public void setIsfbSelected(boolean isfbSelected) {
        this.isfbSelected = isfbSelected;
    }

    boolean istwitterSelected;



    public Pastor(String id, String name, String pic, String phone, String facebook, String twitter, String email, String designation)
    {
        this.id=id;
        this.name=name;
        this.pic=pic;
        this.phone=phone;
        this.facebook=facebook;
        this.twitter=twitter;
        this.email=email;
        this.designation=designation;
    }
}
