package com.example.wireframe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 * @author freeson
 * 
 */
public class CircleImageView extends ImageView {

	private static final Xfermode MASK_XFERMODE;
	private Bitmap mask;
	private Paint paint;
	private int mBorderWidth = 3;
	private int mBorderColor = Color.parseColor("#f2f2f2");

	static {
		PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
		MASK_XFERMODE = new PorterDuffXfermode(localMode);
	}

	public CircleImageView(Context paramContext) {
		super(paramContext);
	}

	public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public CircleImageView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	private boolean useDefaultStyle = false;

	public void setUseDefaultStyle(boolean useDefaultStyle) {
		this.useDefaultStyle = useDefaultStyle;
	}

	@Override
	protected void onDraw(Canvas paramCanvas) {
		if (useDefaultStyle) {
			super.onDraw(paramCanvas);
			return;
		}
		final Drawable localDrawable = getDrawable();
		if (localDrawable == null)
			return;
		if (localDrawable instanceof NinePatchDrawable) {
			return;
		}
		if (this.paint == null) {
			final Paint localPaint = new Paint();
			localPaint.setFilterBitmap(false);
			localPaint.setAntiAlias(true);
			localPaint.setXfermode(MASK_XFERMODE);
			this.paint = localPaint;
		}
		final int width = getWidth();
		final int height = getHeight();
		/** 保存layer */
		int layer = paramCanvas.saveLayer(0.0F, 0.0F, width, height, null, 31);
		super.onDraw(paramCanvas);
		if ((this.mask == null) || (this.mask.isRecycled())) {
			this.mask = createOvalBitmap(width, height);
		}
		/** 将bitmap画到canvas上面 */
		paramCanvas.drawBitmap(this.mask, 0.0F, 0.0F, this.paint);
		/** 将画布复制到layer上 */
		paramCanvas.restoreToCount(layer);
	}

	/**
	 * 获取一个bitmap，目的是用来承载drawable;
	 * <p>
	 * 将这个bitmap放在canvas上面承载，并在其上面画一个椭圆(其实也是一个圆，因为width=height)来固定显示区域
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap createOvalBitmap(final int width, final int height) {
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
		Bitmap localBitmap = Bitmap.createBitmap(width, height, localConfig);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint();
		final int padding = mBorderWidth != 0 ? mBorderWidth - 2 : 0;
		/**
		 * 设置椭圆的大小(因为椭圆的最外边会和border的最外边重合的，如果图片最外边的颜色很深，有看出有棱边的效果，所以为了让体验更加好，
		 * 让其缩进padding px)
		 */
		RectF localRectF = new RectF(padding, padding, width - padding, height
				- padding);
		localCanvas.drawOval(localRectF, localPaint);
		return localBitmap;
	}
}