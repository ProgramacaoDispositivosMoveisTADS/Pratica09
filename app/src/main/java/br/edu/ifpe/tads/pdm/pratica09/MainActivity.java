package br.edu.ifpe.tads.pdm.pratica09;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Button btDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btDownload =  (Button)findViewById(R.id.button);

                btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTest();
            }
        });
    }


    public void onClickTest() {
        String url = ((EditText)findViewById(R.id.edit_url)).getText().toString();
        String filename = url.substring(url.lastIndexOf("/") + 1);
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.URL, url);
        intent.putExtra(DownloadService.FILENAME, filename);
        startService(intent);
        Toast.makeText(this, "Download started.", Toast.LENGTH_LONG).show();
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
