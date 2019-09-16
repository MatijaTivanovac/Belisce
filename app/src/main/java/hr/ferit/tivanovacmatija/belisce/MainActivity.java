package hr.ferit.tivanovacmatija.belisce;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bNews, bMap, bLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setUpUI();
    }

    private void setUpUI() {
        this.bNews = (Button) findViewById(R.id.bNews);
        this.bMap = (Button) findViewById(R.id.bMap);
        this.bLocations = (Button) findViewById(R.id.bLocations);

        this.bNews.setOnClickListener(this);
        this.bMap.setOnClickListener(this);
        this.bLocations.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent expIntent = null;
        switch (v.getId()){
            case (R.id.bNews):
                expIntent = new Intent();
                expIntent.setClass(getApplicationContext(), News.class);
                this.startActivity(expIntent);
                break;
            case (R.id.bMap):
                expIntent = new Intent();
                expIntent.setClass(getApplicationContext(), activity_map.class);
                this.startActivity(expIntent);
                break;
            case (R.id.bLocations):
                expIntent = new Intent();
                expIntent.setClass(getApplicationContext(), News.class);
                this.startActivity(expIntent);
                break;
        }
    }
}
