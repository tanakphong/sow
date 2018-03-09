package th.co.wesoft.sow.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import th.co.wesoft.sow.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends DialogFragment {

    private String msg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            msg = bundle.getString("MSG", getResources().getString(R.string.please_wait));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.progress, null);
        TextView mTxtMessage = (TextView) view.findViewById(R.id.txtMessage);
        mTxtMessage.setText(msg);
        builder.setView(view);

        return builder.create();
    }
}
