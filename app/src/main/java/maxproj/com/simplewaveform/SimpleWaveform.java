package maxproj.com.simplewaveform;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

/**
 * Created by yhy on 2016/1/1.
 */
public class SimpleWaveform extends View {
    Context context;

    public final static int DATA_MODE_REAL = 1;
    public final static int DATA_MODE_ABSOLUTE = 2;
    public int dataMode = DATA_MODE_ABSOLUTE;
    public final static int HEIGHT_MODE_PX = 1;
    public final static int HEIGHT_MODE_PERCENT = 2;
    public int heightMode = HEIGHT_MODE_PERCENT;
    public final static int ZERO_MODE_TOP = 1;
    public final static int ZERO_MODE_CENTER = 2;
    public final static int ZERO_MODE_BOTTOM = 3;
    public final static int ZERO_MODE_USR_DEFINE = 4;
    public int zeroMode = ZERO_MODE_CENTER;
    public final static int HEIGHT_MODE_BAR = 1;
    public final static int HEIGHT_MODE_PEER = 2;
    public int shapeMode = HEIGHT_MODE_BAR;

    public int height;
    public int width;
    public boolean haveGotWidthHeight = false;
    public int barWidth = 10;
    public int barGap = 10;
    public int barColorWithMask = 0xff901f5f;
    public LinkedList<Integer> dataList = new LinkedList<Integer>();
    private LinkedList<Integer> innerDataList = new LinkedList<Integer>();

    boolean clearScreen = false;

    Paint mForeground = new Paint();
    private float[] mPoints;
    public int barNum;


    public int dp2Px(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static int getPxFromDimen(Context context, int dimen_id) {
        return context.getResources().getDimensionPixelSize(dimen_id);
    }

    public SimpleWaveform(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SimpleWaveform(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    private void getWidthLength(){

        post(new Runnable() {
            @Override
            public void run() {
                width = SimpleWaveform.this.getWidth();
                height = SimpleWaveform.this.getHeight();

                haveGotWidthHeight = true;
            }
        });


    }
    private void init() {

        getWidthLength();

    }


    public void setDataList(LinkedList<Integer> ampList) {
        this.dataList = ampList;
    }

    public void refresh(){
        this.invalidate();
    }
    public void clearScreen(){
        clearScreen = true;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Log.d("", "normal view: onDraw()");

        if(clearScreen){
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            clearScreen = false;
            return;
        }

        drawWaveList(canvas);

    }

    private float calculateBarHeight(int input){

        float barInPx;

        switch (heightMode){
            case HEIGHT_MODE_PERCENT:
                barInPx = (input * height) / 100;
                break;
            case HEIGHT_MODE_PX:
                barInPx = input;
                break;
            default:
                barInPx = 0;
        }

        return barInPx;
    }

    public void drawWaveList(Canvas canvas) {
        if (canvas == null) {
            return;
        }


        mForeground.setStrokeWidth(barWidth);//这里设置信号柱的宽度
        mForeground.setColor(barColorWithMask);
        mForeground.setAntiAlias(false);


        barNum = (width / (barWidth + barGap)) + 1;
        if(barNum > dataList.size()){
            barNum = dataList.size();
        }

        for (int i = 0; i < barNum; i++) {
            innerDataList.addLast(dataList.get(i));
        }

        if(dataMode == DATA_MODE_ABSOLUTE){
            for (int i = 0; i < barNum; i++) {
                innerDataList.set(i, (Math.abs(innerDataList.get(i))));
            }
        }

        if(heightMode == HEIGHT_MODE_PERCENT){
            for (int i = 0; i < barNum; i++) {
                innerDataList.set(i, (innerDataList.get(i) * height) / 100);
            }
        }

        switch (zeroMode){
            case ZERO_MODE_TOP:
                break;
            case ZERO_MODE_CENTER:
                for (int i = 0; i < barNum; i++) {
                    innerDataList.set(i, innerDataList.get(i) + height / 2);
                }
                break;
            case ZERO_MODE_BOTTOM:
                for (int i = 0; i < barNum; i++) {
                    innerDataList.set(i, innerDataList.get(i) + height);
                }
                break;
            case ZERO_MODE_USR_DEFINE:
                break;
        }

        mPoints = new float[barNum * 4];
        for (int i = 0; i < barNum; i++) {
            float barHeight = calculateBarHeight(innerDataList.get(i));
            mPoints[i * 4] = i * (barWidth + barGap);
            mPoints[i * 4 + 1] = (height - barHeight) / 2;
            mPoints[i * 4 + 2] = i * (barWidth + barGap);
            mPoints[i * 4 + 3] = (height + barHeight) / 2;
        }

        /**
         * 清屏
         */
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        canvas.drawColor(MyConfig.win_wave_backgrand);

        /**
         * 画波形图
         */
        canvas.drawLines(mPoints, mForeground);

    }


}
