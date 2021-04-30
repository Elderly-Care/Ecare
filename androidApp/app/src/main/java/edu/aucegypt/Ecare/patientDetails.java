//package edu.aucegypt.Ecare;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import java.util.ArrayList;
//
//public class patientDetails extends ArrayAdapter<patientOutsideProfile> {
//
//    public patientDetails(@NonNull Context context, ArrayList<patientOutsideProfile> item) {
//        super(context,0, item);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        patientOutsideProfile user = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_details, parent, false);
//        }
//        // Lookup view for data population
//        TextView tvName = (TextView) convertView.findViewById(R.id.detail);
//        TextView tvHome = (TextView) convertView.findViewById(R.id.detail2);
//        // Populate the data into the template view using the data object
//        tvName.setText(user.detial);
//        tvHome.setText(user.ans);
//        // Return the completed view to render on screen
//        return convertView;
//    }
//}
