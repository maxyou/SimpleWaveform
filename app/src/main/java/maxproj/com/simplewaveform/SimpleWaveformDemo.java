package maxproj.com.simplewaveform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.Random;

public class SimpleWaveformDemo extends AppCompatActivity {

    SimpleWaveform simpleWaveform;
    LinkedList<Integer> ampList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_waveform_demo);

        simpleWaveform = (SimpleWaveform)findViewById(R.id.simplewaveform);
        for(int i = 0; i < 200; i++){
            ampList.add(randInt(0, 1000));
        }
        simpleWaveform.setDataList(ampList);
        simpleWaveform.refresh();

    }

    public int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
