package ru.jorik.homeaid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static ru.jorik.homeaid.LittleUtils.getCustomCalendar;
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
//            addTempMedicine();
            addNewMedicine();
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

    private void addTempMedicine(Medicine newMedicine){
        tempMedicineList.add(newMedicine);
        db.createItem(newMedicine);
    }



    private void refreshRV(){
        //Не работает
//        tempMedicineList = db.readAll();
        recyclerView.getAdapter().notifyDataSetChanged();

        //Костыль, который работает
//        if (recyclerView.getAdapter() != null) {
//            recyclerView.setAdapter(null);
//        }
//        MedicineAdapter medicineAdapter = new MedicineAdapter(this, db.readAll());
//        medicineAdapter.setOnClickListener(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,
//                LinearLayoutManager.VERTICAL, false));
//        recyclerView.setAdapter(medicineAdapter);
    }

    public void addNewMedicine(){
//        ConstraintLayout dialogLayout = (ConstraintLayout) findViewById(R.id.dialog_input);
        ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_input_medicine, null);
        final EditText editText = (EditText) dialogLayout.findViewById(R.id.input_newMedicineName);
        final DatePicker datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        Calendar tempCalendar = Calendar.getInstance();
        int year = tempCalendar.get(Calendar.YEAR);
        int month = tempCalendar.get(Calendar.MONTH);
        int day = tempCalendar.get(Calendar.DAY_OF_MONTH);

        datePicker.updateDate(year, month, day);//установка сегоднешней даты для отображения
        new AlertDialog.Builder(this)
                .setTitle("Новый препарат")
                .setView(dialogLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth()+1;
                        int day = datePicker.getDayOfMonth();
                        Calendar calendar = getCustomCalendar(year, month, day);
                        Medicine medicine = new Medicine(String.valueOf(editText.getText()), calendar.getTime());
                        addTempMedicine(medicine);
                        refreshRV();
                    }
                })
                .show();
    }

}
