package app.besafx.com.smartmanager.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import app.besafx.com.smartmanager.R;
import app.besafx.com.smartmanager.activity.LoaderFragment;
import app.besafx.com.smartmanager.activity.Main;
import app.besafx.com.smartmanager.entity.Person;
import app.besafx.com.smartmanager.entity.Task;
import com.google.common.collect.Lists;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class Menu1 extends LoaderFragment {

    protected static final String TAG = Menu1.class.getSimpleName();

    private ArrayAdapter<Person> myPersonsAdapter;

    private ArrayAdapter<Task> myTasksAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.fragment_menu_1 for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("مهام تحت التنفيذ");

        initMyPersonsComboBox();

        initMyPendingIncomingTasksComboBox();

        //Fetch persons data
        new FetchMyPersons().execute();
    }

    private void initMyPersonsComboBox() {

        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner_persons);

        // Create an ArrayAdapter using the string array and a default spinner layout
        myPersonsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<Person>());

        // Specify the layout to use when the list of choices appears
        myPersonsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the myPersonsAdapter to the spinner
        spinner.setAdapter(myPersonsAdapter);

        spinner.setPrompt("اختر جهة التكليف");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Person person = (Person) parent.getItemAtPosition(position);

                //Fetch tasks data
                new FetchPendingIncomingTasks(person).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initMyPendingIncomingTasksComboBox() {

        ListView listView = (ListView) getView().findViewById(R.id.listView_persons);

        // Create an ArrayAdapter using the string array and a default listView layout
        myTasksAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<Task>());

        // Apply the myTasksAdapter to the listView
        listView.setAdapter(myTasksAdapter);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class FetchMyPersons extends AsyncTask<Void, Void, Person[]> {

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected Person[] doInBackground(Void... params) {
            final String url = getString(R.string.rest_url) + "/api/person/findPersonUnderMeSummery";

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            try {
                // Make the network request
                ResponseEntity<Person[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(Main.requestHeaders), Person[].class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Person[] persons) {
            dismissProgressDialog();
            myPersonsAdapter.clear();
            myPersonsAdapter.addAll(Lists.newArrayList(persons));
            myPersonsAdapter.notifyDataSetChanged();
        }

    }

    private class FetchPendingIncomingTasks extends AsyncTask<Void, Void, Task[]> {

        private Person selectedPerson;

        public FetchPendingIncomingTasks(Person person) {
            this.selectedPerson = person;
        }

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected Task[] doInBackground(Void... params) {
            final String url = getString(R.string.rest_url) + "/api/task/filter2?toPerson=" + selectedPerson.getId() + "&closeType=Pending";

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            try {
                // Make the network request
                ResponseEntity<Task[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(Main.requestHeaders), Task[].class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Task[] tasks) {
            dismissProgressDialog();
            Log.d(TAG, "TASKS " + tasks.length);
//            myTasksAdapter.clear();
//            myTasksAdapter.addAll(Lists.newArrayList(tasks));
//            myTasksAdapter.notifyDataSetChanged();
        }

    }
}
