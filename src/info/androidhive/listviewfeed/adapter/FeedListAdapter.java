package info.androidhive.listviewfeed.adapter;

import info.androidhive.listviewfeed.FeedImageView;
import info.androidhive.listviewfeed.MainActivity;
import info.androidhive.listviewfeed.R;
import info.androidhive.listviewfeed.ResponsiveScrollView;
import info.androidhive.listviewfeed.Util;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class FeedListAdapter extends BaseAdapter
{
	private static final String TAG = MainActivity.class.getSimpleName();

	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	private View mConvertView;
	private ResponsiveScrollView mScrollView;

	public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void TouchReleased() {

		Log.i(TAG, "TouchReleased ");

		final ResponsiveScrollView sv = (ResponsiveScrollView)mConvertView.findViewById(R.id.horizontalScrollview);

		new Handler().post(new Runnable() {

		    @Override
		    public void run() {
		    	sv.scrollTo(150, 150);
		    }

		});


	}

//	OnTouchListener itemTouch = new OnTouchListener() {
//	    private int position;
//	    @Override
//	    public boolean onTouch(View v, MotionEvent event) {
//
//	    	LinearLayout ll = (LinearLayout)v.findViewById(R.layout.push_item);
//	        String itemTag = ll.getTag().toString();
//	        int itemPosition = Integer.parseInt(itemTag);
//
//	        Log.i(TAG, "itemPositon : "+ itemPosition);
//
//	    	return true;
//	    }
//	 };

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.i(TAG, "position : "+position );


		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.push_item, null);

		mConvertView = convertView;

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

	//	convertView.setOnTouchListener(itemTouch);

		LinearLayout layoutTitleRecipt = (LinearLayout)convertView.findViewById(R.id.layoutTitleRecipt);
		LinearLayout layoutTitleEvent = (LinearLayout)convertView.findViewById(R.id.layoutTitleEvent);

		mScrollView = (ResponsiveScrollView)convertView.findViewById(R.id.horizontalScrollview);
		mScrollView.setSmoothScrollingEnabled(true);
		mScrollView.SetAdapter(this);

		ImageView imageTopIcon = (ImageView) convertView.findViewById(R.id.imageViewTopIcon);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestampRecipt = (TextView) convertView
				.findViewById(R.id.timestampRecipt);
		TextView timestampEvent = (TextView) convertView
				.findViewById(R.id.timestampEvent );
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);
		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		//NetworkImageView profilePic = (NetworkImageView) convertView
		//		.findViewById(R.id.profilePic);
//		FeedImageView feedImageView = (FeedImageView) convertView
//				.findViewById(R.id.feedImage1);
//		FeedImageView feedImageView2 = (FeedImageView) convertView
//				.findViewById(R.id.feedImage2);
		LinearLayout layoutShare = (LinearLayout)convertView.findViewById(R.id.layoutShare);
		LinearLayout layoutBottom = (LinearLayout)convertView.findViewById(R.id.layoutBottom);

		TextView textBottomButton = (TextView) convertView.findViewById(R.id.textBottomButton);

		FeedItem item = feedItems.get(position);


		LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.a);


		linearLayout.removeAllViews();
        for (int i = 0; i < 3; i++) {
	        ImageView imageView = new ImageView(convertView.getContext());
	        imageView.setImageResource(R.drawable.list_img02);

	        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

	        if(i == 0)
	        	lp.setMargins(Util.getDpToPixel(convertView.getContext(), 25), 0,
	        				Util.getDpToPixel(convertView.getContext(), 10), 0);
	        else if(i == 2)
	        	lp.setMargins(Util.getDpToPixel(convertView.getContext(), 10), 0,
        				Util.getDpToPixel(convertView.getContext(), 25), 0);
	        else
	        	lp.setMargins(Util.getDpToPixel(convertView.getContext(), 10), 0,
        				Util.getDpToPixel(convertView.getContext(), 10), 0);

	        imageView.setLayoutParams(lp);


	        //imageView.setLayoutParams(new LayoutParams(600,600));
	        imageView.setScaleType(ImageView.ScaleType.CENTER);
	        linearLayout.addView(imageView);
        }


		boolean bRecipt = false;

		if(bRecipt == true)
		{

			layoutTitleRecipt.setVisibility(View.VISIBLE);
			layoutTitleEvent.setVisibility(View.GONE);

			imageTopIcon.setImageResource(R.drawable.list_icon01);

			// Converting timestamp into x ago format
			CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
					Long.parseLong(item.getTimeStamp()),
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
			//timestamp.setText(timeAgo);
			timestampRecipt.setText("2015.07.18 11:34:32");

			statusMsg.setText("KB국민체크(7****-4****)\n김*지님\n07/17 08:27\n3,400원\n한솥도시락당산동4가 스타샵 이용");

			url.setVisibility(View.GONE);

//			feedImageView.setVisibility(View.GONE);

			layoutShare.setVisibility(View.GONE);

			layoutBottom.setVisibility(View.VISIBLE);

			textBottomButton.setText("1588-1788");
		}
		else
		{
			layoutTitleRecipt.setVisibility(View.GONE);
			layoutTitleEvent.setVisibility(View.VISIBLE);

			imageTopIcon.setImageResource(R.drawable.list_icon02);

			//name.setText(item.getName());
			name.setText("KB국민앱카드 Kmotion 이벤트");

			// Converting timestamp into x ago format
			CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
					Long.parseLong(item.getTimeStamp()),
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
			//timestamp.setText(timeAgo);
			timestampEvent.setText("2015.07.18 11:34:32");

			// Chcek for empty status message
			if (!TextUtils.isEmpty(item.getStatus())) {
				//statusMsg.setText(item.getStatus());
				statusMsg.setText("KB국민카드\nKB국민카드 App은 스마트폰/테블릿PC에서 카드이용조회, 금융서비스(장기카드대출(카드론), 단기카드대출(현금서비스))\n 포인트/할인, 카드안내 신청등 KB국민카드 웹페이지에서 운영되는 서비스를 이용하 실 수 있습니다.");
				statusMsg.setVisibility(View.VISIBLE);
			} else {
				//status is empty, remove from view
				statusMsg.setVisibility(View.GONE);
			}


			// Checking for null feed url
			if (item.getUrl() != null) {
				url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
						+ item.getUrl() + "</a> "));

				// Making url clickable
				url.setMovementMethod(LinkMovementMethod.getInstance());
				url.setVisibility(View.VISIBLE);
			} else {
				// url is null, remove from the view
				url.setVisibility(View.GONE);
			}

			// user profile pic
			//profilePic.setImageUrl(item.getProfilePic(), imageLoader);

			// Feed image
//			if (item.getImge() != null) {
//				feedImageView.setImageUrl(item.getImge(), imageLoader);
//				feedImageView2.setImageUrl(item.getImge(), imageLoader);
//				feedImageView.setVisibility(View.VISIBLE);
//				feedImageView
//						.setResponseObserver(new FeedImageView.ResponseObserver() {
//							@Override
//							public void onError() {
//							}
//
//							@Override
//							public void onSuccess() {
//							}
//						});
//			} else {
//				feedImageView.setVisibility(View.GONE);
//			}

		}

		return convertView;
	}

}
