package com.example.pavanivellal.playmybeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MediaList extends Activity {

    ListView lv;
    public ArrayList<HashMap<String, String>> mySongs_hm = new ArrayList<HashMap<String, String>>();
    String[] from = {"song_name", "bmp"};
    int[] to = {R.id.song_name, R.id.bmp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);

        lv = (ListView) findViewById(R.id.listView);

        String song_res = getIntent().getStringExtra("song_list");




        try {
            JSONObject jsonRootObject = new JSONObject(song_res);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("songs");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.clear();
                    map.put("song_id", jsonObject.getString("song_id"));
                    map.put("song_name", jsonObject.getString("song_name"));
                    map.put("mp3_link", jsonObject.getString("mp3_link"));
                    map.put("image_link", jsonObject.getString("image_link"));
                    map.put("bpm", jsonObject.getString("bpm"));

                    mySongs_hm.add(map);
                }
                ListAdapter adapter = new SimpleAdapter(MediaList.this, mySongs_hm, R.layout.list_fragment, from, to);
                lv.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // register onClickListener to handle click events on each item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                HashMap<String, String> selectedSong = mySongs_hm.get(position);
                Intent intent = new Intent(MediaList.this, MediaPlayer.class);
                intent.putExtra("sel_song", selectedSong);
                startActivity(intent);
            }
        });

    }


    public void button_Add_Songs(View v)
    {
        Intent intent1 = new Intent(MediaList.this, Add_Songs.class);
        startActivity(intent1);
    }


}
