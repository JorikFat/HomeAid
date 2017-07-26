package ru.jorik.homeaid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static ru.jorik.homeaid.MedicineDBHandler.dateFormat;

public class MainActivity extends AppCompatActivity
    implements MedicineAdapter.HolderClickListener{

    List<Medicine> tempMedicineList;
    MedicineDBHandler db;
    RecyclerView recyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        db = new MedicineDBHandler(this);

        tempMedicineList = new ArrayList<>();

        tempMedicineList = db.readAll();
        tempMedicineList.add(new Medicine("1 Лекарство 1", shortCall()));
        tempMedicineList.add(new Medicine("4 ЛекарствоБСГ 1"));
        tempMedicineList.add(new Medicine("5 ЛекарствоБСГ 2"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MedicineAdapter medicineAdapter = new MedicineAdapter(this, tempMedicineList);
        medicineAdapter.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(medicineAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addTempItem) {
            addTempMedicine();
            return true;
        } else if(id == R.id.action_refresh){
            refreshRV();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onHolderClick(int numMed) {
        String textToast;
        Medicine med = tempMedicineList.get(numMed);
        if (med.getDateOver() != null) {
            textToast = dateFormat.format(med.getDateOver());
        } else {
            textToast = "Дата не указана";
        }
        Toast.makeText(this, textToast, Toast.LENGTH_SHORT).show();
    }

    private void addTempMedicine(){
        db.createItem(new Medicine("newMedicine", shortCall()));
    }

    //костыль разработки
    public static Date shortCall(){
        return Calendar.getInstance().getTime();
    }

    private void refreshRV(){
        //Не работает
//        tempMedicineList = db.readAll();
//        recyclerView.getAdapter().notifyDataSetChanged();

        //Костыль, который работает
        if (recyclerView.getAdapter() != null) {
            recyclerView.setAdapter(null);
        }
        MedicineAdapter medicineAdapter = new MedicineAdapter(this, db.readAll());
        medicineAdapter.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setAdapter(medicineAdapter);
    }

}
