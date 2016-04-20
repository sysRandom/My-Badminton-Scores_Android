package sg.xs_tech.mybadmintonscores;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class NewMatch extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    static final int PICK_PLAYERS = 1;

    private Button btnDatePicker;
    private Button btnSubmitMatch;
    private Button btnSelectPlayers;

    private EditText etTeamScore;
    private EditText etOpponentScore;

    private Integer mMatchType = 0;
    private Integer mTeamScore = 0;
    private Integer mOpponentScore = 0;

    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        btnDatePicker = (Button) findViewById(R.id.date_picker);
        btnSubmitMatch = (Button) findViewById(R.id.submit_match);
        btnSelectPlayers = (Button) findViewById(R.id.select_players);
        etTeamScore = (EditText) findViewById(R.id.team_score);
        etOpponentScore = (EditText) findViewById(R.id.opponent_score);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        btnDatePicker.setText(String.format("%d-%d-%d",day,month,year));
        btnSubmitMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeamScore = 0;
                mOpponentScore = 0;
                if (etTeamScore.getText().toString().trim().length() > 0) {
                    mTeamScore = Integer.parseInt(etTeamScore.getText().toString());
                }
                if (etOpponentScore.getText().toString().trim().length() > 0) {
                    mOpponentScore = Integer.parseInt(etOpponentScore.getText().toString());
                }
                Log.i(this.toString(), String.format(
                        "Match date: %s, Match type: %d, Team score: %d, Opponent score: %d",
                        btnDatePicker.getText(), mMatchType, mTeamScore, mOpponentScore
                ));
            }
        });
        btnSelectPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                Log.i(this.toString(), "Friends List Result: " + response.toString());
                                try {
                                    JSONArray mFriends = response.getJSONObject().getJSONArray("data");
                                    JSONObject mSummary = response.getJSONObject().getJSONObject("summary");
                                    Log.i(this.toString(), "Friends List Array: " + mFriends.toString());
                                    Log.i(this.toString(), "Friends List Summary: " + mSummary.getInt("total_count"));
                                    Log.i(this.toString(), String.format(
                                            "Avail friends: %d, total friends: %d",
                                            mFriends.length(), mSummary.getInt("total_count")
                                    ));
                                    Intent intent = new Intent(NewMatch.this, SelectPlayers.class);
                                    intent.putExtra("match_type", mMatchType);
                                    intent.putExtra("friends", mFriends.toString());
                                    startActivityForResult(intent, PICK_PLAYERS);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PLAYERS) {
                Log.i(this.toString(), "");
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
                    Log.i(this.toString(), "Match type: Doubles");
                    mMatchType = 1;
                }
                break;
            default:
                Log.i(this.toString(), "Match type: Singles");
                mMatchType = 0;
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        Button btnDate = (Button) view.findViewById(R.id.date_picker);
        btnDatePicker.setText(String.format("%d-%d-%d", dayOfMonth, monthOfYear, year));
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),(NewMatch)getActivity(),year,month,day);
        }
    }
}

