package co.com.eleinco.cowsurfing;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
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
    private RecyclerView recyclerView;
    private adaptador adapter;
    public static zonas zone = new zonas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adaptador adapter = new adaptador();
        recyclerView = (RecyclerView) findViewById(R.id.rv_Tabla);
        recyclerView.setAdapter(adapter);

        zoneArray = new ArrayList<Long>();
        tagArray = new ArrayList<Long>();
        idArray = new ArrayList<Long>();
        ult_Ubicacion = new ArrayList<String>();

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
                    sharedPreferences = getApplicationContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putInt("zona1",zone1);
                    editor.putInt("zona2",zone2);
                    editor.putInt("zona3",zone3);
                    System.out.println("zona1**********************" +zone2);
                    zone.setZona1(zone1);
                    zone.setZona2(zone2);
                    zone.setZona3(zone3);
                    adaptador adapter = new adaptador();
                    adapter.notifyDataSetChanged();
                    System.out.println("zonaaaaaaaaaa1**********************" +zone.getZona1());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private class adaptador extends RecyclerView.Adapter<adaptador.plantilla>{



        public adaptador() {

        }

        @Override
        public adaptador.plantilla onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_tabla,parent,false);
            return new plantilla(view);
        }

        @Override
        public void onBindViewHolder(adaptador.plantilla holder, int position) {

            if(position==0){
                holder.tvId.setText("Id");
                holder.tvId.setTextSize(20);
                holder.tvId.setTextColor(getResources().getColor(R.color.negro));
                holder.tvZona.setText("Zona");
                holder.tvZona.setTextSize(20);
                holder.tvZona.setTextColor(getResources().getColor(R.color.negro));
                holder.tvHato.setText("Cantidad");
                holder.tvHato.setTextSize(20);
                holder.tvHato.setTextColor(getResources().getColor(R.color.negro));
                holder.tvFecha.setText("Fecha");
                holder.tvFecha.setTextSize(20);
                holder.tvFecha.setTextColor(getResources().getColor(R.color.negro));
            }else{
                holder.tvId.setText(Integer.toString(position));
                holder.tvZona.setText(Integer.toString(position));
                if(position == 1){
                    System.out.println("zonaa para comprobar" + zone.getZona1());
                    holder.tvHato.setText(Integer.toString(sharedPreferences.getInt("zona1",-1)));
                    System.out.println("The read failed1: " + sharedPreferences.getInt("zona1",-1));
                }else if(position == 2){
                    holder.tvHato.setText(Integer.toString(sharedPreferences.getInt("zona2",-1)));
                    System.out.println("The read failed2: " + zone.getZona2());
                }else if(position == 3){
                    holder.tvHato.setText(Integer.toString(sharedPreferences.getInt("zona3",-1)));
                    System.out.println("The read failed3: " + zone.getZona3());
                }
                holder.tvFecha.setText("25/09/2016");
            }

        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class plantilla extends RecyclerView.ViewHolder{

            TextView tvId;
            TextView tvZona;
            TextView tvHato;
            TextView tvFecha;

            public plantilla(View itemView) {
                super(itemView);

                tvId = (TextView) itemView.findViewById(R.id.id);
                tvZona = (TextView) itemView.findViewById(R.id.tvZona);
                tvHato = (TextView) itemView.findViewById(R.id.tvHatos);
                tvFecha = (TextView) itemView.findViewById(R.id.tvFecha);
            }
        }

    }

    public static class zonas{
        int zona1;
        int zona2;
        int zona3;

        public zonas() {
        }

        public int getZona1() {return zona1;}
        public int getZona2() {return zona2;}
        public int getZona3() {return zona3;}

        public void setZona1(int zona1) {
            this.zona1 = zona1;
        }

        public void setZona2(int zona2) {
            this.zona2 = zona2;
        }

        public void setZona3(int zona3) {
            this.zona3 = zona3;
        }
    }

}
