package listgrid.com.myapplicationdrawer.ui.news;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import listgrid.com.myapplicationdrawer.R;

import com.google.firebase.firestore.FirebaseFirestore;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsItem> dataItem;
    Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public NewsAdapter(List<NewsItem> dataItem, Context context) {
        this.dataItem = dataItem;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        NewsItem nItem = dataItem.get(position);
        holder.title.setText(nItem.getTitle());
        holder.desc.setText(nItem.getDesc());

        Glide.with(context)
                .load(nItem.getGambar())
                .into(holder.imageNews);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("test", "layout Clicked on pos" + holder.getAdapterPosition());
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v("test", "PREPARE DIALOG");
                        switch (i) {
                            case 0:
                                //edit data disini
                                final Dialog dialog = new Dialog(context);
                                Log.v("test", "OPEN DIALOG");
                                dialog.setContentView(R.layout.editor);
                                EditText title, image, desc;
                                Button simpan;
                                title = dialog.findViewById(R.id.title);
                                image = dialog.findViewById(R.id.image);
                                desc = dialog.findViewById(R.id.desc);
                                simpan = dialog.findViewById(R.id.simpan);

                                title.setText(nItem.getTitle());
                                image.setText(nItem.getGambar());
                                desc.setText(nItem.getDesc());

                                dialog.show();
                                Window window = dialog.getWindow();
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                                        WindowManager.LayoutParams.MATCH_PARENT);

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
                                        dialog.setTitle("Title...");


                                        db.collection("databaseNews").document(nItem.getId())
                                                .set(data)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(context, "Berhasil!",
                                                                    Toast.LENGTH_SHORT).show();

                                                            Log.v("test", "Update Sukses");
                                                            holder.title.setText(judul);
                                                            holder.desc.setText(konten);
                                                            Glide.with(context)
                                                                    .load(urlGambar)
                                                                    .into(holder.imageNews);
                                                            dialog.dismiss();
                                                        } else {
                                                            Toast.makeText(context, "Gagal!",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                });
                                break;
                            case 1:
                                //source delete disini
                                deleteData(nItem.getId());
                                int removeIndex = holder.getAdapterPosition();
                                dataItem.remove(removeIndex);
                                notifyItemRemoved(removeIndex);
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        ImageView imageNews;
        LinearLayout itemLayout;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            desc = itemView.findViewById(R.id.newsDesc);
            imageNews = itemView.findViewById(R.id.imgNews);
            itemLayout = itemView.findViewById(R.id.linearLayoutNewsItem);
        }
    }

    private void deleteData(String id) {

        db.collection("databaseNews").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Data gagal di hapus!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        notifyDataSetChanged();
                    }
                });
    }

}
