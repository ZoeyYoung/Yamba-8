package com.marakana.android.yamba;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TimelineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = TimelineFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private static final String[] FROM = {
            StatusContract.Column.USER,
            StatusContract.Column.MESSAGE,
            StatusContract.Column.CREATED_AT//,
            //StatusContract.Column.CREATED_AT
    };
    private static final int[] TO = {
            R.id.list_item_text_user,
            R.id.list_item_text_message,
            R.id.list_item_text_created_at//,
            //R.id.list_item_text_freshness
    };
    // 任意的ID
    private static final int LOADER_ID = 42;
    private SimpleCursorAdapter mAdapter;
    private static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {

        @Override
        public boolean setViewValue(View view, Cursor cursor, int i) {
            long timestamp;
            // 自定义绑定
            switch (view.getId()) {
                case R.id.list_item_text_created_at:
                    timestamp = cursor.getLong(i);
                    CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp);
                    ((TextView) view).setText(relativeTime);
                    return true;
                default:
                    return false;
            }
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TimelineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//        // TODO: Change Adapter to display your content
//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("Loading data ...");
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item, null, FROM, TO, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mAdapter.setViewBinder(VIEW_BINDER);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        DetailsFragment fragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        if (fragment != null && fragment.isVisible()) {
            fragment.updateView(id);
        } else {
            startActivity(new Intent(getActivity(), DetailsActivity.class).putExtra(StatusContract.Column.ID, id));
        }
    }

    // 在一个非UI线程上执行
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id != LOADER_ID) {
            return null;
        }
        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(getActivity(), StatusContract.CONTENT_URI, null, null, null, StatusContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        DetailsFragment fragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        if (fragment != null && fragment.isVisible() && cursor.getCount() == 0) {
            fragment.updateView(-1);
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "onLoadFinished with cursor: " + cursor.getCount());
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

}
