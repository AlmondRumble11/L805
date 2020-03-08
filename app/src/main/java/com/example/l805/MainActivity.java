package com.example.l805;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button pois;
    Button osta;
    Button lisaa;
    TextView teksti;
    TextView viesti;
    EditText valinta;
    SeekBar rahan_muutos;
    Spinner valikkoPullo;
    Spinner valikkoKoko;
    Context context;
    private  int seekbar_rahanmaara;
    private  int kuittinumero  = 1;
    private  double raha_alussa  = 0;
    private  double raha_lopussa  = 0;
    private double raha = 0;
    private int ticket = 1;
    private int tarkistus_kuittia_varten = 0;
    private List<String> nimilista;
    private String pullo;
    private String koko;
    private String tiedosto;
    private String kuitin_sisalto;
    private String viimeinen_osostos = "";
    private double viimeisen_ostoksen_hinta = 0;
    int tarkistus;

    BottleDispencer bd = BottleDispencer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = MainActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pois = (Button) findViewById(R.id.nosto);
        osta = (Button) findViewById(R.id.osta);
        lisaa = (Button) findViewById(R.id.lisaa);
        teksti = (TextView) findViewById(R.id.ui);
        viesti = (TextView) findViewById(R.id.tulostus);
        //valinta = (EditText) findViewById(R.id.valinta);
        rahan_muutos = (SeekBar) findViewById(R.id.seekBar);
        seekbar_rahanmaara = rahan_muutos.getProgress();

        rahan_muutos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int maara, boolean b) {
                seekbar_rahanmaara = maara;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ilmotus("You are setting " + seekbar_rahanmaara + "€ to your balance");
            }
        });
        if (ticket == 1) {
            bd.lisaaPullot();
            ticket = 0;
        }
        viesti.setText("You have " + String.format("%.2f", bd.getMoney()) + "€ to spend");

        valikkoPullo = (Spinner) findViewById(R.id.pullospinner);
        List<String> nimilista = new ArrayList<String>();
        nimilista.add("Pepsi Max");
        nimilista.add("Coca-Cola Zero");
        nimilista.add("Fanta Zero");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nimilista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valikkoPullo.setAdapter(adapter);

        valikkoKoko = (Spinner) findViewById(R.id.kokospinner);
        List<String> kokolista = new ArrayList<String>();
        kokolista.add("0.5");
        kokolista.add("1.5");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kokolista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valikkoKoko.setAdapter(adapter2);
        tulostapullo();

    }
    public void tulostapullo(){
            int n = 0;
            teksti.setText("");
            for (Bottle i : bd.getList()) {
                teksti.append((n + 1) + ". Name: " + i.getName() + "\n" + "	Size: " + i.getSize() + "   Price: " + i.getPrice() + "\n");
                n++;
            }
        }


    public void tulostaKuitti(View v){
        if (tarkistus_kuittia_varten == 0){
            ilmotus("You have to buy a bottle of soda first before getting a receipt");
        }else {
            try {
                tiedosto = "kuitti" + kuittinumero + ".txt";
                kuitin_sisalto = "****Receipt****\n" + "You bought: " + viimeinen_osostos + "l\n It cost: " + String.format("%.2f",viimeisen_ostoksen_hinta) + "€\n";
                OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(tiedosto, Context.MODE_PRIVATE));
                System.out.println(kuitin_sisalto);
                ows.write(kuitin_sisalto);
                ows.close();
                kuittinumero++;

            } catch (IOException e) {
                Log.e("IOException", "Virhe syötteessä");
            } finally {
                System.out.println("Kirjoitettu");
            }
        }
    }


    public void addMoney(View v){
        //double lisays = 1;
        bd.addMoney(seekbar_rahanmaara);
        viesti.setText("You have "+String.format("%.2f", bd.getMoney())+"€ to spend");
        ilmotus("Added "+seekbar_rahanmaara+"€ to balance");
        rahan_muutos.setProgress(0);


    }
    public void returnMoney(View v){
        ilmotus("Klink klink. Money came out! You got "+String.format("%.2f", bd.getMoney())+"€ back");
        raha = bd.returnMoney();
        viesti.setText("You have "+String.format("%.2f", bd.getMoney())+"€ to spend");

    }



    public void buyBottle(View v) {
        pullo = valikkoPullo.getSelectedItem().toString();
        koko = valikkoKoko.getSelectedItem().toString();
        try {
            raha_alussa = bd.getMoney();

            int kokoalussa = bd.getList().size();
            tarkistus = bd.buyBottle(pullo,koko);
            int kokolopussa = bd.getList().size();
            raha_lopussa = bd.getMoney();
            if (bd.getList().size() == 0){
                ilmotus("Sorry no more bottles. Machine is empty");
            }
            else if (kokoalussa != kokolopussa) {
                ilmotus("You got soda " + pullo + ". Hope you enjoy it!!");
                viimeinen_osostos = pullo+" " +koko;
                viimeisen_ostoksen_hinta =raha_alussa - raha_lopussa;
                tarkistus_kuittia_varten = 1;
            }else if(tarkistus == 0){
                ilmotus("No more that kind of bottles");
            }else if (tarkistus == 2){
                ilmotus("Not enough money!");
            }
            int n = 0;
            //teksti.setText("");
            /*for (Bottle i : bd.getList()) {
               teksti.append((n + 1) + ". Name: " + i.getName() + "\n" + "	Size: " + i.getSize() + "   Price: " + i.getPrice() + "\n");
               n++;
            }*/

            viesti.setText("You have " + String.format("%.2f", bd.getMoney()) + "€ to spend");
        }catch(NumberFormatException nfe){
            ilmotus("Please select one of the sodas shown");
        }


    }
    public void ilmotus(String s){
        Toast.makeText(MainActivity.this, s,Toast.LENGTH_SHORT).show();
    }
}
