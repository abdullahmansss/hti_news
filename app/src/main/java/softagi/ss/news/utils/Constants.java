package softagi.ss.news.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants
{
    // shared preferences
    private static SharedPreferences sharedPreferences;

    private static DatabaseReference databaseReference;

    public static DatabaseReference initRef ()
    {
        if(databaseReference == null)
        {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        return databaseReference;
    }

    public static void saveUid(Activity activity,String id)
    {
        sharedPreferences = activity.getSharedPreferences("SOCIAL", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Uid", id);
        editor.apply();
    }

    public static String getUid(Activity activity)
    {
        sharedPreferences = activity.getSharedPreferences("SOCIAL", Context.MODE_PRIVATE);

        return sharedPreferences.getString("Uid", "empty");
    }
}