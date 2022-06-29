package listgrid.com.myapplicationdrawer.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import listgrid.com.myapplicationdrawer.R;

public class SlideshowFragment extends Fragment {

   View view;
   EditText title, image, desc;
   Button simpan;
   private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_slideshow, container, false);
        title= (EditText) view.findViewById(R.id.title);
        image = (EditText) view.findViewById(R.id.image);
        desc = (EditText) view.findViewById(R.id.desc);
        simpan = (Button) view.findViewById(R.id.simpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String judul = title.getText().toString();
                String urlGambar = image.getText().toString();
                String konten = desc.getText().toString();
                Map<String, Object> data = new HashMap<>();
                data.put("title", judul);
                data.put("image", urlGambar);
                data.put("desc", konten);

                db.collection("databaseNews")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Berhasil Menyimpan Data!Cek di Menu News",
                                        Toast.LENGTH_LONG).show();
                                title.getText().clear();
                                image.getText().clear();
                                desc.getText().clear();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
    }
