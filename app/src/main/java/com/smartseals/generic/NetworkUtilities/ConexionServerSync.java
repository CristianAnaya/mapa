package com.smartseals.generic.NetworkUtilities;

import android.content.Context;
import android.os.AsyncTask;


public class ConexionServerSync {

    public class getDescargosTask extends AsyncTask<Void,Void, Void> {
        private Context mContext;
        private String  usuarioId;

        public getDescargosTask(String usuarioId) {
            this.usuarioId = usuarioId;

        }

        @Override
        protected Void doInBackground(Void... voids) {


            return null;

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);





        }

        @Override
        protected void onCancelled() {

        }
    }

}
