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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements MedicineAdapter.HolderClickListener{

    List<Medicine> tempMedicineList;
    
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

        tempMedicineList = new ArrayList<>();
        tempMedicineList.add(new Medicine("1 Лекарство 1", Calendar.getInstance().getTime()));
        tempMedicineList.add(new Medicine("2 Лекарство 2", Calendar.getInstance().getTime()));
        tempMedicineList.add(new Medicine("3 Лекарство 3", Calendar.getInstance().getTime()));
        tempMedicineList.add(new Medicine("4 ЛекарствоБСГ 1"));
        tempMedicineList.add(new Medicine("5 ЛекарствоБСГ 2"));
        tempMedicineList.add(new Medicine("6 Лекарство 4", Calendar.getInstance().getTime()));
        tempMedicineList.add(new Medicine("7 Лекарство 5", Calendar.getInstance().getTime()));
        tempMedicineList.add(new Medicine("8 ЛекарствоБСГ 3"));
        tempMedicineList.add(new Medicine("9 ЛекарствоБСГ 4"));
        tempMedicineList.add(new Medicine("10 Лекарство 6", Calendar.getInstance().getTime()));
        tempMedicineList.add(new Medicine("11 Лекарство 7", Calendar.getInstance().getTime()));
        tempMedicineList.add(new Medicine("12 Лекарство 8", Calendar.getInstance().getTime()));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onHolderClick(int numMed) {
        String textToast;
        Medicine med = tempMedicineList.get(numMed);
        if (med.getDateOver() != null) {
            textToast = new SimpleDateFormat("d.MM.yyyy").format(med.getDateOver());
        } else {
            textToast = "Дата не указана";
        }
        Toast.makeText(this, textToast, Toast.LENGTH_SHORT).show();
    }
}
