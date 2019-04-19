/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lefuyun.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.lefuyun.R;
import com.lefuyun.zxing.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public final class ViewfinderView extends View {

	/**
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;
	/**
	 * 四个绿色边角对应的长度
	 */
	private int ScreenRate;
	/**
	 * 四个绿色边角对应的宽度
	 */
	private static final int CORNER_WIDTH = 6;
	/**
	 * 中间那条线每次刷新移动的距离
	 */
	private static final int SPEEN_DISTANCE = 5;
	/**
	 * 手机的屏幕密度
	 */
	private static float density;
	/**
	 * 字体大小
	 */
	private final int mTextSize;
	/**
	 * 字体距离扫描框下面的距离
	 */
	private static final int TEXT_PADDING_TOP = 30;
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;
	// 扫描以外的背景色
	private final int maskColor;
	// 四个角尖角的颜色
	private final int mAngelColor;
	// 中间扫描的横线
	private Bitmap mLineBitmap;
	// 中间扫描横线的范围
	private Rect lineRect;
	// 屏幕的宽高
	private int mScreenWidth;
	private int mScreenHeight;
	// 扫描框的范围
	private Rect frame;
	// 记录扫描是否首次进入
	boolean isFirst;
	/**
	 * 中间滑动线距最顶端的位置
	 */
	private int slideTop;
	// 提示性文字
	private String topNote;
	private String bottomNote;

	// 下面的属性暂时不知用途
	private Bitmap resultBitmap;
	private final int resultColor;
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		density = context.getResources().getDisplayMetrics().density;
		// 将像素转换成dp
		ScreenRate = (int) (15 * density);
		isFirst = true;
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		mAngelColor = resources.getColor(R.color.main_background);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		mTextSize = (int) resources
				.getDimensionPixelSize(R.dimen.camera_text_size);
		topNote = resources.getString(R.string.scan_top_text);
		bottomNote = resources.getString(R.string.scan_bottom_text);
		mLineBitmap = ((BitmapDrawable) (getResources()
				.getDrawable(R.drawable.qrcode_scan_line))).getBitmap();
		possibleResultPoints = new HashSet<ResultPoint>(5);
		lineRect = new Rect();
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (frame == null) {
			// 中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
			frame = CameraManager.get().getFramingRect();
			if (frame == null) {
				return;
			}
		}
		// 初始化中间线滑动的最上边和最下边
		if (isFirst) {
			isFirst = false;
			slideTop = frame.top;
		}

		// 获取屏幕的宽和高
		mScreenWidth = canvas.getWidth();
		mScreenHeight = canvas.getHeight();
		paint.setColor(resultBitmap != null ? resultColor : maskColor);

		// 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		// 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		canvas.drawRect(0, 0, mScreenWidth, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, mScreenWidth,
				frame.bottom + 1, paint);
		canvas.drawRect(0, frame.bottom + 1, mScreenWidth, mScreenHeight, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			// 画扫描框边上的角，总共8个部分
			paint.setColor(mAngelColor);
			// 画左上角的尖角
			canvas.drawRect(frame.left - CORNER_WIDTH,
					frame.top - CORNER_WIDTH, frame.left + ScreenRate,
					frame.top, paint);
			canvas.drawRect(frame.left - CORNER_WIDTH, frame.top, frame.left,
					frame.top + ScreenRate, paint);
			// 右上角
			canvas.drawRect(frame.right - ScreenRate, frame.top - CORNER_WIDTH,
					frame.right + CORNER_WIDTH, frame.top, paint);
			canvas.drawRect(frame.right, frame.top, frame.right + CORNER_WIDTH,
					frame.top + ScreenRate, paint);
			// 左下角
			canvas.drawRect(frame.left - CORNER_WIDTH, frame.bottom, frame.left
					+ ScreenRate, frame.bottom + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left - CORNER_WIDTH, frame.bottom
					- ScreenRate, frame.left, frame.bottom, paint);
			// 右下角
			canvas.drawRect(frame.right - ScreenRate, frame.bottom, frame.right
					+ CORNER_WIDTH, frame.bottom + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right, frame.bottom - ScreenRate, frame.right
					+ CORNER_WIDTH, frame.bottom, paint);

			// 绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE

			slideTop += SPEEN_DISTANCE;
			if (slideTop >= frame.bottom) {
				slideTop = frame.top;
			}
			lineRect.set(frame.left, slideTop, frame.right, slideTop + 9);
			canvas.drawBitmap(mLineBitmap, null, lineRect, paint);

			// 画扫描框下面的字
			paint.setColor(Color.WHITE);
			paint.setTextSize(mTextSize);
			paint.setAlpha(0x40);
			float textWidth = paint.measureText(topNote);
			canvas.drawText(
					topNote,
					(mScreenWidth - textWidth) / 2,
					(float) (frame.bottom + (float) TEXT_PADDING_TOP * density),
					paint);
			paint.setColor(mAngelColor);
			textWidth = paint.measureText(bottomNote);
			canvas.drawText(bottomNote, (mScreenWidth - textWidth) / 2,
					(float) (frame.bottom + (float) TEXT_PADDING_TOP * density
							* 2), paint);

			if (possibleResultPoints.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints.clear();
				lastPossibleResultPoints = possibleResultPoints;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : possibleResultPoints) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, paint);
				}
			}
			if (lastPossibleResultPoints != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : lastPossibleResultPoints) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}

			// 只刷新扫描框的内容，其他地方不刷新
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
