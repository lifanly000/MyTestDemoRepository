package com.example.wireframe.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.eblock.emama.R;


public abstract class TabHostActivity extends TabActivity {

    public static TabHost mTabHost;
    private TabWidget mTabWidget;
    private LayoutInflater mLayoutflater;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        // setContentView(R.layout.ui_tab_host);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
        // R.layout.custom_title);

        // set theme because we do not want the shadow
        setTheme(R.style.Tab_host);
        setContentView(R.layout.ui_tab_host);

        mLayoutflater = getLayoutInflater();

        mTabHost = getTabHost();
        mTabWidget = getTabWidget();
        // mTabWidget.setStripEnabled(false); // need android2.2
        prepare();
        initTabSpec();
    }

    private void initTabSpec() {

        int count = getTabItemCount();

        for (int i = 0; i < count; i++) {
            // set text view
            View tabItem = mLayoutflater.inflate(R.layout.tabs, null);

            TextView tvTabItem = (TextView) tabItem.findViewById(R.id.txtTitle);
            setTabItemTextView(tvTabItem, i);
            // LinearLayout tvTabItem = (LinearLayout)
            // tabItem.findViewById(R.id.imgBtn);
            // setTabImg(tvTabItem, i);
            // set id
            String tabItemId = getTabItemId(i);
            // set tab spec
            TabSpec tabSpec = mTabHost.newTabSpec(tabItemId);
            tabSpec.setIndicator(tabItem);
            tabSpec.setContent(getTabItemIntent(i));

            mTabHost.addTab(tabSpec);
        }

    }

    /** 在初始化界面之前调用 */
    protected void prepare() {
        // do nothing or you override it
    }

    /** 自定义头部布局 */
    protected View getTop() {
        // do nothing or you override it
        return null;
    }

    protected int getTabCount() {
        return mTabHost.getTabWidget().getTabCount();
    }

    /** 设置TabItem的图标和标题等 */
    abstract protected void setTabItemTextView(TextView textView, int position);

    abstract protected void setTabImg(LinearLayout tvTabItem, int position);

    abstract protected String getTabItemId(int position);

    abstract protected Intent getTabItemIntent(int position);

    abstract protected int getTabItemCount();

    protected void setCurrentTab(int index) {
        mTabHost.setCurrentTab(index);
    }

    protected void focusCurrentTab(int index) {
        mTabWidget.focusCurrentTab(index);
    }

}
