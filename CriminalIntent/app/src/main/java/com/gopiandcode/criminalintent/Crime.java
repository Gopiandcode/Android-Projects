package com.gopiandcode.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 *  Model for the application, encapsulates details of an office crime.
 * Created by gopia on 03/06/2017.
 */

public class Crime {
    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiresPolice;

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Crime() {
        mID = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(UUID id) {
        mID = id;
        mDate = new Date();
    }

    public boolean isRequiresPolice() {
        return mRequiresPolice;
    }

    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
    }
}
