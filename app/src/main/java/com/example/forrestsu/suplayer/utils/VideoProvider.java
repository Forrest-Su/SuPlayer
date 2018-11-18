package com.example.forrestsu.suplayer.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.forrestsu.suplayer.bean.VideoBean;
import com.example.forrestsu.suplayer.my_interface.AbstractProvider;

import java.util.ArrayList;
import java.util.List;

public class VideoProvider implements AbstractProvider {

    private Activity context;

    public VideoProvider(Activity context) {
        this.context = context;
    }

    @Override
    public List<?> getList() {

        List<VideoBean> list = null;

        ContentResolver contentResolver = context.getContentResolver();
        if (context != null) {
            Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String title = cursor.getString(cursor.
                            getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String album = cursor.getString(cursor.
                            getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor.getString(cursor.
                            getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String displayName = cursor.getString(cursor.
                            getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String mineType = cursor.getString(cursor.
                            getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    String path = cursor.getString(cursor.
                            getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    String size = cursor.getString(cursor.
                            getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    String duration = cursor.getString(cursor.
                            getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                    VideoBean video = new VideoBean(id, title, album, artist, displayName,
                            mineType, path, size, duration);
                    list.add(video);

                }
                cursor.close();
            }
        }
        return list;
    }
}
