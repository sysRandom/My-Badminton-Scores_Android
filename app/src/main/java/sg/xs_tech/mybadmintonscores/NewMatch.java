package sg.xs_tech.mybadmintonscores;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class NewMatch extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    static final int PICK_PLAYERS = 1;

    private Button btnDatePicker;

    private EditText etTeamScore;
    private EditText etOpponentScore;

    private Integer mMatchType = 0;
    private Integer mTeamScore = 0;
    private Integer mOpponentScore = 0;

    private JSONObject mSelectedPlayers = new JSONObject();
    private AccessToken accessToken;

    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        btnDatePicker = (Button) findViewById(R.id.date_picker);
        etTeamScore = (EditText) findViewById(R.id.team_score);
        etOpponentScore = (EditText) findViewById(R.id.opponent_score);

        accessToken = AccessToken.getCurrentAccessToken();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        btnDatePicker.setText(String.format(Locale.getDefault(),"%d-%d-%d",day,month,year));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PLAYERS) {
                try {
                    mSelectedPlayers = new JSONObject(data.getStringExtra("Players"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showDatePickerDialog(View view) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    public void onMatchTypeSelected(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_2v2:
                if (checked) {
//                    Log.i(this.toString(), "Match type: Doubles");
                    mMatchType = 1;
                }
                break;
            default:
//                Log.i(this.toString(), "Match type: Singles");
                mMatchType = 0;
                break;
        }
    }

    public void onSubmitMatch(View view) {
        try {
            final JSONObject queryData = new JSONObject();
            mTeamScore = 0;
            mOpponentScore = 0;
            if (etTeamScore.getText().toString().trim().length() > 0) {
                mTeamScore = Integer.parseInt(etTeamScore.getText().toString());
            }
            if (etOpponentScore.getText().toString().trim().length() > 0) {
                mOpponentScore = Integer.parseInt(etOpponentScore.getText().toString());
            }
            queryData.put("fb_app_id", getResources().getString(R.string.facebook_app_id));
            queryData.put("fb_id", accessToken.getUserId());
            queryData.put("token", accessToken.getToken());
            queryData.put("match_date", btnDatePicker.getText().toString());
            queryData.put("game_type", mMatchType);
            queryData.put("team_score", mTeamScore);
            queryData.put("opponent_score", mOpponentScore);
            if (mSelectedPlayers.has("TeamPlayer1")) {
                if (mSelectedPlayers.getJSONObject("TeamPlayer1").has("id")) {
                    queryData.put("team_player1", mSelectedPlayers.getJSONObject("TeamPlayer1").getString("id"));
                }
                else {
                    queryData.put("team_player1", mSelectedPlayers.getJSONObject("TeamPlayer1").getString("name"));
                }
            }
            if (mSelectedPlayers.has("TeamPlayer2")) {
                if (mSelectedPlayers.getJSONObject("TeamPlayer2").has("id")) {
                    queryData.put("team_player2", mSelectedPlayers.getJSONObject("TeamPlayer2").getString("id"));
                }
                else {
                    queryData.put("team_player2", mSelectedPlayers.getJSONObject("TeamPlayer2").getString("name"));
                }
            }
            if (mSelectedPlayers.has("OpponentPlayer1")) {
                if (mSelectedPlayers.getJSONObject("OpponentPlayer1").has("id")) {
                    queryData.put("opponent_player1", mSelectedPlayers.getJSONObject("OpponentPlayer1").getString("id"));
                }
                else {
                    queryData.put("opponent_player1", mSelectedPlayers.getJSONObject("OpponentPlayer1").getString("name"));
                }
            }
            if (mSelectedPlayers.has("OpponentPlayer2")) {
                if (mSelectedPlayers.getJSONObject("OpponentPlayer2").has("id")) {
                    queryData.put("opponent_player2", mSelectedPlayers.getJSONObject("OpponentPlayer2").getString("id"));
                }
                else {
                    queryData.put("opponent_player2", mSelectedPlayers.getJSONObject("OpponentPlayer2").getString("name"));
                }
            }
//                    Log.i(this.toString(), "Query string: " + queryData.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.post_match_api_url),
                    queryData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                            Log.i(this.toString(), "Post Match Response: " + response.toString());
                    Toast.makeText(NewMatch.this, getResources().getString(R.string.add_match_submit_successful), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                            final JSONObject ErrMsgFull = new JSONObject(volleyError.getMessage());
                            final String MsgFull = ErrMsgFull.getJSONObject("error").getString("message");
                            final String Msg = MsgFull.substring(MsgFull.indexOf(":") + 1).trim();
                            showToastError(Msg);
//                            Log.i(this.toString(), "Response error message: " + volleyError.getMessage());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onSelectPlayer(View view) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
//                        Log.i(this.toString(), "Friends List Result: " + response.toString());
                        try {
//                            JSONArray mFriends = response.getJSONObject().getJSONArray("data");
                            JSONObject mSummary = response.getJSONObject().getJSONObject("summary");
                            Intent intent = new Intent(NewMatch.this, SelectPlayers.class);
                            intent.putExtra("match_type", mMatchType);
                            intent.putExtra("friends_count", mSummary.getInt("total_count"));
                            startActivityForResult(intent, PICK_PLAYERS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        Log.i(this.toString(), "onDateSet: " + dayOfMonth + " " + monthOfYear + " " + year);
        btnDatePicker.setText(String.format(Locale.getDefault(),"%d-%d-%d", dayOfMonth, monthOfYear, year));
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            Log.i(this.toString(), "onCreateDialog: " + day + " " + month + " " + year);
            return new DatePickerDialog(getActivity(),(NewMatch)getActivity(),year,month,day);
        }
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
        Toast.makeText(NewMatch.this, msg, Toast.LENGTH_SHORT).show();
    }
}

