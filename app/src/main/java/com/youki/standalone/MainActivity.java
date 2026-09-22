package com.youki.standalone;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private TextView clock;
    private final Handler handler = new Handler();
    private final List<AppInfo> apps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(10, 14, 22));

        clock = new TextView(this);
        clock.setTextColor(Color.WHITE);
        clock.setTextSize(18);
        clock.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        clock.setPadding(20, 15, 20, 10);

        root.addView(clock,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 60));

        GridView grid = new GridView(this);
        grid.setNumColumns(GridView.AUTO_FIT);
        grid.setColumnWidth(100);
        grid.setVerticalSpacing(18);
        grid.setHorizontalSpacing(8);
        grid.setPadding(15, 10, 15, 80);

        loadApps();

        grid.setAdapter(new AppAdapter());

        grid.setOnItemClickListener((parent, view, position, id) -> {
            AppInfo app = apps.get(position);

            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName(app.packageName, app.activityName);
                startActivity(intent);
            } catch (Exception e) {
                Intent fallback =
                        getPackageManager().getLaunchIntentForPackage(
                                app.packageName);

                if (fallback != null) {
                    startActivity(fallback);
                }
            }
        });

        root.addView(grid,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        TextView dock = new TextView(this);
        dock.setText("  Youki DEX  ");
        dock.setTextColor(Color.WHITE);
        dock.setTextSize(16);
        dock.setGravity(Gravity.CENTER);
        dock.setBackgroundColor(Color.rgb(32, 36, 44));

        root.addView(dock,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 65));

        setContentView(root);
        updateClock();
    }

    private void updateClock() {
        clock.setText(
                DateFormat.getTimeInstance(DateFormat.SHORT)
                        .format(new Date()));

        handler.postDelayed(this::updateClock, 30000);
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> list =
                pm.queryIntentActivities(
                        launcherIntent, PackageManager.MATCH_ALL);

        for (ResolveInfo info : list) {
            if (info.activityInfo.packageName.equals(getPackageName()))
                continue;

            apps.add(new AppInfo(
                    info.loadLabel(pm).toString(),
                    info.activityInfo.packageName,
                    info.activityInfo.name,
                    info.loadIcon(pm)
            ));
        }

        Collections.sort(apps,
                Comparator.comparing(a -> a.name.toLowerCase()));
    }

    private class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return apps.size();
        }

        @Override
        public Object getItem(int position) {
            return apps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent) {

            LinearLayout item = new LinearLayout(MainActivity.this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.setPadding(5, 5, 5, 5);

            ImageView icon = new ImageView(MainActivity.this);
            icon.setImageDrawable(apps.get(position).icon);

            item.addView(icon,
                    new LinearLayout.LayoutParams(58, 58));

            TextView name = new TextView(MainActivity.this);
            name.setText(apps.get(position).name);
            name.setTextColor(Color.WHITE);
            name.setTextSize(12);
            name.setGravity(Gravity.CENTER);
            name.setMaxLines(2);

            item.addView(name,
                    new LinearLayout.LayoutParams(
                            90, 45));

            return item;
        }
    }

    static class AppInfo {
        String name;
        String packageName;
        String activityName;
        Drawable icon;

        AppInfo(
                String name,
                String packageName,
                String activityName,
                Drawable icon) {

            this.name = name;
            this.packageName = packageName;
            this.activityName = activityName;
            this.icon = icon;
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
        }
