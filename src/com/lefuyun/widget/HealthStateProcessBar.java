package com.lefuyun.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lefuyun.R;

public class HealthStateProcessBar extends View {

	private Context mContext;
	// 画图片的画笔
	private Paint mPicturePaint;
	// 用于画圆弧的画笔
	private Paint mCircularArcPaint;
	// 用于写字的画笔
	private Paint mTextPaint;
	// 此控件分配的宽度
	private float mWidth;
	// 此控件分配的高度
	private float mHeight;
	// 圆心点的X轴坐标
	private float mCircleX;
	// 圆心点的Y轴坐标
	private float mCircleY;
	// 图片矩阵的最大值
	private float mMaxSize;
	// 圆弧的宽度
	private float mCircularArcWidth = 2;
	// 圆弧的半径
	private float mCircularArcRadius;
	// 外部圆环所在的范围
	private RectF mCircularArcRectF;
	// 画外部图片的矩阵
	private RectF mPictureRectF;
	// 最大进度
	private int mMax;
	// 当前进度
	private int mCurrentValue;
	// 用于展示动画的中间值
	private int mProgress;
	// 老人身体状态评估
	private String mHealthStateText;
	private float mHealthStateTextSize;
	// 标题属性
	private String mTitle;
	private float mTitleSize;

	// 文字的宽度
	private float mTextWidth;
	//
	private Bitmap bmp;
	// 监视动画是否完成
	private boolean isStart = true;

	private SweepGradient sweepGradient;
	
	private float startX;
	private float startY;
	private OnTargetClickListener mOnClickListener;
	private boolean mTarget;

	public HealthStateProcessBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public HealthStateProcessBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HealthStateProcessBar(Context context) {
		this(context, null);
	}
	/**
	 * 点击事件接口
	 */
	public interface OnTargetClickListener {
		/**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v);
	}
	/**
	 * 设置点击事件
	 * @param listener
	 */
	public void setOnClickListener(OnTargetClickListener listener) {
		mOnClickListener = listener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画外部图片
		drawBitmap(canvas);

		// 画文字
		drawText(canvas);

		// 画圆环
		drawCircularArc(canvas);
	}

	/**
	 * 画外部图片
	 * 
	 * @param canvas
	 */
	private void drawBitmap(Canvas canvas) {
		canvas.drawBitmap(bmp, null, mPictureRectF, mPicturePaint);
	}

	/**
	 * 画圆环
	 * 
	 * @param canvas
	 */
	private void drawCircularArc(Canvas canvas) {
		mCircularArcPaint.setShader(sweepGradient);
		canvas.drawArc(mCircularArcRectF, 150, 240 * (mProgress * 1.0f / mMax),
				false, mCircularArcPaint);
	}


	/**
	 * 画文字
	 * 
	 * @param canvas
	 */
	private void drawText(Canvas canvas) {

		// 画老人状态文字
		// 设置颜色的字体为白色
		mTextPaint.setColor(Color.WHITE);
		// 设置字体的大小
		mTextPaint.setTextSize(mHealthStateTextSize);
		// 获取文字的宽度
		mTextWidth = mTextPaint.measureText(mHealthStateText);
		// 画文字
		canvas.drawText(mHealthStateText, mCircleX - mTextWidth / 2,
				mCircleY, mTextPaint);
		
		// 画标题文字
		// 设置颜色的字体为白色
		mTextPaint.setColor(Color.parseColor("#ffdec3"));
		// 设置字体的大小
		mTextPaint.setTextSize(mTitleSize);
		// 获取文字的宽度
		mTextWidth = mTextPaint.measureText(mTitle);
		// 画文字
		canvas.drawText(mTitle, mCircleX - mTextWidth / 2, mCircleY - mCircularArcRadius/2, mTextPaint);
	}

	private void init() {
		// 创建一个画外部图片的画笔
		mPicturePaint = new Paint();
		mPicturePaint.setAntiAlias(true);
		// 设置画笔的颜色
		mPicturePaint.setColor(Color.GRAY);
		mPicturePaint.setStrokeWidth(0);
		// 设置为填充
		mPicturePaint.setStyle(Style.FILL);

		// 创建一个画圆弧的画笔
		mCircularArcPaint = new Paint();
		mCircularArcPaint.setAntiAlias(true);
		// 设置画笔的颜色
		mCircularArcPaint.setStrokeWidth(0);
		// 设置为填充
		mCircularArcPaint.setStyle(Style.FILL);
		// 设置空心线的宽度为指定的宽度
		mCircularArcWidth = mContext.getResources().getDimension(
				R.dimen.circular_arc_width);
		mCircularArcPaint.setStrokeWidth(mCircularArcWidth);
		// 设置为空心
		mCircularArcPaint.setStyle(Style.STROKE);

		// 创建画外部图片的矩阵
		mPictureRectF = new RectF();
		// 创建圆弧的所在范围
		mCircularArcRectF = new RectF();

		// 创建一个画字的画笔
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		// 画圆中心字体类型
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

		// 获取外部资源
		Resources res = getResources();
		// 获取外部图片资源
		bmp = BitmapFactory.decodeResource(res, R.drawable.group);
		// 获取老人状态字体大小
		mHealthStateTextSize = res.getDimension(R.dimen.health_state_size);
		// 获取标题字体大小
		mTitleSize = res.getDimension(R.dimen.old_man_assess_size);
		// 注册当前控件的点击事件
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mTarget && mOnClickListener != null) {
					// 如果点击区域为中心圆盘的区域内,则触发当前用户添加的点击事件
					mOnClickListener.onClick(v);
				}
				
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	startX = event.getX();
            startY = event.getY();
            double bb = Math.pow(Math.abs(startX - mCircleX), 2.0) + Math.pow(Math.abs(startY - mCircleY), 2.0);
            // 判断当前点击区域是否是在中心圆环内
            if(bb <= Math.pow(mCircularArcRadius, 2)) {
            	mTarget = true;
            }else {
            	mTarget = false;
            }
            break;
            
        case MotionEvent.ACTION_UP:
            break;
        case MotionEvent.ACTION_MOVE:
            break;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// 获取控件分配的宽高
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		// 获取图片的最大值
		mMaxSize = mWidth >= mHeight ? mHeight : mWidth;
		// 初始化圆心的坐标
		mCircleX = mWidth / 2;
		mCircleY = mHeight / 2;
		float perMaxSize = mMaxSize/2;
		// 设置外部图片的范围
		mPictureRectF.set(mCircleX - perMaxSize, mCircleY - perMaxSize, 
				mCircleX + perMaxSize, mCircleY + perMaxSize);
		// 初始化圆弧的半径
		mCircularArcRadius = (float) (mMaxSize * (2.0 / 7));
		// 设置圆弧图片的范围
		mCircularArcRectF.set(mCircleX - mCircularArcRadius + 3, mCircleY - mCircularArcRadius - 2, 
				mCircleX + mCircularArcRadius + 3, mCircleY + mCircularArcRadius - 2);
		int colors[] = {Color.parseColor("#00bdff"), Color.parseColor("#00bdff"),
				Color.parseColor("#ffffffff"), Color.parseColor("#ffffffff"), Color.parseColor("#ff284c"),
				Color.parseColor("#ff284c"), Color.parseColor("#ff284c"),Color.parseColor("#ffc400"),
				Color.parseColor("#ffc400"),Color.parseColor("#b9ff00"),Color.parseColor("#b9ff00"),
				Color.parseColor("#00bdff"),Color.parseColor("#00bdff")};
		float[] positions = {0,1f/12,1f/6,1f/4,1f/3,5f/12,1f/2,7f/12,2f/3,3f/4,5f/6,11f/12,1};
		sweepGradient = new SweepGradient(mCircleX, mCircleY, colors, positions);
	}

	public int getMax() {
		return mMax;
	}
	/**
	 * 设置控件的最大值
	 * @param mMax
	 */
	public void setMax(int mMax) {
		if (mMax < 0) {
			throw new IllegalArgumentException("the mMax must more than 0");
		}
		this.mMax = mMax;
	}
	/**
	 * 设置控件当前最大值
	 * @param value
	 */
	public void setCurrentValue(int value) {
		if (value < 0) {
			// mProgress小于0，抛出异常
			throw new IllegalArgumentException("the mProgress must more than 0");
		}
		if (value > mMax) {
			// 如果当前进度大于最大进度，则等于最大进度
			value = mMax;
		}
		mCurrentValue = value;
	}
	/**
	 * 获取当前进度
	 * @return
	 */
	public int getProgress() {
		return mProgress;
	}
	/**
	 * 设置控件当前显示的进度
	 * @param mProgress
	 */
	private void setProgress(int mProgress) {
		if (mProgress < 0) {
			// mProgress小于0，抛出异常
			throw new IllegalArgumentException("the mProgress must more than 0");
		}
		if (mProgress > mMax) {
			// 如果当前进度大于最大进度，则等于最大进度
			mProgress = mMax;
		}
		this.mProgress = mProgress;
		// 更新UI
		invalidate();
	}
	/**
	 * 设置控件当前的状态文字
	 * @param state
	 */
	public void setHealthState(String state) {
		mHealthStateText = state;
	}
	/**
	 * 设置控件的名称
	 * @param title
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	public void start() {
		if (isStart) {
			isStart = false;
			new AsyncTask<Integer, Integer, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Integer... params) {
					Integer integer = params[0];
					for (int i = 0; i <= integer; i++) {
						SystemClock.sleep(10);
						publishProgress(i);
					}
					return null;
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					isStart = true;
				}

				@Override
				protected void onProgressUpdate(Integer... values) {
					setProgress(values[0]);
				}

			}.execute(mCurrentValue);
		}
	}

}
