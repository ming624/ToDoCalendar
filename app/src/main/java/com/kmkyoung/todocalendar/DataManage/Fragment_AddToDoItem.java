package com.kmkyoung.todocalendar.DataManage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.kmkyoung.todocalendar.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_AddToDoItem.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_AddToDoItem#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Fragment_AddToDoItem extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;
    private EditText title_editview;
    private TextView date_textview;
    private Spinner category_spinner;
    private RatingBar importance_ratingbar;
    private Button ok_button, cancel_button;

    private int get_year=0, get_month=0, get_day=0;
    private String get_title, get_category;
    private float get_importance;

    public static Fragment_AddToDoItem newInstance(String param1, String param2) {
        Fragment_AddToDoItem fragment = new Fragment_AddToDoItem();
        return fragment;
    }
    public Fragment_AddToDoItem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_do_item, container, false);
        setLayout(view);
        setListener();


        return view;
    }

    public void setLayout(View view)
    {
        title_editview = (EditText)view.findViewById(R.id.add_todo_title);
        date_textview = (TextView)view.findViewById(R.id.add_todo_date);
        category_spinner = (Spinner)view.findViewById(R.id.add_todo_spinner);
        importance_ratingbar = (RatingBar)view.findViewById(R.id.add_todo_ratingbar);
        ok_button = (Button)view.findViewById(R.id.add_todo_ok);
        cancel_button = (Button)view.findViewById(R.id.add_todo_cancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.categorys_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);

    }

    public void setListener()
    {
        date_textview.setOnClickListener(this);
        ok_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_todo_date:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        get_year = year;
                        get_month = monthOfYear;
                        get_day = dayOfMonth;
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                break;
            case R.id.add_todo_ok:
                if(getAllData())
                {
                    Log.d("kmkyoung","title "+get_title + "\ndate "+get_year+"년 "+get_month+"월 "+get_day);
                    Log.d("kmkyoung","category"+get_category+"\nstarts :"+get_importance);
                    //DB 에 저장
                }
                else
                {
                    //Dialog 로 안내

                }
                getFragmentManager().popBackStack();
                break;
            case R.id.add_todo_cancel:
                getFragmentManager().popBackStack();
                break;
        }
    }

    public boolean getAllData()
    {
        get_title = title_editview.getText().toString();
        get_category = category_spinner.getSelectedItem().toString();
        get_importance = importance_ratingbar.getRating();
        return true;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
