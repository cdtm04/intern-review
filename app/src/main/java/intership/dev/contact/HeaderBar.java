package intership.dev.contact;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Class custom header bar for this application
 */
public class HeaderBar extends RelativeLayout implements View.OnClickListener {

    /**
     * Interface definition for a callback to be invoked when a view of Actionbar is clicked.
     */
    public interface OnHeaderBarListener {
        /**
         * Called when the back button has been clicked
         *
         * @param v The view which was clicked
         */
        void onButtonClick(View v);
    }

    private OnHeaderBarListener mOnHeaderBarListener;

    private Context mContext;
    private View mRootView;

    private ImageButton mBtnBack;
    private TextView mTvTitle;

    public HeaderBar(Context context) {
        super(context);
        initialize(context);
    }

    public HeaderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        setAttributeSet(context, attrs);
    }

    public HeaderBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
        setAttributeSet(context, attrs);
    }

    /**
     * Inflater the view from xml and set listener for subitems.
     *
     * @param context The context that the view is running in.
     */
    private void initialize(Context context) {
        mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.custom_headerbar, this, false);
        addView(mRootView);

        mTvTitle = (TextView) mRootView.findViewById(R.id.tvTitle);
        mBtnBack = (ImageButton) mRootView.findViewById(R.id.btnBack);

        mBtnBack.setOnClickListener(this);

        // set default view
        mBtnBack.setVisibility(GONE);
        mTvTitle.setVisibility(VISIBLE);
    }

    private void setAttributeSet(Context context, AttributeSet attrs) {
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.HeaderBar);

        // get attributes
        String title = type.getString(R.styleable.HeaderBar_header_title);
        int back_icon = type.getResourceId(R.styleable.HeaderBar_back_icon, 0);

        // set values on subviews
        if (title != null) {
            mTvTitle.setText(title);
            mTvTitle.setVisibility(VISIBLE);
        }

        if (back_icon == 0) {
            mBtnBack.setVisibility(GONE);
        } else {
            mBtnBack.setImageResource(back_icon);
            mBtnBack.setVisibility(VISIBLE);
        }

        type.recycle();
    }

    /**
     * Register a callback to be invoked when this view is clicked.
     *
     * @param onHeaderBarListener The callback that will run.
     */
    public void setOnHeaderBarListener(OnHeaderBarListener onHeaderBarListener) {
        mOnHeaderBarListener = onHeaderBarListener;
    }

    /**
     * Setting title for HeaderBar
     *
     * @param title The title which want to show
     */
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    /**
     * Setting visibility for the back button
     *
     * @param visibility
     */
    public void setVisibilityBtnBack(int visibility) {
        mBtnBack.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            if (mOnHeaderBarListener != null) {
                // call the method onButtonClick to do
                mOnHeaderBarListener.onButtonClick(v);
            } else {
                // if event weren't setted, set it's like onBackPressed()
                ((Activity) mContext).onBackPressed();
            }
        }
    }
}
