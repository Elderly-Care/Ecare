package edu.aucegypt.Ecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

public class addPatient extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    TextView userName;

    MainAdapter adapter;

    userAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        final ArrayList<Item> homeList = new ArrayList<>();
        final ArrayList<ItemsModel> arrayList = new ArrayList<>();
        final ListView grid = (ListView) findViewById(R.id.list);
        db.collection("users").whereEqualTo("registerType", "Patient")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JSONObject userDetail = new JSONObject(document.getData());
                                try {
                                    homeList.add(new Item(userDetail.getString("name"), userDetail.getString("email"), R.drawable.profile));
                                    arrayList.add(new ItemsModel(userDetail.getString("name"), userDetail.getString("email"), R.drawable.profile));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Log.d(TAG, document.getId() + " => " + userDetail.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        myAdapter = new userAdapter(addPatient.this, R.layout.activity_add_patient, homeList);
                        adapter = new MainAdapter(addPatient.this, arrayList);
                        grid.setAdapter(myAdapter);
                        grid.setAdapter(adapter);

                        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Item item = homeList.get(position);
                                String email = item.getbuttonEmail();
                                Intent i = new Intent(addPatient.this, patientOutsideProfile.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("email", email);
                                i.putExtras(bundle);
                                startActivity(i);
                            }
                        });
                    }
                });
    }
    public class MainAdapter extends BaseAdapter implements Filterable {

        private List<ItemsModel> itemsModelList;
        private List<ItemsModel> itemsModelListfiltered;
        private Context context;

        public MainAdapter(Context c,List<ItemsModel> itemsModelList){
            this.context=c;
            this.itemsModelList=itemsModelList;
            this.itemsModelListfiltered=itemsModelList;
        }

        @Override
        public int getCount() {
            if(itemsModelListfiltered == null)
                return 0;
            else
                return itemsModelListfiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view=getLayoutInflater().inflate(R.layout.user_item, null);

            ImageView imageView=view.findViewById(R.id.imageView4);
            TextView textView=view.findViewById(R.id.textView2);
            TextView textView1=view.findViewById(R.id.email2);

            imageView.setImageResource(itemsModelListfiltered.get(position).getImage());
            textView.setText(itemsModelListfiltered.get(position).getName());
            textView1.setText(itemsModelListfiltered.get(position).getEmail());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(addPatient.this, patientOutsideProfile.class).putExtra("email",itemsModelListfiltered.get(position).getEmail()));
                }
            });

            return view;
        }

        @Override
        public Filter getFilter() {


            Filter filter=new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults=new FilterResults();

                    if(constraint==null||constraint.length()==0){
                        filterResults.count=itemsModelList.size();
                        filterResults.values=itemsModelList;
                    }else{
                        String searchstr=constraint.toString().toLowerCase();
                        List<ItemsModel>resultData=new ArrayList<>();

                        for(ItemsModel itemsModel:itemsModelList){
                            if(itemsModel.getName().toLowerCase().contains(searchstr)){
                                resultData.add(itemsModel);
                            }
                            filterResults.count=resultData.size();
                            filterResults.values=resultData;
                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    itemsModelListfiltered=(List<ItemsModel>)results.values;
                    notifyDataSetChanged();

                }
            };

            return filter;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem menuItem = menu.findItem(R.id.search_icon);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter != null) adapter.getFilter().filter(newText);

                return true;
            }
        });

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}