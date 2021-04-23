package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.note.databinding.ActivityMainBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    private int REQ_CODE_PICK_SOUNDFILE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        final Calendar c = Calendar.getInstance();
        lastSelectedYear = c.get(Calendar.YEAR);
        lastSelectedMonth = c.get(Calendar.MONTH);
        lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        binding.setdate.setText(lastSelectedDayOfMonth + "/" + (lastSelectedMonth + 1) + "/" + lastSelectedYear);

        List<String> list = new ArrayList<>();
        list.add("Work");
        list.add("Friend");
        list.add("Family");

        binding.sethours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });
        
        binding.setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerType.setAdapter(stringArrayAdapter);

        binding.tvTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings = {"Android","Kotlin","Flutter","IOS","PHP",".NET"};
                boolean[] booleans = {false,false,false,false,false,false};
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("Choose tags:").setMultiChoiceItems(strings, booleans, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i=0;i<booleans.length;i++){
                            boolean checked = booleans[i];
                            if (checked){
                                binding.tvShowTags.setText(binding.tvShowTags.getText() + strings[i]+", ");
                            }
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(),"Lam quen nha",Toast.LENGTH_LONG).show();
                    }
                }).create();
                alertDialog.show();
            }
        });

        binding.tvWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
                boolean[] booleans = {false,false,false,false,false,false,false};
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("Choose tags:").setMultiChoiceItems(strings, booleans, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i=0;i<booleans.length;i++){
                            boolean checked = booleans[i];
                            if (checked){
                                binding.tvShowDay.setText(binding.tvShowDay.getText() + strings[i]+", ");
                            }
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(),"Lam quen nha",Toast.LENGTH_LONG).show();
                    }
                }).create();
                alertDialog.show();
            }
        });

        binding.btnTune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getBaseContext(),v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.item_popupmenu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return menuItemClicked(item);
                    }
                });
                popupMenu.show();
            }
        });
    }

    private boolean menuItemClicked(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuOne:
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        fileChooser();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };

                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                break;
            case R.id.menuTwo:
                String[] strings = {"Nexus Tune","Winphone tune","Peep tune","Nokia tune","Etc"};
                Set<String> selectedItems = new HashSet<String>();

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("Select tune:").setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItems.clear();
                        selectedItems.add(strings[which]);
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.tvTune.setText("Tune selected: "+selectedItems.iterator().next());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(),"Lam quen nha",Toast.LENGTH_LONG).show();
                    }
                }).create();
                alertDialog.show();
                break;
        }
        return true;
    }

    private void setDate() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                binding.setdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };

        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(this, dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

        // Show
        datePickerDialog.show();
    }

    private void setTime() {
        if(lastSelectedHour == -1)  {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            this.lastSelectedMinute = c.get(Calendar.MINUTE);
        }

        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                binding.sethours.setText(hourOfDay + ":" + minute );
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = null;
        timePickerDialog = new TimePickerDialog(this, timeSetListener, lastSelectedHour, lastSelectedMinute, true);

        // Show
        timePickerDialog.show();
    }

    private void fileChooser() {

        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_audio_file_title)), REQ_CODE_PICK_SOUNDFILE);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                Uri audioFileUri = data.getData();
                String filePath = audioFileUri.getPath();
                binding.tvTune.setText("Tune selected: "+filePath);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu1:
                Toast.makeText(getBaseContext(),"Save",Toast.LENGTH_LONG).show();
                break;
            case R.id.menu2:
                Toast.makeText(getBaseContext(),"Cancel",Toast.LENGTH_LONG).show();
                break;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
}