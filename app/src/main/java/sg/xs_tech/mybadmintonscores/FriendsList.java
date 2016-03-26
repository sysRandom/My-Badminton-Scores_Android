package sg.xs_tech.mybadmintonscores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class FriendsList extends AppCompatActivity {

    private ListView mFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        mFriendList = (ListView) findViewById(R.id.friends_list);

        Intent intent = getIntent();
        String jsonFriends = intent.getStringExtra("fbFriends");
    }
}
