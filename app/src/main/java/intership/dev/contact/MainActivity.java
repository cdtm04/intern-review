package intership.dev.contact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * MainActivity
 */
public class MainActivity extends FragmentActivity implements ListViewContactsAdapter.OnClickListViewContacts {

    private ListView mLvContacts;

    private ListViewContactsAdapter mListViewContactsAdapter;
    private ArrayList<Contact> mContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    /**
     * initialize views from layout
     */
    private void initialize() {
        mLvContacts = (ListView) findViewById(R.id.lvContacts);

        mContacts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mContacts.add(new Contact("Thomas Anders", "Thomas Anders", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar1)));
            mContacts.add(new Contact("Valery Meladze", "Valery Meladze", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar2)));
            mContacts.add(new Contact("Jack Black", "Jack Black", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar3)));
            mContacts.add(new Contact("Kevin James", "Kevin James", BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar4)));
        }

        mListViewContactsAdapter = new ListViewContactsAdapter(this, mContacts);
        mListViewContactsAdapter.setOnClickListViewContacts(this);

        mLvContacts.setAdapter(mListViewContactsAdapter);
    }

    @Override
    public void onClickBtnEdit(int position) {
        //TODO when click the edit button on mLvContacts
    }

    @Override
    public void onClickBtnDelete(int position) {
        //TODO when click the delete button on mLvContacts
    }
}
