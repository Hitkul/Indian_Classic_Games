package com.upstairs.indianclassic;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by karm on 24/6/16.
 */
public class XMLAssetHandler extends AsyncTask<Void, Void, Void> {
    Context context;
    MainActivity activity;
    CardDBHelper cardbhelper;
    ProgressDialog pd;
    public static final String SHARED_PREFERENCES_KEY = "petcareflags";

    public XMLAssetHandler(Context foo, MainActivity activity){//constructor to pass it an application context
        context = foo;
        this.activity = activity;
        pd=new ProgressDialog(activity);
    }
    @Override
    protected Void doInBackground(Void... params) {

        this.cardbhelper= new CardDBHelper(context);
        List<Card> Cards = null;
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            //Populate Cards and call XML Handler
            Cards = parser.parse(context.getAssets().open("data.xml"));
            Log.d("CARDS",Cards.toString());
            writeCardstodb(Cards);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;//why does this function need a return?
    }

    private void writeCardstodb(List<Card> Cards) {
        for(int i=0;i<Cards.size();i++){
            writetodb(Cards.get(i));
        }
    }

    private void writetodb(Card temp) {
        SQLiteDatabase db = cardbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(temp!=null) {
            if (temp.getTitle()!=null)
                values.put(CardContract.CardTable.COLUMN_NAME_CARD_TITLE, temp.getTitle());
            if (temp.getText()!=null)
                values.put(CardContract.CardTable.COLUMN_NAME_CARD_TEXT, temp.getText());
            if (temp.getImage()!=null)
                values.put(CardContract.CardTable.COLUMN_NAME_CARD_IMAGE, temp.getImage());
            if (temp.getList_string()!=null)
                values.put(CardContract.CardTable.COLUMN_NAME_CARD_LIST, temp.getList_string());
            if (temp.getClassification()!=null)
                values.put(CardContract.CardTable.COLUMN_NAME_CLASSIFICATION, temp.getClassification());
            if (temp.getSubclassification()!=null)
                values.put(CardContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION, temp.getSubclassification());
            if (temp.getSubsubclassification()!=null)
                values.put(CardContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION, temp.getSubsubclassification());
            values.put(CardContract.CardTable.COLUMN_NAME_CARD_POSITION,temp.getCardPosition());
            long newRowId = db.insert(CardContract.CardTable.TABLE_NAME,
                    CardContract.CardTable.COLUMN_NAME_CARD_TEXT,
                    values);
            Log.d("DBINSERT",String.valueOf(newRowId));
        }
    }
    @Override
    protected void onPreExecute(){
        pd.setMessage("Readying Nostalgia Lane");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

    }
    @Override
    protected void  onPostExecute(Void res){
        if(pd.isShowing())
            pd.dismiss();
        SharedPreferencesHelper.setFlag(SharedPreferencesHelper.DB_READY_KEY,context,true);
        Intent foo = new Intent(context,Home_Activity.class);
        foo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(foo);
        activity.finish();
    }/*
    @Override
    protected void onCancelled(){
        super.onCancelled();
        Log.d("CANCELLED THREAD","CANCEL");
        SQLiteDatabase db =cardbhelper.getWritableDatabase();
        cardbhelper.onDelete(db);

    }*/

}
