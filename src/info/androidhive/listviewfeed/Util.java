package info.androidhive.listviewfeed;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;


public class Util {
	public static int getPixelToDp(Context context, int pixel) {
		float dp = 0;
		try {
			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			dp = pixel / (metrics.densityDpi / 160f);
		} catch (Exception e) {

		}
		return (int) dp;
	}

	public static int getDpToPixel(Context context, int DP) {
		float px = 0;
		try {
			px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources().getDisplayMetrics());
		} catch (Exception e) {

		}
		return (int) px;
	}

	public static int getDpToPixel(Context context, float DP) {
		float px = 0;
		try {
			px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources().getDisplayMetrics());
		} catch (Exception e) {

		}
		return (int) px;
	}
}
