package maxproj.com.simplewaveform;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.LinkedList;
import java.util.Random;

public class SimpleWaveformDemo extends AppCompatActivity {

    Button button_switch;
    Button button_goto_advance;

    int demo_loop;

    SimpleWaveform simpleWaveform;

    Paint barPencil = new Paint();
    Paint peakPencil = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_waveform_demo);

        simpleWaveform = (SimpleWaveform)findViewById(R.id.simplewaveform);

        button_switch = (Button)findViewById(R.id.button_switch);
        button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demo_loop++;
                switch (demo_loop % 4){
                    case 0:
                        demo1();
                        break;
                    case 1:
                        demo2();
                        break;
                    case 2:
                        demo3();
                        break;
                    case 3:
                        demo4();
                        break;
                }
            }
        });
        button_goto_advance = (Button)findViewById(R.id.button_goto_advance);

        demo1();


    }

    private void demo1() {

        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for(int i = 0; i < 80; i++){
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 50;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ORIGIN;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_ORIGIN;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //define pencil to draw bar
        barPencil.setStrokeWidth(15);
        barPencil.setColor(0xf11dcf1f);
        simpleWaveform.barPencil = barPencil;

        //define pencil to draw peaks outline
        peakPencil.setStrokeWidth(5);
        peakPencil.setColor(0xfffe2f3f);
        simpleWaveform.peakPencil = peakPencil;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };

        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("","you touch at: "+progress);
            }
        };

        //show...
        simpleWaveform.refresh();
    }

    private void demo2() {

        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for(int i = 0; i < 80; i++){
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 30;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_PARALLEL;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //define pencil to draw bar
        barPencil.setStrokeWidth(15);
        barPencil.setColor(0xf11dcf1f);
        simpleWaveform.barPencil = barPencil;

        //define pencil to draw peaks outline
        peakPencil.setStrokeWidth(5);
        peakPencil.setColor(0xfffe2f3f);
        simpleWaveform.peakPencil = peakPencil;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
            }
        };
        //show...
        simpleWaveform.refresh();
    }


    private void demo3() {

        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for(int i = 0; i < 80; i++){
            ampList.add(randomInt(0, 100));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 100;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_RIGHT_LEFT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_BOTTOM;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_PARALLEL;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //define pencil to draw bar
        barPencil.setStrokeWidth(75);
        barPencil.setColor(0xf11dcf1f);
        simpleWaveform.barPencil = barPencil;

        //define pencil to draw peaks outline
        peakPencil.setStrokeWidth(5);
        peakPencil.setColor(0xfffe2f3f);
        simpleWaveform.peakPencil = peakPencil;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
            }
        };
        //show...
        simpleWaveform.refresh();
    }


    private void demo4() {

        simpleWaveform.init();

        LinkedList<Integer> ampList = new LinkedList<>();
        //generate random data
        for(int i = 0; i < 80; i++){
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 50;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_RIGHT_LEFT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_CROSS_TOP_BOTTOM;
        //if show peaks outline?
        simpleWaveform.showPeak = true;

        //define pencil to draw bar
        barPencil.setStrokeWidth(5);
        barPencil.setColor(0xf11dcf1f);
        simpleWaveform.barPencil = barPencil;

        //define pencil to draw peaks outline
        peakPencil.setStrokeWidth(5);
        peakPencil.setColor(0xfffe2f3f);
        simpleWaveform.peakPencil = peakPencil;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };
        simpleWaveform.progressTouch = new SimpleWaveform.ProgressTouch() {
            @Override
            public void progressTouch(int progress, MotionEvent event) {
                Log.d("", "you touch at: " + progress);
            }
        };
        //show...
        simpleWaveform.refresh();
    }

    public int randomInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
