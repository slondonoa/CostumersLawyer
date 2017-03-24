package com.costumers.lawyer.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.costumers.lawyer.Manifest;
import com.costumers.lawyer.R;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.Persons;
import com.costumers.lawyer.fragments.Calendar;
import com.costumers.lawyer.fragments.FilterFragment;
import com.costumers.lawyer.fragments.OneFragment;
import com.costumers.lawyer.service.RestService;
import com.costumers.lawyer.fragments.RegisterCostumer;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DataBaseManager manager;
    RestService restService;
    ProgressDialog dialog =null;
    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.mipmap.ic_logo);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);  flecha de atras

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        restService = new RestService();

        manager=new DataBaseManager(this);
        List<Persons> lstperson=manager.getValidatePersons();
        if (lstperson.size()<1)
        {
            refreshCostumers();
        }
        manager.Close(this);
        //refreshScreen();

        if (!checkPermissionContact()) {

            requestPermissionContact();

        } else {

            Toast.makeText(MainActivity.this, "Tiene permisos para los contactos", Toast.LENGTH_LONG).show();

        }

        if (!checkPermissionCall()) {

            requestPermissionCall();

        } else {

            Toast.makeText(MainActivity.this, "Tiene permisos de llamada", Toast.LENGTH_LONG).show();

        }


    }

    private void refreshCostumers(){
        //Call to server to grab list of student records. this is a asyn
        try {
            if (conecNetWork()) {
                dialog = ProgressDialog.show(this, "",
                        "Actualizando clientes. Por favor espere...", true);
                restService.getService().getCostumers(new Callback<List<Persons>>() {
                    @Override
                    public void success(List<Persons> persons, Response response) {
                        List<Persons> lstpr = persons;
                        manager.Open(MainActivity.this);
                        manager.InsertCostumers(lstpr);
                        manager.Close(MainActivity.this);
                        if (dialog != null) {
                            dialog.cancel();
                        }
                        Toast.makeText(MainActivity.this, "Clientes actualizados", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }else {

                showAlertDialog(this,"Conexión a internet","Usted no cuenta con conexión a internet para la actualizacion de clientes",true);
            }
        }catch (Exception e){
            if (dialog != null) {
                dialog.cancel();
            }
            showAlertDialog(this,"Error","No fue posible realizar la actualización de clientes, por favor intentelo de nuevo",true);
        };

    }
 public RegisterCostumer RegisterCostumer;
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FilterFragment(), "Filtro");

        RegisterCostumer=new RegisterCostumer();

        adapter.addFrag(RegisterCostumer, "Registro");
        adapter.addFrag(new Calendar(), "Calendario");
        //app:tabMode="scrollable"
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

        if(id == R.id.action_refresh){
            refreshCostumers();
        }
        if(id == R.id.action_reload){
            finish();
            startActivity(getIntent());
        }
        if (id== R.id.action_savecontacts)
        {
            try {
                if (conecNetWork()) {
                    dialog = ProgressDialog.show(this, "",
                            "Guardando clientes en libreta de contactos. Por favor espere...", true);
                    restService.getService().getCostumers(new Callback<List<Persons>>() {
                        @Override
                        public void success(List<Persons> persons, Response response) {
                            List<Persons> lstpr = persons;
                            manager.Open(MainActivity.this);
                            manager.InsertCostumers(lstpr);
                            manager.Close(MainActivity.this);

                            for (Persons person:lstpr) {

                                String cell1 = "";
                                if (person.cell1 != null) {
                                    cell1 = person.cell1.trim();

                                }
                                String cell2 = "";
                                if (person.cell2 != null) {
                                    cell2 = person.cell2.trim();

                                }
                                String cell3 = "";
                                if (person.cell3 != null) {
                                    cell3 = person.cell3.trim();

                                }

                                String mail = "";
                                if (person.maial1 != null) {
                                    mail = person.maial1.trim();
                                }

                                String phone = "";
                                if (person.phone != null) {
                                    phone = person.phone.trim();
                                }

                                String phone2 = "";
                                if (person.phone2 != null) {
                                    phone2 = person.phone2.trim();
                                }

                                String phone3 = "";
                                if (person.phone3 != null) {
                                    phone3 = person.phone3.trim();
                                }

                                final String finalName = person.Name;
                                final String finalLastname = person.LastName;
                                final String finalCell1 = cell1;
                                final String finalCell2 = cell2;
                                final String finalCell3 = cell3;
                                final String finalPhone1 = phone;
                                final String finalPhone2 = phone2;
                                final String finalPhone3 = phone3;
                                final String finalMail1 = mail;

                                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                                ops.add(ContentProviderOperation.newInsert(
                                        ContactsContract.RawContacts.CONTENT_URI)
                                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                        .build());

                                //------------------------------------------------------ Names
                                if (!TextUtils.isEmpty(finalName)) {
                                    ops.add(ContentProviderOperation.newInsert(
                                            ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                            .withValue(
                                                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                                    finalName + " " + finalLastname).build());
                                }

                                //------------------------------------------------------ Mobile Number
                                if (!TextUtils.isEmpty(finalCell1) && finalCell1.length() == 10) {
                                    ops.add(ContentProviderOperation.
                                            newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, finalCell1)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                            .build());
                                }

                                if (!TextUtils.isEmpty(finalCell2) && finalCell2.length() == 10) {
                                    ops.add(ContentProviderOperation.
                                            newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, finalCell2)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                            .build());
                                }

                                if (!TextUtils.isEmpty(finalCell3) && finalCell3.length() == 10) {
                                    ops.add(ContentProviderOperation.
                                            newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, finalCell3)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                            .build());
                                }

                                //------------------------------------------------------ Home Numbers
                                if (!TextUtils.isEmpty(finalPhone1) && finalPhone1.length() == 7) {
                                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "034" + finalPhone1)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                                            .build());
                                }

                                //------------------------------------------------------ Home Numbers
                                if (!TextUtils.isEmpty(finalPhone2) && finalPhone2.length() == 7) {
                                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "034" + finalPhone2)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                                            .build());
                                }


                                //------------------------------------------------------ Home Numbers
                                if (!TextUtils.isEmpty(finalPhone3) && finalPhone3.length() == 7) {
                                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "034" + finalPhone3)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                                            .build());
                                }

                                //------------------------------------------------------ Email
                                if (finalMail1 != null) {
                                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                            .withValue(ContactsContract.Data.MIMETYPE,
                                                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Email.DATA, finalMail1)
                                            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                                            .build());
                                }

                                // Asking the Contact provider to create a new contact
                                try {
                                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (dialog != null) {
                                dialog.cancel();
                            }
                            Toast.makeText(MainActivity.this, "Clientes guardados", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else {

                    showAlertDialog(this,"Conexión a internet","Usted no cuenta con conexión a internet para la actualizacion de clientes",true);
                }
            }catch (Exception e){
                if (dialog != null) {
                    dialog.cancel();
                }
                showAlertDialog(this,"Error","No fue posible guardar los clientes, por favor intentelo de nuevo",true);
            };


        }

        return super.onOptionsItemSelected(item);
    }


    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }


    protected Boolean conecNetWork(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    private boolean checkPermissionContact(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermissionContact(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.WRITE_CONTACTS)){

            Toast.makeText(context,"Tiene permisos sobre los contactos",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_CONTACTS},PERMISSION_REQUEST_CODE);
        }
    }


    private boolean checkPermissionCall(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermissionCall(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.CALL_PHONE)){

            Toast.makeText(context,"Tiene permisos sobre los contactos",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(context,"Permisos generados",Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(context,"Permisos no generados",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}





