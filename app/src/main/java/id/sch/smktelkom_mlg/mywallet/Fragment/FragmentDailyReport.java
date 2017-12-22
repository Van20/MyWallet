package id.sch.smktelkom_mlg.mywallet.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.sch.smktelkom_mlg.mywallet.Controller.CategoryController;
import id.sch.smktelkom_mlg.mywallet.Controller.ExpenseController;
import id.sch.smktelkom_mlg.mywallet.Controller.ReportController;
import id.sch.smktelkom_mlg.mywallet.Model.Category;
import id.sch.smktelkom_mlg.mywallet.Model.Expense;
import id.sch.smktelkom_mlg.mywallet.R;
import id.sch.smktelkom_mlg.mywallet.Utils.DatePicker;
import id.sch.smktelkom_mlg.mywallet.Utils.Utils;
import id.sch.smktelkom_mlg.mywallet.beranda_screen.Home_activity;

public class FragmentDailyReport extends Fragment {

    public FragmentDailyReport() {
    }

    RelativeLayout view;

    /**
     * Initial Variable
     */
    private CategoryController category;
    private ExpenseController expense;
    private ReportController report;

    List<Expense> Expenses = new ArrayList<Expense>();
    List<Expense> ExpensesByKategori;
    ArrayAdapter<Expense> adapter;

    public Button btnTgl;

    ListView dailyReportList;
    MaterialSpinner spinCategory;
    TextView totalOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_daily_report, container, false);
        getActivity().setTitle("Laporan Harian");
        ((Home_activity) getActivity()).hideFloatingActionButton();
        category = new CategoryController(getActivity());
        expense = new ExpenseController(getActivity());
        report = new ReportController(getActivity());

        btnTgl = (Button) view.findViewById(R.id.tgl);
        btnTgl.setText(Utils.getDateNow());
        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        totalOut = (TextView) view.findViewById(R.id.todayOut);
        dailyReportList = (ListView) view.findViewById(R.id.outList);

        new MyAsynch().execute(btnTgl.getText().toString());

        dailyReportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        spinCategory = (MaterialSpinner) view.findViewById(R.id.kategori);
        loadKategori();
        return view;
    }

    /**
     * Asytask Class
     */
    private class MyAsynch extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... Strings) {
            Expenses = report.getDailyReport(Strings[0]);
            return null;
        }

        protected void onPostExecute(String result) {
            setListAdapter(Expenses);
            changeTodayOut(Expenses);
            dailyReportList.setAdapter(adapter);
            if (adapter.getCount() == 0) {
                dailyReportList.setEmptyView(view.findViewById(R.id.emptyview));
            }
        }
    }


    /**
     * Custom Adapater For ListView
     */
    public void setListAdapter(List<Expense> expenses) {
        adapter = new ArrayAdapter<Expense>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, expenses) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                Expense current = getItem(position);
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.item_expense, null, false);
                }
                TextView txtDes = (TextView) convertView.findViewById(R.id.deskrip);
                TextView txtKat = (TextView) convertView.findViewById(R.id.namakat);
                TextView txtJml = (TextView) convertView.findViewById(R.id.jumlah);

                txtDes.setText(current.getDeskripsi());
                txtKat.setText(category.getName(current.getIdkat()));
                txtJml.setText(Utils.convertCur(String.valueOf(current.getJumlah())));
                return convertView;
            }
        };
    }

    /**
     * Change Total Today Expense
     */
    public void changeTodayOut(List<Expense> expenses) {
        if (expenses.size() != 0) {
            int total = 0;
            for (Expense pe : expenses) {
                total += pe.getJumlah();
            }
            totalOut.setText(" " + Utils.convertCur(String.valueOf(total)));
        } else {
            totalOut.setText(" - ");
        }

    }

    /**
     * Custom Adapater For
     * Inflate Kategori To Spinner
     */
    public void loadKategori() {
        List<Category> Categories = category.getAllCategory();
        Category All = new Category();
        All.setKategori("Semua Kategori");
        Categories.add(0, All);
        spinCategory.setItems(Categories);
        spinCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<Category>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Category item) {
                if (position != 0) {
                    changeListByKategori("K", item.getId());
                } else {
                    changeListByKategori("A", item.getId());

                }
            }

        });
    }

    /**
     * Change ListView Data After SpinnerOnclick
     */
    public void changeListByKategori(String by, int id) {
        if (by.equalsIgnoreCase("K")) {
            ExpensesByKategori = new ArrayList<Expense>();
            for (Expense pe : Expenses) {
                if (pe.getIdkat() == id) {
                    ExpensesByKategori.add(pe);
                }
            }
            setListAdapter(ExpensesByKategori);
            changeTodayOut(ExpensesByKategori);

        } else {
            setListAdapter(Expenses);
            changeTodayOut(Expenses);
        }
        dailyReportList.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            dailyReportList.setEmptyView(view.findViewById(R.id.emptyview));
        }
    }

    /**
     * Show Dialog To Open Date Picker
     */
    private void showDatePicker() {
        DatePicker date = new DatePicker();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onGetDate);
        date.show(getFragmentManager(), "Date Picker");
    }

    /**
     * Datepicker SetOn Listener
     */
    DatePickerDialog.OnDateSetListener onGetDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            btnTgl.setText(formattedDate);
            new MyAsynch().execute(btnTgl.getText().toString());
            spinCategory.setSelectedIndex(0);
        }
    };

}