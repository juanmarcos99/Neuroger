package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
//import androidx.collection.LongSparseArray;
import androidx.collection.LongSparseArray;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 24/03/2017.
 */

public class SearchCohabitant_AsyncTask extends AsyncTask<Void, Void, long[]> {


    public interface OnCohabitantSearched {
        void OnCohabitantSearched(long[] mCohabitantIds);
    }

    public final Context context;
    private final LongSparseArray<Boolean> mIds;
    private final long[] mCohabitantIds;
    private final SearchCohabitant_AsyncTask.OnCohabitantSearched mCallback;

    public SearchCohabitant_AsyncTask(Context context, LongSparseArray<Boolean> ids,
                                      SearchCohabitant_AsyncTask.OnCohabitantSearched callback) {
        this.context = context;
        this.mIds = ids;
        this.mCallback = callback;
        this.mCohabitantIds = new long[mIds.size()];
    }

    @Override
    protected long[] doInBackground(Void... params) {

        ContentResolver cr = context.getContentResolver();

        Uri uriCohabitant = Constant.URI_TABLE_COHABITANT;
        String[] projection = new String[]{Constant.COL_COHABITANT_ID};

        for (int i = 0; i < mIds.size(); i++) {
            long id_paciente = mIds.keyAt(i);

            String selection = Constant.COL_COHABITANT_PATIENT_ID + "=" + id_paciente;

            Cursor cursor = cr.query(uriCohabitant, projection, selection, null, null);
            if (cursor != null && cursor.moveToFirst())
                mCohabitantIds[i] = cursor.getLong(cursor.getColumnIndex(Constant.COL_COHABITANT_ID));

        }

        return mCohabitantIds;
    }

    @Override
    protected void onPostExecute(long[] mCohabitantIds) {
        // TODO Auto-generated method stub
        super.onPostExecute(mCohabitantIds);
        mCallback.OnCohabitantSearched(mCohabitantIds);
    }
}
