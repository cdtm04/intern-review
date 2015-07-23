package intership.dev.contact;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * MainActivity
 */
public class MainActivity extends FragmentActivity implements ListViewContactsAdapter.OnClickListViewContacts {

    private SwipeRefreshLayout mSrlRefreshContacts;
    private HeaderBar hbListContacts;
    private ListView mLvContacts;
    private ListViewContactsAdapter mListViewContactsAdapter;
    private ArrayList<Contact> mContacts = new ArrayList<>();

    private DeleteDialog mDeleteDialog;
    private ProgressDialog mProgressDialog;

    private SQLiteDatabase mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating database and table tblContact and return new database, if it's existing return it
        getDatabase();

        initialize();

        // load first Data from database
        new LoadingFirstDataTask().execute();
    }

    /**
     * Checking if a table is existing in a batabase
     *
     * @param database  The database want to check
     * @param tableName - The table want to check
     * @return true if it's existing
     */
    private boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    /**
     * Creating database and table tblContact and return new database, if it's existing return it
     *
     * @return The database
     */
    private SQLiteDatabase getDatabase() {
        try {
            mDatabase = openOrCreateDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContactApp/contact.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (mDatabase != null) {
                // if tblContact is existing, return itself
                if (isTableExists(mDatabase, "tblContact"))
                    return mDatabase;

                // if tblContact is not existing, create it
                mDatabase.setLocale(Locale.getDefault());
                mDatabase.setVersion(1);
                String sqlContact = "create table tblContact ("
                        + "id integer,"
                        + "name text, "
                        + "description text)";
                mDatabase.execSQL(sqlContact);
                Toast.makeText(MainActivity.this, "OK OK", Toast.LENGTH_LONG).show();

                // create some example data
                for (int i = 0; i < 100; i++) {
                    ContentValues values = new ContentValues();
                    values.put("id", i);
                    values.put("name", "Contact " + i);
                    values.put("description", "Contact " + i + "'s Desciption");
                    if (mDatabase.insert("tblContact", null, values) == -1) {
                        Toast.makeText(this, "Faild to add record", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return mDatabase;
    }

    /**
     * initialize views from layout and events
     */
    private void initialize() {
        // get
        mSrlRefreshContacts = (SwipeRefreshLayout) findViewById(R.id.srlRefreshContacts);
        hbListContacts = (HeaderBar) findViewById(R.id.hbListContacts);
        mLvContacts = (ListView) findViewById(R.id.lvContacts);
        mDeleteDialog = new DeleteDialog(this);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setCancelable(false);

        mListViewContactsAdapter = new ListViewContactsAdapter(this, mContacts);
        mListViewContactsAdapter.setOnClickListViewContacts(this);

        mLvContacts.setAdapter(mListViewContactsAdapter);
        // hanlde event scroll to the last item of listview
        mLvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (mLvContacts.getLastVisiblePosition() - mLvContacts.getHeaderViewsCount() -
                        mLvContacts.getFooterViewsCount()) >= (mListViewContactsAdapter.getCount() - 1)) {
                    new LoadingNextDataTask().execute();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        // refresh page
        mSrlRefreshContacts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // call LoadingFirstDataTask to refresh
                new LoadingFirstDataTask().execute();
            }
        });
    }

    @Override
    public void onClickBtnEdit(int position) {
        // creating a contactFragment and sending it the contact to show
        ContactFragment contactFragment = new ContactFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ContactFragment.EXTRA_CONTACT, mContacts.get(position));
        contactFragment.setArguments(bundle);

        // adding fragment in the layout
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.rlContactFragment, contactFragment);
        fragmentTransaction.addToBackStack("ContactFragment");
        fragmentTransaction.commit();

        // change title of HeaderBar
        hbListContacts.setTitle("Contact");
    }

    @Override
    public void onClickBtnDelete(int position) {
        //TODO when click the delete button on mLvContacts
        mDeleteDialog.setMessage("Do you want to <b>delete</b><br><b>" + mContacts.get(position).getName() + "</b>?");
        mDeleteDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // reset headerbar title
        hbListContacts.setTitle("Contacts");
    }

    /**
     * Loading beginning data
     */
    private class LoadingFirstDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // if mSrlRefreshContacts is refreshing, don't need mProgressDialog to show
            if (!mSrlRefreshContacts.isRefreshing()) {
                mProgressDialog.setMessage("Loading data...");
                mProgressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mContacts.clear();
            // get 30 first rows from database
            Cursor cursor = mDatabase.rawQuery("select * from tblContact where id < 30", null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                // add to arraylist
                mContacts.add(new Contact(name, description, BitmapFactory.decodeResource(MainActivity.this.getResources(),
                        R.drawable.img_avatar1)));
                cursor.moveToNext();
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mListViewContactsAdapter.notifyDataSetChanged();
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (mSrlRefreshContacts.isRefreshing()) {
                mSrlRefreshContacts.setRefreshing(false);
            }
        }
    }

    /**
     * Loading next data
     */
    private class LoadingNextDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Loading more...");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // load next 30 rows from database
            Cursor cursor = mDatabase.rawQuery("select * from tblContact where id >= " + mContacts.size() + " and id <= " + (mContacts.size() + 30), null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                // add to arraylist
                mContacts.add(new Contact(name, description, BitmapFactory.decodeResource(MainActivity.this.getResources(),
                        R.drawable.img_avatar1)));
                cursor.moveToNext();
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mListViewContactsAdapter.notifyDataSetChanged();
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (mSrlRefreshContacts.isRefreshing()) {
                mSrlRefreshContacts.setRefreshing(false);
            }
        }
    }
}
