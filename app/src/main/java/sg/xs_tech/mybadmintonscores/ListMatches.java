package sg.xs_tech.mybadmintonscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListMatches extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_matches);
        swipeMatches = (SwipeRefreshLayout) findViewById(R.id.swipeMatches);
        swipeMatches.setOnRefreshListener(this);
        getMatches();
    }

    private void getMatches() {
        final ListView lvMatchList = (ListView) findViewById(R.id.match_list);
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final String queryString = "?fb_id=" + accessToken.getUserId() + "&token=" + accessToken.getToken() + "&fb_app_id=" + getString(R.string.facebook_app_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getString(R.string.get_matches_api_url) + queryString,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.i(this.toString(), "Get match list: " + response.toString());
                        try {
                            final JSONArray matches = response.getJSONArray("result");
                            final ArrayList<Match> mMatchList = new ArrayList<>();
                            mMatchList.clear();
                            int cnt = matches.length();
                            for (int i = 0; i < cnt; ++i) {
                                JSONObject match = matches.getJSONObject(i);
                                Friend mPoster;
                                Friend mTeamPlayer1;
                                Friend mTeamPlayer2 = null;
                                Friend mOpponentPlayer1;
                                Friend mOpponentPlayer2 = null;
                                final int mTeamScore = match.getInt("team_score");
                                final int mOpponentScore = match.getInt("opponent_score");
                                if (match.getJSONObject("poster").has("fb_id")) {
                                    mPoster = new Friend(
                                            match.getJSONObject("poster").getString("fb_id"),
                                            match.getJSONObject("poster").getString("fb_name")
                                    );
                                } else {
                                    mPoster = new Friend(
                                            match.getJSONObject("poster").getString("fb_name")
                                    );
                                }

                                if (match.get("team_player1") instanceof String) {
                                    mTeamPlayer1 = new Friend(
                                            match.getString("team_player1")
                                    );
                                }
                                else {
                                    mTeamPlayer1 = new Friend(
                                            match.getJSONObject("team_player1").getString("fb_id"),
                                            match.getJSONObject("team_player1").getString("fb_name")
                                    );
                                }

                                if (match.has("team_player2")) {
                                    if (match.get("team_player2") instanceof String) {
                                        mTeamPlayer2 = new Friend(
                                                match.getString("team_player2")
                                        );
                                    }
                                    else {
                                        mTeamPlayer2 = new Friend(
                                                match.getJSONObject("team_player2").getString("fb_id"),
                                                match.getJSONObject("team_player2").getString("fb_name")
                                        );
                                    }
                                }

                                if (match.get("opponent_player1") instanceof String) {
                                    mOpponentPlayer1 = new Friend(
                                            match.getString("opponent_player1")
                                    );
                                }
                                else {
                                    mOpponentPlayer1 = new Friend(
                                            match.getJSONObject("opponent_player1").getString("fb_id"),
                                            match.getJSONObject("opponent_player1").getString("fb_name")
                                    );
                                }

                                if (match.has("opponent_player2")) {
                                    if (match.get("opponent_player2") instanceof String) {
                                        mOpponentPlayer2 = new Friend(
                                                match.getString("opponent_player2")
                                        );
                                    }
                                    else {
                                        mOpponentPlayer2 = new Friend(
                                                match.getJSONObject("opponent_player2").getString("fb_id"),
                                                match.getJSONObject("opponent_player2").getString("fb_name")
                                        );
                                    }

                                }
                                final Match mMatch = new Match(
                                        match.getString("id"), mTeamPlayer1, mTeamPlayer2, mTeamScore, mOpponentPlayer1,
                                        mOpponentPlayer2, mOpponentScore, mPoster, match.getString("match_date"),
                                        match.getString("create_date"), match.getString("modify_date")
                                );
                                mMatchList.add(mMatch);
                                final MatchAdapter matchAdapter = new MatchAdapter(getApplicationContext(), mMatchList);
                                lvMatchList.setAdapter(matchAdapter);
                                lvMatchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Match selected = (Match) parent.getItemAtPosition(position);
                                        Intent intent = new Intent(ListMatches.this, MatchDetailActions.class);
                                        intent.putExtra("match", selected);
                                        if (selected.getPoster().getId().equalsIgnoreCase(accessToken.getUserId())) {
                                            intent.putExtra("isCreator", true);
                                        }
                                        startActivity(intent);
                                    }
                                });
                            }
                            swipeMatches.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                                final JSONObject ErrMsgFull = new JSONObject(volleyError.getMessage());
                                final String MsgFull = ErrMsgFull.getJSONObject("error").getString("message");
                                final String Msg = MsgFull.substring(MsgFull.indexOf(":") + 1).trim();
                                showToastError(Msg);
//                                Log.i(this.toString(), "Response error message: " + volleyError.getMessage());
                            }
                            swipeMatches.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

    private void showToastError(final String error) {
        String msg;
        switch (error) {
            case "missing_fb_app_id":
                msg = getString(R.string.missing_fb_app_id);
                break;
            case "missing_token":
                msg = getString(R.string.missing_token);
                break;
            case "missing_identification":
                msg = getString(R.string.missing_identification);
                break;
            case "invalid_score":
                msg = getString(R.string.invalid_score);
                break;
            case "invalid_team_score":
                msg = getString(R.string.invalid_team_score);
                break;
            case "invalid_opponent_score":
                msg = getString(R.string.invalid_opponent_score);
                break;
            case "not_a_member":
                msg = getString(R.string.not_a_member);
                break;
            case "token_mismatch":
                msg = getString(R.string.token_mismatch);
                break;
            case "missing_team_player1":
                msg = getString(R.string.missing_team_player1);
                break;
            case "missing_team_player2":
                msg = getString(R.string.missing_team_player2);
                break;
            case "missing_opponent_player1":
                msg = getString(R.string.missing_opponent_player1);
                break;
            case "missing_opponent_player2":
                msg = getString(R.string.missing_opponent_player2);
                break;
            case "missing_team_player_details":
                msg = getString(R.string.missing_team_player_details);
                break;
            case "missing_opponent_details":
                msg = getString(R.string.missing_opponent_details);
                break;
            case "facebook_graph_error":
                msg = getString(R.string.facebook_graph_error);
                break;
            case "facebook_error":
                msg = getString(R.string.facebook_error);
                break;
            default:
                msg = getString(R.string.server_error);
        }
        Toast.makeText(ListMatches.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        getMatches();
    }
}
