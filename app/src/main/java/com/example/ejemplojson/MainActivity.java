/*
App que se conecta con openweather usando la ID proporcionada
Por defecto tiene una ciudad "La Rioja" (sin espacios sino no funciona la url)
Y luego muestra la temperatura
 */
package com.example.ejemplojson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


        /*
        Se le envia el nombre de la ciudad sin espacios
        y tambien la ID de openweather para realizar la busqueda web
        Se usa el "execute" de la clase AsyncTask para realizar llamadas
        Asincronas en el fondo y que se conecte a internet
        */

        String openID = "aa43128c1614074c31228079baa6869a";
		String ciudad = "La%20Rioja";//tener cuidado con los espacios

		new LeerClima().execute("http://api.openweathermap.org/data/2.5/weather?q="+ciudad+"&appid="+ openID);
		//http://api.openweathermap.org/data/2.5/weather?q=La%20Rioja&appid=aa43128c1614074c31228079baa6869a
		//http://api.openweathermap.org/data/2.5/weather?q=La%20Rioja&appid=aa43128c1614074c31228079baa6869a
		//http://api.openweathermap.org/data/2.5/weather?q=La%20Rioja&appid=aa43128c1614074c31228079baa6869a
		//http://api.openweathermap.org/data/2.5/weather?q=La%20Rioja&appid=aa43128c1614074c31228079baa6869a
	}

	//Estos son los datos que obtiene el execute
    //Luego doInBackground toma
	//{"coord":{"lon":-66.86,"lat":-29.41},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"base":"stations","main":{"temp":299.179,"pressure":967.39,"humidity":44,"temp_min":299.179,"temp_max":299.179,"sea_level":1023.19,"grnd_level":967.39},"wind":{"speed":2.09,"deg":80.5017},"clouds":{"all":0},"dt":1521659285,"sys":{"message":0.0033,"country":"AR","sunrise":1521628316,"sunset":1521671773},"id":3848950,"name":"La Rioja","cod":200}

	@SuppressLint("StaticFieldLeak")
	public class LeerClima extends AsyncTask<String, Void, String> {
		
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MainActivity.this, "Por favor espere...", "Descargando clima...", true);
			super.onPreExecute();
		}
		
	    //Se usa varags "String... urls" puede ser pasado como un array o como una secuencia de arguments
		@Override
	    protected String doInBackground(String... urls) {
		    InputStream inputStream;
		    StringBuilder result = new StringBuilder();
		    //Se conecta con openweathermap para descargar la informacion requerida

		    try {
		        //Carga la
                inputStream = new URL(urls[0]).openStream();

		        if(inputStream != null) {
			        	BufferedReader buffer = new BufferedReader( new InputStreamReader(inputStream));
			        String line;
			        while ((line = buffer.readLine()) != null) {
						result.append(line);
					}
			 
			        inputStream.close();
		        } //else {
		            // ERROR;
		        //}
		
		    } catch (Exception e) {
	            // ERROR;
		        Log.d("InputStream", e.getLocalizedMessage());
		    }
		    return result.toString();//devuelve automaticamente al metodo onPostExecute los datos web transformados a String
	    }
	

	    //Recibe automaticamente el String result de doInBackground y lo guarda en String texto
	    @SuppressLint("SetTextI18n")
		@Override
	    protected void onPostExecute(String texto) {	
	    		dialog.cancel();

	    		try {
		       	JSONObject json = new JSONObject(texto);
		       	
		       	JSONObject jsonMain = json.getJSONObject("main");
		       	
		       	double temperaturaK = jsonMain.getDouble("temp");
		       	
		       	float temperaturaKFloat = ((int)temperaturaK*100)/100;
		       	float temperaturaC = (float) (temperaturaKFloat-273.15);
		       	
		       	TextView txttemp = (TextView) findViewById(R.id.temp);
		       	
		       	String mensaje = "La temperatura es " + String.valueOf(temperaturaC) + " C";

				Toast.makeText(MainActivity.this,mensaje, Toast.LENGTH_LONG).show();
		       	txttemp.setText(String.valueOf(temperaturaC) + " C");

	    		} catch (Exception e) {
	    			Toast.makeText(MainActivity.this, "Hubo un problema", Toast.LENGTH_LONG).show();
	    		}
	    }
	    
	}
	
}
