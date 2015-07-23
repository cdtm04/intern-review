package intership.dev.contact;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * MainActivity
 */
public class MainActivity extends FragmentActivity {
    public final String TABLE_CONTACT = "tblContact";
    public final String COL_ID = "id";
    public final String COL_NAME = "name";
    public final String COL_DESCRIPTION = "description";
    public final String COL_AVATAR = "avatar";

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

        // Creating database and table tblContact and return new database, if it's existing return itself
        createDatabase();

        initialize();

        // load first Data from database, "true" param means load first rows
        new LoadingDataTask().execute(true);
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
     * Creating database and table tblContact and return new database, if it's existing return itself
     *
     * @return The database
     */
    private SQLiteDatabase createDatabase() {
        try {
            mDatabase = openOrCreateDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ContactApp/contact.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (mDatabase != null) {
                // if tblContact is existing, return itself
                if (isTableExists(mDatabase, TABLE_CONTACT))
                    return mDatabase;

                // if tblContact is not existing, create it
                mDatabase.setLocale(Locale.getDefault());
                mDatabase.setVersion(1);
                String sqlContact = "create table " + TABLE_CONTACT + " ("
                        + COL_ID + " integer,"
                        + COL_NAME + " text, "
                        + COL_DESCRIPTION + " text, "
                        + COL_AVATAR + " blob)";
                mDatabase.execSQL(sqlContact);
                Toast.makeText(MainActivity.this, "OK OK", Toast.LENGTH_LONG).show();

                // create some example data
                for (int i = 0; i < 100; i++) {
                    ContentValues values = new ContentValues();
                    values.put(COL_ID, i);
                    values.put(COL_NAME, "Contact " + i);
                    values.put(COL_DESCRIPTION, "Contact " + i + "'s Desciption");
                    values.put(COL_AVATAR, DbBitmapUtility.getBytes(BitmapFactory.decodeResource(getResources(),
                            R.drawable.img_avatar1)));
                    if (mDatabase.insert(TABLE_CONTACT, null, values) == -1) {
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
     * initialize fields
     */
    private void initialize() {
        // views from layout
        mSrlRefreshContacts = (SwipeRefreshLayout) findViewById(R.id.srlRefreshContacts);
        hbListContacts = (HeaderBar) findViewById(R.id.hbListContacts);
        mLvContacts = (ListView) findViewById(R.id.lvContacts);

        // init dialogs
        mDeleteDialog = new DeleteDialog(this);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setCancelable(false);

        // init adapter and set event
        mListViewContactsAdapter = new ListViewContactsAdapter(this, mContacts);
        mListViewContactsAdapter.setOnClickListViewContacts(new ListViewContactsAdapter.OnClickListViewContacts() {
            @Override
            public void onClickBtnEdit(int position) {
                clickBtnEdit(position);
            }

            @Override
            public void onClickBtnDelete(int position) {
                clickBtnDelete(position);
            }
        });
        // set adapter for listview
        mLvContacts.setAdapter(mListViewContactsAdapter);

        // event: scroll to the last item of listview
        mLvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (mLvContacts.getLastVisiblePosition() - mLvContacts.getHeaderViewsCount() -
                        mLvContacts.getFooterViewsCount()) >= (mListViewContactsAdapter.getCount() - 1)) {
                    // call LoadingNextDataTask to load, if loading the last data show Toast
                    int numOfTblContactRows = (int) DatabaseUtils.queryNumEntries(mDatabase, TABLE_CONTACT);
                    if (numOfTblContactRows == mContacts.size()) {
                        Toast.makeText(MainActivity.this, "No more contact.", Toast.LENGTH_SHORT).show();
                    } else {
                        // "false" param means load next rows
                        new LoadingDataTask().execute(false);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        // event: refreshing page
        mSrlRefreshContacts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // call LoadingFirstDataTask to refresh, "true" param means load first rows
                new LoadingDataTask().execute(true);
            }
        });
    }

    /**
     * Involked when edit button of any row had just clicked
     *
     * @param position The position of edit button
     */
    private void clickBtnEdit(int position) {
        // creating a contactFragment and sending it the contact to show
        ContactFragment contactFragment = new ContactFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ContactFragment.EXTRA_CONTACT, mContacts.get(position));
        bundle.putInt(ContactFragment.EXTRA_POSITION, position);
        contactFragment.setArguments(bundle);

        // adding fragment in the layout
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.rlContactFragment, contactFragment);
        fragmentTransaction.addToBackStack("ContactFragment");
        fragmentTransaction.commit();

        // change title of HeaderBar
        hbListContacts.setTitle("Contact");
    }

    /**
     * Involked when delete button of any row had just clicked
     *
     * @param position The position of delete button
     */
    private void clickBtnDelete(final int position) {
        mDeleteDialog.setMessage("Do you want to <b>delete</b><br><b>" + mContacts.get(position).getName() + "</b>?");
        mDeleteDialog.show();
        mDeleteDialog.setOnClickButtonDeleteDialog(new DeleteDialog.OnClickButtonDeleteDialog() {
            @Override
            public void onOkClick() {
                // delete row from database and update UI
                int delete = mDatabase.delete(TABLE_CONTACT, COL_ID + "=?", new String[]{mContacts.get(position).getId() + ""});
                if (delete == -1) {
                    Toast.makeText(MainActivity.this, "Can't delete " + mContacts.get(position).getName() + ".", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, mContacts.get(position).getName() + " is deleted.", Toast.LENGTH_SHORT).show();
                    mContacts.remove(position);
                    mListViewContactsAdapter.notifyDataSetChanged();
                }
                mDeleteDialog.dismiss();
            }

            @Override
            public void onCancelClick() {
                mDeleteDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // reset headerbar title
        hbListContacts.setTitle("Contacts");
    }

    /**
     * Loading data from database
     * if first param = true, it means load the first rows
     * else first param =false, it means load the next rows
     */
    private class LoadingDataTask extends AsyncTask<Boolean, Void, Void> {
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
        protected Void doInBackground(Boolean... booleans) {
            String query = "";
            if (!booleans[0]) {
                // load next rows
                query = "select * from " + TABLE_CONTACT + " where " + COL_ID + " >= " + mContacts.size() + " and " + COL_ID + " <= " + (mContacts.size() + 30);
            } else {
                // load first rows
                query = "select * from " + TABLE_CONTACT + " where " + COL_ID + " < 30";
                // clear old rows
                mContacts.clear();
            }
            Cursor cursor = mDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Bitmap avatar = DbBitmapUtility.getImage(cursor.getBlob(3));
                // add to arraylist
                mContacts.add(new Contact(id, name, description, avatar));
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
     * Updating a row in database
     *
     * @param position The position or id in Database
     * @param contact  The contact contains information which want to update
     */
    public void updateRow(int position, Contact contact) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, contact.getName());
        values.put(COL_DESCRIPTION, contact.getDecription());

        // update data in database
        int update = mDatabase.update(TABLE_CONTACT, values, COL_ID + "=?", new String[]{contact.getId() + ""});
        if (update == -1) {
            Toast.makeText(this, "Can't update " + contact.getName() + ".", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, contact.getName() + " is updated.", Toast.LENGTH_SHORT).show();
            mContacts.remove(position);
            mContacts.add(position, contact);
            mListViewContactsAdapter.notifyDataSetChanged();
        }
    }
}
