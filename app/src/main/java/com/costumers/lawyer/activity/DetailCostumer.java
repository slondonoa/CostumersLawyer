package com.costumers.lawyer.activity;

import android.Manifest;
import android.app.TaskStackBuilder;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.PersonAdapter;
import com.costumers.lawyer.entities.Persons;
import com.costumers.lawyer.fragments.FilterFragment;
import com.costumers.lawyer.fragments.OneFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailCostumer extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private DataBaseManager manager;

    private ImageButton imbemail;
    private ImageButton imbedit;
    private ImageButton imbsave;


    private ImageButton imbcall1;
    private ImageButton imbcall2;
    private ImageButton imbcall3;

    private ImageButton imbphone1;
    private ImageButton imbphone2;
    private ImageButton imbphone3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_costumer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalle de cliente");
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String strIdPerson = getIntent().getStringExtra(AlarmClock.EXTRA_MESSAGE);

        TextView tvDocument = (TextView) findViewById(R.id.txtDocument);
        TextView tvName = (TextView) findViewById(R.id.txtName);
        TextView tvLastName = (TextView) findViewById(R.id.txtLastName);
        TextView tvPhone1 = (TextView) findViewById(R.id.txtPhone1);
        TextView tvPhone2 = (TextView) findViewById(R.id.txtPhone2);
        TextView tvPhone3 = (TextView) findViewById(R.id.txtPhone3);
        TextView tvEmail = (TextView) findViewById(R.id.txtEmail);
        TextView tvClientstatus = (TextView) findViewById(R.id.txtClientstatus);
        TextView tvProcessstatus = (TextView) findViewById(R.id.txtProcessstatus);
        TextView tvProcessed = (TextView) findViewById(R.id.txtProcessed);
        TextView tvLimitProcess = (TextView) findViewById(R.id.txtLimitProcess);
        TextView tvLastcontact = (TextView) findViewById(R.id.txtLastContact);
        TextView tvObservations = (TextView) findViewById(R.id.txtObservations);
        TextView tvcell1 = (TextView) findViewById(R.id.txtCell1);
        TextView tvcell2 = (TextView) findViewById(R.id.txtCell2);
        TextView tvcell3 = (TextView) findViewById(R.id.txtCell3);
        TextView tvSource = (TextView) findViewById(R.id.txtSource);

        manager = new DataBaseManager(this);
        manager.Open(DetailCostumer.this);
        String where = "WHERE " + "IdPerson='" + strIdPerson + "'";
        List<Persons> lstperson = manager.getFilterPersons(where);
        manager.Close(DetailCostumer.this);

        for (Persons persons : lstperson) {

            String document = "";
            if (persons.Document != null) {
                document = persons.Document;
            }
            String name = "";
            if (persons.Name != null) {
                name = persons.Name;
            }

            String lastname = "";
            if (persons.LastName != null) {
                lastname = persons.LastName;
            }

            String phone = "";
            if (persons.phone != null) {
                phone = persons.phone.trim();
            }

            String phone2 = "";
            if (persons.phone2 != null) {
                phone2 = persons.phone2.trim();
            }

            String phone3 = "";
            if (persons.phone3 != null) {
                phone3 = persons.phone3.trim();
            }

            String clientstatus = "";
            if (persons.clientstatus != null) {
                clientstatus = persons.clientstatus;
            }

            String processstatus = "";
            if (persons.ProcessStatus != null) {
                processstatus = persons.ProcessStatus;
            }

            String limitprocess = "";
            if (persons.LimitDateProcessStatus != null) {
                limitprocess = persons.LimitDateProcessStatus.toString();
            }

            String processed = "";
            if (persons.processed != null) {
                processed = persons.processed;
            }

            String source = "";
            if (persons.source != null) {
                source = persons.source;
            }

            String last = "";
            if (persons.lastContact != null) {
                last = persons.lastContact;
            }
            String observations = "";
            if (persons.observations != null) {
                observations = persons.observations;
            }

            String cell1 = "";
            if (persons.cell1 != null) {
                cell1 = persons.cell1.trim();

            }
            String cell2 = "";
            if (persons.cell2 != null) {
                cell2 = persons.cell2.trim();

            }
            String cell3 = "";
            if (persons.cell3 != null) {
                cell3 = persons.cell3.trim();

            }

            String mail = "";
            if (persons.maial1 != null) {
                mail = persons.maial1.trim();
            }

            tvDocument.setText(document);
            tvName.setText(name);
            tvLastName.setText(lastname);
            tvEmail.setText(mail);
            tvClientstatus.setText(clientstatus);
            tvProcessstatus.setText(processstatus);
            tvProcessed.setText(processed);
            tvLimitProcess.setText(limitprocess);
            tvLastcontact.setText(last);
            tvObservations.setText(observations);
            tvSource.setText(source);

            imbedit = (ImageButton) findViewById(R.id.ibedit);
            imbedit.setVisibility(View.VISIBLE);
            imbedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String message = strIdPerson;
                    Intent intent;
                    intent = new Intent(DetailCostumer.this, EditCostumer.class);
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                    startActivity(intent);
                    finish();
                }
            });

            imbemail = (ImageButton) findViewById(R.id.ibemail);
            imbemail.setVisibility(View.GONE);
            if (mail.length() > 0 && isValidEmail(mail)) {
                imbemail.setVisibility(View.VISIBLE);
                final String finalMail = mail;
                imbemail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        String[] TO = {finalMail};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        try {
                            startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));
                            //finish();

                        } catch (android.content.ActivityNotFoundException ex) {
                            //Toast.makeText(MainActivity.this,"There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


            imbsave = (ImageButton) findViewById(R.id.ibsave);
            imbsave.setVisibility(View.GONE);
            if (phone.length() == 7 || phone2.length() == 7 || phone3.length() == 7 || cell1.length() == 10 || cell2.length() == 10 || cell3.length() == 10) {
                imbsave.setVisibility(View.VISIBLE);
                final String finalName = name;
                final String finalLastname = lastname;
                final String finalCell1 = cell1;
                final String finalPhone1 = phone;
                final String finalCell2 = cell2;
                final String finalCell3 = cell3;
                final String finalPhone2 = phone2;
                final String finalPhone3 = phone3;
                final String finalMail1 = mail;
                imbsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
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
                            Toast.makeText(DetailCostumer.this, "Contacto guardado", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DetailCostumer.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            imbcall1 = (ImageButton) findViewById(R.id.ibcall1);
            imbcall2 = (ImageButton) findViewById(R.id.ibcall2);
            imbcall3 = (ImageButton) findViewById(R.id.ibcall3);
            imbcall1.setVisibility(View.GONE);
            imbcall2.setVisibility(View.GONE);
            imbcall3.setVisibility(View.GONE);

            if (cell1.length() == 10) {
                tvcell1.setText(cell1);
                tvcell1.setVisibility(View.VISIBLE);
                imbcall1.setVisibility(View.VISIBLE);
                final String finalCell = cell1;
                imbcall1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + finalCell));
                        if (ActivityCompat.checkSelfPermission(arg0.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(DetailCostumer.this, "No tiene permisos para llamar", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(callIntent);
                    }
                });
            }
            if(cell2.length()==10)
            {
                tvcell2.setText(cell2);
                tvcell2.setVisibility(View.VISIBLE);
                final String finalCell = cell2;
                imbcall2.setVisibility(View.VISIBLE);
                imbcall2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + finalCell));
                        startActivity(callIntent);
                    }
                });

            }
            if(cell3.length()==10)
            {
                tvcell3.setText(cell3);
                tvcell3.setVisibility(View.VISIBLE);
                final String finalCell = cell3;
                imbcall3.setVisibility(View.VISIBLE);
                imbcall3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + finalCell));
                        startActivity(callIntent);
                    }
                });
            }


            imbphone1 = (ImageButton) findViewById(R.id.ibphone1);
            imbphone2 = (ImageButton) findViewById(R.id.ibphone2);
            imbphone3 = (ImageButton) findViewById(R.id.ibphone3);
            imbphone1.setVisibility(View.GONE);
            imbphone2.setVisibility(View.GONE);
            imbphone3.setVisibility(View.GONE);

            if (phone.length() == 7) {
                tvPhone1.setText(phone);
                tvPhone1.setVisibility(View.VISIBLE);
                imbphone1.setVisibility(View.VISIBLE);
                final String finalPhone = "034"+phone;
                imbphone1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + finalPhone));
                        startActivity(callIntent);
                    }
                });
            }

            if (phone2.length() == 7) {
                tvPhone2.setText(phone2);
                tvPhone2.setVisibility(View.VISIBLE);
                imbphone2.setVisibility(View.VISIBLE);
                final String finalPhone = "034"+phone2;
                imbphone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + finalPhone));
                        startActivity(callIntent);
                    }
                });
            }

            if (phone3.length() == 7) {
                tvPhone3.setText(phone3);
                tvPhone3.setVisibility(View.VISIBLE);
                imbphone3.setVisibility(View.VISIBLE);
                final String finalPhone = "034"+phone3;
                imbphone3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + finalPhone));
                        startActivity(callIntent);
                    }
                });
            }

        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        boolean validate= !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        return  validate;
    }


}
