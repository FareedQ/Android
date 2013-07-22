package com.example.crazytipcal;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class CrazyTipCal extends Activity {

		private static final String TOTAL_BILL = "TOTAL_BILL";
	    private static final String CURRENT_TIP = "CURRENT_TIP";
	    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";
	     
	    private double billBeforeTip; // Users bill before tip
	    private double tipAmount; // Tip amount
	    private double finalBill; // Bill plus Tip
	     
	    EditText billBeforeTipET;
	    EditText tipAmountET;
	    EditText finalBillET;
	    
	    SeekBar tipSeekBar;
	    
	    private int[] checklistValues = new int[12];
	    	     
	    CheckBox friendlyCheckBox;
	    CheckBox specialsCheckBox;
	    CheckBox opinionCheckBox;
	    	     
	    RadioGroup availableRadioGroup;
	    RadioButton availableBadRadio;
	    RadioButton availableOKRadio;
	    RadioButton availableGoodRadio;
	    
	    Spinner problemsSpinner;
	    
	    Button startChronometerButton;
	    Button pauseChronometerButton;
	    Button resetChronometerButton;
	    	     
	    Chronometer timeWaitingChronometer;
	    
	    long secondsYouWaited = 0;
	    
	    TextView timeWaitingTextView;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crazy_tip_cal);
		
		if(savedInstanceState == null){
	            billBeforeTip = 0.0;
	            tipAmount = .15;
	            finalBill = 0.0;
	        } else {
	            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
	            tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
	            finalBill = savedInstanceState.getDouble(TOTAL_BILL);
	        }
	        billBeforeTipET = (EditText) findViewById(R.id.billEditText); // Users bill before tip
	        tipAmountET = (EditText) findViewById(R.id.tipEditText); // Tip amount
	        finalBillET = (EditText) findViewById(R.id.finalBillEditText);
	        
	        billBeforeTipET.addTextChangedListener(billBeforeTipListener);
	        
	        tipSeekBar = (SeekBar) findViewById(R.id.changeTipSeekBar);
        
	        tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListener);
	        
	        friendlyCheckBox = (CheckBox) findViewById(R.id.friendlyCheckBox);
	        specialsCheckBox = (CheckBox) findViewById(R.id.specialsCheckBox);
	        opinionCheckBox = (CheckBox) findViewById(R.id.opinionCheckBox);
	        	         
	        setUpIntroCheckBoxes();

	        availableBadRadio = (RadioButton) findViewById(R.id.availableBadRadio);
	        availableOKRadio = (RadioButton) findViewById(R.id.availableOKRadio);
	        availableGoodRadio = (RadioButton) findViewById(R.id.availableGoodRadio);
	        
	        availableRadioGroup = (RadioGroup) findViewById(R.id.availableRadioGroup);
	        
	        addChangeListenerToRadios();
	        
	        problemsSpinner = (Spinner) findViewById(R.id.problemsSpinner);
	        
	        problemsSpinner.setPrompt("Problem Solving");
	        
	        addItemSelectedListenerToSpinner();
	        
	        startChronometerButton = (Button) findViewById(R.id.startChronometerButton);
	        pauseChronometerButton = (Button) findViewById(R.id.pauseChronometerButton);
	        resetChronometerButton = (Button) findViewById(R.id.resetChronometerButton);
	        
	        setButtonOnClickListeners();
	        
	        timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);
	        
	        timeWaitingTextView = (TextView) findViewById(R.id.timeWaitingTextView);

	}

	private TextWatcher billBeforeTipListener = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			try{
				billBeforeTip = Double.parseDouble(s.toString());
			}
			catch(NumberFormatException e)
			{
				billBeforeTip = 0.0;
				
			}
			
			updateTipAndFinalBill();
		}
		
	};
	
	private void updateTipAndFinalBill(){
		double tipAmount = Double.parseDouble(tipAmountET.getText().toString());
		
		double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
		
		finalBillET.setText(String.format("%.02f", finalBill));
	}
			
	
    protected void onSaveInstanceState(Bundle outState){
	         
	        super.onSaveInstanceState(outState);
	         
	        outState.putDouble(TOTAL_BILL, finalBill);
	        outState.putDouble(CURRENT_TIP, tipAmount);
	        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
	         
    }
    
    private OnSeekBarChangeListener tipSeekBarListener = new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			tipAmount = (tipSeekBar.getProgress()) * 0.01;
			
			tipAmountET.setText(String.format("%.02f", tipAmount));
			
			updateTipAndFinalBill();
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {			
		}
    	
    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crazy_tip_cal, menu);
		return true;
	}

	public void setUpIntroCheckBoxes(){
		friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				checklistValues[0] = (friendlyCheckBox.isChecked())?4:0;

				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();

			}
		});
		
		opinionCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				checklistValues[1] = (opinionCheckBox.isChecked())?1:0;

				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();

			}
		});
		
		specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				checklistValues[2] = (specialsCheckBox.isChecked())?2:0;

				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();

			}
		});
	}
	
	private void addChangeListenerToRadios(){
		
		availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				checklistValues[3] = (availableBadRadio.isChecked())?-1:0;
				checklistValues[4] = (availableOKRadio.isChecked())?2:0;
				checklistValues[5] = (availableGoodRadio.isChecked())?4:0;

				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();
			}
		});
	}
	
	private void addItemSelectedListenerToSpinner(){
		
		problemsSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				checklistValues[6] = (problemsSpinner.getSelectedItem().equals("Bad"))?-1:0;
				checklistValues[7] = (problemsSpinner.getSelectedItem().equals("OK"))?3:0;
				checklistValues[8] = (problemsSpinner.getSelectedItem().equals("Good"))?6:0;

				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
		});
		
	}
	
	private void setButtonOnClickListeners()
	{
		startChronometerButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int stoppedMilliseconds = 0;
				
				String chronoText = timeWaitingChronometer.getText().toString();
				String array[] = chronoText.split(":");

				if (array.length == 2) {
					stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
	                        + Integer.parseInt(array[1]) * 1000;
	                } else if (array.length == 3) {
	                  stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
	                        + Integer.parseInt(array[1]) * 60 * 1000
	                        + Integer.parseInt(array[2]) * 1000;
	                }

				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
				
				secondsYouWaited = Long.parseLong(array[1]);

				updateTipBasedOnTimeWaited(secondsYouWaited);

				timeWaitingChronometer.start();				
			}
		});
		
		pauseChronometerButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				timeWaitingChronometer.stop();
				
			}
		});
		
		resetChronometerButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
				
				secondsYouWaited = 0;
				
			}
		});
	}
	
	private void updateTipBasedOnTimeWaited(long secondsYouWaited){
		checklistValues[9] = (secondsYouWaited > 10)? -2:2;

		setTipFromWaitressChecklist();
		
		updateTipAndFinalBill();
	}
	
	private void setTipFromWaitressChecklist(){
		  int checklistTotal = 0;

		  for(int item : checklistValues){
	            checklistTotal += item;
	      }

	      tipAmountET.setText(String.format("%.02f", checklistTotal * .01));

	}	
}
