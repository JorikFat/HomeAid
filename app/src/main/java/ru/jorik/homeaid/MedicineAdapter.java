package ru.jorik.homeaid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.Holder> {

    Context context;
    List<Medicine> medicineList;
    HolderClickListener hcl;
    private final LayoutInflater inflater;


    public MedicineAdapter(Context context, List<Medicine> lMedicine) {
        this.medicineList = lMedicine;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnClickListener(HolderClickListener hcl){
        this.hcl = hcl;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_rv_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String dateOverString;
        DateFormat dateFormat = new SimpleDateFormat("d.MM.yyyy", Locale.UK);
        Medicine medicine = medicineList.get(position);

        if (medicine.getDateOver() != null){
            dateOverString = dateFormat.format(medicine.getDateOver());
        } else {
            dateOverString = "";
        }
        holder.num = position;
        holder.name.setText(medicine.getName());
        holder.dateOver.setText(dateOverString);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        int num = -1;
        TextView name;
        TextView dateOver;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hcl != null){
                        hcl.onHolderClick(num);
                    }
                }
            });
            name = (TextView) itemView.findViewById(R.id.medicineName);
            dateOver = (TextView) itemView.findViewById(R.id.medicineOver);
        }
    }

    interface HolderClickListener{
        void onHolderClick(int numMed);
    }
}
