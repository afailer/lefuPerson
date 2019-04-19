package com.lefuyun.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.lefuyun.R;
import com.lefuyun.util.TLog;

public class CircleImageView extends ImageView {
	/**
	 * 默认图片的显示方式:居中切图
	 */
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    /**
     * 转换成Bitmap的格式是ARGB_8888
     */
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    /**
     * 颜色DRAWABLE类型转换成bitmap类型的宽高值
     */
    private static final int COLORDRAWABLE_DIMENSION = 2;
    /**
     * 默认边界的宽度
     */
    private static final int DEFAULT_BORDER_WIDTH = 0;
    /**
     * 默认边界的背景颜色
     */
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    /**
     * 默认填充颜色
     */
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    /**
     * 默认标记颜色
     */
    private static final int DEFAULT_SIGN_COLOR = Color.GRAY;
    /**
     * 默认标记选中颜色
     */
    private static final int DEFAULT_SIGN_SELECT_COLOR = Color.RED;
    
    private static final boolean DEFAULT_BORDER_OVERLAY = false;
    /**
     * 图片要画的范围
     */
    private final RectF mDrawableRect = new RectF();
    /**
     * 边界要画的范围
     */
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    /**
     * 图片所需的画笔
     */
    private final Paint mBitmapPaint = new Paint();
    /**
     * 边界所需的画笔
     */
    private final Paint mBorderPaint = new Paint();
    /**
     * 填充颜色的画笔
     */
    private final Paint mFillPaint = new Paint();
    /**
     * 指示小圆圈的画笔
     */
    private final Paint mDisplaySignPaint = new Paint();
    /**
     * 边界颜色
     */
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    /**
     * 边界宽度
     */
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    /**
     * 填充颜色
     */
    private int mFillColor = DEFAULT_FILL_COLOR;
    /**
     * 指示小圆圈的背景颜色
     */
    private int mDisplaySignColor = DEFAULT_SIGN_COLOR;
	/**
     * 指示小圆圈选中的背景颜色
     */
    private int mDisplaySignSelectColor = DEFAULT_SIGN_SELECT_COLOR;
    /**
     * 指示小圆圈当前的背景颜色
     */
    private int mCurrentSignColor;
    /**
     * 指示小圆圈所需的画笔
     */
    private final Paint mDisplaySignBorderPaint = new Paint();
    /**
     * 指示小圆圈边界要画的范围
     */
    private final RectF mDisplaySignBorderRect = new RectF();
    /**
     * 指示小圆圈边界颜色
     */
    private int mDisplaySignBorderColor = DEFAULT_BORDER_COLOR;
    /**
     * 指示小圆圈边界宽度
     */
    private int mDisplaySignBorderWidth = DEFAULT_BORDER_WIDTH;
    /**
     * 保存图像的Bitmap格式
     */
    private Bitmap mBitmap;
    /**
     * 图像渲染器
     */
    private BitmapShader mBitmapShader;
    /**
     * 图像的宽度
     */
    private int mBitmapWidth;
    /**
     * 图像的高度
     */
    private int mBitmapHeight;
    /**
     * 图形的半径
     */
    private float mDrawableRadius;
    /**
     * 边界的半径
     */
    private float mBorderRadius;
    /**
     * 指示小圆圈的半径
     */
    private float mDisplaySignRadius;
    private float mDisplaySignCircleWidth;
    private float mDisplaySignCircleHeight;

    private ColorFilter mColorFilter;
    /**
     * 是否准备就绪
     */
    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;
    /**
     * 是否显示指示小圆圈
     */
    private boolean mDisplaySignCircle = true;

	/**
     * 记录当前图片是否被选中
     */
    private boolean mSelectOn;
	/**
     * 当前控件是否被选择的监听控件
     */
    private OnCheckedChangeListener mListener;
    private OnClickListener mClickListener;
    
    public CircleImageView(Context context) {
        super(context);

        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color, DEFAULT_BORDER_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_civ_border_overlay, DEFAULT_BORDER_OVERLAY);
        mFillColor = a.getColor(R.styleable.CircleImageView_civ_fill_color, DEFAULT_FILL_COLOR);
        mDisplaySignCircle = a.getBoolean(R.styleable.CircleImageView_civ_display_Sign, false);
        mDisplaySignColor = a.getColor(R.styleable.CircleImageView_civ_display_Sign_default, DEFAULT_SIGN_COLOR);
        mDisplaySignSelectColor = a.getColor(R.styleable.CircleImageView_civ_display_Sign_select, DEFAULT_SIGN_SELECT_COLOR);
        mCurrentSignColor = mDisplaySignColor;
        mDisplaySignBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_display_Sign_border_width, DEFAULT_BORDER_WIDTH);
        mDisplaySignBorderColor = a.getColor(R.styleable.CircleImageView_civ_display_Sign_border_color, DEFAULT_BORDER_COLOR);
        
        a.recycle();

        init();
    }
    /**
     * 当前控件是否被选中的监听事件
     */
    public interface OnCheckedChangeListener {
    	// 判断当前控件的选中状态
    	public void onCheckedChanged(View v, boolean isChecked);
    }

    private void init() {
    	// 图片默认的显示方式
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
    	// 仅支持一种scaleType 即ScaleType.CENTER_CROP
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }

        if (mFillColor != Color.TRANSPARENT) {
        	// 如果有填充颜色则画填充颜色
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mFillPaint);
        }
        // 画图片
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mBitmapPaint);
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius, mBorderPaint);
        }
        if(mDisplaySignCircle) {
        	canvas.drawCircle(mDisplaySignCircleWidth, mDisplaySignCircleHeight, 
        			mDisplaySignRadius, mDisplaySignPaint);
        	if(mDisplaySignBorderWidth != 0) {
        		canvas.drawArc(mDisplaySignBorderRect, 0, 360, true, mDisplaySignBorderPaint);
        	}
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }
    
    /**
     * 将Drawable对象转换成Bitmap格式
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
            	// 将ColorDrawable转换成Bitmap
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            // 将drawable画到画布上
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 根据当前指示小圆圈的选择状态,显示背景颜色
     */
    private void checkSelect() {
    	if(!mDisplaySignCircle) {
    		return;
    	}
    	TLog.log("设置背景颜色" + mSelectOn);
    	if(mSelectOn) {
    		mCurrentSignColor = mDisplaySignSelectColor;
    	}else {
    		mCurrentSignColor = mDisplaySignColor;
    	}
    	mDisplaySignPaint.setColor(mCurrentSignColor);
    	postInvalidate();
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // 创建图片画笔
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
        // 创建边界画笔
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        // 创建填充颜色画笔
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);
        // 指示小圆圈的画笔
        mDisplaySignPaint.setStyle(Paint.Style.FILL);
        mDisplaySignPaint.setAntiAlias(true);
        mDisplaySignPaint.setColor(mCurrentSignColor);
        // 创建小圆圈边界的画笔
        mDisplaySignBorderPaint.setStyle(Paint.Style.STROKE);
        mDisplaySignBorderPaint.setAntiAlias(true);
        mDisplaySignBorderPaint.setColor(mDisplaySignBorderColor);
        mDisplaySignBorderPaint.setStrokeWidth(mDisplaySignBorderWidth);
        
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
        // 边界圆环的半径
        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);
        mDisplaySignRadius = mDrawableRadius / 4;
        // 获取标记圆心的偏移距离
        float m = (float) (mDrawableRadius / Math.pow(2, 0.5));
        mDisplaySignCircleWidth =  getWidth() / 2 + m;
        mDisplaySignCircleHeight = getHeight() / 2 - m;
        
        // 标记圆边界
        mDisplaySignBorderRect.set(mDisplaySignCircleWidth - mDisplaySignRadius - mDisplaySignBorderWidth + 1.5f, 
        		mDisplaySignCircleHeight - mDisplaySignRadius - mDisplaySignBorderWidth + 1.5f, 
        		mDisplaySignCircleWidth + mDisplaySignRadius + mDisplaySignBorderWidth - 1.5f, 
        		mDisplaySignCircleHeight + mDisplaySignRadius + mDisplaySignBorderWidth - 1.5f);
        
        updateShaderMatrix();
        invalidate();
    }
    /**
     * 更新图片的位图
     */
    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);
        // 对图片按比例进行缩放, 按最小比例进行缩放
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
        	// 高的比例缩放最小
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
        	// 宽的比例缩放最小
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }
    
    public void setOnCheckedChangeListener(@NonNull OnCheckedChangeListener l) {
    	mListener = l;
    	mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectOn = !mSelectOn;
				checkSelect();
				mListener.onCheckedChanged(v, mSelectOn);
			}
		};
    	setOnClickListener(mClickListener);
    }
    
    
    /**
     * 获取边界颜色
     * @return
     */
    public int getBorderColor() {
        return mBorderColor;
    }
    /**
     * 设置边界颜色
     * @param borderColor 颜色值
     */
    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }
    /**
     * 设置指示小圆圈边界颜色
     * @param displaySignBorderColor 颜色值
     */
    public void setDisplaySignBorderColor(int displaySignBorderColor) {
    	if (displaySignBorderColor == mDisplaySignBorderColor) {
    		return;
    	}
    	
    	mDisplaySignBorderColor = displaySignBorderColor;
    	mDisplaySignBorderPaint.setColor(mDisplaySignBorderColor);
    	invalidate();
    }
    /**
     * 通过资源文件设置边界颜色
     * @param borderColorRes
     */
    public void setBorderColorResource(int borderColorRes) {
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }
    /**
     * 获取填充颜色
     * @return
     */
    public int getFillColor() {
        return mFillColor;
    }
    /**
     * 设置填充颜色的
     * @param fillColor
     */
    public void setFillColor(int fillColor) {
        if (fillColor == mFillColor) {
            return;
        }

        mFillColor = fillColor;
        mFillPaint.setColor(fillColor);
        invalidate();
    }
    /**
     *  通过资源文件设置填充颜色
     * @param fillColorRes
     */
    public void setFillColorResource(int fillColorRes) {
        setFillColor(getContext().getResources().getColor(fillColorRes));
    }
    /**
     * 获取边界的宽度
     * @return
     */
    public int getBorderWidth() {
        return mBorderWidth;
    }
    /**
     * 设置边界的宽度
     * @param borderWidth
     */
    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        // 更新数据
        setup();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        // 更新数据
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        setup();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }
    
    public int getmDisplaySignColor() {
		return mDisplaySignColor;
	}

	public void setmDisplaySignColor(int color) {
		if(mDisplaySignColor == color) {
			return;
		}
		
		mDisplaySignColor = color;
		checkSelect();
	}

	public int getmDisplaySignSelectColor() {
		return mDisplaySignSelectColor;
	}

	public void setmDisplaySignSelectColor(int color) {
		if(mDisplaySignSelectColor == color) {
			return;
		}
		mDisplaySignSelectColor = color;
		checkSelect();
	}
	public boolean ismSelectOn() {
		return mSelectOn;
	}

	public void setmSelectOn(boolean mSelectOn) {
		if(this.mSelectOn != mSelectOn) {
			this.mSelectOn = mSelectOn;
			checkSelect();
		}
	}
	
	public boolean ismDisplaySignCircle() {
		return mDisplaySignCircle;
	}

	public void setmDisplaySignCircle(boolean mDisplaySignCircle) {
		this.mDisplaySignCircle = mDisplaySignCircle;
		checkSelect();
	}

}
