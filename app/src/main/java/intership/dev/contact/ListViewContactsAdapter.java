package intership.dev.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter manage the main listview
 */
public class ListViewContactsAdapter extends BaseAdapter {

    /**
     * Interface definition for a callback to be invoked when a view of ListView is clicked.
     */
    public interface OnClickListViewContacts {

        void onClickBtnEdit(int position);

        void onClickBtnDelete(int position);
    }

    Context mContext;
    ArrayList<Contact> mContacts;
    OnClickListViewContacts mOnClickListViewContacts;

    public ListViewContactsAdapter(Context context, ArrayList<Contact> contacts) {
        this.mContext = context;
        this.mContacts = contacts;
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int i) {
        return mContacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        private CircleImageView mImgAvatar;
        private TextView mTvUserNameOfContact;
        private ImageButton mBtnEditContact, mBtnDeleteContact;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_contacts, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.mImgAvatar = (CircleImageView) view.findViewById(R.id.imgAvatar);
            viewHolder.mTvUserNameOfContact = (TextView) view.findViewById(R.id.tvUserNameOfContact);
            viewHolder.mBtnEditContact = (ImageButton) view.findViewById(R.id.btnEditContact);
            viewHolder.mBtnDeleteContact = (ImageButton) view.findViewById(R.id.btnDeleteContact);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        setValueOnView(viewHolder, mContacts.get(i), i);

        return view;
    }

    /**
     * Setting values and registering listener for view
     *
     * @param viewHolder the viewholder contains items of view
     * @param contact    The object contains information will be setted on view
     */
    private void setValueOnView(ViewHolder viewHolder, Contact contact, final int position) {
        viewHolder.mImgAvatar.setImageBitmap(contact.getAvatar());
        viewHolder.mTvUserNameOfContact.setText(contact.getName());

        // registering listener
        viewHolder.mBtnDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListViewContacts != null) {
                    mOnClickListViewContacts.onClickBtnDelete(position);
                }
            }
        });
        viewHolder.mBtnEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListViewContacts != null) {
                    mOnClickListViewContacts.onClickBtnEdit(position);
                }
            }
        });
    }

    /**
     * Setting OnClickListViewContacts for ListView
     *
     * @param onClickListViewContacts OnClickListViewContacts
     */
    public void setOnClickListViewContacts(OnClickListViewContacts onClickListViewContacts) {
        mOnClickListViewContacts = onClickListViewContacts;
    }
}
