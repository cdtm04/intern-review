package intership.dev.contact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ContactFragment to show information of a contact
 */
public class ContactFragment extends Fragment {

    private CircleImageView mIvAvatar;
    private TextView mTvUserNameOfContact;
    private EditText mEtName, mEtDescription;
    private Button mBtnSave, mBtnCancel;

    private Contact mContact;

    public ContactFragment(Contact mContact) {
        this.mContact = mContact;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        initialize(rootView);
        return rootView;
    }

    /**
     * Getting views from layout and setting values on them
     *
     * @param rootView RootView inflater from layout
     */
    private void initialize(View rootView) {
        mIvAvatar = (CircleImageView) rootView.findViewById(R.id.ivAvatar);
        mTvUserNameOfContact = (TextView) rootView.findViewById(R.id.tvUserNameOfContact);
        mEtName = (EditText) rootView.findViewById(R.id.etName);
        mEtDescription = (EditText) rootView.findViewById(R.id.etDescription);
        mBtnSave = (Button) rootView.findViewById(R.id.btnSave);
        mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);

        mIvAvatar.setImageBitmap(mContact.getAvatar());
        mTvUserNameOfContact.setText(mContact.getName());
        mEtName.setText(mContact.getName());
        mEtDescription.setText(mContact.getDecription());
    }
}
