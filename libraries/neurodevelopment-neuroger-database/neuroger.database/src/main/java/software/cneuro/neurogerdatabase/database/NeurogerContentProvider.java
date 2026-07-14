package software.cneuro.neurogerdatabase.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
//import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 16/06/2015.
 */
public class NeurogerContentProvider extends ContentProvider {

    private static SQLiteOpenHelper mOpenHelper;

    // region private static final CONSTANTES
    private static final String mAuthority = Constant.AUTHORITY;
    private static final int MATCH_TABLE_PATIENT = 1;
    private static final int MATCH_TABLE_PATIENT_ID = 2;

    private static final int MATCH_TABLE_COHABITANT = 20;
    private static final int MATCH_TABLE_COHABITANT_ID = 21;

    private static final int MATCH_TABLE_PRUEBA_RF = 30;
    private static final int MATCH_TABLE_PRUEBA_RF_ID = 31;

    private static final int MATCH_TABLE_ANT_PATOLOGICOS = 40;
    private static final int MATCH_TABLE_ANT_PATOLOGICOS_ID = 41;

    private static final int MATCH_TABLE_SUBJECTIVE_EVALUATION_TEST = 50;
    private static final int MATCH_TABLE_SUBJECTIVE_EVALUATION_TEST_ID = 51;

    private static final int MATCH_TABLE_CDR_TEST = 140;
    private static final int MATCH_TABLE_CDR_TEST_ID = 141;

    private static final int MATCH_TABLE_PFEIFFER_TEST = 150;
    private static final int MATCH_TABLE_PFEIFFER_TEST_ID = 151;

    private static final int MATCH_TABLE_ALERT_SIGNS = 160;
    private static final int MATCH_TABLE_ALERT_SIGNS_ID = 161;

    private static final int MATCH_TABLE_DEPRESSION_TEST = 170;
    private static final int MATCH_TABLE_DEPRESSION_TEST_ID = 171;

    private static final int MATCH_TABLE_HHIES_TEST = 180;
    private static final int MATCH_TABLE_HHIES_TEST_ID = 181;

    private static final int MATCH_TABLE_KATZ_TEST = 190;
    private static final int MATCH_TABLE_KATZ_TEST_ID = 191;

    private static final int MATCH_TABLE_LAWTON_TEST = 200;
    private static final int MATCH_TABLE_LAWTON_TEST_ID = 201;

    private static final int MATCH_TABLE_FRAIL_TEST = 210;
    private static final int MATCH_TABLE_FRAIL_TEST_ID = 211;

    private static final int MATCH_TABLE_CLASSIFICATION = 310;
    private static final int MATCH_TABLE_CLASSIFICATION_ID = 311;

    private static final int MATCH_TABLE_COGNITIVE = 410;
    private static final int MATCH_TABLE_COGNITIVE_ID = 411;

    // MINIMENTAL
    private static final int MATCH_TABLE_MINIMENTAL_TEST = 510;
    private static final int MATCH_TABLE_MINIMENTAL_TEST_ID = 511;

    private static final int MATCH_TABLE_PSYCHOFAMILY_TEST = 610;
    private static final int MATCH_TABLE_PSYCHOFAMILY_TEST_ID = 611;

    private static final int MATCH_TABLE_PSYCHOAFFECTIVE_TEST = 710;
    private static final int MATCH_TABLE_PSYCHOAFFECTIVE_TEST_ID = 711;
    // endregion

    // Creates and initializes a UriMatcher object.
    private static final UriMatcher mURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PATIENT,
                MATCH_TABLE_PATIENT);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_COHABITANT,
                MATCH_TABLE_COHABITANT);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PRUEBA_RF,
                MATCH_TABLE_PRUEBA_RF);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PATHOLOGICAL_ANTECEDENTS,
                MATCH_TABLE_ANT_PATOLOGICOS);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_SUBJECTIVE_EVALUATION_TEST,
                MATCH_TABLE_SUBJECTIVE_EVALUATION_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PFEIFFER_TEST,
                MATCH_TABLE_PFEIFFER_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_ALERT_SIGNS,
                MATCH_TABLE_ALERT_SIGNS);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_CDR_TEST,
                MATCH_TABLE_CDR_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_DEPRESSION_TEST,
                MATCH_TABLE_DEPRESSION_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_HHIES_TEST,
                MATCH_TABLE_HHIES_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_KATZ_TEST,
                MATCH_TABLE_KATZ_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_LAWTON_TEST,
                MATCH_TABLE_LAWTON_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_FRAIL_TEST,
                MATCH_TABLE_FRAIL_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_FRAIL_TEST,
                MATCH_TABLE_FRAIL_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PSYCHOFAMILY_TEST,
                MATCH_TABLE_PSYCHOFAMILY_TEST);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PSYCHOAFFECTIVE_TEST,
                MATCH_TABLE_PSYCHOAFFECTIVE_TEST);

        mURIMatcher.addURI(mAuthority, Constant.TABLE_CLASSIFICATION,
                MATCH_TABLE_CLASSIFICATION);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_COGNITIVE,
                MATCH_TABLE_COGNITIVE);

        //MINIMENTAL
        mURIMatcher.addURI(mAuthority, Constant.TABLE_MINIMENTAL_TEST,
                MATCH_TABLE_MINIMENTAL_TEST);


        mURIMatcher.addURI(mAuthority, Constant.TABLE_PATIENT + "/#",
                MATCH_TABLE_PATIENT_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_COHABITANT + "/#",
                MATCH_TABLE_COHABITANT_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PRUEBA_RF + "/#",
                MATCH_TABLE_PRUEBA_RF_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PATHOLOGICAL_ANTECEDENTS + "/#",
                MATCH_TABLE_ANT_PATOLOGICOS_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_SUBJECTIVE_EVALUATION_TEST + "/#",
                MATCH_TABLE_SUBJECTIVE_EVALUATION_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PFEIFFER_TEST + "/#",
                MATCH_TABLE_PFEIFFER_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_ALERT_SIGNS + "/#",
                MATCH_TABLE_ALERT_SIGNS_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_CDR_TEST + "/#",
                MATCH_TABLE_CDR_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_DEPRESSION_TEST + "/#",
                MATCH_TABLE_DEPRESSION_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_HHIES_TEST + "/#",
                MATCH_TABLE_HHIES_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_KATZ_TEST + "/#",
                MATCH_TABLE_KATZ_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_LAWTON_TEST + "/#",
                MATCH_TABLE_LAWTON_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_FRAIL_TEST + "/#",
                MATCH_TABLE_FRAIL_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PSYCHOFAMILY_TEST + "/#",
                MATCH_TABLE_PSYCHOFAMILY_TEST_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_PSYCHOAFFECTIVE_TEST + "/#",
                MATCH_TABLE_PSYCHOAFFECTIVE_TEST_ID);

        mURIMatcher.addURI(mAuthority, Constant.TABLE_CLASSIFICATION + "/#",
                MATCH_TABLE_CLASSIFICATION_ID);
        mURIMatcher.addURI(mAuthority, Constant.TABLE_COGNITIVE + "/#",
                MATCH_TABLE_COGNITIVE_ID);

        //MINIMENTAL
        mURIMatcher.addURI(mAuthority, Constant.TABLE_MINIMENTAL_TEST + "/#",
                MATCH_TABLE_MINIMENTAL_TEST_ID);
    }

    @Override
    public boolean onCreate() {
        /* Creates a new helper object. This method always returns quickly.
         * Notice that the database itself isn't created or opened
         * until SQLiteOpenHelper.getWritablaDatabase is called */
        try {
            mOpenHelper = new NEUROGER_DBHelper(getContext());
            // , mDatabaseName,null, mVersion);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tabla = matchTable(uri);

        int deleted = db.delete(tabla, selection, selectionArgs);
        if (deleted > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, ContentValues[] values) {

        int nrInserted = 0;
        String TABLE = matchTable(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Begin inner transaction
        db.beginTransaction();

        try {
            for (ContentValues cv : values) {

                db.insertOrThrow(TABLE, null, cv);
                nrInserted++;
            }

            db.setTransactionSuccessful();
            if (getContext() != null)
                getContext().getContentResolver().notifyChange(uri, null);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return nrInserted;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tabla = matchTable(uri);

        long rowID = db.insert(tabla, null, values);
        if (rowID != -1 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.withAppendedPath(uri, String.valueOf(rowID));
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String tableName = matchTable(uri);
        long id = matcheaID(uri);

        // SQLiteQueryBuilder is a helper class that creates the proper SQL
        // syntax for us.
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        // Set the tabla we're querying.
        qBuilder.setTables(tableName);

        // If the query ends in a specific record number, we're being asked for
        // a specific record,
        // so set the WHERE clause in our query.
        //if (id != -1) {
        // if (true) {
        // qBuilder.appendWhere("_id=" + uri.getPathLeafId());
        //}

        // String s = qBuilder.buildQuery(projection, selection, null, null,
        // sortOrder, null);

        // Make the query.
        Cursor c = qBuilder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);

        assert getContext() != null;
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tabla = matchTable(uri);

        int updated = db.update(tabla, values, selection, selectionArgs);
        if (updated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    // /////////////////////////////////////////////

    /**
     * Se encarga de matchear la URI de entrada contra lo que se espera, a ver
     * que sale.
     *
     * @param uri URI a la que va dirigida la query.
     * @return Nombre de la tabla a la que esta dirigida la query, o null si no
     * la reconoce.
     */
    private String matchTable(Uri uri) {

        String tableName;
        int match = mURIMatcher.match(uri);

        switch (match) {

            case MATCH_TABLE_PATIENT:
                tableName = Constant.TABLE_PATIENT;
                break;

            case MATCH_TABLE_COHABITANT:
                tableName = Constant.TABLE_COHABITANT;
                break;

            case MATCH_TABLE_PRUEBA_RF:
                tableName = Constant.TABLE_PRUEBA_RF;
                break;

            case MATCH_TABLE_ANT_PATOLOGICOS:
                tableName = Constant.TABLE_PATHOLOGICAL_ANTECEDENTS;
                break;

            case MATCH_TABLE_SUBJECTIVE_EVALUATION_TEST:
                tableName = Constant.TABLE_SUBJECTIVE_EVALUATION_TEST;
                break;

            case MATCH_TABLE_PFEIFFER_TEST:
                tableName = Constant.TABLE_PFEIFFER_TEST;
                break;

            case MATCH_TABLE_ALERT_SIGNS:
                tableName = Constant.TABLE_ALERT_SIGNS;
                break;

            case MATCH_TABLE_CDR_TEST:
                tableName = Constant.TABLE_CDR_TEST;
                break;

            case MATCH_TABLE_DEPRESSION_TEST:
                tableName = Constant.TABLE_DEPRESSION_TEST;
                break;

            case MATCH_TABLE_HHIES_TEST:
                tableName = Constant.TABLE_HHIES_TEST;
                break;

            case MATCH_TABLE_KATZ_TEST:
                tableName = Constant.TABLE_KATZ_TEST;
                break;

            case MATCH_TABLE_LAWTON_TEST:
                tableName = Constant.TABLE_LAWTON_TEST;
                break;

            case MATCH_TABLE_FRAIL_TEST:
                tableName = Constant.TABLE_FRAIL_TEST;
                break;

            case MATCH_TABLE_CLASSIFICATION:
                tableName = Constant.TABLE_CLASSIFICATION;
                break;

            case MATCH_TABLE_COGNITIVE:
                tableName = Constant.TABLE_COGNITIVE;
                break;

            //MINIMENTAL
            case MATCH_TABLE_MINIMENTAL_TEST:
                tableName = Constant.TABLE_MINIMENTAL_TEST;
                break;

            case MATCH_TABLE_PSYCHOFAMILY_TEST:
                tableName = Constant.TABLE_PSYCHOFAMILY_TEST;
                break;

            case MATCH_TABLE_PSYCHOAFFECTIVE_TEST:
                tableName = Constant.TABLE_PSYCHOAFFECTIVE_TEST;
                break;

            default:
                tableName = null;
                break;
        }

        return tableName;
    }

    private long matcheaID(Uri uri) {

        long id;
        int match = mURIMatcher.match(uri);

        if (match == MATCH_TABLE_PATIENT_ID) {
            id = extraeID(uri);
        } else {
            id = -1;
        }
        return id;
    }

    private long extraeID(Uri uri) {
        return Long.parseLong(uri.getLastPathSegment());
    }

}
