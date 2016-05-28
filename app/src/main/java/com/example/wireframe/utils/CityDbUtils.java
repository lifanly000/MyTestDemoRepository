package com.example.wireframe.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 银行卡和城市列表的查询
 * @author FullCat
 *
 */
public class CityDbUtils {
	
	/**
	 * 查询所有的省
	 * @param ctx
	 * @return
	 */
	public static List<String> queryAllProv(Context ctx){
		List<String> provs=new ArrayList<String>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir()+"/city.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select city_name from city where clevel = 1  ",null);
		while(cursor.moveToNext()){
			provs.add(cursor.getString(0));
		}
		return provs;
	}
	/**
	 * 根据省名查询该省下面的所有市名
	 * @param ctx
	 * @return
	 */
	public static List<String> queryAllCity(Context ctx,String provName){
		List<String> provs=new ArrayList<String>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir()+"/city.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select city_name from city where parent_id=?",new String[]{CityDbUtils.queryProvCode(ctx, provName)} );
		while(cursor.moveToNext()){
			provs.add(cursor.getString(0));
		}
		return provs;
	}
	/**
	 * 根据省名查询该省所对应的代号
	 * @param ctx
	 * @return
	 */
	public static String queryProvCode(Context ctx,String provName){
		String code=null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir()+"/city.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select city_id from city where city_name=?  and clevel=1", new String[]{provName});
		if(cursor.moveToNext()){
			code=cursor.getString(0);
		}
		return code;
	}
	/**
	 * 根据省名查询该省所对应的代号
	 * @param ctx
	 * @return
	 */
	public static String queryProvCode2(Context ctx,String provName){
		String code=null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir()+"/city.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select city_id from city where city_name=?  and clevel=2", new String[]{provName});
		if(cursor.moveToNext()){
			code=cursor.getString(0);
		}
		return code;
	}
	/**
	 * 根据市名查找县名
	 * @param ctx
	 * @return
	 */
	public static List<String> queryAllCountry(Context ctx,String provName){
		List<String> provs=new ArrayList<String>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir()+"/city.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select city_name from city where parent_id=? and clevel = 3",new String[]{CityDbUtils.queryProvCode2(ctx, provName)} );
		while(cursor.moveToNext()){
			provs.add(cursor.getString(0));
		}
		return provs;
	}
	/**
	 * 根据省名、城市名查询该城市所对应的代号
	 * @param ctx
	 * @return
	 */
	public static String queryCityCode(Context ctx,String cityName){
		String code=null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(ctx.getFilesDir()+"/city.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select city_code from city where city_name=? and clevel = 3", new String[]{cityName});
		if(cursor.moveToNext()){
			code=cursor.getString(0);
		}
		return code;
	}
}
