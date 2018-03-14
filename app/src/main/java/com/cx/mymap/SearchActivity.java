package com.cx.mymap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchActivity extends Activity implements View.OnClickListener{

    private Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search:

                break;
        }
    }
}
