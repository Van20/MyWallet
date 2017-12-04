package id.sch.smktelkom_mlg.mywallet.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.fianekame.uangku.Controller.CategoryController;
import io.github.fianekame.uangku.Controller.ExpenseController;
import io.github.fianekame.uangku.MainActivity;
import io.github.fianekame.uangku.Model.Category;
import io.github.fianekame.uangku.R;

/**
 * Created by fianxeka on 29/06/17.
 */

public class FragmentCategory extends Fragment {

    public FragmentCategory() {
    }


    RelativeLayout view;

    /**
     * Initial Variable
     */
    private CategoryController category;
    private ExpenseController expense;
    private Category selectedCategory;
    ProgressDialog pd;
    ListView categoryList;
    List<Category> Categoris;
    ArrayAdapter<Category> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.fragment_category, container, false);
        getActivity().setTitle("Kategori");
        ((MainActivity) getActivity()).hideFloatingActionButton();

        category = new CategoryController(getActivity());
        expense = new ExpenseController(getActivity());
        Button addBtn = (Button) view.findViewById(R.id.addKategori);
        categoryList = (ListView) view.findViewById(R.id.listKategori);

        new MyAsynch().execute();
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (Category) parent.getItemAtPosition(position);
                menuItemDialog();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormDialog("Add");
            }
        });
        return view;
    }


    /**
     * Asyntask Class
     */

    private class MyAsynch extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... Strings) { // run time intensive task in separate thread
            Categoris = category.getAllCategory();
            return null;
        }

        protected void onPostExecute(String result) {
            // Give the data to you adapter from here,instead of the place where you gave it earlier
            categoryList = (ListView) view.findViewById(R.id.listKategori);
            setListAdapter(Categoris);
            categoryList.setAdapter(adapter);
            if (adapter.getCount() == 0) {
                categoryList.setEmptyView(view.findViewById(R.id.emptyview));
            }
        }
    }


    /**
     * Custom Adapater For ListView
     */
    public void setListAdapter(List<Category> categoris) {
        adapter = new ArrayAdapter<Category>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, categoris) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                Category current = getItem(position);
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.item_category, null, false);
                }
                TextView txtId = (TextView) convertView.findViewById(R.id.idkat);
                TextView txtKat = (TextView) convertView.findViewById(R.id.namakat);
                txtId.setText(String.valueOf(current.getId()));
                txtKat.setText(current.getKategori());
                return convertView;
            }
        };

    }

    /**
     * Dialog To Select Action Update Or Delete
     */
    public void menuItemDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit / Hapus Kategori");
        builder.setItems(R.array.dialog_kategori, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showFormDialog("Update");
                        break;
                    case 1:
                        if (category.getCount() < 4) {
                            Toast.makeText(getActivity(), "Minimal Memiliki 3 Kategori", Toast.LENGTH_SHORT).show();
                        } else {
                            category.deleteCategory(
                                    selectedCategory.getId(),
                                    expense.isKategoriExist(selectedCategory.getId()));
                           // makeList();
                            new MyAsynch().execute();
                        }
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
        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        View mView = mainLayout.inflate(R.layout.dialog_category, null);
        final String todo = to;
        final EditText katnama = (EditText) mView.findViewById(R.id.userInputDialog);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        if (todo.equals("Update")) {
            katnama.setText(selectedCategory.getKategori());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Category kat = new Category();
                        if (todo.equals("Add")) {
                            kat.setKategori(katnama.getText().toString());
                            category.addCategory(kat);
                        } else {
                            kat.setId(selectedCategory.getId());
                            kat.setKategori(katnama.getText().toString());
                            category.updateCategory(kat);
                        }
                       // makeList();
                        new MyAsynch().execute();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
}