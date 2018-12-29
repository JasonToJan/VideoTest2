package com.example.audioeffecttest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private MediaPlayer mediaPlayer;
	private Visualizer visualizer;
	private Equalizer equalizer;
	private BassBoost bassBoost;
	private PresetReverb presetReverb;
	private LinearLayout layout;
	private ScrollView scrollView;
	private List<Short> reverbNames = new ArrayList<Short>();
	private List<String> reverbVals = new ArrayList<String>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //layout = new LinearLayout(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        scrollView = new ScrollView(this);
        scrollView.addView(layout);
        
        setContentView(scrollView);
        
        mediaPlayer = MediaPlayer.create(this, R.raw.hometown);
        setupVisualizer();
        setupBassBoost();
        setupPresetReverb();
        mediaPlayer.start();
    }
    public void setupPresetReverb() {
    	presetReverb = new PresetReverb(0, mediaPlayer.getAudioSessionId());
    	presetReverb.setEnabled(true);
    	TextView prTitle = new TextView(this);
    	prTitle.setText("设置预设场");
    	layout.addView(prTitle);
    	
    	for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
    		reverbNames.add(i);
    		reverbVals.add(equalizer.getPresetName(i));
    	}
    	
    	Spinner sp = new Spinner(this);
    	sp.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, reverbVals));
    	sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				presetReverb.setPreset(reverbNames.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
    		
		});
    	layout.addView(sp);
    	
    }
    public void setupBassBoost() {
    	bassBoost = new BassBoost(0, mediaPlayer.getAudioSessionId());
    	bassBoost.setEnabled(true);
    	TextView bbTitle = new TextView(this);
    	bbTitle.setText("�ص�����");
    	layout.addView(bbTitle);
    	
    	SeekBar bar = new SeekBar(this);
    	//�ص�����ΧΪ0-1000
    	bar.setMax(1000);
    	bar.setProgress(0);
    	
    	bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				bassBoost.setStrength((short)progress);
			}
		});
    	layout.addView(bar);
    }
    public void setupEqualizer() {
    	equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
    	equalizer.setEnabled(true);
    	TextView eqTitle = new TextView(this);
    	eqTitle.setText("��������");
    	layout.addView(eqTitle);
    	//��ȡ������������֧����Сֵ�����ֵ
    	final short minEQLevel = equalizer.getBandLevelRange()[0];
    	short maxEQLevel = equalizer.getBandLevelRange()[1];
    	
    	//��ȡ������������֧�ֵ�����Ƶ��
    	short brands = equalizer.getNumberOfBands();
    	for (short i = 0; i < brands; i++) {
    		TextView eqTextView = new TextView(this);
    		eqTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    		eqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
    		eqTextView.setText((equalizer.getCenterFreq(i)/1000)+"Hz");
    		layout.addView(eqTextView);
    		
    		LinearLayout tmpLayout = new LinearLayout(this);
    		tmpLayout.setOrientation(LinearLayout.HORIZONTAL);
    		
    		TextView minDbTextView = new TextView(this);
    		minDbTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		minDbTextView.setText((minEQLevel/100)+"dB");
    		
    		TextView maxDbTextView = new TextView(this);
    		maxDbTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		maxDbTextView.setText((maxEQLevel/100)+"dB");
    		
    		SeekBar bar = new SeekBar(this);
    		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    		layoutParams.weight = 1;
    		bar.setLayoutParams(layoutParams);
    		bar.setMax(maxEQLevel-minEQLevel);
    		bar.setProgress(equalizer.getBandLevel(i));
    		final short brand = i;
    		
    		bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					equalizer.setBandLevel(brand, (short)(progress+minEQLevel));
				}
			});
    		//ʹ��ˮƽ���������LinearLayoutʢװ�������
    		tmpLayout.addView(minDbTextView);
    		tmpLayout.addView(bar);
    		tmpLayout.addView(maxDbTextView);
    		layout.addView(tmpLayout);
    	}
    	
    	
    }
    public void setupVisualizer() {
    	final MyVisualizerView myVisualizerView = new MyVisualizerView(this);
    	myVisualizerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
    			(int)(120f*getResources().getDisplayMetrics().density)));
    	layout.addView(myVisualizerView);
    	visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
    	visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
    	visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
			
			@Override
			public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform,
					int samplingRate) {
				// TODO Auto-generated method stub
				//String str = new String(waveform, 0, waveform.length);
				System.out.println("capture->" + waveform.length);
				/*
				for (byte b: waveform) {
					System.out.println(b);
				}*/
				myVisualizerView.updateVisualizer(waveform);
			}
			
			@Override
			public void onFftDataCapture(Visualizer visualizer, byte[] fft,
					int samplingRate) {
				// TODO Auto-generated method stub
				
			}
		}, Visualizer.getMaxCaptureRate()/2, true, false);
    	visualizer.setEnabled(true);
    	
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (isFinishing() && mediaPlayer != null) {
			visualizer.release();
			equalizer.release();
			presetReverb.release();
			bassBoost.release();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onPause();
	}
    
    
    
}
