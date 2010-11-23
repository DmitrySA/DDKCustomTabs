package com.didikot.ddkcustom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public final class DDKRelativeLayout extends RelativeLayout
{
	private Paint	mPaint;
	private Rect	mRect;
	
	public void init()
	{
		mRect = new Rect( );
		mPaint = new Paint( );
		
		mPaint.setStyle( Paint.Style.FILL_AND_STROKE );
		mPaint.setColor( 0xFFCBD2D8 );	
		
		setClipChildren(false);
	}
	
	public DDKRelativeLayout(Context context) {
		super(context);
		init();
	}

	public DDKRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DDKRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	@Override
	protected void onDraw( Canvas canvas )
	{
		super.onDraw( canvas );
		this.getDrawingRect( mRect );
		for( int i = 0; i < mRect.right; i += 7 )
			canvas.drawRect( mRect.left + i, mRect.top, mRect.left + i + 2, mRect.bottom, mPaint );

	}
}
