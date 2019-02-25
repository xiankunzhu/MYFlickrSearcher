package com.xiankunz.myflickrsearcher.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.xiankunz.myflickrsearcher.R;
import com.xiankunz.myflickrsearcher.model.Photo;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    static final String TAG = "SearchResultAdapter";
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView title;
        private final ImageView image;


        public ViewHolder(View view) {
            super(view);
            mView = view;

            title = (TextView) view.findViewById(R.id.title);
            image = (ImageView) view.findViewById((R.id.poster));
        }
    }

    private List<Photo> mSearchPhoto;
    Context mContext;
    private String mTitle;
    private String mImageUrl;

    public SearchResultAdapter(Context context, List<Photo> photos) {
        mContext = context;
        mSearchPhoto = photos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.result_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Photo model = (Photo) mSearchPhoto.get(i);
        mTitle = "";
        mImageUrl = "";

        if (model.getTitle() != null) {
            mTitle = model.getTitle();
            viewHolder.title.setText(mTitle);
        }

        if (model.getUrl_s() != null) {
            mImageUrl = model.getUrl_s();
            new DownloadImageTask(viewHolder.image).execute(mImageUrl);
        }


        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: got full screen gallery Activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchPhoto == null? 0 : mSearchPhoto.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
