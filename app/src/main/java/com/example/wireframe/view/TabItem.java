package com.example.wireframe.view;

import android.content.Intent;

/**
 * @author Javen
 * 
 */
public class TabItem {
	private String title; // tab item title
	private String tag; // tab item title
	private int icon; // tab item icon
	private int bg; // tab item background
	private Intent intent; // tab item intent

	public TabItem(String title, int icon, Intent intent) {
		super();
		this.title = title;
		this.icon = icon;
		this.intent = intent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getBg() {
		return bg;
	}

	public void setBg(int bg) {
		this.bg = bg;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
