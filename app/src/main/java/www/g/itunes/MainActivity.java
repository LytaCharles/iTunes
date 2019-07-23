package www.g.itunes;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

     Toolbar mToolbar;

     ListView mySongsListView;

     String[] items;

     List<Display.Mode> modeList;


     TextView  wht_listview;

     ArrayAdapter<String> myAdapter;
    private CharSequence s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = (DrawerLayout)findViewById(R.id.myDrawerLayout_id) ;
        navigationView = (NavigationView)findViewById(R.id.myNavigation_id) ;
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar,R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mToolbar = (Toolbar)findViewById(R.id.mainapp_toorbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("iTunes");
        getSupportActionBar().setSubtitle("Enjoy cool music");
        mToolbar.setLogo(R.drawable.ituneslogo);


        wht_listview =  (TextView)findViewById(R.id.list_content) ;
        mySongsListView = (ListView)findViewById(R.id.mySongListView);


        runtimePermission();



    }

    private void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        modeList.clear();





    }

    // requesting runtime permission using dexter
    private void runtimePermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                }).check();
    }

    public ArrayList<File>  findSongs(File file){

        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for (File singleFile: files){

            if (singleFile.isDirectory() && !singleFile.isHidden()){


                arrayList.addAll(findSongs(singleFile));
            }else
            {
                if (singleFile.getName().endsWith(".mp3")||
                    singleFile.getName().endsWith(".wav")){

                    arrayList.add(singleFile);
                }
            }

        }
        return arrayList;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);



        return true;




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.ic_Search_view_id)
        {
            Toast.makeText(this, "This feature is currently unavailable", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.share){
            Toast.makeText(this, "This feature is currently unavailable", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.userprofile_id){

            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);

        }
        else if (id == R.id.settings){

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    void display(){

        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        items = new  String[mySongs.size()];

        for (int i=0;i<mySongs.size();i++){

            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items)

        {

            @Override
            public View getView(int position, View convertView,  ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.WHITE);

                return view;
            }
        };
        mySongsListView.setAdapter(myAdapter);



        mySongsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName = mySongsListView.getItemAtPosition(position).toString();

                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                .putExtra("songs",mySongs).putExtra("songname",songName)
                .putExtra("pos",position));

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {
        closeDrawer();
        return true;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void  openDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            closeDrawer();
        }
        super.onBackPressed();

    }


}
