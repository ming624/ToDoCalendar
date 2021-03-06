package com.andante624.todocalendar.DataManage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andante624.todocalendar.DataManage.DB.DBManager;
import com.andante624.todocalendar.DataManage.DB.ToDo_Item;
import com.andante624.todocalendar.R;
import com.andante624.todocalendar.Utils;

import java.util.Calendar;

public class Fragment_AddToDoItem extends Fragment implements View.OnClickListener{
    public static int[] array_rectangle = new int[]{
            R.drawable.round_rectangle_blue, R.drawable.round_rectangle_green, R.drawable.round_rectangle_yellow,
            R.drawable.round_rectangle_orange, R.drawable.round_rectangle_red, R.drawable.round_rectangle_black
    };
    SpinnerAdapter_AddToDo spinnerAdapter_addToDo;
    private OnFragmentInteractionListener mListener;
    private EditText title_editview;
    private TextView date_textview, title_text, importance_text, date_text, category_text;
    private Spinner category_spinner;
    private RatingBar importance_ratingbar;
    private Button ok_button, cancel_button;

    private Boolean editmode = false;
    private int todo_id = -1;
    private ToDo_Item editItem;

    private int get_deadline_year=0, get_deadline_month=0, get_deadline_day=0;
    private String get_title, get_category, get_deadline_date;
    private float get_importance;

    public static Fragment_AddToDoItem newInstance(int todo_id) {
        Fragment_AddToDoItem fragment_addToDoItem = new Fragment_AddToDoItem();
        Bundle bundle = new Bundle();
        bundle.putInt("ToDo_ID", todo_id);
        fragment_addToDoItem.setArguments(bundle);
        return fragment_addToDoItem;
    }

    public Fragment_AddToDoItem() {
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            editmode = true;
            todo_id = getToDoID();
            DBManager dbManager = DBManager.open(getActivity().getApplicationContext());
            editItem = dbManager.select_ToDoItem(todo_id);
            dbManager.close();
        }

        spinnerAdapter_addToDo = new SpinnerAdapter_AddToDo();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
    }

    public int getToDoID()
    {
        return getArguments().getInt("ToDo_ID",-1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_to_do_item, container, false);
        setLayout(view);
        setListener();

        if(editmode)
        {
            get_deadline_date = editItem.getDeadlineDate();
            title_editview.setText(editItem.getTitle());
            date_textview.setText(get_deadline_date);
            DBManager dbManager = DBManager.open(getActivity().getApplicationContext());
            String title = dbManager.get_CategoryTitle(editItem.getCategory());
            dbManager.close();
            category_spinner.setSelection(spinnerAdapter_addToDo.getSelectPosition(title));
            importance_ratingbar.setRating(editItem.getImportance());
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            get_deadline_year = calendar.get(Calendar.YEAR);
            get_deadline_month = calendar.get(Calendar.MONTH)+1;
            get_deadline_day = calendar.get(Calendar.DAY_OF_MONTH);
            get_deadline_date = Utils.getStringDate(get_deadline_year, get_deadline_month, get_deadline_day);
            date_textview.setText(get_deadline_date);
        }

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

        title_text = (TextView)view.findViewById(R.id.add_todo_titletext);
        importance_text = (TextView)view.findViewById(R.id.add_todo_importancetext);
        date_text = (TextView)view.findViewById(R.id.add_todo_datetext);
        category_text = (TextView)view.findViewById(R.id.add_todo_categorytext);

        spinnerAdapter_addToDo.setContext(getActivity().getApplicationContext());
        spinnerAdapter_addToDo.setCategory();
        category_spinner.setAdapter(spinnerAdapter_addToDo);
        setDesing();

    }

    public void setDesing()
    {
        LayerDrawable layerDrawable = (LayerDrawable)importance_ratingbar.getProgressDrawable();
        layerDrawable.getDrawable(2).setColorFilter(getActivity().getResources().getColor(Utils.getBackgroundId(getActivity())), PorterDuff.Mode.SRC_ATOP);

        title_text.setTextColor(getActivity().getResources().getColor(Utils.getBackgroundDarkColorID(getActivity())));
        title_editview.setTextColor(getActivity().getResources().getColor(Utils.getBackgroundId(getActivity())));
        importance_text.setTextColor(getActivity().getResources().getColor(Utils.getBackgroundDarkColorID(getActivity())));
        date_text.setTextColor(getActivity().getResources().getColor(Utils.getBackgroundDarkColorID(getActivity())));
        date_textview.setTextColor(getActivity().getResources().getColor(Utils.getBackgroundId(getActivity())));
        category_text.setTextColor(getActivity().getResources().getColor(Utils.getBackgroundDarkColorID(getActivity())));
        ok_button.setBackgroundResource(array_rectangle[Utils.getBackgroundPosition(getActivity())]);

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
                        get_deadline_year = year;
                        get_deadline_month = monthOfYear+1;
                        get_deadline_day = dayOfMonth;
                        get_deadline_date = Utils.getStringDate(get_deadline_year,get_deadline_month,get_deadline_day);
                        date_textview.setText(get_deadline_date);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                break;
            case R.id.add_todo_ok:
                if(editmode) {
                    if (editToDoItem())
                        getFragmentManager().popBackStack();
                }
                else {
                    if (saveToDoItem())
                        getFragmentManager().popBackStack();
                }
                break;
            case R.id.add_todo_cancel:
                getFragmentManager().popBackStack();
                break;
        }
    }


    public boolean saveToDoItem()
    {
        if(getInputData())
        {
            DBManager dbManager = DBManager.open(getActivity().getApplicationContext());
            dbManager.insert_ToDoItem(get_title, get_deadline_date, get_category, get_importance);
            dbManager.close();
            return true;
        }
        return false;

    }

    public boolean getInputData()
    {
        get_title = title_editview.getText().toString();

        if(get_title.length() != 0)
        {
            get_category = category_spinner.getSelectedItem().toString();
            get_importance = importance_ratingbar.getRating();
            return true;
        }
        else
            Toast.makeText(getActivity().getApplicationContext(),"Title 을 입력해 주세요",Toast.LENGTH_SHORT).show();

        return false;
    }

    public boolean editToDoItem()
    {
        if(getInputData())
        {
            editItem.setTitle(get_title);
            editItem.setDeadlineDate(get_deadline_date);
            editItem.setImportance(get_importance);
            DBManager dbManager = DBManager.open(getActivity().getApplicationContext());
            editItem.setCategory(dbManager.get_CategoryID(get_category));
            dbManager.update_ToDoItem(editItem);
            dbManager.close();
            return true;
        }
        return false;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type an d name
        public void onFragmentInteraction(Uri uri);
    }

}
