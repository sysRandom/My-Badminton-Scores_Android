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

import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class MatchDetailActions extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    static final int PICK_PLAYERS = 1;
    private final Calendar calendar = Calendar.getInstance();
    private Button btnDatePicker;
    private EditText etTeamScore;
    private EditText etOpponentScore;
    private Integer mMatchType = 0;
    private Integer mTeamScore = 0;
    private Integer mOpponentScore = 0;
    private JSONObject mSelectedPlayers = new JSONObject();
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail_actions);
        Intent intent = getIntent();
        final Match mMatch = intent.getParcelableExtra("match");
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        btnDatePicker.setText(String.format(Locale.getDefault(), "%d-%d-%d", dayOfMonth, monthOfYear, year));
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            Log.i(this.toString(), "onCreateDialog: " + day + " " + month + " " + year);
            return new DatePickerDialog(getActivity(), (NewMatch) getActivity(), year, month, day);
        }
    }
}
