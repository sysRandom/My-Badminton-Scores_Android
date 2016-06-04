package sg.xs_tech.mybadmintonscores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        final ListView mFriendList = (ListView) findViewById(R.id.friends_list);

        Intent intent = getIntent();
        ArrayList<Friend> friends = intent.getParcelableArrayListExtra("fbFriends");

        final FriendAdapter adapter = new FriendAdapter(this, friends);
        mFriendList.setAdapter(adapter);
        mFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend selected = (Friend) parent.getItemAtPosition(position);
                Intent _result = new Intent();
                _result.putExtra("friend", selected);
                setResult(Activity.RESULT_OK, _result);
                finish();
            }
        });
    }
}
