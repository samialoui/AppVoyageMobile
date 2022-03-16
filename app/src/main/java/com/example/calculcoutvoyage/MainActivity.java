package com.example.calculcoutvoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
  public String Gouvernorats []={"bizert","sousse","tunis"};
  public String typeEssance[] ={"Super Sans Blomb","Gasoil Super","Gasoil"};

  String typeEss = "Super Sans Blomb";
     //tunis --> sousse: 149.68 km
    // tunis --> bizert: 71.01
    //sousse --> bizert: 212.10
    public Double distances[][]={
             {0.0,212.10,71.10},
             {212.10,0.0,149.68},
             {71.01,149.68,0.0}

     };

    public String source = "bizert"; // mettre le spinner par deffaut
    public String destination = "bizert";

    EditText consEssanceTxt;
    TextView _textResult, _textDistance,_textPrixEssance;
    Spinner _spnSource,_spnDestination,_spnPrixLitre;
    RadioButton _btnNational, _btnAutoroute;
    CheckBox _checkEau,_checkCafe,_checkPizza;
    Button _btnCalculerCout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consEssanceTxt = (EditText) findViewById(R.id.consEssenText);
        _textResult = (TextView) findViewById(R.id.textResult);
        _textDistance = (TextView) findViewById(R.id.textDistance);
        _textPrixEssance = (TextView) findViewById(R.id.textPrixEssence);
        _spnSource= (Spinner) findViewById(R.id.spnSource);
        _spnDestination=(Spinner) findViewById(R.id.spnDestination);
        _spnPrixLitre=(Spinner) findViewById(R.id.spnType);
        _btnNational=(RadioButton) findViewById(R.id.rdbRouteNatio);
        _btnAutoroute=(RadioButton) findViewById(R.id.rdbAutoroute);
        _btnCalculerCout=(Button) findViewById(R.id.btnCalCoutVoyage);
        _checkEau=(CheckBox) findViewById(R.id.checkEau);
        _checkCafe=(CheckBox) findViewById(R.id.checkCafe);
        _checkPizza=(CheckBox) findViewById(R.id.checkBox);


        //remplir les spinner (comboBox) par les données correspondants

        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Gouvernorats);
        _spnSource.setAdapter(adapt);
        _spnDestination.setAdapter(adapt);
        ArrayAdapter<String> EssenceAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,typeEssance);
        _spnPrixLitre.setAdapter(EssenceAdapt);

// calculer distance
        _spnSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Double dis = 0.0;
                source = _spnSource.getSelectedItem().toString();
                dis = selectDistance(source,destination);
                _textDistance.setText(dis.toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        _spnDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Double dis = 0.0;
                destination = _spnDestination.getSelectedItem().toString();
                dis= selectDistance(source,destination);
                _textDistance.setText(dis.toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //afficher prix Essence
        _spnPrixLitre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             Double prix = 0.0;
             typeEss = _spnPrixLitre.getSelectedItem().toString();
             prix = PrixEssence(typeEss);
             _textPrixEssance.setText(prix.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Calculer le cout global
        _btnCalculerCout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String v1 = consEssanceTxt.getText().toString().trim();
                Double consVoiture = 0.0;
                if(v1.isEmpty()){
                    consVoiture = 0.0;
                }else{
                    consVoiture = Double.parseDouble(consEssanceTxt.getText().toString().trim());
                }

               Double distanceKm = Double.parseDouble(_textDistance.getText().toString().trim());
               Double prEssence = Double.parseDouble(_textPrixEssance.getText().toString().trim());

               Double consommation = 0.0;
               //par deffaut on choisie Route nationale
                Double piage = 0.0;
                if(distanceKm > 0.0){
                    if(_checkEau.isChecked()){
                        consommation = consommation + 1.5;
                    }
                    if(_checkCafe.isChecked()){
                        consommation = consommation + 2.5;
                    }
                    if(_checkPizza.isChecked()){
                        consommation = consommation + 6.0;
                    }
                    if (_btnAutoroute.isChecked()){
                        if((source.equals("bizert") && destination.equals("tunis")) || (source.equals("tunis") && destination.equals("bizert")) )
                            consommation = consommation + 1.400;
                       else if((source.equals("sousse") && destination.equals("tunis")) || (source.equals("tunis") && destination.equals("sousse")) )
                            consommation = consommation + 3.200;
                        else if((source.equals("bizert") && destination.equals("sousse")) || (source.equals("sousse") && destination.equals("bizert")) )
                            consommation = consommation + 5.100;
                    }
                }

                Double TotalConsommationVoiture = (Double) ((consVoiture/100)*distanceKm*prEssence);
                Double TotalConsommation = TotalConsommationVoiture + consommation;

                DecimalFormat f = new DecimalFormat("##.000");
                String formatedValue = f.format(TotalConsommation);
                _textResult.setText("Le Resultat est "+formatedValue+" DT");

            }
        });

    }



    private int returnIndex(String str){
        int index = -1;
        for (int i=0;i<=Gouvernorats.length; i++){
            if(Gouvernorats[i].equalsIgnoreCase(str)){
                index= i;
                break;
            }
        }
        return  index;
    }

    public Double selectDistance(String source, String destination){

        int isource, idestination;
        isource = returnIndex(source);
        idestination = returnIndex(destination);

        return distances[isource][idestination];
    }

    //"Super Sans Blomb","Gasoil Super","Gasoil"
    public Double PrixEssence(String typeEssence){
        Double prix = 0.0;
        switch (typeEssence){
            case "Super Sans Blomb":
                prix = 1.95;
                break;
            case "Gasoil Super":
                prix = 2.54;
                break;
            case "Gasoil":
                prix = 1.65;
                break;
            default:
                prix = 0.0;
                break;
        }
        return prix;
    }
}