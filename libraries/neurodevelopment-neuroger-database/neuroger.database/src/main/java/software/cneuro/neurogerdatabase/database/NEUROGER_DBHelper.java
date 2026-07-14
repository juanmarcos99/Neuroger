package software.cneuro.neurogerdatabase.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 03/06/2015.
 */
public class NEUROGER_DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    //<--CREATE REGION-->
    public static final String CREATE_TABLE_PATIENT = "CREATE TABLE \"patient\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"_guid\" TEXT NOT NULL DEFAULT (null) ,\"name\" TEXT NOT NULL DEFAULT (null) ,\"lastname\" TEXT NOT NULL  DEFAULT (null) ,\"id_number\" TEXT NOT NULL DEFAULT (null) ,\"birthdate\" TEXT NOT NULL DEFAULT ('CURRENT_DATE') ,\"sex\" INTEGER NOT NULL  DEFAULT (null) ,\"occupation\" TEXT NOT NULL  DEFAULT (null),\"years_studies\" TEXT NOT NULL  DEFAULT (null) ,\"civil_status\" TEXT NOT NULL  DEFAULT (null) ,\"hospital\" TEXT NOT NULL  DEFAULT (null) ,\"origin\" TEXT NOT NULL  DEFAULT (null) ,\"clinic_classification\" TEXT NOT NULL  DEFAULT (null) ,\"country\" TEXT NOT NULL  DEFAULT (null) ,\"province_state\" TEXT NOT NULL  DEFAULT (null) ,\"municipality\" TEXT NOT NULL  DEFAULT (null) ,\"address\" TEXT NOT NULL  DEFAULT (null) ,\"skin_color\" TEXT NOT NULL  DEFAULT (null) ,\"profession\" TEXT NOT NULL  DEFAULT (null) ,\"coexistence\" TEXT NOT NULL  DEFAULT (null),\"phone_number\" TEXT NOT NULL  DEFAULT (null),\"version\" INTEGER NOT NULL  DEFAULT (null),\"ins_date\" TEXT NOT NULL  DEFAULT (null),\"full_name\" TEXT NOT NULL  DEFAULT (null),\"compensated\" INTEGER NOT NULL  DEFAULT (null) );";
    public static final String CREATE_TABLE_COHABITANT = "CREATE TABLE \"cohabitant\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"name\" TEXT NOT NULL  DEFAULT (null) ,\"lastname\" TEXT NOT NULL  DEFAULT (null) ,\"age\" TEXT NOT NULL  DEFAULT (null) ,\"sex\" INTEGER NOT NULL  DEFAULT (null) ,\"familiarity\" TEXT NOT NULL  DEFAULT (null) ,\"phone_number\" TEXT NOT NULL  DEFAULT (null),\"patient_id\" INTEGER NOT NULL  DEFAULT (null), \"fullname\" TEXT NOT NULL  DEFAULT (null)  );";
    public static final String CREATE_TABLE_PHYSICAL_PERFORMANCE = "CREATE TABLE \"prueba_rf\" (\"_id_prueba\" INTEGER PRIMARY KEY NOT NULL  DEFAULT (1) ,\"paciente_id\" INTEGER NOT NULL  DEFAULT (null) ,\"m_tiempo_segundos\" REAL NOT NULL  DEFAULT (null) ,\"m_cantidad_pasos\" INTEGER NOT NULL  DEFAULT (null) ,\"ee_pies_paralelos\" INTEGER NOT NULL  DEFAULT (null) ,\"ee_semi_tandem\" INTEGER NOT NULL  DEFAULT (null) ,\"ee_tandem\" INTEGER NOT NULL  DEFAULT (null) ,\"l_tiempo_segundos\" REAL NOT NULL  DEFAULT (null) ,\"l_lograr_levantadas\" INTEGER NOT NULL  DEFAULT (null) ,\"l_numero_levantadas\" INTEGER NOT NULL  DEFAULT (null) ,\"fa_fuerza_mano_derecha1\" REAL NOT NULL  DEFAULT (null) ,\"fa_fuerza_mano_izquierda1\" REAL NOT NULL  DEFAULT (null) ,\"fa_fuerza_mano_derecha2\" REAL NOT NULL  DEFAULT (null) ,\"fa_fuerza_mano_izquierda2\" REAL NOT NULL  DEFAULT (null) ,\"mc_peso\" REAL NOT NULL  DEFAULT (null),\"mc_talla\" REAL NOT NULL  DEFAULT (null),\"evaluacion_general\" INTEGER NOT NULL  DEFAULT (null),\"puntaje_general\" INTEGER NOT NULL  DEFAULT (null) );";
    public static final String CREATE_TABLE_PATHOLOGICAL_ANTECEDENTS = "CREATE TABLE \"pathological_antecedents\" (\"id_pathological_antecedents\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"id_patient\" INTEGER NOT NULL  DEFAULT (null) ,\"id_check_pathological_ant\" TEXT NOT NULL  DEFAULT (null),\"medication_quantity\" INTEGER NOT NULL  DEFAULT (null) ,\"evaluation\" INTEGER NOT NULL  DEFAULT (null)  );";
    public static final String CREATE_TABLE_SUBJECTIVE_EVALUATION_TEST = "CREATE TABLE \"sub_evaluation_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"question_0\" INTEGER NOT NULL  DEFAULT (null) ,\"question_1a\" INTEGER NOT NULL  DEFAULT (null) ,\"question_1b\" INTEGER NOT NULL  DEFAULT (null),\"question_2\" TEXT NOT NULL  DEFAULT (null),\"question_3\" INTEGER NOT NULL  DEFAULT (null),\"question_4\" INTEGER NOT NULL  DEFAULT (null),\"question_5\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";
    public static final String CREATE_TABLE_PFEIFFER_TEST = "CREATE TABLE \"pfeiffer_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"answers_list_positions\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";
    public static final String CREATE_TABLE_ALERT_SIGNS = "CREATE TABLE \"alert_signs\" (\"id_alert_signs\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"cohabitant_id\" INTEGER NOT NULL  DEFAULT (null) ,\"answers_list_positions\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)  );";
    public static final String CREATE_TABLE_CDR_TEST = "CREATE TABLE \"cdr_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"memory\" INTEGER NOT NULL  DEFAULT (null) ,\"orientation\" INTEGER NOT NULL  DEFAULT (null) ,\"judgement\" INTEGER NOT NULL  DEFAULT (null) ,\"community\" INTEGER NOT NULL  DEFAULT (null) ,\"hobbies\" INTEGER NOT NULL  DEFAULT (null) ,\"personal_care\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null) );";
    public static final String CREATE_TABLE_DEPRESSION_TEST = "CREATE TABLE \"depression_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"answers_list_positions\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";
    public static final String CREATE_TABLE_HHIES_TEST = "CREATE TABLE \"hhies_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"answers_list_positions\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";

    public static final String CREATE_TABLE_KATZ_TEST = "CREATE TABLE \"katz_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"answers_list_positions\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null) );";
    public static final String CREATE_TABLE_LAWTON_TEST = "CREATE TABLE \"lawton_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"answers_list_positions\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";
    public static final String CREATE_TABLE_FRAIL_TEST = "CREATE TABLE \"frail_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"fatigue\" INTEGER NOT NULL  DEFAULT (null) ,\"resistance\" INTEGER NOT NULL  DEFAULT (null) ,\"wandering\" INTEGER NOT NULL  DEFAULT (null),\"diseases\" TEXT NOT NULL  DEFAULT (null),\"weight_current\" INTEGER NOT NULL  DEFAULT (null),\"weight_year_ago\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";

    public static final String CREATE_TABLE_PSYCHOFAMILY_TEST = "CREATE TABLE \"psychofamily_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"answers_list_positions\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";

    public static final String CREATE_TABLE_PSYCHOAFFECTIVE_TEST = "CREATE TABLE \"psychoaffective_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"answers_list_positions\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";

    public static final String CREATE_TABLE_CLASSIFICATION = "CREATE TABLE \"classification\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"antecedents_count\" INTEGER NOT NULL  DEFAULT (null) ,\"medication_quantity\" INTEGER NOT NULL  DEFAULT (null) ,\"subjective_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"katz_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"frail_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"lawton_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"pas_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"paffective_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";
    public static final String CREATE_TABLE_COGNITIVE = "CREATE TABLE \"cognitive\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null) ,\"cdr_evaluation\" INTEGER NOT NULL  DEFAULT (null) ,\"gds_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"hhies_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"mmse_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"pfeiffer_evaluation\" INTEGER NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null)   );";

    public static final String CREATE_TABLE_MIMIMENTAL_TEST = "CREATE TABLE \"minimental_test\" (\"_id\" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (1) ,\"patient_id\" INTEGER NOT NULL  DEFAULT (null),\"temporal_orientation\" TEXT NOT NULL  DEFAULT (null) ,\"spatial_orientation\" TEXT NOT NULL  DEFAULT (null) ,\"retention_register_information\" TEXT NOT NULL  DEFAULT (null),\"math_attention_flag\" INTEGER NOT NULL  DEFAULT (null),\"math_attention\" TEXT NOT NULL  DEFAULT (null) ,\"remembering\" TEXT NOT NULL  DEFAULT (null) ,\"language\" TEXT NOT NULL  DEFAULT (null),\"order_3\" TEXT NOT NULL  DEFAULT (null),\"design\" TEXT NOT NULL  DEFAULT (null),\"evaluation\" INTEGER NOT NULL  DEFAULT (null) );";


    //<--DROP REGION-->
    public static final String DROP_TABLE_PATIENT = "DROP TABLE IF EXISTS \"patient\"";
    public static final String DROP_TABLE_COHABITANT = "DROP TABLE IF EXISTS \"cohabitant\"";
    public static final String DROP_TABLE_PHYSICAL_PERFORMANCE = "DROP TABLE IF EXISTS \"prueba_rf\"";
    public static final String DROP_TABLE_PATHOLOGICAL_ANTECEDENTS = "DROP TABLE IF EXISTS \"pathological_antecedents\"";
    public static final String DROP_TABLE_SUBJECTIVE_EVALUATION_TEST = "DROP TABLE IF EXISTS \"sub_evaluation_test\"";
    public static final String DROP_TABLE_PFEIFFER_TEST = "DROP TABLE IF EXISTS \"pfeiffer_test\"";
    public static final String DROP_TABLE_ALERT_SIGNS = "DROP TABLE IF EXISTS \"alert_signs\"";
    public static final String DROP_TABLE_CDR_TEST = "DROP TABLE IF EXISTS \"cdr_test\"";
    public static final String DROP_TABLE_DEPRESSION_TEST = "DROP TABLE IF EXISTS \"depression_test\"";
    public static final String DROP_TABLE_HHIES_TEST = "DROP TABLE IF EXISTS \"hhies_test\"";

    public static final String DROP_TABLE_KATZ_TEST = "DROP TABLE IF EXISTS \"katz_test\"";
    public static final String DROP_TABLE_LAWTON_TEST = "DROP TABLE IF EXISTS \"lawton_test\"";
    public static final String DROP_TABLE_FRAIL_TEST = "DROP TABLE IF EXISTS \"frail_test\"";
    public static final String DROP_TABLE_PSYCHOFAMILY_TEST = "DROP TABLE IF EXISTS \"psychofamily_test\"";
    public static final String DROP_TABLE_PSYCHOAFFECTIVE_TEST = "DROP TABLE IF EXISTS \"psychoaffective_test\"";

    public static final String DROP_TABLE_CLASSIFICATION = "DROP TABLE IF EXISTS \"classification\"";
    public static final String DROP_TABLE_COGNITIVE = "DROP TABLE IF EXISTS \"cognitive\"";

    //MINIMENTAL
    public static final String DROP_TABLE_MINIMENTAL_TEST = "DROP TABLE IF EXISTS \"minimental_test\"";
    // endregion

    /**
     * Instantiates an open helper for the provider's SQLite data repository
     * Do not do database creation and upgrade here.
     */
    public NEUROGER_DBHelper(Context context) {

        super(context, Constant.DATABASE_FULL_NAME_PATH, null,
                DATABASE_VERSION);

        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // stay cool!!!
        } else {
            String txt = "Problemas con la tarjeta de memoria externa";
            Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
            throw new SQLiteException(txt);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PATIENT);
        db.execSQL(CREATE_TABLE_COHABITANT);
        db.execSQL(CREATE_TABLE_PHYSICAL_PERFORMANCE);
        db.execSQL(CREATE_TABLE_PATHOLOGICAL_ANTECEDENTS);
        db.execSQL(CREATE_TABLE_SUBJECTIVE_EVALUATION_TEST);
        db.execSQL(CREATE_TABLE_PFEIFFER_TEST);
        db.execSQL(CREATE_TABLE_ALERT_SIGNS);
        db.execSQL(CREATE_TABLE_CDR_TEST);
        db.execSQL(CREATE_TABLE_DEPRESSION_TEST);
        db.execSQL(CREATE_TABLE_HHIES_TEST);

        db.execSQL(CREATE_TABLE_KATZ_TEST);
        db.execSQL(CREATE_TABLE_LAWTON_TEST);
        db.execSQL(CREATE_TABLE_FRAIL_TEST);
        db.execSQL(CREATE_TABLE_PSYCHOFAMILY_TEST);
        db.execSQL(CREATE_TABLE_PSYCHOAFFECTIVE_TEST);

        db.execSQL(CREATE_TABLE_CLASSIFICATION);
        db.execSQL(CREATE_TABLE_COGNITIVE);

        //MINIMENTAL
        db.execSQL(CREATE_TABLE_MIMIMENTAL_TEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_PATIENT);
        db.execSQL(DROP_TABLE_COHABITANT);
        db.execSQL(DROP_TABLE_PHYSICAL_PERFORMANCE);
        db.execSQL(DROP_TABLE_PATHOLOGICAL_ANTECEDENTS);
        db.execSQL(DROP_TABLE_SUBJECTIVE_EVALUATION_TEST);
        db.execSQL(DROP_TABLE_PFEIFFER_TEST);
        db.execSQL(DROP_TABLE_ALERT_SIGNS);
        db.execSQL(DROP_TABLE_CDR_TEST);
        db.execSQL(DROP_TABLE_DEPRESSION_TEST);
        db.execSQL(DROP_TABLE_HHIES_TEST);

        db.execSQL(DROP_TABLE_KATZ_TEST);
        db.execSQL(DROP_TABLE_LAWTON_TEST);
        db.execSQL(DROP_TABLE_FRAIL_TEST);
        db.execSQL(DROP_TABLE_PSYCHOFAMILY_TEST);
        db.execSQL(DROP_TABLE_PSYCHOAFFECTIVE_TEST);

        db.execSQL(DROP_TABLE_CLASSIFICATION);
        db.execSQL(DROP_TABLE_COGNITIVE);

        //MINIMENTAL
        db.execSQL(DROP_TABLE_MINIMENTAL_TEST);
    }
}
