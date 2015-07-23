package intership.dev.contact;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
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

        getDatabase();

        // loadData from SQLite
        new LoadDataTask().execute();

        initialize();
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
                        + "id integer primary key autoincrement,"
                        + "name text, "
                        + "description text)";
                mDatabase.execSQL(sqlContact);
                Toast.makeText(MainActivity.this, "OK OK", Toast.LENGTH_LONG).show();

                // create some example data
                ContentValues values = new ContentValues();
                values.put("name", "Jack Black");
                values.put("description", "Jack Black's Desciption");
                if (mDatabase.insert("tblContact", null, values) == -1) {
                    Toast.makeText(this, "Faild to add record", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return mDatabase;
    }

    /**
     * initialize views from layout
     */
    private void initialize() {
        hbListContacts = (HeaderBar) findViewById(R.id.hbListContacts);
        mLvContacts = (ListView) findViewById(R.id.lvContacts);
        mDeleteDialog = new DeleteDialog(this);

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
                    //TODO load more listview
                    loadMoreData();
                    mListViewContactsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    /**
     * Loading more data here
     */
    private void loadMoreData() {
        // example data
        for (int j = 0; j < 10; j++) {
            mContacts.add(new Contact("Thomas Anders", "Thomas Anders", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar1)));
            mContacts.add(new Contact("Valery Meladze", "Valery Meladze", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar2)));
            mContacts.add(new Contact("Jack Black", "Jack Black", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar3)));
            mContacts.add(new Contact("Kevin James", "Kevin James", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar4)));
        }
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
    private class LoadDataTask extends AsyncTask<Void, Void, ArrayList<Contact>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Loading data...");
            mProgressDialog.show();
        }

        @Override
        protected ArrayList<Contact> doInBackground(Void... voids) {
            // example data
            for (int j = 0; j < 10; j++) {
                mContacts.add(new Contact("Thomas Anders", "Thomas Anders", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar1)));
                mContacts.add(new Contact("Valery Meladze", "Valery Meladze", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar2)));
                mContacts.add(new Contact("Jack Black", "Jack Black", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar3)));
                mContacts.add(new Contact("Kevin James", "Kevin James", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar4)));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mListViewContactsAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            super.onPostExecute(contacts);
            mProgressDialog.dismiss();
        }
    }
}

