package com.gopiandcode.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by gopia on 03/06/2017.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();

        // DEBUG code - test crimes
        for(int i =0; i<100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved((i%13*5)%2 == 0);
            crime.setRequiresPolice((i%17*23)%2 == 1);
            mCrimes.add(crime);
        }

    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id)  {
        for(Crime crime : mCrimes) {
            if(crime.getID().equals(id))
                return crime;
        }
        return null;
    }

    public static CrimeLab get(Context context) {
        if(sCrimeLab == null)
            sCrimeLab = new CrimeLab(context);
        return sCrimeLab;
    }
}
