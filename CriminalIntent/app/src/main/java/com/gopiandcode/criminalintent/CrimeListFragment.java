package com.gopiandcode.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gopia on 03/06/2017.
 */

public class CrimeListFragment extends Fragment {
    private static final int RESULT_CRIME_ACTIVITY = 1;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private boolean mSubtitleVisible;
    private abstract class AbstractCrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private int mPosition;


        public AbstractCrimeHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }


        public void bind(Crime crime, int position) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            mPosition = position;
        }

        @Override
        public void onClick(View v) {

            Intent intent  = CrimePagerActivity.newIntent(getActivity(), mCrime.getID());
            startActivityForResult(intent,RESULT_CRIME_ACTIVITY);
        }
    }

    private class CrimeHolder extends AbstractCrimeHolder {
        public CrimeHolder(LayoutInflater inflator, ViewGroup parent) {
            super(inflator.inflate(R.layout.list_item_crime, parent, false));
        }
    }

    private class SeriousCrimeHolder extends AbstractCrimeHolder {
        Button mReportToPolice;
        public SeriousCrimeHolder(LayoutInflater inflator, ViewGroup parent) {
            super(inflator.inflate(R.layout.list_item_crime_serious, parent, false));
            mReportToPolice = (Button) itemView.findViewById(R.id.report_crime_button);
            mReportToPolice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Crime reported to police", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<AbstractCrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public AbstractCrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if(viewType == 0)
                return new CrimeHolder(layoutInflater, parent);
            else
                return new SeriousCrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AbstractCrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime, position);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            if(crime.isRequiresPolice())
                return 1;
            else
                return 0;
        }
    }


    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private LinearLayout mEmptyCrimeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        int crimeCount = CrimeLab.get(getActivity()).getCrimes().size();

            view = inflater.inflate(R.layout.fragment_crime_list, container, false);
            mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
            mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mEmptyCrimeView = (LinearLayout) view.findViewById(R.id.empty_crime_list);

            if(savedInstanceState != null) {
                mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
            }


            view.findViewById(R.id.new_crime_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Crime crime = new Crime();
                    CrimeLab.get(getActivity()).addCrime(crime);
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getID());
                    startActivity(intent);
                }
            });

            updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(crimes.size() == 0) {
            // We'll need to recreate the whole view.
            mEmptyCrimeView.setVisibility(View.VISIBLE);
        } else {
            mEmptyCrimeView.setVisibility(View.INVISIBLE);
        }

        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getID());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_format_plural, crimeCount, crimeCount);

        if(!mSubtitleVisible)
            subtitle = null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
}
