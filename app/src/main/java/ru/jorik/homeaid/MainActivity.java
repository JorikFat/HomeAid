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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static ru.jorik.homeaid.LittleUtils.dateFormat;
import static ru.jorik.homeaid.LittleUtils.getCustomCalendar;

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
        int id = item.getItemId();

        if (id == R.id.action_addTempItem) {
            addNewMedicine();
            return true;
        } else if(id == R.id.action_refresh){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onHolderClick(int numMed) {
        Medicine med = tempMedicineList.get(numMed);
        showMedicineDetails(med);
    }

    private void addMedicineDB(Medicine newMedicine){
        /*
        * Вот тут вообще костыль:
        * для того, чтобы получить id записи из базы данных сначала делается запись объекта
        * потом считывается этот объект из базы данных
        * и только тогда присваивается ему id.
        * Иначе это не работает, потому что невозможно узнать, какой последний id был добавлен в бд,
        * так как последнее добавление можно удалить и тогда будет крах
        * */
        tempMedicineList.add(newMedicine);
        db.createItem(newMedicine);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void editMedicineDB(int index, Medicine medicine){
        tempMedicineList.set(index, medicine);
        db.updateItem(medicine.getId(), medicine);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void removeMedicineDB(int index, long id){
        tempMedicineList.remove(index);
        db.deleteItem(id);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void addNewMedicine(){
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
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth()+1;
                        int day = datePicker.getDayOfMonth();
                        Calendar calendar = getCustomCalendar(year, month, day);
                        Medicine medicine = new Medicine(String.valueOf(editText.getText()), calendar.getTime());
                        addMedicineDB(medicine);
                    }
                })
                .show();
    }

    public void showMedicineDetails(final Medicine medicine){
        View view = getLayoutInflater().inflate(R.layout.dialog_details, null);
        ((TextView) view.findViewById(R.id.details_name)).setText(medicine.getName());
        ((TextView) view.findViewById(R.id.detail_dateOver)).setText(dateFormat.format(medicine.getDateOver()));
        ((TextView) view.findViewById(R.id.textView3)).setText(String.valueOf(medicine.getId()));
        new AlertDialog.Builder(this)
                .setTitle("Препарат")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        updateMedicine(medicine);
                    }
                })
                .show();
    }

    public void updateMedicine(final Medicine medicine){
        final int index = tempMedicineList.indexOf(medicine);
        final long id = medicine.getId();
        ConstraintLayout dialogLayout = (ConstraintLayout) getLayoutInflater()
                .inflate(R.layout.dialog_input_medicine, null);
        final EditText editText = (EditText) dialogLayout.findViewById(R.id.input_newMedicineName);
        final DatePicker datePicker = (DatePicker) dialogLayout.findViewById(R.id.datePicker);
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(medicine.getDateOver());
        int year = tempCalendar.get(Calendar.YEAR);
        int month = tempCalendar.get(Calendar.MONTH);
        int day = tempCalendar.get(Calendar.DAY_OF_MONTH);
        editText.setText(medicine.getName());

        datePicker.updateDate(year, month, day);
        new AlertDialog.Builder(this)
                .setTitle("Изменить препарат")
                .setView(dialogLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Calendar calendar = getCustomCalendar(year, month, day);
                        Medicine medicine = new Medicine(id, String.valueOf(editText.getText()), calendar.getTime());
                        editMedicineDB(index, medicine);
                    }
                })
                .setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeMedicineDB(index, id);
                    }
                })
                .show();
    }
}
