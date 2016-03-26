package sg.xs_tech.mybadmintonscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SelectPlayers extends AppCompatActivity {

    private Button mTeamPlayer1;
    private Button mTeamPlayer2;

    private Button mFbTeamPlayer1;
    private Button mFbTeamPlayer2;

    private Button mOpponentPlayer1;
    private Button mOpponentPlayer2;

    private Button mFbOpponentPlayer1;
    private Button mFbOpponentPlayer2;

    private Button mSubmitPlayer;
    private TextView mFriendStats;

    private JSONArray mFriendsList = new JSONArray();
    private ArrayList<String> mFriends = new ArrayList<String>();
//    private Integer mMatchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);

        Intent intent = getIntent();
        Integer mMatchType = intent.getIntExtra("match_type", 0);
        final String mFriendsString = intent.getStringExtra("friends"); // response.getJSONObject().getJSONArray("data");
        try {
            mFriendsList = new JSONArray(mFriendsString);
            for (int i=0; i < mFriendsList.length(); i++) {
                mFriends.add(mFriendsList.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(this.toString(), "Match Type: " + mMatchType);

        mTeamPlayer1 = (Button) findViewById(R.id.team_player1);
        mFbTeamPlayer1 = (Button) findViewById(R.id.fb_team_player1);
        mOpponentPlayer1 = (Button) findViewById(R.id.opponent_player1);
        mFbOpponentPlayer1 = (Button) findViewById(R.id.fb_opponent_player1);

        mSubmitPlayer = (Button) findViewById(R.id.submit_players);
        mFriendStats = (TextView) findViewById(R.id.friend_stats);

        if (mMatchType == 1) {
            // Doubles
            mTeamPlayer2 = (Button) findViewById(R.id.team_player2);
            mFbTeamPlayer2 = (Button) findViewById(R.id.fb_team_player2);
            mOpponentPlayer2 = (Button) findViewById(R.id.opponent_player2);
            mFbOpponentPlayer2 = (Button) findViewById(R.id.fb_opponent_player2);
            if (mFriendsList.length() < 1) {
                mFbTeamPlayer1.setVisibility(View.GONE);
                mFbTeamPlayer2.setVisibility(View.GONE);
                mFbOpponentPlayer1.setVisibility(View.GONE);
                mFbOpponentPlayer2.setVisibility(View.GONE);
            }
            else {
                mTeamPlayer2.setVisibility(View.VISIBLE);
                mFbTeamPlayer2.setVisibility(View.VISIBLE);
                mOpponentPlayer2.setVisibility(View.VISIBLE);
                mFbOpponentPlayer2.setVisibility(View.VISIBLE);
            }
        }
        else {
            // Singles
            mTeamPlayer2 = (Button) findViewById(R.id.team_player2);
            mFbTeamPlayer2 = (Button) findViewById(R.id.fb_team_player2);
            mOpponentPlayer2 = (Button) findViewById(R.id.opponent_player2);
            mFbOpponentPlayer2 = (Button) findViewById(R.id.fb_opponent_player2);

            mTeamPlayer2.setVisibility(View.GONE);
            mOpponentPlayer2.setVisibility(View.GONE);
            mFbTeamPlayer2.setVisibility(View.GONE);
            mFbOpponentPlayer2.setVisibility(View.GONE);

            if (mFriendsList.length() < 1) {
                mFbTeamPlayer1.setVisibility(View.GONE);
                mFbOpponentPlayer1.setVisibility(View.GONE);
            }
        }
    }
}
