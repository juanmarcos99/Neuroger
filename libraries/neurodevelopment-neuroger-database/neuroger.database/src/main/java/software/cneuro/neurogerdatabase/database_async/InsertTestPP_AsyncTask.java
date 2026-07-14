package software.cneuro.neurogerdatabase.database_async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import software.cneuro.neurogerdatabase.constant.Constant;

/**
 * Created by exel.rodriguez on 21/09/2015.
 */
public class InsertTestPP_AsyncTask extends AsyncTask<Void, Void, Long> {

    @Override
    protected Long doInBackground(Void... params) {

        Uri result = insertTestPP();
        return Constant.getIdFromUri(result);

    }

    private final long id_paciente;
    private final double m_tiempo_seg;
    private final int m_cant_pasos;
    private final int ee_pies_paralelos;
    private final int ee_semi_tandem;
    private final int ee_tandem;
    private final double l_tiempo_seg;
    private final int l_lograr_levantadas;
    private final int l_numero_levantadas;
    private final double fa_fuerza_mano_der1;
    private final double fa_fuerza_mano_izq1;
    private final double fa_fuerza_mano_der2;
    private final double fa_fuerza_mano_izq2;
    private final double mc_peso;
    private final double mc_talla;
    private final int evaluacion_gen;
    private final double puntaje_gen;

    private final OnTestPPInserted mCallback;

    private final ContentResolver cr;

    public InsertTestPP_AsyncTask(Context context, long id_paciente,
                                  double m_tiempo_seg, int m_cant_pasos, int ee_pies_paralelos,
                                  int ee_semi_tandem, int ee_tandem,
                                  double l_tiempo_seg, int l_lograr_levantadas,
                                  int l_numero_levantadas, double fa_fuerza_mano_der1,
                                  double fa_fuerza_mano_izq1, double fa_fuerza_mano_der2,
                                  double fa_fuerza_mano_izq2, double mc_peso, double mc_talla,
                                  int evaluacion_gen, double puntaje,
                                  InsertTestPP_AsyncTask.OnTestPPInserted callback) {

        this.id_paciente = id_paciente;
        this.m_tiempo_seg = m_tiempo_seg;
        this.m_cant_pasos = m_cant_pasos;
        this.ee_pies_paralelos = ee_pies_paralelos;
        this.ee_semi_tandem = ee_semi_tandem;
        this.ee_tandem = ee_tandem;
        this.l_tiempo_seg = l_tiempo_seg;
        this.l_lograr_levantadas = l_lograr_levantadas;
        this.l_numero_levantadas = l_numero_levantadas;
        this.fa_fuerza_mano_der1 = fa_fuerza_mano_der1;
        this.fa_fuerza_mano_izq1 = fa_fuerza_mano_izq1;
        this.fa_fuerza_mano_der2 = fa_fuerza_mano_der2;
        this.fa_fuerza_mano_izq2 = fa_fuerza_mano_izq2;
        this.mc_peso = mc_peso;
        this.mc_talla = mc_talla;
        this.evaluacion_gen = evaluacion_gen;
        this.puntaje_gen = puntaje;

        this.mCallback = callback;

        this.cr = context.getContentResolver();
    }

    private Uri insertTestPP() {


        ContentValues values = new ContentValues();

        values.put(Constant.COL_PRUEBA_RF_PACIENTE_ID, id_paciente);
        values.put(Constant.COL_PRUEBA_RF_MARCHA_TIEMPO_SEGUNDOS, m_tiempo_seg);
        values.put(Constant.COL_PRUEBA_RF_MARCHA_CANTIDAD_PASOS, m_cant_pasos);
        values.put(Constant.COL_PRUEBA_RF_EE_PIES_PARALELOS, ee_pies_paralelos);
        values.put(Constant.COL_PRUEBA_RF_EE_SEMI_TANDEM, ee_semi_tandem);
        values.put(Constant.COL_PRUEBA_RF_EE_TANDEM, ee_tandem);
        values.put(Constant.COL_PRUEBA_RF_LEVANTADAS_TIEMPO_SEGUNDOS, l_tiempo_seg);
        values.put(Constant.COL_PRUEBA_RF_LEVANTADAS_LOGRAR_LEVANTADAS, l_lograr_levantadas);
        values.put(Constant.COL_PRUEBA_RF_LEVANTADAS_NUMERO_LEVANTADAS, l_numero_levantadas);
        values.put(Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA1, fa_fuerza_mano_der1);
        values.put(Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA1, fa_fuerza_mano_izq1);
        values.put(Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_DERECHA2, fa_fuerza_mano_der2);
        values.put(Constant.COL_PRUEBA_RF_FA_FUERZA_MANO_IZQUIERDA2, fa_fuerza_mano_izq2);
        values.put(Constant.COL_PRUEBA_RF_MC_PESO, mc_peso);
        values.put(Constant.COL_PRUEBA_RF_MC_TALLA, mc_talla);
        values.put(Constant.COL_PRUEBA_RF_EVALUACION_GENERAL, evaluacion_gen);
        values.put(Constant.COL_PRUEBA_RF_PUNTAJE_GENERAL, puntaje_gen);


        // insercion a la base de datos mediante la URI y el conjunto de valores.

        return cr.insert(Constant.URI_TABLE_PRUEBA_RF, values);

    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        mCallback.onTestPPInserted(result);
    }

    public interface OnTestPPInserted {
        void onTestPPInserted(long id);
    }
}
