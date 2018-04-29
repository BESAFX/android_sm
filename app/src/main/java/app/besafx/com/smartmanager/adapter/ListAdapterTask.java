package app.besafx.com.smartmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.besafx.com.smartmanager.R;
import app.besafx.com.smartmanager.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterTask extends ArrayAdapter<Task> {

    protected static final String TAG = ListAdapterTask.class.getSimpleName();

    private Context mContext;
    private List<Task> tasks = new ArrayList<>();

    public ListAdapterTask(Context context, int resource, List<Task> objects) {
        super(context, resource, objects);
        mContext = context;
        tasks = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_row_task, parent, false);

        if (!tasks.isEmpty()) {
            Task currentTask = tasks.get(position);

            Log.d(TAG, currentTask.getTitle());

            TextView tt1 = (TextView) listItem.findViewById(R.id.id);
            TextView tt2 = (TextView) listItem.findViewById(R.id.categoryId);
            TextView tt3 = (TextView) listItem.findViewById(R.id.description);

            tt1.setText(currentTask.getId().toString());
            tt2.setText(currentTask.getCode());
            tt3.setText(currentTask.getTitle());
        }

        return listItem;

    }
}
