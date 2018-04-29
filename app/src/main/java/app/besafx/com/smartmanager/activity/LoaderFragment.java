package app.besafx.com.smartmanager.activity;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class LoaderFragment extends Fragment {

    private ProgressDialog progressDialog;

    private boolean destroyed = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    public void showLoadingProgressDialog() {
        this.showProgressDialog("فضلا انتظر قليلاً");
    }

    public void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && !destroyed) {
            progressDialog.dismiss();
        }
    }
}
