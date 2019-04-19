package com.lefuyun.widget.circleindicator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;

import com.lefuyun.R;

public class CircleIndicator extends View {

	// 小圆半径的默认值
	private static final int DEFAULT_INDICATOR_RADIUS = 10;
	// 小圆之间默认的距离
	private static final int DEFAULT_INDICATOR_MARGIN = 40;
	// 小圆默认的背景颜色
	private static final int DEFAULT_INDICATOR_BACKGROUND = Color.BLUE;
	// 小圆当前被选中的颜色
	private static final int DEFAULT_INDICATOR_SELECTED_BACKGROUND = Color.RED;
	// 当前默认布局是居中显示
	private static final int DEFAULT_INDICATOR_LAYOUT_GRAVITY = Gravity.CENTER
			.ordinal();
	// 当前移动小圆的滑动方式是直接移动,没有滑动方式
	private static final int DEFAULT_INDICATOR_MODE = Mode.SOLO.ordinal();

	private ViewPager mViewPager;
	// 用于存放所有的小圆点
	private List<ShapeHolder> mTabItems;
	// 存放当前滑动的点
	private ShapeHolder mMovingItem;

	private OnPageSelectedListener mOnPageSelectedListener;

	// 目标移动前的位置
	private int mCurItemPosition;
	// 距离移动前的偏移位置
	private float mCurItemPositionOffset;
	// 小圆半径
	private float mIndicatorRadius;
	// 小圆之间的边距
	private float mIndicatorMargin;
	// 小圆的背景颜色
	private int mIndicatorBackground;
	// 当前圆被选中的颜色
	private int mIndicatorSelectedBackground;
	// 当前圆环的位置
	private Gravity mIndicatorLayoutGravity;
	// 小圆的滑动方式
	private Mode mIndicatorMode;
	
	/**
	 * 指示器的布局方式
	 * 
	 * LEFT 居左显示 CENTER 居中显示 RIGHT 居右显示
	 */
	public enum Gravity {
		LEFT, CENTER, RIGHT
	}

	/**
	 * 指示器移动小圆的滑动方式
	 * 
	 * INSIDE 从内部进入,即只显示重叠的地方 OUTSIDE 从上面覆盖图标 SOLO 直接跳转到指定的位置,没有滑动过程
	 */
	public enum Mode {
		INSIDE, OUTSIDE, SOLO
	}

	public CircleIndicator(Context context) {
		super(context);
		init();
	}

	public CircleIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CircleIndicator);
		mIndicatorRadius = typedArray
				.getDimensionPixelSize(R.styleable.CircleIndicator_ci_radius,
						DEFAULT_INDICATOR_RADIUS);
		mIndicatorMargin = typedArray
				.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin,
						DEFAULT_INDICATOR_MARGIN);
		mIndicatorBackground = typedArray.getColor(
				R.styleable.CircleIndicator_ci_background,
				DEFAULT_INDICATOR_BACKGROUND);
		mIndicatorSelectedBackground = typedArray.getColor(
				R.styleable.CircleIndicator_ci_selected_background,
				DEFAULT_INDICATOR_SELECTED_BACKGROUND);
		int gravity = typedArray.getInt(R.styleable.CircleIndicator_ci_gravity,
				DEFAULT_INDICATOR_LAYOUT_GRAVITY);
		mIndicatorLayoutGravity = Gravity.values()[gravity];
		int mode = typedArray.getInt(R.styleable.CircleIndicator_ci_mode,
				DEFAULT_INDICATOR_MODE);
		mIndicatorMode = Mode.values()[mode];
		typedArray.recycle();

		init();
	}

	/**
	 * 初始化当前控件所需要的对象
	 */
	private void init() {
		mTabItems = new ArrayList<ShapeHolder>();
	}

	/**
	 * 关联当前的ViewPager
	 * 
	 * @param viewPager
	 */
	public void setViewPager(ViewPager viewPager) {
		if (viewPager == null) {
			throw new NullPointerException("The ViewPager is null");
		}
		this.mViewPager = viewPager;
		createTabItems();
		createMovingItem();
		setUpListener();
	}

	/**
	 * 初始化ViewPager的监听事件
	 */
	private void setUpListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (mIndicatorMode == Mode.SOLO) {
					trigger(position, 0);
				}
				if (mOnPageSelectedListener != null) {
					mOnPageSelectedListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				if (mIndicatorMode != Mode.SOLO) {
					trigger(position, positionOffset);
				}
				if (mOnPageSelectedListener != null) {
					mOnPageSelectedListener.onPageScrolled(position,
							positionOffset, positionOffsetPixels);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (mOnPageSelectedListener != null) {
					mOnPageSelectedListener.onPageScrollStateChanged(arg0);
				}
			}
		});
	}

	/**
	 * 当ViewPager的item发生变化时候,trigger将会触发当前控件重新绘制
	 * 
	 * @param position
	 *            目标移动前的位置
	 * @param positionOffset
	 *            距离移动前的偏移位置
	 */
	private void trigger(int position, float positionOffset) {
		CircleIndicator.this.mCurItemPosition = position;
		CircleIndicator.this.mCurItemPositionOffset = positionOffset;
		// 重新设置移动Item的位置
		layoutMovingItem(mCurItemPosition, mCurItemPositionOffset);
		invalidate();
	}

	/**
	 * 创建所有的小圆点
	 */
	private void createTabItems() {
		// 创建小圆点前先清空当前的缓存
		mTabItems.clear();
		if (mViewPager.getAdapter() == null) {
			// 不存在adapter则直接返回
			return;
		}
		// 根据adapter的条目数创建小圆点,并存入缓存
		for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
			OvalShape circle = new OvalShape();
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			Paint paint = drawable.getPaint();
			paint.setColor(mIndicatorBackground);
			paint.setAntiAlias(true);
			shapeHolder.setPaint(paint);
			shapeHolder.resizeShape(2 * mIndicatorRadius, 2 * mIndicatorRadius);
			mTabItems.add(shapeHolder);
		}
	}

	/**
	 * 创建滑动点
	 */
	private void createMovingItem() {
		OvalShape circle = new OvalShape();
		ShapeDrawable drawable = new ShapeDrawable(circle);
		mMovingItem = new ShapeHolder(drawable);
		Paint paint = drawable.getPaint();
		paint.setColor(mIndicatorSelectedBackground);
		paint.setAntiAlias(true);

		switch (mIndicatorMode) {
		case INSIDE:
			// 从内部进入,即只显示重叠的地方
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
			break;
		case OUTSIDE:
			// 从上面覆盖图标
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
			break;
		case SOLO:
			// 直接完成移动,没有滑动的过程
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
			break;
		}
		mMovingItem.setPaint(paint);
		mMovingItem.resizeShape(2 * mIndicatorRadius, 2 * mIndicatorRadius);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		layoutTabItems(getWidth(), getHeight());
		layoutMovingItem(mCurItemPosition, mCurItemPositionOffset);
	}

	/**
	 * 布局所有的小圆
	 * 
	 * @param containerWidth
	 *            当前空间的宽度
	 * @param containerHeight
	 *            当前控件的高度
	 */
	private void layoutTabItems(int containerWidth, int containerHeight) {
		// 所有小圆垂直居中显示
		final float y = containerHeight * 0.5f - mIndicatorRadius;
		final float sPosition = startDrawPosition(containerWidth);
		// 设置所有小圆点额位置
		for (int i = 0; i < mTabItems.size(); i++) {
			ShapeHolder item = mTabItems.get(i);
			item.setY(y);
			float x = sPosition + (mIndicatorMargin + mIndicatorRadius * 2) * i;
			item.setX(x);
		}
	}

	/**
	 * 获取小圆点绘制的开始位置
	 * 
	 * @param containerWidth
	 *            小圆所绘制布局的宽度
	 * @return
	 */
	private float startDrawPosition(int containerWidth) {
		if (mIndicatorLayoutGravity == Gravity.LEFT) {
			// 靠布局左显示,直接返回0
			return 0;
		}
		// 获取绘制所有小圆所需的宽度
		float tabItemsLength = mTabItems.size()
				* (2 * mIndicatorRadius + mIndicatorMargin) - mIndicatorMargin;
		if (containerWidth < tabItemsLength) {
			// 如果分配的宽度无法满足绘制所有小圆所需的宽度,则直接返回0
			return 0;
		}
		if (mIndicatorLayoutGravity == Gravity.CENTER) {
			// 居中显示
			return (containerWidth - tabItemsLength) / 2;
		}
		// 居右显示
		return containerWidth - tabItemsLength;
	}

	/**
	 * 布局当前移动小圆
	 * 
	 * @param position
	 *            目标移动前的位置
	 * @param positionOffset
	 *            距离移动前的偏移位置
	 */
	private void layoutMovingItem(int position, float positionOffset) {
		if (mTabItems.size() > 0) {
			ShapeHolder item = mTabItems.get(position);
			float x = item.getX() + (mIndicatorMargin + mIndicatorRadius * 2)
					* positionOffset;
			mMovingItem.setX(x);
			mMovingItem.setY(item.getY());
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 保存当前图层,并记录此图层的id
		int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
				Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
						| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
						| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
						| Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		// 对所有的小圆进行绘制
		for (ShapeHolder item : mTabItems) {
			canvas.save();
			canvas.translate(item.getX(), item.getY());
			item.getShape().draw(canvas);
			canvas.restore();
		}
		// 绘制移动小圆
		if (mMovingItem != null && mTabItems.size() > 0) {
			canvas.save();
			canvas.translate(mMovingItem.getX(), mMovingItem.getY());
			mMovingItem.getShape().draw(canvas);
			canvas.restore();
		}
		// 返回到原始图层
		canvas.restoreToCount(sc);
	}

	/**
	 * 通知控件当前数据有更新
	 */
	public void notifyDataSetChanged() {
		if (mViewPager.getAdapter() != null) {
			mViewPager.getAdapter().notifyDataSetChanged();
		}
		// 只是数据的更新,所以移动小圆不用重新创建
		createTabItems();
		invalidate();
	}

	public void setOnPageSelectedLisrener(OnPageSelectedListener listener) {
		mOnPageSelectedListener = listener;
	}

	/**
	 * 设置指示器小圆的半径
	 * 
	 * @param mIndicatorRadius
	 */
	public void setIndicatorRadius(float mIndicatorRadius) {
		this.mIndicatorRadius = mIndicatorRadius;
	}

	/**
	 * 设置指示器小圆之间的距离
	 * 
	 * @param mIndicatorMargin
	 */
	public void setIndicatorMargin(float mIndicatorMargin) {
		this.mIndicatorMargin = mIndicatorMargin;
	}

	/**
	 * 设置指示器所有小圆的背景
	 * 
	 * @param mIndicatorBackground
	 */
	public void setIndicatorBackground(int mIndicatorBackground) {
		this.mIndicatorBackground = mIndicatorBackground;
	}

	/**
	 * 设置移动小圆的背景颜色
	 * 
	 * @param mIndicatorSelectedBackground
	 */
	public void setIndicatorSelectedBackground(int mIndicatorSelectedBackground) {
		this.mIndicatorSelectedBackground = mIndicatorSelectedBackground;
	}

	/**
	 * 设置指示器的布局方式
	 * 
	 * @param mIndicatorLayoutGravity
	 */
	public void setIndicatorLayoutGravity(Gravity mIndicatorLayoutGravity) {
		this.mIndicatorLayoutGravity = mIndicatorLayoutGravity;
	}

	/**
	 * 设置指示器移动小圆的移动方式
	 * 
	 * @param mIndicatorMode
	 */
	public void setIndicatorMode(Mode mIndicatorMode) {
		this.mIndicatorMode = mIndicatorMode;
	}
}
