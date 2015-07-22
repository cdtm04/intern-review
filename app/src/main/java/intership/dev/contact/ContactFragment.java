package intership.dev.contact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ContactFragment to show information of a contact
 */
public class ContactFragment extends Fragment implements View.OnTouchListener {
    public final static String EXTRA_CONTACT = "mContact";

    private CircleImageView mIvAvatar;
    private TextView mTvUserNameOfContact;
    private EditText mEtName, mEtDescription;
    private Button mBtnSave, mBtnCancel;

    private Contact mContact;
    private DeleteDialog mDeleteDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        // get contact from activity
        mContact = (Contact) getArguments().getSerializable(ContactFragment.EXTRA_CONTACT);
        initialize(rootView);
        return rootView;
    }

    /**
     * Getting views from layout and setting values on them
     *
     * @param rootView RootView inflater from layout
     */
    private void initialize(View rootView) {
        // get
        mIvAvatar = (CircleImageView) rootView.findViewById(R.id.ivAvatar);
        mTvUserNameOfContact = (TextView) rootView.findViewById(R.id.tvUserNameOfContact);
        mEtName = (EditText) rootView.findViewById(R.id.etName);
        mEtDescription = (EditText) rootView.findViewById(R.id.etDescription);
        mBtnSave = (Button) rootView.findViewById(R.id.btnSave);
        mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mDeleteDialog = new DeleteDialog(getActivity());

        // set
        mIvAvatar.setImageBitmap(mContact.getAvatar());
        mTvUserNameOfContact.setText(mContact.getName());
        mEtName.setText(mContact.getName());
        mEtDescription.setText(mContact.getDecription());
        mBtnSave.setOnTouchListener(this);
        mBtnCancel.setOnTouchListener(this);
    }


    private void clickOkButton() {
        //TODO when click OK button
    }

    // just do some UI
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == mBtnCancel) {
            if (motionEvent.getAction() == MotionEvent.AXIS_PRESSURE) {
                // if press cancel button, its textcolor will be changed
                mBtnCancel.setTextColor(getResources().getColor(R.color.theme_button_pressed_text));
            } else if ((motionEvent.getAction() == MotionEvent.ACTION_UP)) {
                mBtnCancel.setTextColor(getResources().getColor(R.color.theme_button_text));
                // call the onBackPressed() of FragmentActivity
                getActivity().onBackPressed();
            }
        } else if (view == mBtnSave) {
            if (motionEvent.getAction() == MotionEvent.AXIS_PRESSURE) {
                // if press cancel button, its textcolor will be changed
                mBtnSave.setTextColor(getResources().getColor(R.color.theme_button_pressed_text));
            } else if ((motionEvent.getAction() == MotionEvent.ACTION_UP)) {
                // change color
                mBtnSave.setTextColor(getResources().getColor(R.color.theme_button_text));

                // create dialog and show it
                mDeleteDialog.setMessage("Do you want to <b>save</b><br><b>" + mContact.getName() + "</b>?");
                mDeleteDialog.setmOnClickButtonDeleteDialog(new DeleteDialog.OnClickButtonDeleteDialog() {
                    @Override
                    public void onOkClick() {
                        clickOkButton();
                    }

                    @Override
                    public void onCancelClick() {
                        mDeleteDialog.cancel();
                    }
                });
                mDeleteDialog.show();
            }
        }
        return false;
    }
}
