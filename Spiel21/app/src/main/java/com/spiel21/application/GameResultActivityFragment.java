package com.spiel21.application;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class GameResultActivityFragment extends Fragment {

    public GameResultActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AsyncTaskGet verbindung = new AsyncTaskGet();
        View rootView = inflater.inflate(R.layout.fragment_wetter, container, false);

        String result = null;
        Server server = new Server();

        try {
            // Resource ansprechen
            result = verbindung.execute(server.getAdresse() + "/users/").get();

            // aus dem String werden die User erzeugt in eine Liste
            ArrayList<User> userList = User.createUserList(result);
            // ein Array wird erzeugt mit der Laenge von Liste aller User
            String[] usersName = new String[userList.size()];
            // die Daten werden von der Collection uebergeben
            for (int i = 0; i < 5; i++) {
                usersName[i] = "(" + userList.get(i).getGender() + ") " + userList.get(i).getUsername() + "\n" +
                        "Gewonen: " + userList.get(i).getWin() + "\n" +
                        "Verloren: " + userList.get(i).getLoss();
            }
            // Erstelle ein ArrayList fuer den Adapter
            ArrayList<String> listeUser = new ArrayList<String>(Arrays.asList(usersName));

            ArrayAdapter<String> testAdapter =
                    new ArrayAdapter<String>(
                            getActivity(), // Die aktuelle Umgebung (diese Activity)
                            R.layout.list_item_wetter, // ID der XML-Layout Datei
                            R.id.list_item_wetter_textview, // ID des TextViews
                            listeUser); // User in einer ArrayList

            ListView listView = (ListView) rootView.findViewById(R.id.listview_wetter);
            // Ausgabe
            listView.setAdapter(testAdapter);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return rootView;
    }
}
