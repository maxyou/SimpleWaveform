package maxproj.com.simplewaveform;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.Random;

public class SimpleWaveformDemo extends AppCompatActivity {

    SimpleWaveform simpleWaveform;
    LinkedList<Integer> ampList = new LinkedList<>();
    Paint barPencil = new Paint();
    Paint peakPencil = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_waveform_demo);

        simpleWaveform = (SimpleWaveform)findViewById(R.id.simplewaveform);
        demo3();


    }

    private void demo1() {
        //generate random data
        for(int i = 0; i < 80; i++){
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 50;

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

        //we use default pencil to draw peaks outline

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };

        //show...
        simpleWaveform.refresh();
    }

    private void demo2() {
        //generate random data
        for(int i = 0; i < 80; i++){
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 30;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_ORIGIN;
        //if show peaks outline?
        simpleWaveform.showPeak = false;

        //define pencil to draw bar
        barPencil.setStrokeWidth(15);
        barPencil.setColor(0xf11dcf1f);
        simpleWaveform.barPencil = barPencil;

        //we use default pencil to draw peaks outline

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        };

        //show...
        simpleWaveform.refresh();
    }


    private void demo3() {
        //generate random data
        for(int i = 0; i < 80; i++){
            ampList.add(randomInt(-50, 50));
        }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 50;

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

        //define pencil to draw peak outline
        peakPencil.setStrokeWidth(5);
        peakPencil.setColor(0xfffe2f3f);
        simpleWaveform.barPencil = barPencil;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
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
