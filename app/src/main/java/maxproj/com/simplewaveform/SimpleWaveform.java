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

    public final static int MODE_AMP_REAL = 1;
    public final static int MODE_AMP_ABS = 2;
    public int modeAmp = MODE_AMP_ABS;
    public final static int MODE_HEIGHT_PX = 1;
    public final static int MODE_HEIGHT_PERCENT = 2;
    public int modeHeight = MODE_HEIGHT_PERCENT;
    public final static int MODE_ZERO_TOP = 1;
    public final static int MODE_ZERO_CENTER = 2;
    public final static int MODE_ZERO_BOTTOM = 3;
    public final static int MODE_ZERO_USR_DEFINE = 4;
    public int modeZero = MODE_ZERO_CENTER;
    public final static int MODE_PAINT_BAR = 1;
    public final static int MODE_PAINT_PEER = 2;
    public int modePaint = MODE_PAINT_BAR;

    public int height;
    public int width;
    public boolean haveGotWidthHeight = false;
    public int barWidth = 10;
    public int barGap = 10;
    public int barColorWithMask = 0xff901f5f;
    public LinkedList<Integer> dataList = new LinkedList<Integer>();
    private LinkedList<BarPoints> innerDataList = new LinkedList<BarPoints>();

    class BarPoints{
        int amplitude;
        int amplitudePx;
        int amplitudePxTop;
        int amplitudePxBottom;
        int amplitudePxTopAbs;
        int amplitudePxBottomAbs;

        public BarPoints(int amplitude){
            this.amplitude = amplitude;
        }
    }

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
        mPoints = new float[barNum * 4];

        for (int i = 0; i < barNum; i++) {

            BarPoints barPoints = new BarPoints(dataList.get(i));
            if(modeHeight == MODE_HEIGHT_PERCENT) {
                barPoints.amplitudePx = (barPoints.amplitude * height) / 100;
            }else{
                barPoints.amplitudePx = barPoints.amplitude;
            }

            if(modeAmp == MODE_AMP_ABS){
                if(barPoints.amplitudePx > 0) {
                    barPoints.amplitudePxTop = barPoints.amplitudePx;
                    barPoints.amplitudePxBottom = -barPoints.amplitudePx;
                }else{
                    barPoints.amplitudePxTop = -barPoints.amplitudePx;
                    barPoints.amplitudePxBottom = barPoints.amplitudePx;
                }
            }else{
                if(barPoints.amplitudePx > 0) {
                    barPoints.amplitudePxTop = barPoints.amplitudePx;
                    barPoints.amplitudePxBottom = 0;
                }else{
                    barPoints.amplitudePxTop = 0;
                    barPoints.amplitudePxBottom = barPoints.amplitudePx;
                }
            }


            switch (modeZero){
                case MODE_ZERO_TOP:
                    barPoints.amplitudePxTopAbs = -barPoints.amplitudePxTop;
                    barPoints.amplitudePxBottomAbs = -barPoints.amplitudePxBottom;
                    break;
                case MODE_ZERO_CENTER:
                    barPoints.amplitudePxTopAbs = -barPoints.amplitudePxTop + height / 2;
                    barPoints.amplitudePxBottomAbs = -barPoints.amplitudePxBottom + height / 2;
                    break;
                case MODE_ZERO_BOTTOM:
                    barPoints.amplitudePxTopAbs = -barPoints.amplitudePxTop + height;
                    barPoints.amplitudePxBottomAbs = -barPoints.amplitudePxBottom + height;
                    break;
                case MODE_ZERO_USR_DEFINE:
                    break;
            }
            innerDataList.addLast(barPoints);

            mPoints[i * 4] = i * (barWidth + barGap);
            mPoints[i * 4 + 1] = barPoints.amplitudePxTopAbs;
            mPoints[i * 4 + 2] = i * (barWidth + barGap);
            mPoints[i * 4 + 3] = barPoints.amplitudePxBottomAbs;

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
