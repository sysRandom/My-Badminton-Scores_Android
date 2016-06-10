package sg.xs_tech.mybadmintonscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MatchDetailActions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra("isCreator") && intent.getBooleanExtra("isCreator",false) == true) {
            setContentView(R.layout.activity_match_detail_actions);
        }
        else {
            setContentView(R.layout.activity_match_detail_view);
        }
        Match mMatch = intent.getParcelableExtra("match");
    }
}
