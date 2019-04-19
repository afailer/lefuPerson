package com.lefuyun.api.remote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ImageLoader {

	private static Context sContext;
	private static Picasso sPicasso;

	static {
		sContext = AppContext.sCotext;
		sPicasso = Picasso.with(sContext);
	}

	/**
	 * 
	 * @param uri
	 * @param target
	 */
	public static void loadImg(String uri, ImageView target) {
		loadImgByNormalImg(uri, R.drawable.default_img, R.drawable.default_img,
				target);
	}

	/**
	 * 加载指定宽和高的图片
	 * 
	 * @param uri
	 *            图片的uri地址
	 * @param widthResId
	 *            图片的宽度
	 * @param heightResId
	 *            图片的高度
	 * @param target
	 */
	public static void loadImg(String uri, int width, int height,
			ImageView target) {
		loadImg(uri, width, height, R.drawable.default_img,
				R.drawable.default_img, target);
	}

	/**
	 * 加载图片失败显示指定的图片
	 * 
	 * @param uri
	 *            图片的uri地址
	 * @param beforeImg
	 *            加载前显示的图片
	 * @param errorImg
	 *            加载后显示的图片
	 * @param target
	 *            目标ImageView
	 */
	public static void loadImgByNormalImg(String uri, int beforeImg,
			int errorImg, ImageView target) {
		sPicasso.load(LefuApi.IMG_URL + uri).placeholder(beforeImg)
				.error(errorImg).config(Config.RGB_565).into(target);
	}

	/**
	 * 加载图片
	 * 
	 * @param uri
	 *            图片的uri地址
	 * @param width
	 *            图片的宽度
	 * @param height
	 *            图片的高度
	 * @param beforeImg
	 *            加载前显示的图片
	 * @param errorImg
	 *            加载后显示的图片
	 * @param target
	 *            目标ImageView
	 */
	public static void loadImg(String uri, int width, int height,
			int beforeImg, int errorImg, ImageView target) {
		sPicasso.load(LefuApi.IMG_URL + uri).resize(width, height)
				.placeholder(beforeImg).error(errorImg).config(Config.RGB_565).into(target);
	}
	
	public static void loadImgWithCorners(String uri, ImageView target) {
		sPicasso.load(LefuApi.IMG_URL + uri).transform(new CircleTransform()).into(target);  
	}
	/**
	 * 实现图片的圆角功能,暂时没有实现
	 */
	public static class CircleTransform implements Transformation {
		@Override
		public Bitmap transform(Bitmap source) {
			int size = Math.min(source.getWidth(), source.getHeight());

			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;

			Bitmap squaredBitmap = Bitmap
					.createBitmap(source, x, y, size, size);
			if (squaredBitmap != source) {
				source.recycle();
			}

			Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			BitmapShader shader = new BitmapShader(squaredBitmap,
					BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
			paint.setShader(shader);
			paint.setAntiAlias(true);

			float r = size / 2f;
			canvas.drawCircle(r, r, r, paint);

			squaredBitmap.recycle();
			return bitmap;
		}

		@Override
		public String key() {
			return "circle";
		}
	}

}
