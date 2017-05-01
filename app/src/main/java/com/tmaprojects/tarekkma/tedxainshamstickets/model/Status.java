package com.tmaprojects.tarekkma.tedxainshamstickets.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tarekkma on 4/22/17.
 */

public class Status {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("err")
    @Expose
    private String err;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
