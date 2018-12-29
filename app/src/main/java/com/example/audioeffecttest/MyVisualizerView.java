package com.example.audioeffecttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class MyVisualizerView extends View {
	private byte[] bytes;
	private float[] points;
	private Paint paint = new Paint();
	private Rect rect = new Rect();
	private byte type = 0;
	
	public MyVisualizerView(Context context) {
		super(context);
		bytes = null;
		paint.setStrokeWidth(1f);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
	}
	
	public void updateVisualizer(byte[] ftt) {
		bytes = ftt;
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return false;
		}
		type++;
		if (type >= 3) {
			type = 0;
		}
		return true;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		
		if (bytes == null) {
			return;
		}
		
		canvas.drawColor(Color.WHITE);
		rect.set(0, 0, this.getWidth(), this.getHeight());
		switch (type) {
		case 0://���ƿ�״����ͼ
			for (int i = 0; i < bytes.length-1; i++) {
				float left = this.getWidth() * i / (bytes.length-1);
				float top = rect.height() - (byte)(bytes[i+1]+128) * rect.height() / 128;//�������128����ȡbyte�����ضϳ�����Χֵ
				float right = left + 1;
				float bottom = rect.height();
				//System.out.println("left:"+left+"top:"+top+"right:"+right+"bottom:"+bottom);
				canvas.drawRect(left, top, right, bottom, paint);
			}
			break;
		case 1://������״����ͼ(ÿ��18���������һ������)
			for (int i = 0; i < bytes.length-1; i += 18) {
				float left = rect.width() * i / (bytes.length-1);
				float top = rect.height() - (byte)(bytes[i+1]+128)*rect.height()/128;
				float right = left + 6;
				float bottom = rect.height();
				canvas.drawRect(left, top, right, bottom, paint);
			}
			break;
		case 2:
			if (points == null || points.length < bytes.length * 4) {
				points = new float[bytes.length*4];
				for (int i = 0; i < bytes.length - 1; i++) {
					points[i*4] = rect.width() * i / (bytes.length - 1);
					points[i*4+1] = (rect.height()/2) + ((byte)(bytes[i]+128))*128/(rect.height()/2);
					points[i*4+2] = rect.width() * (i+1) / (bytes.length-1);
					points[i*4+3] = (rect.height()/2)+((byte)(bytes[i+1]+128))*128/(rect.height()/2);
				}//for
			}//if
			canvas.drawLines(points, paint);
			break;
		}
	}
	
	
	
	
	

	
}
