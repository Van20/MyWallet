package id.sch.smktelkom_mlg.mywallet.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import id.sch.smktelkom_mlg.mywallet.Controller.CategoryController;
import id.sch.smktelkom_mlg.mywallet.Controller.ExpenseController;
import id.sch.smktelkom_mlg.mywallet.Controller.SaldoController;
import id.sch.smktelkom_mlg.mywallet.Model.Category;
import id.sch.smktelkom_mlg.mywallet.Model.Expense;
import id.sch.smktelkom_mlg.mywallet.Model.Saldo;
import id.sch.smktelkom_mlg.mywallet.R;
import id.sch.smktelkom_mlg.mywallet.Utils.DatePicker;
import id.sch.smktelkom_mlg.mywallet.Utils.Utils;
import id.sch.smktelkom_mlg.mywallet.beranda_screen.Home_activity;


public class FragmentHome extends Fragment {

    public FragmentHome() {
    }

    RelativeLayout view;
    View mView;


    /**
     * Initial Variable
     */
    int SELECTED_ID = 0;
    int SELECTED_KATID = 0;

    Saldo mySaldo;
    Expense selectedExpense;

    List<Expense> Expenses;
    ArrayAdapter<Expense> adapter;

    EditText inputAmount, inputDesk;
    TextView currentSaldo, todayTotal;
    Spinner spinCategory;
    ListView expenseList;
    Button btnTgl,btnNext,btnPrev;

    private CategoryController category;
    private SaldoController saldo;
    private ExpenseController expense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Uangku");
        ((Home_activity) getActivity()).showFloatingActionButton();

        category = new CategoryController(getActivity());
        expense = new ExpenseController(getActivity());
        saldo = new SaldoController(getActivity());

        currentSaldo = (TextView) view.findViewById(R.id.currentSaldo);
        todayTotal = (TextView) view.findViewById(R.id.todayOut);
        expenseList = (ListView) view.findViewById(R.id.outList);

        btnTgl = (Button) view.findViewById(R.id.tgl);
        btnTgl.setText(Utils.getDateNow());
        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnNext = (Button) view.findViewById(R.id.next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDay("+");
            }
        });

        btnPrev = (Button) view.findViewById(R.id.prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDay("-");
            }
        });

        new MyAsynch().execute(btnTgl.getText().toString());
        expenseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedExpense = (Expense) parent.getItemAtPosition(position);
                SELECTED_ID = selectedExpense.getId();
                menuItemDialog();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormDialog("Add");
            }
        });
        return view;
    }

    private void changeDay(String s){
        String date = btnTgl.getText().toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(date));
            if (s.equalsIgnoreCase("+")){
                c.add(Calendar.DATE,1);
            }else{
                c.add(Calendar.DATE,-1);
            }
            date = df.format(c.getTime());
            btnTgl.setText(date);
            new MyAsynch().execute(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Inflate Item To List
     */

    private class MyAsynch extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... Strings) { // run time intensive task in separate thread
            Expenses = expense.getExpenseByDate(Strings[0]);
            return null;
        }

        protected void onPostExecute(String result) {
            // Give the data to you adapter from here,instead of the place where you gave it earlier
            setmysaldo();
            setListAdapter(Expenses);
            expenseList.setAdapter(adapter);
            if (expense.getCountByDate(btnTgl.getText().toString()) != 0) {
                todayTotal.setText(" " + Utils.convertCur(expense.getTotal(btnTgl.getText().toString())));
            } else {
                todayTotal.setText(" - ");
            }

            if (adapter.getCount() == 0) {
                expenseList.setEmptyView(view.findViewById(R.id.emptyview));
            }
        }
    }

    /**
     * Method To Change Available Money
     */
    public void setmysaldo() {
        mySaldo = new Saldo(1, Integer.parseInt(saldo.getSaldo()));
        String current = Utils.convertCur(String.valueOf(mySaldo.getSaldo()));
        currentSaldo.setText(current);
    }

    /**
     * Custom Adapater For List
     */
    public void setListAdapter(List<Expense> Pengeluarans) {
        adapter = new ArrayAdapter<Expense>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Pengeluarans) {
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
     * Custom Adapater For
     * Inflate Kategori To Spinner
     */
    public void loadKategori() {
        List<Category> Kategoris = category.getAllCategory();
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Kategoris);
        spinCategory.setAdapter(dataAdapter);
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category kat = (Category) parent.getItemAtPosition(position);
                SELECTED_KATID = kat.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    /**
     * Dialog To Select Action Update Or Delete
     */
    public void menuItemDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit / Hapus Pengeluaran");
        builder.setItems(R.array.dialog_expense, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showFormDialog("Update");
                        break;
                    case 1:
                        expense.deleteExpenseById(SELECTED_ID);
                        saldo.updateSaldo(mySaldo.getSaldo() + selectedExpense.getJumlah());
                        new MyAsynch().execute(btnTgl.getText().toString());
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Form Dialog To Add Or Update
     */
    public void showFormDialog(String to) {
        final String todo = to;
        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        mView = mainLayout.inflate(R.layout.dialog_expense, null);
        inputAmount = (EditText) mView.findViewById(R.id.inputJumlah);
        inputDesk = (EditText) mView.findViewById(R.id.inputDesk);
        spinCategory = (Spinner) mView.findViewById(R.id.spinner);
        loadKategori();

        if (todo.equals("Update")) {
            inputAmount.setText(String.valueOf(selectedExpense.getJumlah()));
            inputDesk.setText(selectedExpense.getDeskripsi());
            setSelectedSpinner(spinCategory, category.getName(selectedExpense.getIdkat()));
        }

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Expense peng = new Expense();
                        peng.setId(SELECTED_ID);
                        peng.setIdkat(SELECTED_KATID);
                        peng.setDeskripsi(inputDesk.getText().toString());
                        peng.setJumlah(Integer.parseInt(inputAmount.getText().toString()));
                        peng.setTanggal(btnTgl.getText().toString());
                        if (todo.equals("Add")) {
                            expense.addExpense(peng);
                            saldo.updateSaldo(mySaldo.getSaldo() - peng.getJumlah());
                        } else {
                            expense.updateExpense(peng);
                            saldo.updateSaldo(chageBalance(peng.getJumlah()));
                        }
                        new MyAsynch().execute(btnTgl.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
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
            new MyAsynch().execute(formattedDate);
        }
    };

    /**
     * Return balance to change current saldo
     */
    private int chageBalance(int newjml) {
        int oldjml = selectedExpense.getJumlah();
        if (newjml > oldjml) {
            return mySaldo.getSaldo() - (newjml - oldjml);
        } else if (newjml < oldjml) {
            return mySaldo.getSaldo() + (oldjml - newjml);
        } else {
            return mySaldo.getSaldo();
        }
    }

    /**
     * Checked Item Spinner on Update
     */
    private void setSelectedSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
