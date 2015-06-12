package com.spiel21.application.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.spiel21.application.R;
import com.spiel21.application.async.AsyncTaskGet;
import com.spiel21.application.async.Server;
import com.spiel21.application.util.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class GameResultFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AsyncTaskGet verbindung = new AsyncTaskGet();
        View rootView = inflater.inflate(R.layout.list_view_game_result, container, false);

        String result = null;
        String result2 = null;
        Server server = new Server();

        try {
            // Resource ansprechen
            //result = verbindung.execute(server.getAdresse() + "/users/").get();
            result2 = verbindung.execute(server.getAdresse() + "/result/").get();

            // aus dem String werden die Users erzeugt in eine Liste
            //ArrayList<Users> userList = Users.createUserList(result);
            ArrayList<Result> resultList = Result.createResultList(result2);
            // ein Array wird erzeugt mit der Laenge von Liste aller Users
            //String[] usersName = new String[userList.size()];
            String[] resultName = new String[resultList.size()];
            // die Daten werden von der Collection uebergeben
            // TODO: Die Collections zusammenfassen bzw. identifizieren; PICS einfuegen;
            for (int i = 0; i < resultList.size(); i++) {                //userList.size(); i++) {
                //usersName[i] = "(" + userList.get(i).getGender() + ") " + userList.get(i).getUsername() + "\n";
                //+"Gewonen: " + userList.get(i).getWin() + "\n" +
                // "Verloren: " + userList.get(i).getLoss();
                resultName[i] = "PlayerID: " + resultList.get(i).getPlayer_id() + "\n"
                        + "MatchID: " + resultList.get(i).getMatch_id() + "\n"
                        + "Gewonnen: " + resultList.get(i).getWin() + "\n"
                        + "Verloren: " + resultList.get(i).getLose() + "\n";
            }
            // Erstelle ein ArrayList fuer den Adapter
            ArrayList<String> listeUser = new ArrayList<String>(Arrays.asList(resultName));          //usersName));

            ArrayAdapter<String> testAdapter =
                    new ArrayAdapter<String>(
                            getActivity(), // Die aktuelle Umgebung (diese Activity)
                            R.layout.list_items_game_result, // ID der XML-Layout Datei
                            R.id.list_items_textview, // ID des TextViews
                            listeUser); // Users in einer ArrayList

            ListView listView = (ListView) rootView.findViewById(R.id.listview_game_result);
            // Ausgabe
            listView.setAdapter(testAdapter);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Hinzufuegen von Spielergebnissen
        ImageButton btnAdd = (ImageButton) rootView.findViewById(R.id.imageButton_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Eine Funktion zum Hinzufuegen von Spielergebnissen schreiben.
                Toast.makeText(getActivity(), "wurde geklickt", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}
