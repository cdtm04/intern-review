package intership.dev.contact;

import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * MainActivity
 */
public class MainActivity extends FragmentActivity implements ListViewContactsAdapter.OnClickListViewContacts {

    private HeaderBar hbListContacts;
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
        hbListContacts = (HeaderBar) findViewById(R.id.hbListContacts);
        mLvContacts = (ListView) findViewById(R.id.lvContacts);

        mContacts = new ArrayList<>();
        // example data
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
        // adding contact fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.rlContactFragment, new ContactFragment(mContacts.get(position)));
        fragmentTransaction.addToBackStack("ContactFragment");
        fragmentTransaction.commit();

        // change title of HeaderBar
        hbListContacts.setTitle("Contact");
    }

    @Override
    public void onClickBtnDelete(int position) {
        //TODO when click the delete button on mLvContacts
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // reset headerbar title
        hbListContacts.setTitle("Contacts");
    }
}
