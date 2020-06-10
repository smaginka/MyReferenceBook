package com.example.myreferencebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnListItemListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;
    private List<String> listUrls;
    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL)); // добавить разделитель между айтемами

        // считываем файл с оглавлением
        String jsonString = readAssetFileAsString(this, "table_of_contents.json");

        listItems = new ArrayList<>();
        listUrls = new ArrayList<>();

        // парсим json файл и заполняем оглавление
        try {

            JSONObject json = new JSONObject(jsonString);
            JSONArray jArray = json.getJSONArray("table_of_contents");

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject object = jArray.getJSONObject(i);
                ListItem listItem = new ListItem(object.getString("heading"));
                listItems.add(listItem);

                listUrls.add(object.getString("url"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new MyAdapter(listItems, this, this);
        recyclerView.setAdapter(adapter);

        webView = (WebView) findViewById(R.id.webView1);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(webSettings.LOAD_NO_CACHE);

        webView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onListItemClick(int position) {
        // Toast.makeText(MainActivity.this, listItems.get(position).getText(),Toast.LENGTH_SHORT).show();

        webView.loadUrl("file:///android_asset/" + listUrls.get(position) + ".html");
        recyclerView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE) {
            webView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public String readAssetFileAsString(Context context, String fileName) {

        String string = null;

        try {

            InputStream inputStream = context.getAssets().open(fileName);

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            string = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return string;

    }

}
