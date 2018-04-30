package app.besafx.com.smartmanager.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import app.besafx.com.smartmanager.R;
import app.besafx.com.smartmanager.activity.LoaderFragment;
import app.besafx.com.smartmanager.activity.Main;
import app.besafx.com.smartmanager.adapter.ListAdapterTask;
import app.besafx.com.smartmanager.entity.Person;
import app.besafx.com.smartmanager.entity.Task;
import app.besafx.com.smartmanager.enums.FragmentType;
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

    private ListAdapterTask myTasksAdapter;

    private FragmentType fragmentType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentType = (FragmentType) getArguments().getSerializable("FragmentType");
        return inflater.inflate(R.layout.fragment_menu_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView_persons_combo_title = getView().findViewById(R.id.textView_persons_combo_title);

        switch (fragmentType){
            case Outgoing_Pending_Tasks:
                getActivity().setTitle("تحت التنفيذ");
                textView_persons_combo_title.setText("جهة التكليف");
                break;
            case Outgoing_Auto_Tasks:
                getActivity().setTitle("مغلقة");
                textView_persons_combo_title.setText("جهة التكليف");
                break;
            case Outgoing_Archive_Tasks:
                getActivity().setTitle("الارشيف");
                textView_persons_combo_title.setText("جهة التكليف");
                break;
            case Incoming_Pending_Tasks:
                getActivity().setTitle("تحت التنفيذ");
                textView_persons_combo_title.setText("الموظف");
                break;
            case Incoming_Auto_Tasks:
                getActivity().setTitle("مغلقة");
                textView_persons_combo_title.setText("الموظف");
                break;
            case Incoming_Archive_Tasks:
                getActivity().setTitle("الارشيف");
                textView_persons_combo_title.setText("الموظف");
                break;
        }

        initMyPersonsComboBox();

        initTasksComboBox();

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
                new FetchTasks(person).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initTasksComboBox() {

        ListView listView = (ListView) getView().findViewById(R.id.listView_persons);

        // Create an ArrayAdapter using the string array and a default listView layout
        myTasksAdapter = new ListAdapterTask(getContext(), R.layout.list_row_task, new ArrayList<Task>());

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

    private class FetchTasks extends AsyncTask<Void, Void, Task[]> {

        private Person selectedPerson;

        public FetchTasks(Person person) {
            this.selectedPerson = person;
        }

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected Task[] doInBackground(Void... params) {

            String url = null;

            switch (fragmentType){
                case Outgoing_Pending_Tasks:
                    url = getString(R.string.rest_url) + "/api/task/filter3?fromPerson=" + selectedPerson.getId() + "&closeType=Pending";
                    break;
                case Outgoing_Auto_Tasks:
                    url = getString(R.string.rest_url) + "/api/task/filter3?fromPerson=" + selectedPerson.getId() + "&closeType=Auto";
                    break;
                case Outgoing_Archive_Tasks:
                    url = getString(R.string.rest_url) + "/api/task/filter3?fromPerson=" + selectedPerson.getId() + "&closeType=Manual";
                    break;
                case Incoming_Pending_Tasks:
                    url = getString(R.string.rest_url) + "/api/task/filter3?toPerson=" + selectedPerson.getId() + "&closeType=Pending";
                    break;
                case Incoming_Auto_Tasks:
                    url = getString(R.string.rest_url) + "/api/task/filter3?toPerson=" + selectedPerson.getId() + "&closeType=Auto";
                    break;
                case Incoming_Archive_Tasks:
                    url = getString(R.string.rest_url) + "/api/task/filter3?toPerson=" + selectedPerson.getId() + "&closeType=Manual";
                    break;
            }

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
            myTasksAdapter.clear();
            myTasksAdapter.addAll(Lists.<Task>newArrayList(tasks));
            myTasksAdapter.notifyDataSetChanged();
        }

    }
}
