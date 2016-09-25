package co.com.eleinco.cowsurfing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvZona1;
    private TextView tvZona2;
    private TextView tvZona3;
    private TextView tvFechaActualizacion;
    private TextView tvVacasSensadas;
    private TextView tvVacasRegistradas;
    private Firebase rootRef;
    private ArrayList<Long> tagArray;
    private ArrayList<Long> zoneArray;
    private ArrayList<Long> idArray;
    private ArrayList<String> ult_Ubicacion;
    private int zone1;
    private int zone2;
    private int zone3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zoneArray = new ArrayList<Long>();
        tagArray = new ArrayList<Long>();
        idArray = new ArrayList<Long>();
        ult_Ubicacion = new ArrayList<String>();
        tvFechaActualizacion = (TextView) findViewById(R.id.tvFechaActualizacion);
        tvVacasRegistradas = (TextView) findViewById(R.id.tvVacasRegistradas);
        tvVacasSensadas = (TextView) findViewById(R.id.tvVacasSensadas);
        tvZona1 = (TextView) findViewById(R.id.tvVacasZona1);
        tvZona2 = (TextView) findViewById(R.id.tvVacasZona2);
        tvZona3 = (TextView) findViewById(R.id.tvVacasZona3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_actualizar){
            Firebase.setAndroidContext(this);
            rootRef = new Firebase("https://cowsurfing-35cfc.firebaseio.com/ult_ubicacion");

            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    zone1 = 0;
                    zone2 = 0;
                    zone3 = 0;
                    ult_Ubicacion.clear();
                    for (DataSnapshot s: dataSnapshot.getChildren()) {
                        ult_Ubicacion.add(s.child("clientId").getValue(Long.class).toString()+","+s.child("zoneId").getValue(Long.class).toString()+","+s.child("tagId").getValue(Long.class).toString()+","+s.child("dateTime").getValue(Long.class).toString());
                    }

                    for(int i = 0; i<ult_Ubicacion.size();i++){
                        if(ult_Ubicacion.get(i).split(",")[1].equals("1")){
                            zone1++;
                        }else if(ult_Ubicacion.get(i).split(",")[1].equals("2")){
                            zone2++;
                        }else if(ult_Ubicacion.get(i).split(",")[1].equals("3")){
                            zone3++;
                        }
                    }

                    tvZona1.setText("Zona 1: " + zone1);
                    tvZona2.setText("Zona 2: " + zone2);
                    tvZona3.setText("Zona 3: " + zone3);
                    tvVacasSensadas.setText(""+ult_Ubicacion.size());
                    tvFechaActualizacion.setText("25/09/2016");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
