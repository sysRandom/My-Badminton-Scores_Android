package sg.xs_tech.mybadmintonscores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectPlayers extends AppCompatActivity {

    static final int FB_TEAM_PLAYER1 = 1;
    static final int FB_TEAM_PLAYER2 = 2;
    static final int FB_OPPONENT_PLAYER1 = 3;
    static final int FB_OPPONENT_PLAYER2 = 4;

    private EditText etName;
    private AlertDialog.Builder mPlayerName;
    private View player_name_dialog;
    private Integer mMatchType;

    private ArrayList<Friend> mFriendsList = new ArrayList<>();
    private final JSONObject mPlayerList = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        player_name_dialog = layoutInflater.inflate(R.layout.player_name_dialog, null);
        mPlayerName = new AlertDialog.Builder(this);
        etName = (EditText) player_name_dialog.findViewById(R.id.player_name);

        Intent intent = getIntent();
        mMatchType = intent.getIntExtra("match_type", 0);
        mPlayerName.setView(player_name_dialog);
        final Integer mFriendsCount = intent.getIntExtra("friends_count", 0);
//        Log.i(this.toString(), "Match Type: " + mMatchType);

        final Button mFbTeamPlayer1 = (Button) findViewById(R.id.fb_team_player1);
        final Button mFbOpponentPlayer1 = (Button) findViewById(R.id.fb_opponent_player1);

        final Button mTeamPlayer2 = (Button) findViewById(R.id.team_player2);
        final Button mFbTeamPlayer2 = (Button) findViewById(R.id.fb_team_player2);
        final Button mOpponentPlayer2 = (Button) findViewById(R.id.opponent_player2);
        final Button mFbOpponentPlayer2 = (Button) findViewById(R.id.fb_opponent_player2);

        if (mMatchType == 1) {
            // Doubles
            if (mFriendsCount < 1) {
                mFbTeamPlayer1.setVisibility(View.GONE);
                mFbTeamPlayer2.setVisibility(View.GONE);
                mFbOpponentPlayer1.setVisibility(View.GONE);
                mFbOpponentPlayer2.setVisibility(View.GONE);
            } else {
                mTeamPlayer2.setVisibility(View.VISIBLE);
                mFbTeamPlayer2.setVisibility(View.VISIBLE);
                mOpponentPlayer2.setVisibility(View.VISIBLE);
                mFbOpponentPlayer2.setVisibility(View.VISIBLE);
            }
        } else {
            // Singles
            mTeamPlayer2.setVisibility(View.GONE);
            mOpponentPlayer2.setVisibility(View.GONE);
            mFbTeamPlayer2.setVisibility(View.GONE);
            mFbOpponentPlayer2.setVisibility(View.GONE);

            if (mFriendsCount < 1) {
                mFbTeamPlayer1.setVisibility(View.GONE);
                mFbOpponentPlayer1.setVisibility(View.GONE);
            }
        }
    }

    private void getFacebookFriends(final int player) {
        mFriendsList.clear();
        final Profile profile = Profile.getCurrentProfile();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
//                        Log.i(this.toString(), "Friends List Result: " + response.toString());
                        try {
                            final JSONArray mFriends = response.getJSONObject().getJSONArray("data");
                            JSONObject mSummary = response.getJSONObject().getJSONObject("summary");
//                            Log.i(this.toString(), "Friends List Array: " + mFriends.toString());
//                            Log.i(this.toString(), "Friends List Summary: " + mSummary.getInt("total_count"));
//                            Log.i(this.toString(), String.format(
//                                    "Avail friends: %d, total friends: %d",
//                                    mFriends.length(), mSummary.getInt("total_count")
//                            ));

                            /**
                             * This loop checks for ID or Names previously selected does not gets
                             * added in again.
                             */

                            // Adds user into 1st on the list
//                            Log.i(this.toString(), "Adding myself to list");
                            Boolean has_me = false;
                            if (mPlayerList.has("TeamPlayer1") &&
                                    player != FB_TEAM_PLAYER1 &&
                                    (mPlayerList.getJSONObject("TeamPlayer1").has("id") &&
                                            mPlayerList.getJSONObject("TeamPlayer1").getString("id").equalsIgnoreCase(profile.getId()))) {
                                has_me = true;
                            }
                            if (mPlayerList.has("TeamPlayer2") &&
                                    player != FB_TEAM_PLAYER2 &&
                                    (mPlayerList.getJSONObject("TeamPlayer2").has("id") &&
                                            mPlayerList.getJSONObject("TeamPlayer2").getString("id").equalsIgnoreCase(profile.getId()))) {
                                has_me = true;
                            }
                            if (mPlayerList.has("OpponentPlayer1") &&
                                    player != FB_OPPONENT_PLAYER1 &&
                                    (mPlayerList.getJSONObject("OpponentPlayer1").has("id") &&
                                            mPlayerList.getJSONObject("OpponentPlayer1").getString("id").equalsIgnoreCase(profile.getId()))) {
                                has_me = true;
                            }
                            if (mPlayerList.has("OpponentPlayer2") &&
                                    player != FB_OPPONENT_PLAYER2 &&
                                    (mPlayerList.getJSONObject("OpponentPlayer2").has("id") &&
                                            mPlayerList.getJSONObject("OpponentPlayer2").getString("id").equalsIgnoreCase(profile.getId()))) {
                                has_me = true;
                            }
                            if (!has_me) {
                                final Friend me = new Friend(profile.getId(), profile.getName());
                                mFriendsList.add(me);
                            }

                            for (int i = 0; i < mFriends.length(); ++i) {

                                JSONObject friend = mFriends.getJSONObject(i);
                                final Friend mFriend = new Friend(friend.getString("id"), friend.getString("name"));
                                if (friend.has("email") && !friend.getString("email").isEmpty()) {
                                    mFriend.setEmail(friend.getString("email"));
                                }
                                // Ensure that current friend isn't already inside mPlayerList
                                if (mPlayerList.has("TeamPlayer1") &&
                                        player != FB_TEAM_PLAYER1 &&
                                        (mPlayerList.getJSONObject("TeamPlayer1").has("id") &&
                                                mPlayerList.getJSONObject("TeamPlayer1").getString("id").equalsIgnoreCase(mFriend.getId()))) {
//                                    Log.i(this.toString(), String.format(Locale.getDefault(), "Team Player 1: %s, ID: %s",
//                                            mPlayerList.getJSONObject("TeamPlayer1").getString("name"),
//                                            mPlayerList.getJSONObject("TeamPlayer1").getString("id")));
                                    continue;
                                }
                                if (mPlayerList.has("TeamPlayer2") &&
                                        player != FB_TEAM_PLAYER2 &&
                                        (mPlayerList.getJSONObject("TeamPlayer2").has("id") &&
                                                mPlayerList.getJSONObject("TeamPlayer2").getString("id").equalsIgnoreCase(mFriend.getId()))) {
//                                    Log.i(this.toString(), String.format(Locale.getDefault(), "Team Player 2: %s, ID: %s",
//                                            mPlayerList.getJSONObject("TeamPlayer2").getString("name"),
//                                            mPlayerList.getJSONObject("TeamPlayer2").getString("id")));
                                    continue;
                                }
                                if (mPlayerList.has("OpponentPlayer1") &&
                                        player != FB_OPPONENT_PLAYER1 &&
                                        (mPlayerList.getJSONObject("OpponentPlayer1").has("id") &&
                                                mPlayerList.getJSONObject("OpponentPlayer1").getString("id").equalsIgnoreCase(mFriend.getId()))) {
//                                    Log.i(this.toString(), String.format(Locale.getDefault(), "Opponent Player 1: %s, ID: %s",
//                                            mPlayerList.getJSONObject("OpponentPlayer1").getString("name"),
//                                            mPlayerList.getJSONObject("OpponentPlayer1").getString("id")));
                                    continue;
                                }
                                if (mPlayerList.has("OpponentPlayer2") &&
                                        player != FB_OPPONENT_PLAYER2 &&
                                        (mPlayerList.getJSONObject("OpponentPlayer2").has("id") &&
                                                mPlayerList.getJSONObject("OpponentPlayer2").getString("id").equalsIgnoreCase(mFriend.getId()))) {
//                                    Log.i(this.toString(), String.format(Locale.getDefault(), "Opponent Player 2: %s, ID: %s",
//                                            mPlayerList.getJSONObject("OpponentPlayer2").getString("name"),
//                                            mPlayerList.getJSONObject("OpponentPlayer2").getString("id")));
                                    continue;
                                }
//                                Log.i(this.toString(), "Adding friend: " + mFriend.getFname());
                                mFriendsList.add(mFriend);
                            }
//                            Log.i(this.toString(), "getFacebookFriends List: " + mFriendsList.toString());
                            Intent intent = new Intent(SelectPlayers.this, FriendsList.class);
                            intent.putExtra("fbFriends", mFriendsList);
                            startActivityForResult(intent, player);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Friend selected = data.getParcelableExtra("friend");
                String the_player = "";
//                Log.i(this.toString(), String.format(Locale.getDefault(), "ID: %s, Name: %s", selected.getId(), selected.getFname()));
                switch (requestCode) {
                    case FB_TEAM_PLAYER1:
                        the_player = "TeamPlayer1";
                        break;
                    case FB_TEAM_PLAYER2:
                        the_player = "TeamPlayer2";
                        break;
                    case FB_OPPONENT_PLAYER1:
                        the_player = "OpponentPlayer1";
                        break;
                    case FB_OPPONENT_PLAYER2:
                        the_player = "OpponentPlayer2";
                        break;
                }
                JSONObject myFriend = new JSONObject();
                myFriend.put("id", selected.getId());
                myFriend.put("name", selected.getFname());
                mPlayerList.put(the_player, myFriend);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSelectTeamPlayer1(View view) {
        try {
            etName.setText("");
            if (mPlayerList.has("TeamPlayer1")) {
                if (!mPlayerList.getJSONObject("TeamPlayer1").getString("name").isEmpty()) {
                    etName.setText(String.format("%s", mPlayerList.getJSONObject("TeamPlayer1").getString("name")));
                }
            }
            mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (mPlayerList.has("TeamPlayer1")) {
                            if (etName.getText().length() > 0) {
//                                Log.i(this.toString(), "Team Player 1 name is " + etName.getText().toString());
                                if (!etName.getText().toString().equalsIgnoreCase(mPlayerList.getJSONObject("TeamPlayer1").getString("name"))) {
                                    JSONObject details = new JSONObject();
                                    details.put("name", etName.getText().toString());
                                    mPlayerList.put("TeamPlayer1", details);
                                }
                                etName.setText("");
                            } else {
//                                Log.i(this.toString(), "Team Player 1 has no name");
                                mPlayerList.remove("TeamPlayer1");
                            }
                        } else {
                            if (etName.getText().length() > 0) {
//                                Log.i(this.toString(), "Team Player 1 name is " + etName.getText().toString());
                                JSONObject details = new JSONObject();
                                details.put("name", etName.getText().toString());
                                mPlayerList.put("TeamPlayer1", details);
                                etName.setText("");
                            }
                        }
                        ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Log.i(this.toString(), "Team Player 1 name cancelled");
                    ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                }
            });
            final AlertDialog playerDialog = mPlayerName.create();
            playerDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onSelectFbTeamPlayer1(View view) {
        try {
            getFacebookFriends(FB_TEAM_PLAYER1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSelectTeamPlayer2(View view) {
        try {
            etName.setText("");
            if (mPlayerList.has("TeamPlayer2")) {
                if (!mPlayerList.getJSONObject("TeamPlayer2").getString("name").isEmpty()) {
                    etName.setText(String.format("%s", mPlayerList.getJSONObject("TeamPlayer2").getString("name")));
                }
            }
            mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (mPlayerList.has("TeamPlayer2")) {
                            if (etName.getText().length() > 0) {
//                                Log.i(this.toString(), "Team Player 2 name is " + etName.getText().toString());
                                if (!etName.getText().toString().equalsIgnoreCase(mPlayerList.getJSONObject("TeamPlayer2").getString("name"))) {
                                    JSONObject details = new JSONObject();
                                    details.put("name", etName.getText().toString());
                                    mPlayerList.put("TeamPlayer2", details);
                                }
                                etName.setText("");
                            } else {
//                                Log.i(this.toString(), "Team Player 2 has no name");
                                mPlayerList.remove("TeamPlayer2");
                            }
                        } else {
                            if (etName.getText().length() > 0) {
//                                Log.i(this.toString(), "Team Player 2 name is " + etName.getText().toString());
                                JSONObject details = new JSONObject();
                                details.put("name", etName.getText().toString());
                                mPlayerList.put("TeamPlayer2", details);
                                etName.setText("");
                            }
                        }
                        ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Log.i(this.toString(), "Team Player 2 name cancelled");
                    ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                }
            });
            final AlertDialog playerDialog = mPlayerName.create();
            playerDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onSelectFbTeamPlayer2(View view) {
        try {
            getFacebookFriends(FB_TEAM_PLAYER2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSelectOpponentPlayer1(View view) {
        try {
            etName.setText("");
            if (mPlayerList.has("OpponentPlayer1")) {
                if (!mPlayerList.getJSONObject("OpponentPlayer1").getString("name").isEmpty()) {
                    etName.setText(String.format("%s", mPlayerList.getJSONObject("OpponentPlayer1").getString("name")));
                }
            }
            mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (mPlayerList.has("OpponentPlayer1")) {
                            if (etName.getText().length() > 0) {
//                                Log.i(this.toString(), "Opponent Player 1 name is " + etName.getText().toString());
                                if (!etName.getText().toString().equalsIgnoreCase(mPlayerList.getJSONObject("OpponentPlayer1").getString("name"))) {
                                    JSONObject details = new JSONObject();
                                    details.put("name", etName.getText().toString());
                                    mPlayerList.put("OpponentPlayer1", details);
                                }
                                etName.setText("");
                            } else {
//                                Log.i(this.toString(), "Opponent Player 1 has no name");
                                mPlayerList.remove("OpponentPlayer1");
                            }
                        } else {
                            if (etName.getText().length() > 0) {
//                                Log.i(this.toString(), "Opponent Player 1 name is " + etName.getText().toString());
                                JSONObject details = new JSONObject();
                                details.put("name", etName.getText().toString());
                                mPlayerList.put("OpponentPlayer1", details);
                                etName.setText("");
                            }
                        }
                        ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Log.i(this.toString(), "Opponent Player 1 name cancelled");
                    ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                }
            });
            final AlertDialog playerDialog = mPlayerName.create();
            playerDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onSelectFbOpponentPlayer1(View view) {
        try {
            getFacebookFriends(FB_OPPONENT_PLAYER1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSelectOpponentPlayer2(View view) {
        try {
            etName.setText("");
            if (mPlayerList.has("OpponentPlayer2")) {
                if (!mPlayerList.getJSONObject("OpponentPlayer2").getString("name").isEmpty()) {
                    etName.setText(String.format("%s", mPlayerList.getJSONObject("OpponentPlayer2").getString("name")));
                }
            }
            mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (mPlayerList.has("OpponentPlayer2")) {
                            if (etName.getText().length() > 0) {
//                                            Log.i(this.toString(), "(281) Opponent Player 2 name is " + etName.getText().toString());
                                if (!etName.getText().toString().equalsIgnoreCase(mPlayerList.getJSONObject("OpponentPlayer2").getString("name"))) {
                                    JSONObject details = new JSONObject();
                                    details.put("name", etName.getText().toString());
                                    mPlayerList.put("OpponentPlayer2", details);
                                }
                                etName.setText("");
                            } else {
//                                Log.i(this.toString(), "Opponent Player 2 has no name");
                                mPlayerList.remove("OpponentPlayer2");
                            }
                        } else {
                            if (etName.getText().length() > 0) {
//                                            Log.i(this.toString(), "(293) Opponent Player 2 name is " + etName.getText().toString());
                                JSONObject details = new JSONObject();
                                details.put("name", etName.getText().toString());
                                mPlayerList.put("OpponentPlayer2", details);
                                etName.setText("");
                            }
                        }
                        ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Log.i(this.toString(), "Opponent Player 2 name cancelled");
                    ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                }
            });
            final AlertDialog playerDialog = mPlayerName.create();
            playerDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onSelectFbOpponentPlayer2(View view) {
        try {
            getFacebookFriends(FB_OPPONENT_PLAYER2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSubmitPlayers(View view) {
        if (mMatchType == 0) {
            if (!mPlayerList.has("TeamPlayer1")) {
                Toast.makeText(getApplicationContext(), R.string.missing_team_player1, Toast.LENGTH_LONG).show();
                return;
            }
            if (!mPlayerList.has("OpponentPlayer1")) {
                Toast.makeText(getApplicationContext(), R.string.missing_opponent_player1, Toast.LENGTH_LONG).show();
                return;
            }
        }
        else {
            if (!mPlayerList.has("TeamPlayer1")) {
                Toast.makeText(getApplicationContext(), R.string.missing_team_player1, Toast.LENGTH_LONG).show();
                return;
            }
            if (!mPlayerList.has("TeamPlayer2")) {
                Toast.makeText(getApplicationContext(), R.string.missing_team_player2, Toast.LENGTH_LONG).show();
                return;
            }
            if (!mPlayerList.has("OpponentPlayer1")) {
                Toast.makeText(getApplicationContext(), R.string.missing_opponent_player1, Toast.LENGTH_LONG).show();
                return;
            }
            if (!mPlayerList.has("OpponentPlayer2")) {
                Toast.makeText(getApplicationContext(), R.string.missing_opponent_player2, Toast.LENGTH_LONG).show();
                return;
            }
        }
        Intent _return = new Intent();
        _return.putExtra("Players", mPlayerList.toString());
        setResult(Activity.RESULT_OK, _return);
        finish();
    }
}
