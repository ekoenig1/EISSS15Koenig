package com.spiel21.application.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.spiel21.application.R;
import com.spiel21.application.async.AsyncTaskGet;
import com.spiel21.application.async.Server;
import com.spiel21.application.util.Courts2;
import com.spiel21.application.util.PlayerResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class GameResultFragment extends Fragment {

    private AsyncTaskGet verbindung;
    private AsyncTaskGet verbindung2;
    private AsyncTaskGet verbindung3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        verbindung = new AsyncTaskGet();
        verbindung2 = new AsyncTaskGet();
        View rootView = inflater.inflate(R.layout.list_view_game_result, container, false);

        String result = null;
        String result2 = null;

        // -------------------- USERS ---------------------------
        /*
        try {
            // Resource ansprechen
            result = verbindung.execute(server.getAdresse() + "/users/").get();
            // aus dem String werden die Users erzeugt in eine Liste
            ArrayList<Users> userList = Users.createUserList(result);
            // ein Array wird erzeugt mit der Laenge von Liste aller Users
            String[] usersName = new String[userList.size()];
            // die Daten werden von der Collection uebergeben

            // TODO: Die Collections zusammenfassen bzw. identifizieren; PICS einfuegen;
            for (int i = 0; i < userList.size(); i++) {
                usersName[i] = "(" + userList.get(i).getGender() + ") " + userList.get(i).getUsername() + "\n"
                        + "ID: " + userList.get(i).getId() + "\n"
                        + "AGE: " + userList.get(i).getAge();
            }
            // Erstelle ein ArrayList fuer den Adapter
            ArrayList<String> listeUser = new ArrayList<String>(Arrays.asList(usersName));

            ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(
                    getActivity(), // Die aktuelle Umgebung (diese Activity)
                    R.layout.list_items_game_result, // ID der XML-Layout Datei
                    R.id.list_items_textview, // ID des TextViews
                    listeUser); // Users in einer ArrayList

            ListView listView = (ListView) rootView.findViewById(R.id.listview_game_result);
            // Ausgabe
            listView.setAdapter(usersAdapter);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/


        // -------------------- RESULT ---------------------------

        try {
            result2 = verbindung.execute(Server.getAdresse() + "/matches/").get();

            // aus dem String werden die Users erzeugt in eine Liste
            ArrayList<Courts2> matchesList = Courts2.createCourts2List(result2);
            // ein Array wird erzeugt mit der Laenge von Liste aller Users
            String[] matchesName = new String[matchesList.size()];

            // die Daten werden von der Collection uebergeben
            // TODO: Die Collections zusammenfassen bzw. identifizieren; PICS einfuegen;
            for (int i = 0; i < matchesList.size(); i++) {
                matchesName[i] = "Basketballplatz: " + matchesList.get(i).getName() + "\n"
                        + "Datum: " + matchesList.get(i).getDate() + "\n"
                        + "Uhrzeit: " + matchesList.get(i).getTime() + "\n";
            }


            // Erstelle ein ArrayList fuer den Adapter
            ArrayList<String> listeUser = new ArrayList<String>(Arrays.asList(matchesName));

            final ArrayAdapter<String> resultAdapter = new ArrayAdapter<String>(
                    getActivity(), // Die aktuelle Umgebung (diese Activity)
                    R.layout.list_items_game_result, // ID der XML-Layout Datei
                    R.id.list_items_textview, // ID des TextViews
                    listeUser); // Users in einer ArrayList

            ListView listView = (ListView) rootView.findViewById(R.id.listview_game_result);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Intent hier...
                    Toast.makeText(getActivity(), resultAdapter.getItem(position).toString(), Toast.LENGTH_LONG).show();
                }
            });
            // Ausgabe
            listView.setAdapter(resultAdapter);


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


    //--  Settings, 3 Methoden -- //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Menue bekannt geben
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game_result, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // pruefen, ob Menu ausgewaehlt worden ist anhand der ID
        int id = item.getItemId();

        // Daten aktuallisieren
        if (id == R.id.action_mein_ergebnis) {
            showMeineErgebnisse();
            return true;
        } else {
            schowTopErgebnisse();
        }

        return super.onOptionsItemSelected(item);
    }


    private void showMeineErgebnisse() {

        try {
            // testUser
            String result = verbindung2.execute(Server.getAdresse() + "/playerresult/5576ead315620cf10cd07373").get();

            // aus dem String werden die Users erzeugt in eine Liste
            ArrayList<PlayerResult> matchesList = PlayerResult.createPlayerResultList(result);

            String myResult = "Username: " + matchesList.get(0).getUsername() + "\n" // 5576ead315620cf10cd07373 -> testUser
                    + "Gewonnen: " + matchesList.get(0).getWin() + "\n"
                    + "Verloren: " + matchesList.get(0).getLose() + "\n";

            AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
            // set title
            myAlert.setTitle("Meine Ergebnisse");
            // set dialog message
            myAlert.setMessage(myResult)
                    .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_action_add_person)
                    .create();
            // show it
            myAlert.show();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void schowTopErgebnisse() {

        try {
            // testUser
            String result = verbindung3.execute(Server.getAdresse() + "/playerresult").get();

            // aus dem String werden die Users erzeugt in eine Liste
            ArrayList<PlayerResult> matchesList = PlayerResult.createPlayerResultList(result);

            String[] myResult = new String[matchesList.size()];

            for (int i = 0; i < matchesList.size(); i++) {
                myResult[i] = "Username: " + matchesList.get(i).getUsername() + "\n"
                        + "Gewonnen: " + matchesList.get(i).getWin() + "\n"
                        + "Verloren: " + matchesList.get(i).getLose() + "\n";
            }
            /*
            AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
            // set title
            myAlert.setTitle("Top Ergebnisse");
            // set dialog message
            myAlert.setMessage(myResult)
                    .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_action_add_person)
                    .create();
            // show it
            myAlert.show();
            */


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
