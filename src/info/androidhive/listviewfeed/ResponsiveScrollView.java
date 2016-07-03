package info.androidhive.listviewfeed;

import info.androidhive.listviewfeed.adapter.FeedListAdapter;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class ResponsiveScrollView extends HorizontalScrollView {

	private static final String TAG = MainActivity.class.getSimpleName();
	private boolean currentlyTouching = false;
	private boolean currentlyScrolling = false;
	private float SlowDownThreshold = 1f;
	private FeedListAdapter m_adapter = null;

	public void SetAdapter( FeedListAdapter adapter ){
		m_adapter = adapter;
	}

	public interface OnEndScrollListener {
        public void onEndScroll();
    }

    private OnEndScrollListener mOnEndScrollListener;

    public ResponsiveScrollView(Context context) {
        this(context, null, 0);
    }

    public ResponsiveScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResponsiveScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
	public boolean onTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// MapView에서 터치를 발생할 때, 부모뷰(ScrollView)가 TouchEvent를 가로채는 것을 막음
			this.getParent().requestDisallowInterceptTouchEvent(true);
			currentlyTouching = true;
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			// MapView에서 터치를 뗄때, 부모뷰(ScrollView)가 TouchEvent를 가로채는 것을 허용함
			this.getParent().requestDisallowInterceptTouchEvent(false);
			currentlyTouching = false;


				new Handler().postDelayed(new Runnable() {

				    @Override
				    public void run() {
				    	if( currentlyTouching == false ){
				    		Log.i(TAG, "m_adapter.TouchReleased() ");
				    		m_adapter.TouchReleased();
				    	}

				    }

				}, 1000);


			break;
		}

		Log.d("CUSTOM_MAP", "currentlyTouching "+currentlyTouching);

		// 기존 MapView의 TouchEvent처리를 그대로 수행함
		super.onTouchEvent(ev);
		return true;
	}

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {

        if (Math.abs(x - oldX) > SlowDownThreshold) {
            currentlyScrolling = true;

            Log.i(TAG, "(Math.abs(x - oldX) "+ Math.abs(x - oldX) );
            Log.i(TAG, "currentlyScrolling "+currentlyScrolling);
        } else {
            currentlyScrolling = false;

            Log.i(TAG, "currentlyScrolling "+currentlyScrolling);

            if (!currentlyTouching) {
                //scrolling stopped...handle here

            	Log.i(TAG, "scrolling stopped...");
            }
        }
        super.onScrollChanged(x, y, oldX, oldY);

    }

    public OnEndScrollListener getOnEndScrollListener() {
        return mOnEndScrollListener;
    }

    public void setOnEndScrollListener(OnEndScrollListener mOnEndScrollListener) {
        this.mOnEndScrollListener = mOnEndScrollListener;
    }

}
