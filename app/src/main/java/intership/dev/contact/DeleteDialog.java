package intership.dev.contact;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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

        void onOkClick();

        void onCancelClick();
    }

    private OnClickButtonDeleteDialog mOnClickButtonDeleteDialog;

    private Context mContext;
    private String mMessage = "Your Message Here.";

    private TextView mTvMessage;
    private Button mBtnOk, mBtnCancel;

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
        mTvMessage.setText(Html.fromHtml(mMessage));
        mBtnOk = (Button) findViewById(R.id.btnOk);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        mBtnOk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.AXIS_PRESSURE) {
                    mBtnOk.setTextColor(mContext.getResources().getColor(R.color.theme_button_text));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mBtnOk.setTextColor(mContext.getResources().getColor(R.color.dialog_button_text));
                    if (mOnClickButtonDeleteDialog != null) {
                        mOnClickButtonDeleteDialog.onOkClick();
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
        mMessage = message;
    }

    /**
     * Registering listener for buttons
     *
     * @param mOnClickButtonDeleteDialog The listener
     */
    public void setOnClickButtonDeleteDialog(OnClickButtonDeleteDialog mOnClickButtonDeleteDialog) {
        this.mOnClickButtonDeleteDialog = mOnClickButtonDeleteDialog;
    }
}
