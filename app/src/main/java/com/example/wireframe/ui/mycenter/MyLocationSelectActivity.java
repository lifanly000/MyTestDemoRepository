package com.example.wireframe.ui.mycenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.eblock.emama.R;
import com.example.wireframe.ui.BaseActivity;
import com.example.wireframe.utils.CityDbUtils;

public class MyLocationSelectActivity extends BaseActivity implements OnClickListener {
	
	private Spinner provinceSpinner ;
	private Spinner citySpinner ;
	private Spinner countrySpinner ;
	
	private List<String> provs= new ArrayList<String>();
	private List<String> cities = new ArrayList<String>();
	private List<String> countrys= new ArrayList<String>();
	private ArrayAdapter<String> arr_adapter_provs;
	private ArrayAdapter<String> arr_adapter_cities;
	private ArrayAdapter<String> arr_adapter_country;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_location_select);
		
		provinceSpinner = (Spinner) findViewById(R.id.province);
		citySpinner = (Spinner) findViewById(R.id.city);
		countrySpinner = (Spinner) findViewById(R.id.country);
		provs=CityDbUtils.queryAllProv(this);
		arr_adapter_provs=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, provs);
		provinceSpinner.setAdapter(arr_adapter_provs);
		provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
					cities = CityDbUtils.queryAllCity(MyLocationSelectActivity.this, provinceSpinner
							.getSelectedItem().toString());
					arr_adapter_cities = new ArrayAdapter<String>(MyLocationSelectActivity.this,
							android.R.layout.simple_list_item_1, cities);
					citySpinner.setAdapter(arr_adapter_cities);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		
		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				countrys = CityDbUtils.queryAllCountry(MyLocationSelectActivity.this, citySpinner
						.getSelectedItem().toString());
				arr_adapter_country = new ArrayAdapter<String>(MyLocationSelectActivity.this,
						android.R.layout.simple_list_item_1, countrys);
				countrySpinner.setAdapter(arr_adapter_country);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		
		findViewById(R.id.submit).setOnClickListener( this);
		findViewById(R.id.back).setOnClickListener( this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			setResult(10087);
			finish(); 
			break;
		case R.id.submit:
			gotoSubmit(); 
			break;

		default:
			break;
		}
	}

	private void gotoSubmit() {
		String proStr = provinceSpinner.getSelectedItem().toString();
		String cityStr = citySpinner.getSelectedItem().toString();
		String countryStr = countrySpinner.getSelectedItem().toString();
		
		if(TextUtils.isEmpty(proStr)){
			Toast.makeText(this, "省名不能为空", 0).show();
			return ;
		}
		if(TextUtils.isEmpty(cityStr)){
			Toast.makeText(this, "市名不能为空", 0).show();
			return ;
		}
		if(TextUtils.isEmpty(countryStr)){
			Toast.makeText(this, "区名不能为空", 0).show();
			return ;
		}
		String locationName = proStr+"省"+cityStr+"市"+countryStr+"区";
		String locationCode = CityDbUtils.queryCityCode(this,countryStr);
		Intent intent = new Intent();
		intent.putExtra("locationName", locationName);
		intent.putExtra("locationCode", locationCode);
		setResult(10086, intent);
		finish();
		
	}
}
