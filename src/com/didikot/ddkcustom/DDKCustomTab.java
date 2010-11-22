package com.didikot.ddkcustom;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public final class DDKCustomTab extends ImageView 
{
	public static final String TAG = "DDKCustomTab"; 
	
    private Paint					mPaint;
    private Paint					mActiveTextPaint;
    private Paint					mInactiveTextPaint;
    private ArrayList<TabMember>	mTabMembers;
    private int						mActiveTab;
    private OnTabClickListener		mOnTabClickListener = null;
    
    public void init()
    {
		mTabMembers = new ArrayList<DDKCustomTab.TabMember>( );
		
		mPaint = new Paint( );
		mActiveTextPaint = new Paint( );
		mInactiveTextPaint = new Paint( );
		
		mPaint.setStyle( Paint.Style.FILL );
		mPaint.setColor( 0x00FFFFFF );
		
		mActiveTextPaint.setTextAlign( Align.CENTER );
		mActiveTextPaint.setTextSize( 12 );
		mActiveTextPaint.setColor( 0xFFFFFFFF );
		mActiveTextPaint.setFakeBoldText( true );
		
		mInactiveTextPaint.setTextAlign( Align.CENTER );
		mInactiveTextPaint.setTextSize( 12 );
		mInactiveTextPaint.setColor( 0xFF999999 );
		mInactiveTextPaint.setFakeBoldText( true );
		mActiveTab = 0;

    }
	public DDKCustomTab(Context context) {
		super(context);
		init();
	}

	public DDKCustomTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DDKCustomTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	
    @Override
    protected void onDraw( Canvas canvas )
    {

    	super.onDraw( canvas );
    	
    	Rect r = new Rect( );
    	this.getDrawingRect( r );
    	
    	// Calculate how many pixels each tab can use
    	int singleTabWidth = r.right / ( mTabMembers.size( ) != 0 ? mTabMembers.size( ) : 1 );
    	
    	// Draw gradientShadow
    	LinearGradient shader = new LinearGradient(0, -6, 0, 0,
                new int[]{0x00000000, 0x00000000, 0x7f000000, 0x7F000000},
                new float[] {0, 0.03f, 0.97f, 1}, Shader.TileMode.CLAMP);
    	
    	Paint p = new Paint();
    	p.setShader(shader);
    	p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    	
    	// Draw each tab
    	
    	for( int i = 0; i < mTabMembers.size( ); i++ )
    	{
    		TabMember tabMember = mTabMembers.get( i );
    		
    		Bitmap icon = BitmapFactory.decodeResource( getResources( ), tabMember.getIconResourceId( ) );
			Bitmap iconSelected =  BitmapFactory.decodeResource( getResources( ), tabMember.getSelectedIconResourceId( ) );
			Bitmap bg = BitmapFactory.decodeResource( getResources( ), tabMember.getBgImageResourceId( ) );
			Bitmap bgSelected = BitmapFactory.decodeResource( getResources( ), tabMember.getBgSelectedImageResourceId( ) );
    		// Calculate Tab Image Position
    		int tabImgX = singleTabWidth * i + ( singleTabWidth / 2 - icon.getWidth( ) / 2 );
    		
    		// Active Tab will be drawn in a different way
			String txt = tabMember.getText(); 

			int iconYN =  r.top + r.height()/2 - icon.getHeight()/2;
			int iconYS =  r.top + r.height()/2 - iconSelected.getHeight()/2;
			
			if(txt.length() > 0)
			{
				iconYN -= 6;
				iconYS -= 6;
			}
			
			if( mActiveTab == i )
    		{		
    			//canvas.drawRoundRect(  new RectF( r.left + singleTabWidth * i + 3, r.top + 3, r.left + singleTabWidth * ( i + 1 ) - 3, r.bottom - 2 ), 5, 5, mPaint );
    			canvas.drawBitmap(bgSelected, new Rect(0,0,bg.getWidth(),bg.getHeight()), 
    					new RectF( r.left + singleTabWidth * i, r.top, r.left + singleTabWidth * ( i + 1 ), r.bottom ), null);
    			canvas.drawBitmap( iconSelected, tabImgX , iconYS, null );
    			
    			if(txt.length() > 0)
    				canvas.drawText( tabMember.getText( ), singleTabWidth * i + ( singleTabWidth / 2), r.bottom - 4, mActiveTextPaint );
    		} else
    		{
    			canvas.drawBitmap(bg, new Rect(0,0,bg.getWidth(),bg.getHeight()), 
    					new RectF( r.left + singleTabWidth * i, r.top, r.left + singleTabWidth * ( i + 1 ), r.bottom ), null);
    			canvas.drawBitmap( icon, tabImgX , iconYN, null );
    			
    			if(txt.length() > 0)
    				canvas.drawText( tabMember.getText( ), singleTabWidth * i + ( singleTabWidth / 2), r.bottom - 4, mInactiveTextPaint );
    		}
    	}
    	
    	canvas.drawRect(0, -6, r.width(), 0, p);


    }
    
    @Override
    public boolean onTouchEvent( MotionEvent motionEvent )
    {
    	Rect r = new Rect( );
    	this.getDrawingRect( r );        	
    	float singleTabWidth = r.right / ( mTabMembers.size( ) != 0 ? mTabMembers.size( ) : 1 );
    	
    	int pressedTab = (int) ( ( motionEvent.getX( ) / singleTabWidth ) - ( motionEvent.getX( ) / singleTabWidth ) % 1 );
    	
    	mActiveTab = pressedTab;
    	
    	if( this.mOnTabClickListener != null)
    	{
    		this.mOnTabClickListener.onTabClick( mTabMembers.get( pressedTab ).getId( ) );        	
    	}
    	
    	this.invalidate();
    	
    	return super.onTouchEvent( motionEvent );
    }
    
    public void addTabMember( TabMember tabMember )
    {
    	mTabMembers.add( tabMember );
    }
    
    public void setOnTabClickListener( OnTabClickListener onTabClickListener )
    {
    	mOnTabClickListener = onTabClickListener;
    }
    
    public static class TabMember
    {
    	protected int		mId;
    	protected String	mText;
    	protected int 		mIconResourceId;
    	protected int 		mSelectedIconResourceId;
    	protected int 		mBgImageResourceId;
    	protected int		mBgSelectedImageResourceId;
    	
    	public TabMember( int Id, String Text, int iconResourceId, int selectedIconResourceId
    			, int bgImageResourceId, int bgSelectedImageResourceId )
    	{
    		mId = Id;
    		mIconResourceId = iconResourceId;
    		mSelectedIconResourceId = selectedIconResourceId;
    		mBgImageResourceId = bgImageResourceId;
    		mBgSelectedImageResourceId = bgSelectedImageResourceId;
    		mText = Text;
    	}
    	
    	public int getId( )
    	{
    		return mId;
    	}
    	
    	public String getText( )
    	{
    		return mText;
    	}
    	
    	public int getIconResourceId( )
    	{
    		return mIconResourceId;
    	}
    	    
    	public int getSelectedIconResourceId( )
    	{
    		return mSelectedIconResourceId;
    	}

    	public void setText( String Text )
    	{
    		mText = Text;
    	}
    	
    	public int getBgImageResourceId()
    	{
    		return mBgImageResourceId;
    	}
    	
    	public int getBgSelectedImageResourceId()
    	{
    		return mBgSelectedImageResourceId;
    	}
    	
    	public void setIconResourceId( int iconResourceId )
    	{
    		mIconResourceId = iconResourceId;
    	}
    }
    
    public static interface OnTabClickListener
    {
    	public abstract void onTabClick( int tabId );
    }
}

