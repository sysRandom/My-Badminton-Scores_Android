package sg.xs_tech.mybadmintonscores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectPlayers extends AppCompatActivity {

    static final int PICK_PLAYER = 1;

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

    private ArrayList<Friend> mFriendsList = new ArrayList<>();
    private int mFriendsCount;
    private final JSONObject mPlayerList = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View player_name_dialog = layoutInflater.inflate(R.layout.player_name_dialog, null);
        final AlertDialog.Builder mPlayerName = new AlertDialog.Builder(this);
        mPlayerName.setView(player_name_dialog);
        final EditText etName = (EditText) player_name_dialog.findViewById(R.id.player_name);

        Intent intent = getIntent();
        Integer mMatchType = intent.getIntExtra("match_type", 0);
        mFriendsCount = intent.getIntExtra("friends_count", 0);
        Log.i(this.toString(), "Match Type: " + mMatchType);

        mTeamPlayer1 = (Button) findViewById(R.id.team_player1);
        mFbTeamPlayer1 = (Button) findViewById(R.id.fb_team_player1);
        mOpponentPlayer1 = (Button) findViewById(R.id.opponent_player1);
        mFbOpponentPlayer1 = (Button) findViewById(R.id.fb_opponent_player1);

        mSubmitPlayer = (Button) findViewById(R.id.submit_players);
        mFriendStats = (TextView) findViewById(R.id.friend_stats);

        mTeamPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    etName.setText("");
                    if (mPlayerList.has("TeamPlayer1")) {
                        if (!mPlayerList.getString("TeamPlayer1").isEmpty()) {
                            etName.setText(String.format("%s", mPlayerList.getString("TeamPlayer1")));
                        }
                    }
                    mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                if (mPlayerList.has("TeamPlayer1")) {
                                    if (etName.getText().length() > 0) {
                                        Log.i(this.toString(), "Team Player 1 name is " + etName.getText().toString());
                                        if (!etName.getText().toString().equalsIgnoreCase(mPlayerList.getString("TeamPlayer1"))) {
                                            mPlayerList.put("TeamPlayer1", etName.getText().toString());
                                        }
                                        etName.setText("");
                                    } else {
                                        Log.i(this.toString(), "Team Player 1 has no name");
                                        mPlayerList.remove("TeamPlayer1");
                                    }
                                } else {
                                    if (etName.getText().length() > 0) {
                                        Log.i(this.toString(), "Team Player 1 name is " + etName.getText().toString());
                                        mPlayerList.put("TeamPlayer1", etName.getText().toString());
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
                            Log.i(this.toString(), "Team Player 1 name cancelled");
                            ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                        }
                    });
                    final AlertDialog playerDialog = mPlayerName.create();
                    playerDialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mFbTeamPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getFacebookFriends();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mOpponentPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    etName.setText("");
                    if (mPlayerList.has("OpponentPlayer1")) {
                        if (!mPlayerList.getString("OpponentPlayer1").isEmpty()) {
                            etName.setText(String.format("%s", mPlayerList.getString("OpponentPlayer1")));
                        }
                    }
                    mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                if (mPlayerList.has("OpponentPlayer1")) {
                                    if (etName.getText().length() > 0) {
                                        Log.i(this.toString(), "Opponent Player 1 name is " + etName.getText().toString());
                                        if (!etName.getText().toString().equalsIgnoreCase(mPlayerList.getString("OpponentPlayer1"))) {
                                            mPlayerList.put("OpponentPlayer1", etName.getText().toString());
                                        }
                                        etName.setText("");
//                                            ((ViewGroup)player_name_dialog.getParent()).removeAllViews();
                                    } else {
                                        Log.i(this.toString(), "Opponent Player 1 has no name");
                                        mPlayerList.remove("OpponentPlayer1");
//                                            ((ViewGroup)player_name_dialog.getParent()).removeAllViews();
                                    }
                                } else {
                                    if (etName.getText().length() > 0) {
                                        Log.i(this.toString(), "Opponent Player 1 name is " + etName.getText().toString());
                                        mPlayerList.put("OpponentPlayer1", etName.getText().toString());
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
                            Log.i(this.toString(), "Opponent Player 1 name cancelled");
                            ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                        }
                    });
                    final AlertDialog playerDialog = mPlayerName.create();
                    playerDialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mFbOpponentPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mSubmitPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (mMatchType == 1) {
            // Doubles
            mTeamPlayer2 = (Button) findViewById(R.id.team_player2);
            mFbTeamPlayer2 = (Button) findViewById(R.id.fb_team_player2);
            mOpponentPlayer2 = (Button) findViewById(R.id.opponent_player2);
            mFbOpponentPlayer2 = (Button) findViewById(R.id.fb_opponent_player2);

            mTeamPlayer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etName.setText("");
                        if (mPlayerList.has("TeamPlayer2")) {
                            if (!mPlayerList.getString("TeamPlayer2").isEmpty()) {
                                etName.setText(String.format("%s", mPlayerList.getString("TeamPlayer2")));
                            }
                        }
                        mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (mPlayerList.has("TeamPlayer2")) {
                                        if (etName.getText().length() > 0) {
                                            Log.i(this.toString(), "Team Player 2 name is " + etName.getText().toString());
                                            if (!etName.getText().toString().equalsIgnoreCase(mPlayerList.getString("TeamPlayer2"))) {
                                                mPlayerList.put("TeamPlayer2", etName.getText().toString());
                                            }
                                            etName.setText("");
                                        } else {
                                            Log.i(this.toString(), "Team Player 2 has no name");
                                            mPlayerList.remove("TeamPlayer2");
                                        }
                                    } else {
                                        if (etName.getText().length() > 0) {
                                            Log.i(this.toString(), "Team Player 2 name is " + etName.getText().toString());
                                            mPlayerList.put("TeamPlayer2", etName.getText().toString());
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
                                Log.i(this.toString(), "Team Player 2 name cancelled");
                                ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                            }
                        });
                        final AlertDialog playerDialog = mPlayerName.create();
                        playerDialog.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            mFbTeamPlayer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mOpponentPlayer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etName.setText("");
                        if (mPlayerList.has("OpponentPlayer2")) {
                            if (!mPlayerList.getString("OpponentPlayer2").isEmpty()) {
                                etName.setText(String.format("%s", mPlayerList.getString("OpponentPlayer2")));
                            }
                        }
                        mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (mPlayerList.has("OpponentPlayer2")) {
                                        if (etName.getText().length() > 0) {
                                            Log.i(this.toString(), "(281) Opponent Player 2 name is " + etName.getText().toString());
                                            if (!etName.getText().toString().equalsIgnoreCase(mPlayerList.getString("OpponentPlayer2"))) {
                                                mPlayerList.put("OpponentPlayer2", etName.getText().toString());
                                            }
                                            etName.setText("");
                                        } else {
                                            Log.i(this.toString(), "Opponent Player 2 has no name");
                                            mPlayerList.remove("OpponentPlayer2");
                                        }
                                    } else {
                                        if (etName.getText().length() > 0) {
                                            Log.i(this.toString(), "(293) Opponent Player 2 name is " + etName.getText().toString());
                                            mPlayerList.put("OpponentPlayer2", etName.getText().toString());
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
                                Log.i(this.toString(), "Opponent Player 2 name cancelled");
                                ((ViewGroup) player_name_dialog.getParent()).removeAllViews();
                            }
                        });
                        final AlertDialog playerDialog = mPlayerName.create();
                        playerDialog.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            mFbOpponentPlayer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

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
            mTeamPlayer2 = (Button) findViewById(R.id.team_player2);
            mFbTeamPlayer2 = (Button) findViewById(R.id.fb_team_player2);
            mOpponentPlayer2 = (Button) findViewById(R.id.opponent_player2);
            mFbOpponentPlayer2 = (Button) findViewById(R.id.fb_opponent_player2);

//            mTeamPlayer2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        if (mPlayerList.has("TeamPlayer2")) {
//                            if (!mPlayerList.getString("TeamPlayer2").isEmpty()) {
//                                etName.setText(String.format("%s",mPlayerList.getString("TeamPlayer2")));
//                            }
//                        }
//                        mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                try {
//                                    if (mPlayerList.has("TeamPlayer2")) {
//                                        if (etName.getText().length() > 0) {
//                                            Log.i(this.toString(), "Team Player 2 name is " + etName.getText().toString());
//                                            if (etName.getText().toString() != mPlayerList.getString("TeamPlayer2")) {
//                                                mPlayerList.put("TeamPlayer2",etName.getText().toString());
//                                            }
//                                            etName.setText("");
//                                        } else {
//                                            Log.i(this.toString(), "Team Player 2 has no name");
//                                            mPlayerList.remove("TeamPlayer2");
//                                        }
//                                    }
//                                    else {
//                                        if (etName.getText().length() > 0) {
//                                            Log.i(this.toString(), "Team Player 2 name is " + etName.getText().toString());
//                                            mPlayerList.put("TeamPlayer2",etName.getText().toString());
//                                            etName.setText("");
//                                        }
//                                    }
//                                    ((ViewGroup)player_name_dialog.getParent()).removeAllViews();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Log.i(this.toString(), "Team Player 2 name cancelled");
//                                ((ViewGroup)player_name_dialog.getParent()).removeAllViews();
//                            }
//                        });
//                        final AlertDialog playerDialog = mPlayerName.create();
//                        playerDialog.show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            mFbTeamPlayer2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
//            mOpponentPlayer2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        if (mPlayerList.has("OpponentPlayer2")) {
//                            if (!mPlayerList.getString("OpponentPlayer2").isEmpty()) {
//                                etName.setText(String.format("%s",mPlayerList.getString("OpponentPlayer2")));
//                            }
//                        }
//                        mPlayerName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                try {
//                                    if (mPlayerList.has("OpponentPlayer2")) {
//                                        if (etName.getText().length() > 0) {
//                                            Log.i(this.toString(), "(414) Opponent Player 2 name is " + etName.getText().toString());
//                                            if (etName.getText().toString() != mPlayerList.getString("OpponentPlayer2")) {
//                                                mPlayerList.put("OpponentPlayer2",etName.getText().toString());
//                                            }
//                                            etName.setText("");
//                                        } else {
//                                            Log.i(this.toString(), "Opponent Player 2 has no name");
//                                            mPlayerList.remove("OpponentPlayer2");
//                                        }
//                                    }
//                                    else {
//                                        if (etName.getText().length() > 0) {
//                                            Log.i(this.toString(),"(426) Opponent Player 2 name is " + etName.getText().toString());
//                                            mPlayerList.put("OpponentPlayer2",etName.getText().toString());
//                                            etName.setText("");
//                                        }
//                                    }
//                                    ((ViewGroup)player_name_dialog.getParent()).removeAllViews();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Log.i(this.toString(), "Opponent Player 2 name cancelled");
//                                ((ViewGroup)player_name_dialog.getParent()).removeAllViews();
//                            }
//                        });
//                        final AlertDialog playerDialog = mPlayerName.create();
//                        playerDialog.show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            mFbOpponentPlayer2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });

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

    private void getFacebookFriends() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.i(this.toString(), "Friends List Result: " + response.toString());
                        try {
                            final JSONArray mFriends = response.getJSONObject().getJSONArray("data");
                            JSONObject mSummary = response.getJSONObject().getJSONObject("summary");
                            Log.i(this.toString(), "Friends List Array: " + mFriends.toString());
                            Log.i(this.toString(), "Friends List Summary: " + mSummary.getInt("total_count"));
                            Log.i(this.toString(), String.format(
                                    "Avail friends: %d, total friends: %d",
                                    mFriends.length(), mSummary.getInt("total_count")
                            ));
                            for (int i = 0; i < mFriends.length(); ++i) {
                                JSONObject friend = mFriends.getJSONObject(i);
                                final Friend mFriend = new Friend(friend.getString("id"), friend.getString("name"));
                                if (friend.has("email") && !friend.getString("email").isEmpty()) {
                                    mFriend.setEmail(friend.getString("email"));
                                }
                                mFriendsList.add(mFriend);
                            }
                            Intent intent = new Intent(SelectPlayers.this, FriendsList.class);
                            intent.putExtra("fbFriends", mFriendsList);
                            startActivityForResult(intent, PICK_PLAYER);
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
            if (requestCode == PICK_PLAYER) {
                Log.i(this.toString(), "");
            }
        }
    }
}
