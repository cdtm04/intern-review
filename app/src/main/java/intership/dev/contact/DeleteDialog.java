package intership.dev.contact;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by merit on 21/07/2015.
 */
public class DeleteDialog extends Dialog {
    /**
     * Interface definition for a callback to be invoked when buttons of dialogs are clicked.
     */
    public interface OnClickButtonDeleteDialog {

        void onSaveClick();

        void onCancelClick();
    }

    private OnClickButtonDeleteDialog mOnClickButtonDeleteDialog;

    private Context mContext;

    private TextView mTvMessage;
    private Button mBtnSave, mBtnCancel;

    public DeleteDialog(Context context) {
        super(context);
        // remove the default title of dialog
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // remove the default background of dialog
        getWindow().setBackgroundDrawableResource(R.color.transparent);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_delete);
        initialize();
    }

    private void initialize() {
        mTvMessage = (TextView) findViewById(R.id.tvMessage);
        mBtnSave = (Button) findViewById(R.id.btnSave);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        mBtnSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.AXIS_PRESSURE) {
                    mBtnSave.setTextColor(mContext.getResources().getColor(R.color.theme_button_text));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mBtnSave.setTextColor(mContext.getResources().getColor(R.color.dialog_button_text));
                    if (mOnClickButtonDeleteDialog != null) {
                        mOnClickButtonDeleteDialog.onSaveClick();
                    }
                }
                return false;
            }
        });

        mBtnCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.AXIS_PRESSURE) {
                    mBtnCancel.setTextColor(mContext.getResources().getColor(R.color.theme_button_text));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mBtnCancel.setTextColor(mContext.getResources().getColor(R.color.dialog_button_text));
                    if (mOnClickButtonDeleteDialog != null) {
                        mOnClickButtonDeleteDialog.onCancelClick();
                    } else {
                        cancel();
                    }
                }
                return false;
            }
        });
    }

    /**
     * Setting message on the dialog
     *
     * @param message The message
     */
    public void setMessage(String message) {
        mTvMessage.setText(message);
    }

    /**
     * Registering listener for buttons
     *
     * @param mOnClickButtonDeleteDialog The listener
     */
    public void setmOnClickButtonDeleteDialog(OnClickButtonDeleteDialog mOnClickButtonDeleteDialog) {
        this.mOnClickButtonDeleteDialog = mOnClickButtonDeleteDialog;
    }
}
