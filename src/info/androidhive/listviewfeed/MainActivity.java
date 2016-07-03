package info.androidhive.listviewfeed;

import info.androidhive.listviewfeed.adapter.FeedListAdapter;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.widget.SwipeRefreshLayout;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import ib.push.helper.*;

public class MainActivity extends Activity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
	private SwipeRefreshLayout refreshLayout;
	private DBHelper dbHelper;

	private static final String GCM_SENDER_ID = "566259379995";  //ipstest

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String AccountID = "AccountID12345";
        String VerifyCode = "VerifyCode12345";
        String mUserName = "hong";
        String mPhoneNumber = "01037462265";
        PushClientHelper.TagConfig mTagInfo = new PushClientHelper.TagConfig(mUserName, mPhoneNumber,  null,  null,  null, null);
        PushClientHelper.startGCMService(getBaseContext(), AccountID, VerifyCode, mTagInfo, GCM_SENDER_ID);

        dbHelper = new DBHelper(this);

		// 샘플데이타 넣기
		Log.i(TAG, "Inserting ..");
		dbHelper.addPayload(new Payload("Ravi", "9100000000"));
		dbHelper.addPayload(new Payload("Srinivas", "9199999999"));
		dbHelper.addPayload(new Payload("Tommy", "9522222222"));
		dbHelper.addPayload(new Payload("Karthik", "9533333333"));

		// 집어넣은 데이타 다시 읽어들이기
		Log.i(TAG, "Reading all contacts..");
		List<Payload> payloads = dbHelper.getAllPayloads();

		for (Payload cn : payloads) {
			String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
			Log.i(TAG, "Name: "+ log);
		}



//        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swype);
//        refreshLayout.setOnRefreshListener(this);
//        refreshLayout.setColorScheme( R.color.push_blue, R.color.push_blue, R.color.push_blue, R.color.push_blue);

        TextView textViewSelect = (TextView)findViewById(R.id.textViewSelect);
        textViewSelect.setOnClickListener(this);
        ImageView imageViewSelect = (ImageView)findViewById(R.id.imageViewSelect);
        imageViewSelect.setOnClickListener(this);
        TextView textViewDelete = (TextView)findViewById(R.id.textViewDelete);
        textViewDelete.setOnClickListener(this);
        ImageView imageViewDelete = (ImageView)findViewById(R.id.imageViewDelete);
        imageViewDelete.setOnClickListener(this);


		listView = (ListView) findViewById(R.id.list);
		listView.getParent().requestDisallowInterceptTouchEvent(true);

//		listView.setOnTouchListener(new View.OnTouchListener() {
//		    // Setting on Touch Listener for handling the touch inside ScrollView
//		    @Override
//		    public boolean onTouch(View v, MotionEvent event) {
//			    // Disallow the touch request for parent scroll on touch of child view
//			    v.getParent().requestDisallowInterceptTouchEvent(true);
//			    return false;
//		    }
//		});


		feedItems = new ArrayList<FeedItem>();

		listAdapter = new FeedListAdapter(this, feedItems);
		listView.setAdapter(listAdapter);

		// These two lines not needed,
		// just to get the look of facebook (changing background color & hiding the icon)
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
		getActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		// We first check for cached request
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(URL_FEED);
		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			// making fresh volley request and getting json
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					URL_FEED, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
						}
					});

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);
		}

	}

	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	private void parseJsonFeed(JSONObject response) {
		try {
			JSONArray feedArray = response.getJSONArray("feed");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				FeedItem item = new FeedItem();
				item.setId(feedObj.getInt("id"));
				item.setName(feedObj.getString("name"));

				// Image might be null sometimes
				String image = feedObj.isNull("image") ? null : feedObj
						.getString("image");
				item.setImge(image);
				item.setStatus(feedObj.getString("status"));
				item.setProfilePic(feedObj.getString("profilePic"));
				item.setTimeStamp(feedObj.getString("timeStamp"));

				// url might be null sometimes
				String feedUrl = feedObj.isNull("url") ? null : feedObj
						.getString("url");
				item.setUrl(feedUrl);

				feedItems.add(item);
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		Log.i(TAG, "onClick : " + v.getResources().getResourceEntryName(v.getId()));

		switch (v.getId()) {
//		case R.id.apple:
//			textFruit.setText("Apple");
//			break;
//		case R.id.orange:
//			textFruit.setText("Orange");
//			break;
		}
	}

	@Override
	public void onRefresh() {

		//simulate doing something
		new Handler().postDelayed(new Runnable() {

		    @Override
		    public void run() {
		    	refreshLayout.setRefreshing(false);
		    }

		}, 2000);


	}
}
