package madri.me.vanilai.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import madri.me.vanilai.R;
import madri.me.vanilai.helper.AddressHelper;

/**
 * Created by badri on 7/30/16.
 */
public class AddLocationDialogFragment extends DialogFragment {
//    private static final String TAG = AddLocationDialogFragment.class.getSimpleName();
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(inflater.inflate(R.layout.add_dialog_layout, null)).setPositiveButton("Add/Remove", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                EditText cityEditText = (EditText) dialogInterface.findViewById(R.id.dialog_city);
//                EditText countryEditText = (EditText) dialogView.findViewById(R.id.dialog_state);
//                EditText zipEditText = (EditText) dialogView.findViewById(R.id.dialog_zip);
//                if(cityEditText.getText().toString().isEmpty() || countryEditText.getText().toString().isEmpty() || zipEditText.getText().toString().isEmpty()) {
//                    Log.e(TAG, "Please provide atleast one!!!");
//                } else {
//                    StringBuilder locationBuilder = new StringBuilder();
//                    if(!cityEditText.getText().toString().isEmpty()) {
//                        locationBuilder.append(cityEditText.getText().toString()).append(" ");
//                    }
//                    if(!countryEditText.getText().toString().isEmpty()) {
//                        locationBuilder.append(countryEditText.getText().toString()).append(" ");
//                    }
//                    if(!zipEditText.getText().toString().isEmpty()) {
//                        locationBuilder.append(countryEditText.getText().toString());
//                    }
//                    String location = locationBuilder.toString();
//                    AddressHelper.getAddressFromLocation(location, getApplicationContext(), new GeocoderHandler());
//                }
//
//                dialogInterface.dismiss();
//            }
//        });
//        return builder.create();
//    }


}
