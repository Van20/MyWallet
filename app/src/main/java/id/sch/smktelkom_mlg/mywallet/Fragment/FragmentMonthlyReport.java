package id.sch.smktelkom_mlg.mywallet.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.sch.smktelkom_mlg.mywallet.Controller.ExpenseController;
import id.sch.smktelkom_mlg.mywallet.Controller.ReportController;
import id.sch.smktelkom_mlg.mywallet.DetailReport;
import id.sch.smktelkom_mlg.mywallet.Model.Report;
import id.sch.smktelkom_mlg.mywallet.R;
import id.sch.smktelkom_mlg.mywallet.Utils.Utils;
import id.sch.smktelkom_mlg.mywallet.beranda_screen.MainActivity;


public class FragmentMonthlyReport extends Fragment {

    public FragmentMonthlyReport() {
    }

    RelativeLayout view;

    /**
     * Initial Variable
     */
    MaterialSpinner bulanSpinner;
    LinearLayout contentLayout;
    ImageButton save;
    PieChart mChart;


    List<Report> Reports = new ArrayList<Report>();
    ListView detaillist;

    List<PieEntry> chartValue;
    List<Integer> chartColor;
    ArrayAdapter<Report> adapter;
    String[] arrayColor;


    int nowYear;
    String newMonth;

    private ExpenseController expense;
    private ReportController report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_monthly_report, container, false);
        getActivity().setTitle("Laporan Bulanan");
        ((MainActivity) getActivity()).hideFloatingActionButton();
        expense = new ExpenseController(getActivity());
        report = new ReportController(getActivity());

        contentLayout = (LinearLayout) view.findViewById(R.id.contentlayout);
        detaillist = (ListView) view.findViewById(R.id.outList);
        arrayColor = getResources().getStringArray(R.array.mdcolor_500);
        mChart = (PieChart) view.findViewById(R.id.chart1);
        settingUpchart();

        save = (ImageButton) view.findViewById(R.id.savebtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSaveChart();
            }
        });

        bulanSpinner = (MaterialSpinner) view.findViewById(R.id.bulantahun);
        bulanSpinner.setItems(dataSpinner());
        bulanSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position != 0) {
                    newMonth = String.format("%02d", position);
                    setDataForPieChart(newMonth);
                    setDataForList();
                } else {
                    contentLayout.setVisibility(View.GONE);
                }
            }
        });

        detaillist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Report p = (Report) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailReport.class);
                intent.putExtra("Idkategori", p.getIdkategori());
                intent.putExtra("Kategori", p.getNamakategori());
                intent.putExtra("Jumlah", p.getJumlah());
                intent.putExtra("Month", newMonth);
                intent.putExtra("Year", nowYear);
                intent.putExtra("Color", chartColor.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * To Save Chart Into Gallery
     */
    private void toSaveChart() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String name = "Laporan_" + timeStamp;
        try {
            mChart.saveToGallery(name, 100);
            Toast.makeText(getActivity(), "Saved In Gellery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    /**
     * Custom Adapater For ListDetail Chart
     */
    public void setDataForList() {
        adapter = new ArrayAdapter<Report>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Reports) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                Report current = getItem(position);
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.item_report, null, false);
                }
                TextView txtNama = (TextView) convertView.findViewById(R.id.namakat);
                TextView txtJum = (TextView) convertView.findViewById(R.id.jumlah);
                TextView txtTot = (TextView) convertView.findViewById(R.id.totalkeluar);
                ImageView icon = (ImageView) convertView.findViewById(R.id.iconnya);

                icon.setBackgroundColor(chartColor.get(position));
                txtNama.setText(current.getNamakategori());
                txtTot.setText(String.valueOf(current.getTotal()) + " Pengeluaran");
                txtJum.setText(Utils.convertCur(String.valueOf(current.getJumlah())));
                return convertView;
            }
        };
        detaillist.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(detaillist);
        if (adapter.getCount() == 0) {
            detaillist.setEmptyView(view.findViewById(R.id.emptyview));
        }
    }

    /**
     * Settup Chart MPCHART
     */
    public void settingUpchart() {
        Description description = new Description();
        description.setTextColor(000);
        description.setText("Chart Data");
        mChart.setDescription(description);
        mChart.setRotationEnabled(true);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                PieEntry pe = (PieEntry) e;
                Toast.makeText(getActivity(),
                        String.valueOf(Math.round(pe.getValue())) + " Pengeluaran Di Kategori " +
                                pe.getLabel(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    /**
     * Add Data To Chart And Showing PieChart (Value & Label)
     */
    public void setDataForPieChart(String month) {
        Reports = report.getMontlyReport(month, String.valueOf(nowYear));
        if (Reports.size() != 0) {
            int total = 0, jumlah = 0, col = 0;
            contentLayout.setVisibility(View.VISIBLE);
            chartValue = new ArrayList<PieEntry>();
            chartColor = new ArrayList<Integer>();
            for (Report lp : Reports) {
                jumlah += lp.getJumlah();
                total += lp.getTotal();
                chartValue.add(new PieEntry(lp.getTotal(), lp.getNamakategori()));
                chartColor.add(Color.parseColor(arrayColor[col]));
                col++;
            }

            PieDataSet dataSet = new PieDataSet(chartValue, "");
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);
            dataSet.setColors(chartColor);
            dataSet.setValueFormatter(new MyValueFormatter());

            Legend l = mChart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            l.setWordWrapEnabled(true);
            l.setXEntrySpace(9);
            l.setYEntrySpace(5);

            PieData data = new PieData(dataSet);
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            mChart.setData(data);
            String center = String.valueOf(total) + " Pengeluaran \nDengan Total :\n" + Utils.convertCur(String.valueOf(jumlah));
            mChart.setCenterText(center);

            mChart.highlightValues(null);
            mChart.invalidate();
            mChart.animateXY(1400, 1400);

        } else {
            Toast.makeText(getActivity(), "Tidak Ada Data", Toast.LENGTH_SHORT).show();
            contentLayout.setVisibility(View.GONE);
        }

    }

    /**
     * Spinner For Selected Month - Year
     */
    public List<String> dataSpinner() {
        List<String> tospinner = new ArrayList<>();
        String bulan;
        nowYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 12; i++) {
            bulan = getResources().getStringArray(R.array.Bulan)[i];
            tospinner.add(bulan + " " + String.valueOf(nowYear));
        }
        tospinner.add(0, "Pilih Bulan-Tahun");
        return tospinner;
    }

    /**
     * Change Decimal Format Of Chart Data
     */
    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }

}