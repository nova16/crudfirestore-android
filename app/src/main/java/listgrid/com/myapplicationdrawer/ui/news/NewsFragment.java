package listgrid.com.myapplicationdrawer.ui.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import listgrid.com.myapplicationdrawer.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//new import
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment{
    View view;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ArrayList<NewsItem> newsItems;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBar;



    static String deskripsi1 = "Keterangan mengenai Flash Drive";
    static String deskripsi2 = "Keterangan mengenai Handphone";
    static String deskripsi3 = "Keterangan mengenai Kamera";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        progressBar = (ProgressBar)view.findViewById(R.id.progressbarNews);
        newsItems = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsItems, getActivity());

        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("test","LOAD FIREBASE");
        getData();
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);
        db.collection("databaseNews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        newsItems.clear();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                NewsItem itemNewsData = new NewsItem(
                                        document.getString("image"),
                                        document.getString("title"),
                                        document.getString("desc"));
                                itemNewsData.setId(document.getId());
                                newsItems.add(itemNewsData);
                            }
                            newsAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }



}