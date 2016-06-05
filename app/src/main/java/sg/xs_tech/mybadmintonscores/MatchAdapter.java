package sg.xs_tech.mybadmintonscores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MatchAdapter extends ArrayAdapter<Match> {
    public MatchAdapter(Context context, ArrayList<Match> matches) {
        super(context, 0, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Match selected = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_matches_detail, parent, false);
        }
        final TextView mMatchDate = (TextView) convertView.findViewById(R.id.match_date);
        final TextView mTeamPlayer1 = (TextView) convertView.findViewById(R.id.match_team_player1);
        final TextView mTeamPlayer2 = (TextView) convertView.findViewById(R.id.match_team_player2);
        final TextView mOpponentPlayer1 = (TextView) convertView.findViewById(R.id.match_opponent_player1);
        final TextView mOpponentPlayer2 = (TextView) convertView.findViewById(R.id.match_opponent_player2);

        mMatchDate.setText(String.format(Locale.getDefault(), "Match Date: %s", selected.getMatch_date()));
        mTeamPlayer1.setText(String.format(Locale.getDefault(), "Team Player 1: %s", selected.getTeam_player1().getFname()));
        mOpponentPlayer1.setText(String.format(Locale.getDefault(), "Opponent Player 1: %s", selected.getOpponent_player1().getFname()));
        if (selected.getTeam_player2() != null) {
            mTeamPlayer2.setText(String.format(Locale.getDefault(), "Team Player 2: %s", selected.getTeam_player2().getFname()));
            mTeamPlayer2.setVisibility(View.VISIBLE);
        }
        if (selected.getOpponent_player2() != null) {
            mOpponentPlayer2.setText(String.format(Locale.getDefault(), "Opponent Player 2: %s", selected.getOpponent_player2().getFname()));
            mOpponentPlayer2.setVisibility(View.VISIBLE);
        }

//        Log.i(this.toString(),"Loading match: " + selected.getId());
        return convertView;
    }
}
