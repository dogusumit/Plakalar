package com.dogusumit.plakalar;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    EditText editText;
    String[] liste;
    String cevap;

    final Context context = this;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    plakaListesiYukle();
                    return true;
                case R.id.navigation_notifications:
                    oyunYukle();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listView = (ListView) findViewById(R.id.listview1);
        liste = getResources().getStringArray(R.array.iller);
        for ( int i=0; i<liste.length; i++ )
            liste[i] = String.format(Locale.US,"%02d     ", i+1) + liste[i];
        final ArrayAdapter<String> adapter_list = new ArrayAdapter<>
                (getApplicationContext() ,R.layout.list_beyaz,liste);
        listView.setAdapter(adapter_list);



        editText = (EditText) findViewById(R.id.editText1);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrele(s.toString());
            }
        });

        plakaListesiYukle();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    void plakaListesiYukle() {
        try {
            findViewById(R.id.plakalistelayout).setVisibility(View.VISIBLE);
            findViewById(R.id.oyunlayout).setVisibility(View.GONE);

            findViewById(R.id.plakalistelayout).setEnabled(true);
            findViewById(R.id.oyunlayout).setEnabled(false);
        } catch (Exception e) {
            toastla(e.getMessage());
        }
    }

    void oyunYukle() {
        try {
            findViewById(R.id.plakalistelayout).setVisibility(View.GONE);
            findViewById(R.id.oyunlayout).setVisibility(View.VISIBLE);

            findViewById(R.id.plakalistelayout).setEnabled(false);
            findViewById(R.id.oyunlayout).setEnabled(true);

            AdView mAdView;
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            soruGetir();
            skorGetir();
            butonListenerAta();

        } catch (Exception e) {
            toastla(e.getMessage());
        }
    }

    void filtrele (String s1) {
        try {
            ArrayList<String> yeniliste = new ArrayList<>();
            for (String s2 : liste)
                if (s2.toLowerCase().contains(s1.toLowerCase()))
                    yeniliste.add(s2);
            listView.setAdapter(new ArrayAdapter<>
                    (getApplicationContext(), R.layout.list_beyaz, yeniliste));
        } catch (Exception e) {
            toastla(e.getMessage());
        }
    }

    void toastla (String s) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    void soruGetir () {

        try {
            String[] dizi = getResources().getStringArray(R.array.iller);
            Random generator = new Random();
            int indis = generator.nextInt(81);
            cevap = dizi[indis];
            ((TextView)findViewById(R.id.textView1)).setText(String.format(Locale.US,"%02d", indis+1));

            int bas = indis - 3;
            int son = indis + 3;
            if (bas < 0) {
                if (indis != 0) {
                    bas = 0;
                } else {
                    bas = 1;
                }
            }
            if (son > 80) {
                if (indis != 80) {
                    son = 80;
                } else {
                    son = 79;
                }
            }

            Stack<String> havuz = new Stack<>();
            for (int i = bas; i <= son; i++) {
                if(!dizi[i].equals(cevap))
                    havuz.push(dizi[i]);
            }
            Collections.shuffle(havuz);

            Stack<Button> havuz2 = new Stack<>();
            havuz2.push(((Button) findViewById(R.id.btnA)));
            havuz2.push(((Button) findViewById(R.id.btnB)));
            havuz2.push(((Button) findViewById(R.id.btnC)));
            havuz2.push(((Button) findViewById(R.id.btnD)));
            Collections.shuffle(havuz2);

            havuz2.pop().setText(cevap);
            havuz2.pop().setText(havuz.pop());
            havuz2.pop().setText(havuz.pop());
            havuz2.pop().setText(havuz.pop());
        }catch (Exception e) {
            toastla(e.getMessage());
        }
    }

    void skorGetir() {
        try {
            SharedPreferences prefs = getSharedPreferences("skorlar", MODE_PRIVATE);
            int dogru = prefs.getInt("dogru", 0);
            int yanlis = prefs.getInt("yanlis", 0);

            ((TextView)findViewById(R.id.textView2)).setText((getString(R.string.dogru)+dogru));
            ((TextView)findViewById(R.id.textView3)).setText((getString(R.string.yanlis)+yanlis));

        } catch (Exception e) {
            toastla( e.getMessage() );
        }
    }

    View.OnClickListener klikListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                SharedPreferences prefs = getSharedPreferences("skorlar", MODE_PRIVATE);
                int dogru = prefs.getInt("dogru", 0);
                int yanlis = prefs.getInt("yanlis", 0);
                SharedPreferences.Editor editor = getSharedPreferences("skorlar", MODE_PRIVATE).edit();
                if (cevap.equals(((Button) v).getText())) {
                    editor.putInt("dogru", dogru + 1);
                } else {
                    editor.putInt("yanlis", yanlis + 1);
                }
                editor.apply();
                soruGetir();
                skorGetir();
            } catch (Exception e) {
                toastla(e.getMessage());
            }
        }
    };

    void butonListenerAta() {
        findViewById(R.id.btnA).setOnClickListener(klikListener);
        findViewById(R.id.btnB).setOnClickListener(klikListener);
        findViewById(R.id.btnC).setOnClickListener(klikListener);
        findViewById(R.id.btnD).setOnClickListener(klikListener);
    }


    private void uygulamayiOyla()
    {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            } catch (Exception ane) {
                toastla(e.getMessage());
            }
        }
    }

    private void marketiAc()
    {
        try {
            Uri uri = Uri.parse("market://developer?id="+getString(R.string.play_store_id));
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id="+getString(R.string.play_store_id))));
            } catch (Exception ane) {
                toastla(e.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sifirla:
                istatistikSifirla();
                return true;
            case R.id.oyla:
                uygulamayiOyla();
                return true;
            case R.id.market:
                marketiAc();
                return true;
            case R.id.cikis:
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void istatistikSifirla() {
        try {
            SharedPreferences.Editor editor = getSharedPreferences("skorlar", MODE_PRIVATE).edit();
            editor.putInt("dogru", 0);
            editor.putInt("yanlis", 0);
            editor.apply();
            skorGetir();
        } catch (Exception e) {
            toastla(e.getMessage());
        }
    }
}
